package mx.unam.ciencias.heuristicas.modelo

import com.sun.xml.internal.bind.v2.model.core.NonElement

/**
 * Declaramos nuestra clase DAO que obtiene la información de la base de datos
 *
 * @constructor Crea un Vehiculo
 * @property capacidad Es la capacidad del vehiculo
 */
data class Vehiculo(val id: Int, val capacidad: Int, var rutas:ArrayList<Int>) {
    fun cambiaRuta(ruta: ArrayList<Int>){
        rutas = ruta
    }

    fun quitaDeRuta(ciudad: Int){
        val rutasNuevas = ArrayList<Int>()
        for(a in rutas){
            if(a != ciudad){
                rutasNuevas.add(a)
            }
        }
        rutas = rutasNuevas
    }

    fun agregaRuta(ciudad: Int){
        rutas.add(ciudad)
    }
}