package PA7;


/*
 * AUTHOR: Alex Rendler
 * FILE: PA11Main.java
 * Assignment: PA7
 * COURSE: CSC 210
 * Purpose: This file read the commands from args and creates
 * a graph based on the input. It will emit a heuristic, backtracking
 * or my own graph based off the input. If TIME is inserted, it will
 * give all 3 results and the time it took to execute them.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class PA11Main {

    public static void main(String[] args) {
        Scanner file = null;
        try {
            file = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            ;
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Out of bounds");
            ;
            return;
        }

        DGraph graph = createGraph(file);
        String command = null;
        try {
            command = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No Command");
            return;
        }
        if (command.equals("HEURISTIC"))
            graph.Heuristic(0);
        else if (command.equals("BACKTRACK"))
            graph.recursiveBacktracking(0);
        else if (command.equals("MINE"))
            graph.MINE(0);
        else if (command.equals("TIME"))
            time(graph);
    }

    /*
     * Determines how long each command takes to compute in order
     * to give a time back to the user to determine which method
     * is faster.
     */
    public static void time(DGraph g) {
        long startTimeTSP = System.nanoTime();
        g.Heuristic(0);
        long endTimeTSP = System.nanoTime();
        long durationTSP = endTimeTSP - startTimeTSP;
        System.out.println(
                "heuristic: " + durationTSP / 1000000 + " milliseconds");
        long startTimeBACKTRACK = System.nanoTime();
        g.recursiveBacktracking(0);
        long endTimeBACKTRACK = System.nanoTime();
        long durationBACKTRACK = endTimeBACKTRACK - startTimeBACKTRACK;
        System.out.println(
                "backtrack: " + durationBACKTRACK / 1000000 + " milliseconds");
        long startTimeMINE = System.nanoTime();
        g.MINE(0);
        long endTimeMINE = System.nanoTime();
        long durationMINE = endTimeMINE - startTimeMINE;
        System.out.println("mine: " + durationMINE / 1000000 + " milliseconds");

    }

    /*
     * Creates a DGraph object from the inputed arg file.
     * It does this by reading the file and adding each line
     * into a graph object with all of its connections.
     */
    public static DGraph createGraph(Scanner file) {
        String startLine = null;
        while (file.hasNextLine()) {
            startLine = file.nextLine();
            if (!startLine.startsWith("%"))
                break;
        }

        String[] startLineSplit = startLine.split(" ");
        int numVertices = Integer.parseInt(startLineSplit[0]);
        DGraph graph = new DGraph(numVertices);

        while (file.hasNextLine()) {
            String[] s = file.nextLine().split("( )+");
            graph.addEdge(Integer.parseInt(s[0]) - 1,
                    Integer.parseInt(s[1]) - 1, Float.parseFloat(s[2]));
        }
        return graph;
    }
}