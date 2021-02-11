package mx.unam.ciencias.heuristicas.modelo

/**
 * Declaramos nuestra clase Vehiculo que guarda la información de los vehículos
 *
 * @constructor Crea un Vehiculo
 * @property capacidad Es la capacidad del vehiculo
 * @property rutas Es la ruta que sigue el vehículo
 */
data class Vehiculo(val id: Int, val capacidad: Int, var rutas:ArrayList<Int>) {
    /** Elimina una ciudad de la ruta del vehículo**/
    fun quitaDeRuta(ciudad: Int){
        val rutasNuevas = ArrayList<Int>()
        for(a in rutas){
            if(a != ciudad){
                rutasNuevas.add(a)
            }
        }
        rutas = rutasNuevas
    }

    /** Agrega una ciudad a la ruta del vehículo**/
    fun agregaRuta(ciudad: Int){
        rutas.add(ciudad)
    }
}