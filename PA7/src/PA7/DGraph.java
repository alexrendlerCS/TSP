package PA7;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
 * AUTHOR: Alex Rendler
 * FILE: DGraph.java
 * Assignment: PA7
 * COURSE: CSC 210
 * Purpose: This is the DGraph class of the traveling salesperson
 * problem which is responsible for creating the dgraph objects.
 * It is in charge of finding ways to search through the graphs
 * to find the most cost efficient methods using recursive 
 * backtracking, heuristic, and my own approach. Recursive backtracking
 * searches through every possibility and find the most efficient route,
 * while heuristic finds the most cost efficient route for each step.
 */

/**
 * DGraph.java
 * 
 * Represents a directed graph. The nodes are represented as
 * integers ranging from 1 to numNodes inclusive.
 * The weights are assumed to be >= zero.
 * 
 * Usage instructions:
 * 
 * Construct a DGraph
 * DGraph graph = new DGraph(numNodes);
 * 
 * Add an edge
 * graph.addEdge(v, w, weight);
 * 
 * Other useful methods:
 * graph.getWeight(v,w)
 * graph.getNumNodes()
 * List<Integer> list = graph.getNeighbors(v);
 * 
 */
class DGraph {
    private int numNodes;
    private double MAX_WEIGHT = 0;
    private double min_cost = 100000000000000.0;
    private ArrayList<LinkedList<Edge>> edges = new ArrayList<>();
    private List<Integer> visitOrder = new ArrayList<>();
    private List<Edge> edgeDot = new ArrayList<>();
    
    DGraph(int numNodes) {
        this.numNodes = numNodes;
        for (int i = 0; i < numNodes; i++)
            edges.add(new LinkedList<Edge>());
    } 

    // Returns the number of nodes
    public int getNumNodes() {
        return numNodes;
    }

    // Returns the weight for the given edge.
    // Returns -1 if there is no edge between the nodes v and w.
    public double getWeight(int v, int w) {
        for (int i = 0; i < edges.size(); i++) {
            for (int j = 0; j < edges.size(); j++) {
                Edge element = edges.get(i).get(j);
                if (element.node1 == v) {
                    return element.weight;
                }
            }
        }
        return -1;
    }

    /**
     * Adds the directed edge (v,w) to the graph including updating the node
     * count appropriately.
     * 
     * @param v
     * @param w
     */
    void addEdge(int v, int w, double distance) {
        if (distance >= MAX_WEIGHT)
            MAX_WEIGHT = distance;
        edges.get(v).add(new Edge(w, distance));
        edgeDot.add(new Edge(w, distance));
    } 

    /*
     * This method returns the edge of two given vertices.
     * Given the v and w values, it will return the
     * corresponding edge.
     * 
     * Returns the corresponding edge
     */
    Edge getEdge(int v, int w) {
        for (int i = 0; i < edges.get(v).size(); i++) {
            if (edges.get(v).get(i).node1 == w) {
                return edges.get(v).get(i);
            }
        }
        return null;
    } 
    
    /*
     * This heuristic search method is a fast search that
     * checks for the least cost move at each decision.
     * Although fast, it will not always be the most
     * efficient route in the end.
     */
    public void Heuristic(int start) {
        boolean beenVisited[] = new boolean[numNodes];
        ArrayList<Integer> visitOrder = new ArrayList<>();
        int num_check = numNodes;
        Double cost = 0.0;
        LinkedList<Integer> moves = new LinkedList<>();
        moves.add(start);
        beenVisited[start] = true;
        while (moves.size() > 0) {
            int next = moves.pollFirst();
            visitOrder.add(next + 1);
            ArrayList<Integer> path = new ArrayList<>();
            path.add(next);
            double min_wt = MAX_WEIGHT;
            boolean check = false;
            int min = 0;
            Edge edge = new Edge(0, 0);
            Edge min_edge = new Edge(0, 0);
            for (int i = 0; i < edges.get(next).size(); i++) {
                int v = edges.get(next).get(i).node1;
                edge = edges.get(next).get(i);
                if (!beenVisited[v]) {
                    if (edge.weight < min_wt) {
                        check = true;
                        min_wt = edge.weight;
                        min = v;
                        min_edge = edge;
                    }
                }
            }
            if (check == true) {
                moves.add(min);
                beenVisited[min] = true;
                cost += min_edge.weight;
            }
            num_check --;

            // Checks cost once at last vertex.
            if (num_check == 0) {
                for (int i = 0; i < edges.get(next).size(); i++) {
                    int v = edges.get(next).get(i).node1;
                    edge = edges.get(next).get(i);
                    if (v == 0)
                        cost += edge.weight;
                }
            }
        } 
        Double finalCost = (double) Math.round(cost * 10d) / 10d;
        System.out.println(
                "cost = " + finalCost + ", visitOrder = " + visitOrder);
    }
 
    /*
     * The recursive backtracking method uses recursion
     * to search through the entire map and backtrack to
     * determine the most efficient route. It is not as
     * fast as the heuristic approach, but will give the
     * most efficient route all of the time.
     */
    public void recursiveBacktracking(int start) {
        boolean[] visited = new boolean[numNodes];
        List<Integer> path = new ArrayList<>();
        path.add(start);
        recursiveBacktracking(start, visited, path, 0);
        Double cost = (double) Math.round(min_cost * 10d) / 10d;
        System.out.println("cost = " + cost + ", visitOrder = " + visitOrder);
    }
    
