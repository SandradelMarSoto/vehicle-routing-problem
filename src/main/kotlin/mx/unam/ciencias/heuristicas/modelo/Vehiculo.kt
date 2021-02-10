package mx.unam.ciencias.heuristicas.modelo

/**
 * Declaramos nuestra clase DAO que obtiene la información de la base de datos
 *
 * @constructor Crea un Vehiculo
 * @property capacidad Es la capacidad del vehiculo
 */
data class Vehiculo(val id: Int, val capacidad: Int, val rutas:ArrayList<Int>) {
}