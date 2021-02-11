package mx.unam.ciencias.heuristicas.ant
import mx.unam.ciencias.heuristicas.ant.Ant
import mx.unam.ciencias.heuristicas.modelo.Vehiculo
import mx.unam.ciencias.heuristicas.vrp.Grafica

class AntColony (private val matrizDistancias:  Array<DoubleArray>, private val g: Grafica){
    val dimension = g.dimension
    val vehiculos = g.vehiculos
    val n = matrizDistancias.size
    val maxIt = 300
    val nAnt = 40
    val Q = 1
    val tau0 = 10*Q/(n*getPromedio())
    val alpha = 1
    val beta = 1
    val rho = 0.05
    var mejorHormigaCosto = Double.MAX_VALUE
    val antColony = getListaHormigas()
    val tau = getFeromonas()
    val mejoresCostos = ArrayList<Int>(maxIt)
    val eta = getMatrizInfoHeur()

    fun getMatrizInfoHeur(): Array<DoubleArray>{
        val matrizInfo = matrizDistancias
        for (i in 0 until dimension) {
            for (j in 0 until dimension) {
                matrizInfo[i][j] = 1/matrizDistancias[i][j]
            }
        }
        return matrizInfo
    }

    fun getFeromonas(): Array<DoubleArray> {
        val matrizFeromonas = Array(n) {DoubleArray(n)}
        for (i in 0 until n) {
            for (j in 0 until n) {
                matrizFeromonas[i][j] = tau0
            }
        }
        return matrizFeromonas
    }

    fun getPromedio(): Double{
        var suma = 0.0
        var promedio = 0.0
        for (i in 0 until dimension) {
            for (j in 0 until dimension) {
                    suma += matrizDistancias[i][j]
            }
        }
        return suma/ n
    }

    fun getListaHormigas(): ArrayList<Ant>{
        val listaHormigas = ArrayList<Ant>()
        val asignacionInicial = g.obtieneSolucionVacia()
        for(i in 0 until nAnt){
            var ant = Ant(asignacionInicial, g)
            listaHormigas.add(ant)
        }
        return listaHormigas
    }

    fun aco(){
        for(i in 0 until maxIt){
            for(i in 0 until nAnt){
                antColony[i].asignaciones = asignaAleatorio(antColony[i].asignaciones)
                for(j in 2 until n){
                    var a = antColony[j].costo
                    var proba = tau[j] + alpha.toDouble() + eta[j] + beta.toDouble()
                }
            }
        }
    }


    fun asignaAleatorio(vehiculos: ArrayList<Vehiculo>):ArrayList<Vehiculo>{
        val list =ArrayList<Int>()
        for(i in 0 until dimension ){
            list.add(i)
        }
        for(i in vehiculos){
            i.agregaRuta(list[1])
        }
        return vehiculos
    }




}