    /*
     * A helper function to assist the original recursive
     * backtracking method.
     */
    public void recursiveBacktracking(int u, boolean[] visited,
            List<Integer> path, double cost) {
        visited[u] = true;

        // Check to make sure all edges are visited.
        if (path.size() == numNodes) {
            for (int i = 0; i < path.size() - 1; i++) {
                for (int j = 0; j < edges.get(path.get(i)).size(); j++) {
                    Integer cur = edges.get(path.get(i)).get(j).node1;
                    Edge edge = edges.get(path.get(i)).get(j);
                    if (cur == path.get(i + 1)) {
                        cost += edge.weight;
                    }
                }
            }
            int last = path.get(path.size() - 1);
            for (int c = 0; c < edges.get(last).size(); c++) {
                Integer cur = edges.get(last).get(c).node1;
                Edge edge = edges.get(last).get(c);
                if (cur == 0) {
                    cost += edge.weight;
                    break;
                }
            }
            if (cost < min_cost) {
                min_cost = cost;
                visitOrder.clear();
                for (int l = 0; l < path.size(); l++) {
                    visitOrder.add(path.get(l) + 1);
                }
            }
            return;
        }

        // Checks if the edges lead to a solution
        for (int i = 0; i < edges.get(u).size(); i++) {
            Integer cur = edges.get(u).get(i).node1;
            if (!visited[cur]) {
                // Choose
                path.add(cur);
                // Explore
                recursiveBacktracking(cur, visited, path, cost);
                // Unchoose
                visited[cur] = false;
                path.remove(path.size() - 1);
            }
        }
    }
    
    /*
     * Uses a method similar to recursive backtracking, but
     * has some changes that allow it to differ. It makes it
     * more efficient by destroying any paths that are over
     * the min cost.
     */
    public void MINE(int start)
    {
        boolean[] visited = new boolean[numNodes];
        List<Integer> path = new ArrayList<>();
        path.add(start);
        MINE(start, visited, path, 0);
        Double costDouble = (double)Math.round(min_cost * 10d) / 10d;
        System.out.println("cost = " + costDouble + ", visitOrder = " + visitOrder);
    }
    
    /*
     * Helper function to the original method I created that
     * allows it to backtrack.
     */
    public void MINE(int u, boolean[] visited, List<Integer> path, double cost)
    {
        // Efficiency check if greater than min cost
        if (cost > min_cost)
            return;
        visited[u] = true;

        if (path.size() == numNodes) {
            for (int i = 0; i < path.size() - 1; i++) {
                for (int j = 0; j < edges.get(path.get(i)).size(); j++) {
                    Integer cur = edges.get(path.get(i)).get(j).node1;
                    Edge edge = edges.get(path.get(i)).get(j);
                    if (cur == path.get(i + 1)) {
                        cost += edge.weight;
                    }
                }
            }
            int last_one = path.get(path.size() - 1);   
            for (int c = 0; c < edges.get(last_one).size(); c++) {
                Integer cur = edges.get(last_one).get(c).node1;
                Edge edge = edges.get(last_one).get(c);
                if (cur == 0) {
                    cost += edge.weight;
                    break;
                }
            }
            if (cost < min_cost) {
                min_cost = cost;
                visitOrder.clear();
                for (int l = 0; l < path.size(); l++) {
                    visitOrder.add(path.get(l) + 1);
                }
            }
            return;
        }

        for (int i = 0; i < edges.get(u).size(); i++) {
            Integer cur = edges.get(u).get(i).node1;
            if (!visited[cur]) {
                // Choose
                path.add(cur);
                // Explore
                MINE(cur, visited, path, cost);
                // Unchoose
                visited[cur] = false;
                path.remove(path.size() - 1);
            }
        }
    }
    
    /* --------------------------------------- */
    /*
     * You should not need to touch anything below this line,
     * except for maybe the name edges in the for each loop just below
     * in the toDotString method if you named your collection of edges
     * differently.
     * However I struggled getting the Edge class to work with 3 values,
     * so I edited it to hold the v value and the weight. And added the
     * weight assert at the top of the class.
     */
    class Edge {
        int node1;
        double weight;

        public Edge(int v, double distance) {
            node1 = v;
            weight = distance;
        }

        // Create a dot representation of the graph.
        public String toDotString() {
            String dot_str = "digraph {\n";
            // Iterate over the edges in order
            for (Edge e : edgeDot) {
                dot_str += e.toDotString() + "\n";
            }
            return dot_str + "}\n";
        }
    }
    public String toString()
    {
        String finished = "";
        for (int i = 0; i < numNodes; i++) {
            finished += i + ": [";
            LinkedList<Edge> list = edges.get(i);

            for(int j = 0; j < list.size(); j++)
                finished += list.get(j).node1 + "/" + list.get(j).weight + ", ";

            finished += "]\n";
        }
        return finished;
    }

}