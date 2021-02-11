package mx.unam.ciencias.heuristicas.ant

import mx.unam.ciencias.heuristicas.modelo.Vehiculo
import mx.unam.ciencias.heuristicas.vrp.Grafica

data class Ant(var asignaciones: ArrayList<Vehiculo>, val g: Grafica) {
    val costo = g.getCosto(asignaciones)
}