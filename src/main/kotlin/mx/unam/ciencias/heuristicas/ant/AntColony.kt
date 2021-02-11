package mx.unam.ciencias.heuristicas.ant

class AntColony (matrizDistancias:  Array<DoubleArray>){
    val maxIt = 300
    val nAnt = 40
    val Q = 1
    val tau0 = 10*Q/(nvar*average(matrizDistancias.average()))

}