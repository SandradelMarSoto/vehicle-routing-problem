package mx.unam.ciencias.heuristicas.vrp

import mx.unam.ciencias.heuristicas.DAO
import mx.unam.ciencias.heuristicas.modelo.Vehiculo
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random


/**
 * Declaramos nuestra clase Grafica que representará los distintos métodos de nuestra grafica del problema
 * * @constructor Devuelve una gráfica
 */
class Grafica(url: String, val clientes:Int){
    val dao = DAO(url, clientes).getValores()
    val dimension = dao.first[0]
    val cords = dao.second
    val demanda = dao.third
    /** Matriz que tendrá el valor de las distancias entre los clientes*/
    val matrizDistancia = obtieneDistancias()
    val vehiculos = dao.first[2]
    val capacidad = dao.first[1]

    /**
     * Función que obtiene la distancia entre dos puntos
     * @param x1 El id de la primera ciudad
     * @param y1 El id de la segunda ciudad
     * @param x2 El id de la primera ciudad
     * @param y2 El id de la segunda ciudad
     * @return La distancia entre esas dos coordenadas
     */
    private fun euclideana(x1: Int, y1: Int, x2: Int, y2: Int): Double {
        val distanciaX = abs(x2 - x1)
        val distanciaY = abs(y2 - y1)
        return sqrt((distanciaX.toDouble().pow(2)) +(distanciaY.toDouble().pow(2)))
    }

    /** Función que obtiene y actualiza la matriz de distancias **/
    fun obtieneDistancias(): Array<DoubleArray>{
        val matrizDistancia = Array(dimension) { DoubleArray(dimension) }
        //Llenamos la matriz de distancias obteniendo la distancia euclidena
        for (i in 0 until dimension) {
            for (j in 0 until dimension) {
                if (i == j) {
                    matrizDistancia[i][j] = 0.0
                } else {
                    matrizDistancia[i][j] = euclideana(
                        cords[i][0],
                        cords[i][1],
                        cords[j][0],
                        cords[j][1]
                    )
                }
            }
        }
        return matrizDistancia
    }

    /** Función que obtiene el valor de la demanda dado un  int **/
    fun getDemanda(id: Int): Int{
        return demanda[id]
    }

    /** Función que obtiene el costo de un vehículo **/
    fun getCostoVehiculo(vehiculo: Vehiculo): Double{
        val rutas = vehiculo.rutas
        var costo = 0.0
        if(rutas.size > 0) {
            val primero = rutas[0]
            costo += matrizDistancia[0][primero]
            for (i in 1 until rutas.size) {
                costo += matrizDistancia[rutas[i-1]][rutas[i]]
            }
            costo += matrizDistancia[rutas[rutas.size-1]][0]
        }
        return String.format("%.3f", costo).toDouble()
    }

    /** Función que obtiene el costo total de la solucion de un problema **/
    fun getCosto(vehiculos: ArrayList<Vehiculo>):Double{
        var costo = 0.0
        for(vehiculo in vehiculos){
            costo += getCostoVehiculo(vehiculo)
        }
        if(esFactible(vehiculos)){
            return String.format("%.3f", costo).toDouble()
        }
        return  String.format("%.3f", costo * 100).toDouble()
    }

    /** Función que obtiene la capacidad usada por un vehiculo **/
    fun getCapacidadUsada(vehiculo: Vehiculo): Int{
        val rutas = vehiculo.rutas
        var capacidad = 0
        for (i in rutas){
            capacidad += getDemanda(i)
        }
        return capacidad
    }

    /** Función que revisa si una asignación de vehiculos es factible o no **/
    fun esFactible(vehiculos: ArrayList<Vehiculo>): Boolean{
        for (vehiculo in vehiculos) {
            if  (vehiculo.capacidad < getCapacidadUsada(vehiculo)){
                return false
            }
        }
        return true
    }

    /** Función que obtiene el angulo polar de un nodo, dado su id **/
    fun obtieneAnguloPolar(id: Int): Double {
        val x1 = cords[id][0]
        val y1 = cords[id][1]
        val xDeposito = cords[0][0]
        val yDeposito = cords[0][1]
        val angle = (atan2((y1 - yDeposito).toDouble(), (x1 - xDeposito).toDouble()))
        angle * 180 / Math.PI
        return (90 - angle) % 360
    }

    /** Función que obtiene la solucion inicial con el sweep algorithm **/
    fun obtieneSolucionInicial():ArrayList<Vehiculo>{
        var contaId = 1
        var capacidadUsada = 0
        val vehiculos = ArrayList<Vehiculo>()
        val angulos = ArrayList<Pair<Int,Double>>()
        var ruta = ArrayList<Int>()
        //Iniciamos en 1 porque ignoramos al deposito
        for (i in 1 until dimension){
            var ang = Pair(i, obtieneAnguloPolar(i))
            angulos.add(ang)
        }
        angulos.sortBy { it.second }

        for(i in 0 until angulos.size){
            var propuesta = angulos[i].first
            if((capacidadUsada + getDemanda(propuesta) <= capacidad)){
                ruta.add(propuesta)
                capacidadUsada += getDemanda(propuesta)
                if(i >= angulos.size-1){
                    var veh = Vehiculo(contaId, capacidad, ruta)
                    vehiculos.add(veh)
                }
            }
            else{
                var veh = Vehiculo(contaId, capacidad, ruta)
                vehiculos.add(veh)
                contaId++
                ruta = ArrayList<Int>()
                capacidadUsada = 0
                ruta.add(propuesta)
                capacidadUsada += getDemanda(propuesta)
            }
        }
        return vehiculos
    }

    /** Función que convierte a string el valor de las asignaciones **/
    fun toString(vehiculos: ArrayList<Vehiculo>): String{
        var s = "\n"
        for (vehiculo in vehiculos) {
            s += "Vehiculo "+ vehiculo.id +"\n"
            s += "Ruta: ["
            var rutas = vehiculo.rutas
            for (ruta in rutas){
                s += (ruta+1).toString()
                s += ", "
            }
            s+= "]"
            s += "\n"
            s += "Costo "+ getCostoVehiculo(vehiculo).toString()
            s += "\n"
        }
        return s
    }

    /** Función que obtiene una solucion con vehiculos sin enrutar **/
    fun obtieneSolucionVacia(): ArrayList<Vehiculo>{
        val vehiculosVacios = ArrayList<Vehiculo>()
        val list =ArrayList<Int>()
        for(i in 0 until vehiculos){
            var vehic = Vehiculo(i+1, capacidad, list)
            vehiculosVacios.add(vehic)
        }
        return vehiculosVacios
    }

}