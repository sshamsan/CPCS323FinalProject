import java.util.ArrayList;

/**
 * CPCS324 - DAR | Project Phase 2: Dijkstra 
 * Shahad Shamsan
 * Suha Shafi 
 * Safia Aljahdali 
 */

public class Dijkstra {

    // -- GLOBAL VARIABLES --
    
    // names of the cities from the saudi network 
    static String[] cities = {"Jeddah", "Makkah", "Madinah", "Riyadh", "Dammam",
        "Taif", "Abha", "Tabuk", "Qasim", "Hail", "Jizan", "Najran"};

    // Tree vertices (VT)
    static String tree = "";
    
    // visited vertices (V)
    static String visited = "";
    
    
    // ArrayList to store the Edges of the graph
    static ArrayList<edge> Edges = new ArrayList();
    
    // infinty
    static int INF = Integer.MAX_VALUE;

    public static void main(String[] args) {

        // matrix of the cities and their distances 
        int graph[][] = new int[][]{
            //JED  MKH    MAD    RYD  DAMM  TAIF  ABHA TABUK QASIM  HAIL JIZAN  NAJRN
            {0   , 79  ,  420 , 949 , 1343, 167 , 625 , 1024, 863 , 777 , 710 , 905}, // Jeddah
            {79  , 0   , 358  , 870 , 1265, 88  , 627 , 1037, 876 , 790 , 685 , 912}, // Makkah
            {420 , 358 , 0    , 848 , 1343, 446 , 985 , 679 , 518 , 432 , 1043, 1270}, // Madinah
            {949 , 870 , 848  , 0   , 395 , 782 , 1064, 1304, 330 , 640 , 1272, 950}, // Riyadh
            {1343, 1265, 1343 , 395 , 0   , 1177, 1495, 1729, 725 , 1035, 1667, 1345}, // Dammam
            {167 , 88  , 446  , 782 , 1177, 0   , 561 , 1204, 936 , 957 , 763 , 864}, // Taif
            {625 , 627 , 985  , 1064, 1459, 561 , 0   , 1649, 1488, 1402, 202 , 280}, // Abha
            {1024, 1037, 679  , 1304, 1729, 1204, 1649, 0   , 974 , 664 , 1722, 1929}, // Tabuk
            {863 , 876 , 518  , 330 , 725 , 936 , 1488, 974 , 0   , 310 , 1561, 1280}, // Qasim
            {777 , 790 , 432  , 640 , 1035, 957 , 1402, 664 , 974 , 0   , 1475, 1590}, // Hail
            {710 , 685 , 1043 , 1272, 1667, 763 , 202 , 1722, 1561, 1475, 0   , 482}, // Jizan
            {905 , 912 , 1270 , 950 , 1345, 864 , 280 , 1929, 1280, 1590, 482 , 0}};  // Najran

        // Execute dijkstra method from the Dijkstra class
        // Parameters: graph and (source = 0 = Jeddah)
        Dijkstra.dijkstra(graph, 0);

    } // end of main

    public static void dijkstra(int[][] graph, int source) {
        
        int graphSize = graph.length;
        
        // array to mark visited vertices
        boolean[] visitedVertex = new boolean[graphSize];
        
        // array to store the distance 
        int[] distance = new int[graphSize];
        
        for (int i = 0; i < graphSize; i++) {
            // initialize all vertices unvisited 
            // and the distance unseen (infinity)
            visitedVertex[i] = false;
            distance[i] = Integer.MAX_VALUE;
            
            // all the cities with the source = 0 (Jeddah)
            Edges.add(new edge(source));
        }

        // Distance from jeddah to itself
        distance[source] = 0;
        
        for (int i = 0; i < graphSize; i++) {

            // Update distance between source vertex and neighbouring vertex 
            int u = findMinDistance(distance, visitedVertex);
            
            // mark visited
            visitedVertex[u] = true;

            // Update all the neighbouring vertex distances
            for (int v = 0; v < graphSize; v++) {
                if (!visitedVertex[v] && graph[u][v] != 0 
                        && graph[u][v] != INF 
                        && (distance[u] + graph[u][v] < distance[v])) 
                {
                    distance[v] = distance[u] + graph[u][v];
                    Edges.get(v).setSrc(u);
                    visited += cities[v];
                }
            }
            
            // -- Tracing Part -- // 
            // Print the Tree Vertices (VT) and the Remaining Vertices (VT - V) 
            // for each iteration
            
            System.out.println("\nTree Vertices VT");
            tree += cities[u] + "(" + (cities[source].equals(cities[u]) ? "-" : cities[Edges.get(u).src]) + "," + distance[u] + ")\n";
            System.out.println(tree);
            
            System.out.println("Remaining Vertices V-VT");
            for (int j = 0; j < graphSize; j++) {
                if (!visitedVertex[j]) {
                    System.out.println(cities[j]
                            + "(" + (visited.contains(cities[j]) ? cities[Edges.get(j).src] : "-")
                            + "," + (distance[j] == INF ? "âˆž" : distance[j]) + ")");
                }
            }
            System.out.println("\n-----------------------------------------");

        }
        
        // Source is always Jeddah = 0
        // Print the final result
        // distances from Jeddah to all the cities
        System.out.println("\nShortest distances from " + cities[source] 
                + " to all other cities are:");
        
        for (int i = 0; i < distance.length; i++) {
            
            // from Jeddah to all cities but itself
            if (!cities[source].equals(cities[i])) { 
                
                System.out.println(String.format("Distance from %-6s to %-7s is %-4d km"
                    , cities[source], cities[i], distance[i]));
            }
        }

    }

    // Finding the minimum distance 
    private static int findMinDistance(int[] distance, boolean[] visitedVertex) {
        
        int minDistance = Integer.MAX_VALUE;
        int minDistanceVertex = -1;
        
        for (int i = 0; i < distance.length; i++) {
            // if vertex is unvisited + its distance is less than current
            // update vertex and its distance
            if (!visitedVertex[i] && distance[i] < minDistance) {
                minDistance = distance[i];
                minDistanceVertex = i;

            }
        }
        // return the vertex with the minimumm distance
        return minDistanceVertex;
    }

    static class edge {

        /**
         * edge Class to create edges for the graph
         * Constructor: edge(int src)
         *
         */

        int vert;
        int src;

        edge(int src) {
            this.src = src;
        }

        // Setters and getters
        public int getVert() {
            return vert;
        }

        public void setVert(int vert) {
            this.vert = vert;
        }

        public int getSrc() {
            return src;
        }

        public void setSrc(int src) {
            this.src = src;
        }
    }
}
