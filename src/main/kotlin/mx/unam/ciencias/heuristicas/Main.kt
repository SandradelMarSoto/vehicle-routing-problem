@file:JvmName("Main")
package mx.unam.ciencias.heuristicas

import mx.unam.ciencias.heuristicas.vrp.Grafica


/**
 * Función main del proyecto
 * @param args Argumentos obtenidos por la terminal
 */
fun main(args: Array<String>) {
        println("Iniciamos")
        val graf = Grafica()
        val solInicial = graf.obtieneSolucionInicial()
        println(graf.toString(solInicial))
}