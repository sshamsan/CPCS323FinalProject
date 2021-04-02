
import javafx.util.Pair;
import java.util.*;

/**
 * CPCS324 - DAR | Project Phase 2: Minimmumm Spanning Trees
 *
 * ALgorithms: Priority Queue Based Prim
 * Minimum Heap Based Prim 
 * Kruskal's 
 * 
 * Group Members: Shahad Shamsan, Suha Shafi, Safia Aljahdali
 */
public class CPCS324PrjPhase2 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //Display menu and asks user to enter n and m
        System.out.println("\t\t\t-- CPCS3234 Project Phase 2 -- ");
        System.out.print("\tThis Project will create a graph "
                + "and find the Minimumm Spanning Tree using three Algorithms: \n"
                + "\t\t\t1. Minimum Heap Based Prim Algorithm\n"
                + "\t\t\t2. Priority Queue Based Prim Algorithm\n"
                + "\t\t\t3. Kruskal's Algorithm\n"
                + "\n\n"
                + "Enter the number of Vertices: ");
        int vertices = input.nextInt();
        System.out.print("Enter the number of Edges: ");
        int edges = input.nextInt();
        System.out.println("");

        // Create a graph Object and make the graph depending on the user input
        Graph graph = new Graph(vertices, edges);
        graph.make_graph(graph);

        // Calculate the runtime for each algorithm
        // Execute Minimum Heap Based Prim 
        long stPH = System.currentTimeMillis();
        graph.primMH();
        long etPH = System.currentTimeMillis();
        long ttPH = etPH - stPH;

        // Execute Priority Q Based Prim 
        long stPQ = System.currentTimeMillis();
        graph.primPQ();
        long etPQ = System.currentTimeMillis();
        long ttPQ = etPQ - stPQ;

        // Execute Kruskal's
        long stK = System.currentTimeMillis();
        graph.kruskalMST();
        long etK = System.currentTimeMillis();
        long ttK = etK - stK;

        System.out.println("\n\nRunning Time of the Algorithms (in milliseconds):\n"
                + "1. Minimum Heap Based Prim Algorithm: " + ttPH + " \n"
                + "2. Priority Queue Based Prim Algorithm: " + ttPQ + " \n"
                + "3. Kruskal's Algorithm: " + ttK + " \n");

    }

    // Edge Class, used to create edge objects in the Make_Graph 
    static class Edge {

        int source;
        int destination;
        int weight;

        // Constructor
        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    // used to create node for heap
    static class HeapNode {
        int vertex;
        int key;
    }

    // the result of set
    static class ResultSet {
        int parent;
        int weight;
    }

    static class Graph {

        /**
         * Graph Class Description: Contains all the methods to manipulate the
         * Graph Constructor: Graph(int vertices, int edges)
         *
         */
        int vertices;
        int edges;

        // Each index is a list (Graph representation)
        LinkedList<Edge>[] adjacencylist;

        // Graph Constructor
        Graph(int vertices, int edges) {
            this.vertices = vertices;
            this.edges = edges;
            adjacencylist = new LinkedList[vertices];
            //initialize adjacency lists for all the vertices
            for (int i = 0; i < vertices; i++) {
                adjacencylist[i] = new LinkedList<>();
            }
        }

        // to add an edge in the graph
        public void addEdge(int source, int destination, int weight) {
            Edge edge = new Edge(source, destination, weight);
            adjacencylist[source].addFirst(edge);

            edge = new Edge(destination, source, weight);
            adjacencylist[destination].addFirst(edge); //for undirected graph
        }

        // check if two vertices are connected
        public boolean isConnected(int src, int dest, LinkedList<Edge>[] allEdges) {
            for (LinkedList<Edge> i : allEdges) {
                for (Edge edge : i) {
                    if ((edge.source == src && edge.destination == dest)
                            || (edge.source == dest && edge.destination == src)) {
                        return true;
                    }
                }
            }
            return false;
        }

        // to make the graph (we enter n and m)
        public void make_graph(Graph graph) {
            // Random object
            Random random = new Random();
            // ensure that all vertices are connected
            for (int i = 0; i < vertices-1; i++) {
                    int w = random.nextInt(10) + 1;
                    addEdge(i,i+1,w); 
            }
            
            // generate the rest of edges randomly
            int rem_edges = edges-vertices-1;
            for (int i = 0; i < rem_edges; i++) {
                // Generate random vertices and edges
                int source = random.nextInt(graph.vertices);
                int dest = random.nextInt(graph.vertices);
                
                // avoid duplicate edges
                if (dest == source || isConnected(source, dest, graph.adjacencylist)) { 
                    i--;
                    continue;
                }
                // generate random weights in range 0 to 10
                int weight = random.nextInt(10) + 1;
                addEdge(source, dest, weight);
                
                // Print the full graph
                System.out.println(source + " - " + dest + ", weight= " + weight);

            }
        }
        //--------------------------------------------------------------------//

        public void primPQ() {

            boolean[] mst = new boolean[vertices];
            ResultSet[] resultSet = new ResultSet[vertices];
            int[] key = new int[vertices];  
            // keys used to store the key 
            // to update priority queue when needed

            //Initialize all the keys to infinity and
            //initialize resultSet for all the vertices
            for (int i = 0; i < vertices; i++) {
                key[i] = Integer.MAX_VALUE;
                resultSet[i] = new ResultSet();
            }

            //Initialize priority queue
            //override the comparator to do the sorting based keys
            PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(vertices, new Comparator<Pair<Integer, Integer>>() {
                @Override
                public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
                    //sort using key values
                    int key1 = p1.getKey();
                    int key2 = p2.getKey();
                    return key1 - key2;
                }
            });

            //create the pair for for the first index, 0 key 0 index
            key[0] = 0;
            Pair<Integer, Integer> p0 = new Pair<>(key[0], 0);
            //add it to pq
            pq.offer(p0);

            resultSet[0] = new ResultSet();
            resultSet[0].parent = -1;

            //while priority queue is not empty
            while (!pq.isEmpty()) {
                //extract the min
                Pair<Integer, Integer> extractedPair = pq.poll();

                //extracted vertex
                int extractedVertex = extractedPair.getValue();
                mst[extractedVertex] = true;

                //iterate through all the adjacent vertices and update the keys
                LinkedList<Edge> list = adjacencylist[extractedVertex];
                for (int i = 0; i < list.size(); i++) {
                    Edge edge = list.get(i);
                    //only if edge destination is not present in mst
                    if (mst[edge.destination] == false) {
                        int destination = edge.destination;
                        int newKey = edge.weight;
                        //check if updated key < existing key, if yes, update if
                        if (key[destination] > newKey) {
                            //add it to the priority queue
                            Pair<Integer, Integer> p = new Pair<>(newKey, destination);
                            pq.offer(p);
                            //update the resultSet for destination vertex
                            resultSet[destination].parent = extractedVertex;
                            resultSet[destination].weight = newKey;
                            //update the key[]
                            key[destination] = newKey;
                        }
                    }
                }
            }
            //print minimumm spanning tree
            System.out.println("\n-- Priority Queue Based Prim's -- ");
            printMST(resultSet);
        }

        public void primMH() {

            boolean[] inHeap = new boolean[vertices];
            ResultSet[] resultSet = new ResultSet[vertices];
            //keys[] used to store the key
            // to update min heap when needed
            int[] key = new int[vertices];
            
            //create heapNode for all the vertices
            HeapNode[] heapNodes = new HeapNode[vertices];
            for (int i = 0; i < vertices; i++) {
                heapNodes[i] = new HeapNode();
                heapNodes[i].vertex = i;
                heapNodes[i].key = Integer.MAX_VALUE;
                resultSet[i] = new ResultSet();
                resultSet[i].parent = -1;
                inHeap[i] = true;
                key[i] = Integer.MAX_VALUE;
            }

            //decrease the key for the first index
            heapNodes[0].key = 0;

            //add all the vertices to the MinHeap
            MinHeap minHeap = new MinHeap(vertices);
            //add all the vertices to priority queue
            for (int i = 0; i < vertices; i++) {
                minHeap.insert(heapNodes[i]);
            }

            //while minHeap is not empty
            while (!minHeap.isEmpty()) {
                //extract the min
                HeapNode extractedNode = minHeap.extractMin();

                //extracted vertex
                int extractedVertex = extractedNode.vertex;
                inHeap[extractedVertex] = false;

                //iterate through all the adjacent vertices
                LinkedList<Edge> list = adjacencylist[extractedVertex];
                for (int i = 0; i < list.size(); i++) {
                    Edge edge = list.get(i);
                    //only if edge destination is present in heap
                    if (inHeap[edge.destination]) {
                        int destination = edge.destination;
                        int newKey = edge.weight;
                        //check if updated key < existing key, if yes, update if
                        if (key[destination] > newKey) {
                            decreaseKey(minHeap, newKey, destination);
                            //update the parent node for destination
                            resultSet[destination].parent = extractedVertex;
                            resultSet[destination].weight = newKey;
                            key[destination] = newKey;
                        }
                    }
                }
            }
            // Print Minimumm Spanning Tree
            System.out.println("\n-- Minimumm Heap Based Prim's -- ");
            printMST(resultSet);
        }

        public void decreaseKey(MinHeap minHeap, int newKey, int vertex) {

            //get the index which key's needs a decrease;
            int index = minHeap.indexes[vertex];

            //get the node and update its value
            HeapNode node = minHeap.mH[index];
            node.key = newKey;
            minHeap.bubbleUp(index);
        }

        // Method to Print the minimumm spanning tree
        // for the min heap and pq prim
        public void printMST(ResultSet[] resultSet) {
            int total_min_weight = 0;
            System.out.println("Minimum Spanning Tree: ");
            for (int i = 1; i < vertices; i++) {
                System.out.println("Edge: " + i + " - " + resultSet[i].parent
                        + " weight: " + resultSet[i].weight);
                total_min_weight += resultSet[i].weight;
            }
            System.out.println("Total minimum key: " + total_min_weight);
        }

        /**
         * Kruskal's Algorithm Description:
         *
         *
         * Methods used: KurskalMST() makeSet(int[] parent) find(int[] parent,
         * int vertex) union(int[] parent, int x, int y)
         * printGraph(LinkedList<Edge> edgeList)
         */
        public void kruskalMST() {
            LinkedList<Edge>[] allEdges = adjacencylist.clone(); // added the clone
            PriorityQueue<Edge> pq = new PriorityQueue<>(edges, Comparator.comparingInt(o -> o.weight));

            //add all the edges to priority queue, //sort the edges on weights
            for (int i = 0; i < allEdges.length; i++) {
                for (int j = 0; j < allEdges[i].size(); j++) {
                    pq.add(allEdges[i].get(j));
                }
            }

            //create a parent []
            int[] parent = new int[vertices];

            //makeset
            makeSet(parent);

            LinkedList<Edge> mst = new LinkedList<>();

            //process vertices - 1 edges
            int index = 0;
            while (index < vertices - 1 && !pq.isEmpty()) {
                Edge edge = pq.remove();
                //check if adding this edge creates a cycle
                int x_set = find(parent, edge.source);
                int y_set = find(parent, edge.destination);

                if (x_set == y_set) {
                    //ignore, will create cycle
                } else {
                    //add it to our final result
                    mst.add(edge);
                    index++;
                    union(parent, x_set, y_set);
                }
            }
            //print MST
            System.out.println("\n-- Kruskal's --\nMinimum Spanning Tree: ");
            printGraph(mst);
        }

        public void makeSet(int[] parent) {
            //Make set: creating a new element with a parent pointer to itself.

            for (int i = 0; i < vertices; i++) {
                parent[i] = i;
            }
        }

        public int find(int[] parent, int vertex) {
            //chain of parent pointers from x upwards through the tree
            // until an element is reached whose parent is itself
            if (parent[vertex] != vertex) {
                return find(parent, parent[vertex]);
            };
            return vertex;
        }

        // union the set
        public void union(int[] parent, int x, int y) {
            int x_set_parent = find(parent, x);
            int y_set_parent = find(parent, y);
            //make x as parent of y
            parent[y_set_parent] = x_set_parent;
        }

        // method to print the minimumm spanning tree by kruskals
        public void printGraph(LinkedList<Edge> edgeList) {
            for (int i = 0; i < edgeList.size(); i++) {
                Edge edge = edgeList.get(i);
                System.out.println("Edge-" + i + " source: " + edge.source
                        + " destination: " + edge.destination
                        + " weight: " + edge.weight);
            }
        }

    }

    static class MinHeap {

        int capacity;
        int currentSize;
        HeapNode[] mH;
        //will be used to decrease the key
        int[] indexes; 

        public MinHeap(int capacity) {
            this.capacity = capacity;
            mH = new HeapNode[capacity + 1];
            indexes = new int[capacity];
            mH[0] = new HeapNode();
            mH[0].key = Integer.MIN_VALUE;
            mH[0].vertex = -1;
            currentSize = 0;
        }

        public void display() {
            for (int i = 0; i <= currentSize; i++) {
                System.out.println(" " + mH[i].vertex + "   key   " + mH[i].key);
            }
            System.out.println("________________________");
        }

        public void insert(HeapNode x) {
            currentSize++;
            int idx = currentSize;
            mH[idx] = x;
            indexes[x.vertex] = idx;
            bubbleUp(idx);
        }

        public void bubbleUp(int pos) {
            int parentIdx = pos / 2;
            int currentIdx = pos;
            while (currentIdx > 0 && mH[parentIdx].key > mH[currentIdx].key) {
                HeapNode currentNode = mH[currentIdx];
                HeapNode parentNode = mH[parentIdx];

                //swap the positions
                indexes[currentNode.vertex] = parentIdx;
                indexes[parentNode.vertex] = currentIdx;
                swap(currentIdx, parentIdx);
                currentIdx = parentIdx;
                parentIdx = parentIdx / 2;
            }
        }

        public HeapNode extractMin() {
            HeapNode min = mH[1];
            HeapNode lastNode = mH[currentSize];
//            update the indexes[] and move the last node to the top
            indexes[lastNode.vertex] = 1;
            mH[1] = lastNode;
            mH[currentSize] = null;
            sinkDown(1);
            currentSize--;
            return min;
        }

        public void sinkDown(int k) {
            int smallest = k;
            int leftChildIdx = 2 * k;
            int rightChildIdx = 2 * k + 1;
            if (leftChildIdx < heapSize() && mH[smallest].key > mH[leftChildIdx].key) {
                smallest = leftChildIdx;
            }
            if (rightChildIdx < heapSize() && mH[smallest].key > mH[rightChildIdx].key) {
                smallest = rightChildIdx;
            }
            if (smallest != k) {

                HeapNode smallestNode = mH[smallest];
                HeapNode kNode = mH[k];

                //swap the positions
                indexes[smallestNode.vertex] = k;
                indexes[kNode.vertex] = smallest;
                swap(k, smallest);
                sinkDown(smallest);
            }
        }

        public void swap(int a, int b) {
            HeapNode temp = mH[a];
            mH[a] = mH[b];
            mH[b] = temp;
        }

        public boolean isEmpty() {
            return currentSize == 0;
        }

        public int heapSize() {
            return currentSize;
        }
    }

}
