@file:JvmName("Main")
package mx.unam.ciencias.heuristicas

import mx.unam.ciencias.heuristicas.ant.Heuristica
import mx.unam.ciencias.heuristicas.vrp.Grafica
import mx.unam.ciencias.heuristicas.vrp.Solucion
import java.io.File
import kotlin.random.Random


/**
 * Función main del proyecto
 * @param args Argumentos obtenidos por la terminal
 */
fun main(args: Array<String>) {
        val seedS = (args[0]).toInt()
        val seedF = (args[1]).toInt()
        val dao = DAO("resources/A/A-n32-k5.vrp", 32)
        dao.obtieneValores()
        val graf = Grafica("resources/A/A-n32-k5.vrp", 32)
        var string = "Soluciones de las distintas semillas en el rango\n"
        var mejorCosto = Double.MAX_VALUE
        var mejorSemilla = 0
        var mejorAsignacion = ""
        for (i in seedS until seedF + 1)  {
                println("Semilla: $i")
                val asignacionActual = graf.obtieneSolucionInicial()
                val solucionInicial = Solucion(graf, asignacionActual, Random(i))
                val vrp = Heuristica(graf, solucionInicial)
                //Heuristica
                //vrp.hormigas
                vrp.tabu()
                if (vrp.evaluacion() <= mejorCosto){
                        mejorCosto = vrp.evaluacion()
                        mejorSemilla = i
                        mejorAsignacion = vrp.asignacionString()
                }
                println(mejorAsignacion)
        }
        string += "Mejor Semilla: $mejorSemilla, Mejor Costo: $mejorCosto , Mejor Asignación: $mejorAsignacion"
        if(seedS != seedF) {
                File("resultado/resultado-actual.txt").writeText(string)
                println("Mejor Asignación: $mejorAsignacion")
                println("Mejor Costo: $mejorCosto")
        }
}