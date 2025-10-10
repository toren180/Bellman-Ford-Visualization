package bellman.ford;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;           
import javax.swing.JFrame;               

/**
 * Main application class for the Bellman-Ford algorithm visualization.
 * It sets up the graph, runs the algorithm, and manages the JSwing GUI.
 */
public class App 
{
    public String getGreeting() 
    {
        return "Hello World!";
    }

    public static void main(String[] args) throws InterruptedException 
    {
        // Set up the JSwing window and visualizer panel.
        JFrame frame = new JFrame("Graph Visualizer");
        GraphVisualizer panel = new GraphVisualizer();
        frame.add(panel);
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Create the graph using an adjacency list. The graph has 6 vertices.
        adjacencyList adjList = new adjacencyList(6); 
        // Add edges to the graph. The first number is the source vertex, the second is the destination, and the third is the weight.
        adjList.addEdge(0,5,8);
        adjList.addEdge(0,1,10);
        adjList.addEdge(1,3,2);
        adjList.addEdge(2,1,1);
        adjList.addEdge(3,2,-2);
        adjList.addEdge(4,1,-4);
        adjList.addEdge(4,3,-1);
        adjList.addEdge(5,4,1);
        
        // Map integer indexes to node letters for visualization purposes.
        adjList.keyMap.put(0,"S");
        adjList.keyMap.put(1,"A");
        adjList.keyMap.put(2,"B");
        adjList.keyMap.put(3,"C");
        adjList.keyMap.put(4,"D");
        adjList.keyMap.put(5,"E");

        // Print the graph's adjacency list to the console.
        for (int i = 0; i < adjList.adjList.size(); i ++)
        {
            System.out.println(adjList.edgesToString(i));
        }

        // Run the Bellman-Ford algorithm and print the result.
        System.out.println("\n\n Bellman Ford: " + Arrays.toString(bellmanFord(adjList,0, panel)));

        // Run the naive depth-first traversal approach for comparison.
        adjList.naiveApproach(0);
        System.out.println("Naive Approach: " + Arrays.toString(adjList.shortestPath));
    }

    /**
     * Implements the Bellman-Ford algorithm to find the shortest path from a source node to all other nodes.
     * It also visualizes the process by updating the graph's display.
     * @param adjList The adjacency list representation of the graph.
     * @param src The source vertex for the shortest path calculation.
     * @param graphVisualizer The visualizer object to update the graph's display.
     * @return An integer array containing the shortest distances from the source to each vertex. Returns {-1} if a negative cycle is detected.
     */
    public static int[] bellmanFord(adjacencyList adjList, int src, GraphVisualizer graphVisualizer) throws InterruptedException
    {
        GraphVisualizer.pause(500);
        graphVisualizer.updateNodeDistance("S", 0);

        int[] shortestPath = new int[adjList.getSize()]; // Array to store the shortest distance to each vertex.

        // Initialize all distances to infinity, except for the source node.
        for (int i = 0; i < shortestPath.length; i ++)
        {
            shortestPath[i] = Integer.MAX_VALUE;
        }

        shortestPath[src] = 0; // The distance to the source node is always 0.

        int numCycles = 0; 
        boolean notComplete = true; 

        // The algorithm runs V-1 times, where V is the number of vertices.
        // It runs one extra time to check for a negative cycle.
        while (numCycles < shortestPath.length && notComplete)
        {
            notComplete = false; 

            int index = 0; 
            // Iterate through all edges in the graph (V-1 times).
            while (index < shortestPath.length) 
            {
                if (shortestPath[index] < Integer.MAX_VALUE) // Check if the current vertex is reachable.
                {
                    ArrayList<Edge> currEdges = adjList.getEdges(index); // Get all edges from the current vertex.
                    for (int i = 0; i < currEdges.size(); i ++) // Iterate through all adjacent edges.
                    {
                        int currDist = shortestPath[index];
                        Edge currEdge = currEdges.get(i); 
                        int currVertex = currEdge.vertex; 
                        currDist += currEdge.weight; // Calculate the new path distance.
                        
                        // Visualize the edge being "relaxed" by turning it red.
                        graphVisualizer.highlightEdge(adjList.keyMap.get(index), adjList.keyMap.get(currVertex), Color.RED);
                        GraphVisualizer.pause(1000);

                        // If a shorter path is found, update the shortest distance.
                        if (currDist < shortestPath[currVertex]) 
                        {
                            shortestPath[currVertex] = currDist; 

                            // Update the visualization to show the shorter path in green.
                            graphVisualizer.updateNodeDistance(adjList.keyMap.get(currVertex), shortestPath[currVertex]);
                            graphVisualizer.highlightEdge(adjList.keyMap.get(index), adjList.keyMap.get(currVertex), Color.GREEN);
                            GraphVisualizer.pause(1000);
                            
                            // Set the flag to true to continue the main loop, as a relaxation occurred.
                            notComplete = true; 
                        }

                        // Reset the edge color to black after the relaxation check.
                        graphVisualizer.highlightEdge(adjList.keyMap.get(index), adjList.keyMap.get(currVertex), Color.BLACK);
                    }
                }
                index ++; 
            }

            numCycles++; 
            graphVisualizer.numCycles = numCycles + 1; // Update the cycle count for the visualizer.
        }

        // After V-1 iterations, if any edge is still being relaxed, there is a negative cycle.
        if (notComplete) 
        {
            int[] retArr = {-1};
            return retArr;
        }
        
        // If no negative cycles are found, highlight the entire graph as complete.
        graphVisualizer.complete(); 
        return shortestPath; 
    }
}
