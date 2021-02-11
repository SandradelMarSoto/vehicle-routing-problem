package mx.unam.ciencias.heuristicas.vrp
import mx.unam.ciencias.heuristicas.modelo.Vehiculo
import kotlin.random.Random


/**
 * Clase Solucion que guarda las posibles soluciones al problema
 *
 * @property grafica La grafica del problema
 * @property asignaciones Las asignaciones de tareas a trabajadores de la solución
 * @property random La semilla para generar valores aleatorios
 * @constructor Crea una solución
 */
class Solucion(private val g: Grafica, var asignaciones: ArrayList<Vehiculo>, private val random: Random ) {
    /**
     * Función que obtiene el vecino de una solución, intercambiamos las ciudades de dos vehículos
     * @return Regresa una nueva solución, con los indices intercambiados
     * */
    fun generaVecinoSwap(): Solucion {
        val asignacionesNoVacias = ArrayList<Vehiculo>()
        for(a in asignaciones){
            if(a.rutas.size != 0){
                asignacionesNoVacias.add(a)
            }
        }
        var vehiculoAletorio1 = (asignacionesNoVacias.indices).random(random)
        val vehiculoAletorio2 = (asignacionesNoVacias.indices).random(random)
        while ( vehiculoAletorio1 == vehiculoAletorio2) {
            vehiculoAletorio1 = (asignacionesNoVacias.indices).random(random)
        }
        val vecino = asignaciones
        val rutaVehiculo1 = asignacionesNoVacias[vehiculoAletorio1].rutas
        val rutaVehiculo2 = asignacionesNoVacias[vehiculoAletorio2].rutas
        val ciudadAleatoria1 = rutaVehiculo1[(rutaVehiculo1.indices).random(random)]
        val ciudadAleatoria2 = rutaVehiculo2[(rutaVehiculo2.indices).random(random)]
        val idveh1 = asignacionesNoVacias[vehiculoAletorio1].id
        val idveh2 = asignacionesNoVacias[vehiculoAletorio2].id
        var idVecino1 = 0
        var idVecino2 = 0
        for(i in 0 until vecino.size){
            if(vecino[i].id == idveh1){
                idVecino1 = i
            }
            if(vecino[i].id == idveh2){
                idVecino2 = i
            }
        }
        vecino[idVecino1].agregaRuta(ciudadAleatoria2)
        vecino[idVecino1].quitaDeRuta(ciudadAleatoria1)
        vecino[idVecino2].agregaRuta(ciudadAleatoria1)
        vecino[idVecino2].quitaDeRuta(ciudadAleatoria2)
        return Solucion(g, vecino, random)
    }


    /** Funcion que obtiene el string de una solucion**/
    override fun toString(): String {
       return g.toString(asignaciones)
    }


}