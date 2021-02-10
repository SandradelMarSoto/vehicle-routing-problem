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
class Grafica(val url: String, val clientes:Int){

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

    fun getDemanda(id: Int): Int{
        return demanda[id]
    }

    fun getCostoVehiculo(vehiculo: Vehiculo): Double{
        val rutas = vehiculo.rutas
        var costo = 0.0
        if(rutas.size > 0) {
            val primero = rutas[0]
            var costo = matrizDistancia[0][primero]
            for (i in 1 until rutas.size) {
                costo += matrizDistancia[rutas[i-1]][rutas[i]]
            }
            costo += matrizDistancia[rutas[rutas.size-1]][0]
        }
        return costo
    }

    fun getCosto(vehiculos: ArrayList<Vehiculo>):Double{
        var costo = 0.0
        for(vehiculo in vehiculos){
            costo += getCostoVehiculo(vehiculo)
        }
        if(esFactible(vehiculos)){
            return costo
        }
        return costo * 1000
    }

    fun getCapacidadUsada(vehiculo: Vehiculo): Int{
        val rutas = vehiculo.rutas
        var capacidad = 0
        for (i in rutas){
            capacidad += getDemanda(i)
        }
        return capacidad
    }

    fun esFactible(vehiculos: ArrayList<Vehiculo>): Boolean{
        for (vehiculo in vehiculos) {
            if  (vehiculo.capacidad < getCapacidadUsada(vehiculo)){
                return false
            }
        }
        return true
    }

    fun obtieneAnguloPolar(id: Int): Double {
        val x1 = cords[id][0]
        val y1 = cords[id][1]
        val xDeposito = cords[0][0]
        val yDeposito = cords[0][1]
        val angle = (atan2((y1 - yDeposito).toDouble(), (x1 - xDeposito).toDouble()))
        angle * 180 / Math.PI
        return (90 - angle) % 360
    }

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

    fun toString(vehiculos: ArrayList<Vehiculo>): String{
        var s = "Resultado:\n"
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
        s+= "Es Factible: "
        s+= esFactible(vehiculos).toString()
        s+= "\nCosto Total: "
        s+= getCosto(vehiculos).toString()
        return s
    }
}