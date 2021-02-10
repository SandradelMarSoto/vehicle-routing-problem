package mx.unam.ciencias.heuristicas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Mati on 2017-05-21.
 */
public class Instancia {

    private File file;
    private String m_path = "resources/A/A-n32-k5.vrp";
    private Scanner scanner;
    private int dimensions;
    private int cap;
    private double[][] distanceMatrix;
    private int[][] cords;
    private int[] demands;

    public void init(){
        file = new File(m_path);
        try {
            scanner = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        while(scanner.hasNextLine()){
            String nextLine = scanner.nextLine();
            if(nextLine.split(" ")[0].equals("DIMENSION")){
                dimensions = Integer.parseInt(nextLine.split(" ")[2]);
                distanceMatrix = new double[dimensions][dimensions];
                cords = new int[dimensions][2];
                demands = new int[dimensions];
            }
            if(nextLine.split(" ")[0].equals("CAPACITY")){
                cap = Integer.parseInt(nextLine.split(" ")[2]);
            }
            if(nextLine.split(" ")[0].equals("NODE_COORD_SECTION")){
                int counter = 0;
                nextLine = scanner.nextLine();
                while(!nextLine.equals("DEMAND_SECTION")){
                    cords[counter][0] = Integer.parseInt(nextLine.split(" ")[1]);
                    cords[counter][1] = Integer.parseInt(nextLine.split(" ")[2]);
                    counter++;
                    nextLine = scanner.nextLine();
                }
                counter = 0;
                nextLine= scanner.nextLine();
                while(!nextLine.equals("DEPOT_SECTION")){
                    demands[counter] = Integer.parseInt(nextLine.split(" ")[1]);
                    counter++;
                    nextLine = scanner.nextLine();
                }
            }
        }
    }

}