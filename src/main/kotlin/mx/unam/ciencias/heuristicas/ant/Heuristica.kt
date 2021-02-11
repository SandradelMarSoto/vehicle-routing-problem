package mx.unam.ciencias.heuristicas.ant

import mx.unam.ciencias.heuristicas.modelo.Vehiculo
import mx.unam.ciencias.heuristicas.vrp.Grafica
import mx.unam.ciencias.heuristicas.vrp.Solucion
import kotlin.random.Random

/**
 * Declaramos nuestra clase Heuristica que realizará la búsqueda tabú
 *
 * @property g La grafica en la que estaremos trabajando
 * @property solucionInicial La primera posible solucion
 */
class Heuristica(val g: Grafica, val solucionInicial: Solucion) {
    /** La cantidad de clientes del problema*/
    private val clientes = g.clientes
    /** Variable que irá guardando la solución actual del sistema*/
    private var solucionActual = solucionInicial
    /** Variable que irá guardando la mejor solución del sistema */
    var mejorSolucionActual = Solucion(g,g.obtieneSolucionInicial(), Random(4))
    /** Máximo de iteraciones que realizará la búsqueda tabú*/
    private val maximoIteraciones = 200000
    /** Tamaño máximo de la lista tabú*/
    private val maximoListaTabu = 100
    /** Cantidad máxima de vecinos a generarse por solución*/
    private val maxVecinos = clientes * 10

    /**
     * Función que busca el mejor vecino posible que no sea tabú
     * @param solucionesVecinas La vecindad que se considera
     * @param solucionesTabu Las soluciones que se consideran tabú
     * @return El costo de la mejor solución del sistema
     */
    private fun buscaVecino(solucionesVecinas: ArrayList<Solucion>, solucionesTabu: ArrayList<Solucion>): Solucion{
        //Eliminamos todos los resultados tabú de la lista de vecinos
        solucionesVecinas.removeAll {
            it in solucionesTabu
        }
        //Ordenamos los vecinos de acuerdo a su costo
        solucionesVecinas.sortedWith(compareBy({ g.getCosto(it.asignaciones) }, {g.getCosto(it.asignaciones) }))
        //Devolvemos al vecino con el menor valor
        return solucionesVecinas[0]
    }

    /**
     * Función que genera la vecindad de una solución
     * @param solucion La solucion a generar su vecindad
     * @return La lista de vecinos
     */
    private fun generaVecinos(solucion: Solucion): ArrayList<Solucion>{
        val vecindad =  ArrayList<Solucion>()
        for(i in 0 until maxVecinos) {
            val vecino1 = solucion.generaVecinoSwap()
            //val vecino2 = solucion.generaVecinoShift()
            //println(vecino1)
            //println(vecino2)
            vecindad.add(vecino1)
            //vecindad.add(vecino2)
        }
        return vecindad
    }

    /**
     * Función que realiza la búsqueda tabú y va actualizando la mejor solución del sistema
     *
     */
    fun tabu(){
        var iteracion = 0
        val listaTabu = ArrayList<Solucion>()
        //Agregamos la solución inicial a la lista tabú
        listaTabu.add(solucionActual)
        //Mientras no sobrepasemos el maximo de iteraciones, iteramos
        while(iteracion != maximoIteraciones){
            //Generamos la vecindad de la solución actual del sistema
            val vecindad = generaVecinos(solucionActual)
            // Obtenemos al mejor vecino no tabú de la vecindad
            val mejorVecino = buscaVecino(vecindad, listaTabu)
            // Asignamos al mejorVecino como la nueva solucion actual y lo agregamos a la lista tabú
            listaTabu.add(mejorVecino)
            solucionActual = mejorVecino
            //println("Costo completo:" + solucionActual.costo)
            //Si este vecino tiene mejor costo que la mejor solucion actual, lo actualizamos
            if(g.getCosto(mejorVecino.asignaciones) < g.getCosto(mejorSolucionActual.asignaciones)){
                mejorSolucionActual = mejorVecino
            }
            //Si sobrepasamos el máximo de la lista tabú, sacamos los primeros elementos hasta que sea de menor tamaño
            while(listaTabu.size > maximoListaTabu){
                listaTabu.removeAt(0)
            }
            iteracion ++
        }
    }

    fun ants(){
        val aco = AntColony(g.matrizDistancia, g)
        aco.aco()
        val mejorSolucionHormiga = aco.mejorHormigaCosto
    }


    /**
     * Función que regresa el string de la asignación de la mejor solución del sistema
     * @return La lista de ids de la mejor solución del sistema
     */
    fun asignacionString(): String = mejorSolucionActual.toString()

    /**
     * Función que regresa el costo de la mejor solución del sistema
     * @return El costo de la mejor solución del sistema
     */
    fun evaluacion(): Double = g.getCosto(mejorSolucionActual.asignaciones)

    /**
     * Función que regresa si la mejor solución del sistema es factible o no
     * @return Un booleano que nos dice si es factible o no la solución
     */
    fun esFactible(): Boolean = g.esFactible(mejorSolucionActual.asignaciones)

}