@file:JvmName("Main")
package mx.unam.ciencias.heuristicas

import mx.unam.ciencias.heuristicas.vrp.Grafica


/**
 * Función main del proyecto
 * @param args Argumentos obtenidos por la terminal
 */
fun main(args: Array<String>) {
        println("Iniciamos")
        val dao = DAO("resources/A/A-n32-k5.vrp", 32)
        dao.obtieneValores()
        val graf = Grafica("resources/A/A-n32-k5.vrp", 32)
        val solInicial = graf.obtieneSolucionInicial()
        //val mejorSolucion =
        println(graf.toString(solInicial))
}