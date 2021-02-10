package mx.unam.ciencias.heuristicas

import java.io.File
import java.util.*
import kotlin.collections.ArrayList


/**
 * Declaramos nuestra clase DAO que obtiene la información de la base de datos
 *
 * @constructor Crea un DAO
 * @property url Es la ubicación del archivo vrp
 */
class DAO(val url: String, val clientes:Int) {
    /** Número de clientes*/
    var dimension = 0
    /** Capacidad de los camiones*/
    var capacidad = 0
    /** Óptimo de vehículos */
    var vehiculos = 0
    /**Coordenadas de los clientes*/
    val cords = Array(clientes) { IntArray(2) }
    /** Demandas de los clientes*/
    val pedidos = IntArray(clientes){0}
    /** El archivo donde se encuentra el problema*/
    private val file = File(url)

    /**
     * Función que obtiene la información a partir de los archivos de tipo VRP
     * Archivos obtenidos de: http://vrp.atd-lab.inf.puc-rio.br/index.php/en/
     */
    fun obtieneValores(){
        //Se va a recorrer línea por línea del documento
        val scanner = Scanner(file)
        while (scanner.hasNextLine()) {
            var nextLine: String = scanner.nextLine()
            if (nextLine.split(" ".toRegex()).toTypedArray()[0] == "COMMENT") {
                val linea = nextLine.split(" ".toRegex()).toTypedArray()
                for (i in linea.indices) {
                    if (linea[i] == "trucks:"){
                        vehiculos = linea[i+1].split(",".toRegex()).toTypedArray()[0].toInt()
                    }
                }
            }
            //Obtenemos el valor de la dimension e inicializamos las matrices
            if (nextLine.split(" ".toRegex()).toTypedArray()[0] == "DIMENSION") {
                dimension = nextLine.split(" ".toRegex()).toTypedArray()[2].toInt()
            }
            //Obtenemos el valor de la capacidad
            if (nextLine.split(" ".toRegex()).toTypedArray()[0] == "CAPACITY") {
                capacidad = nextLine.split(" ".toRegex()).toTypedArray()[2].toInt()
            }
            //Obtenemos el valor de las coordenadas de los nodos de los clientes
            if (nextLine.split(" ".toRegex()).toTypedArray()[0] == "NODE_COORD_SECTION") {
                var counter = 0
                nextLine = scanner.nextLine()
                while ((nextLine != "DEMAND_SECTION") and (counter != dimension)) {
                    cords[counter][0] = nextLine.split(" ".toRegex()).toTypedArray()[2].toInt()
                    cords[counter][1] = nextLine.split(" ".toRegex()).toTypedArray()[3].toInt()
                    counter++
                    nextLine = scanner.nextLine()
                }
                nextLine = scanner.nextLine()
                //Obtenemos los valores de los pedidos o demandas de los clientes
                while ((nextLine != "DEPOT_SECTION") and (counter != dimension)) {
                    pedidos[counter] = nextLine.split(" ".toRegex()).toTypedArray()[1].toInt()
                    counter++
                    nextLine = scanner.nextLine()
                }
            }
        }
    }

    fun getValores(): Triple<ArrayList<Int>, Array<IntArray>, IntArray>{
        obtieneValores()
        val valores = ArrayList<Int>()
        valores.add(dimension)
        valores.add(capacidad)
        valores.add(vehiculos)
        return Triple(valores, cords, pedidos)
    }
}
