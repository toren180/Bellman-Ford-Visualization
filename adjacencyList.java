package bellman.ford;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet; 

/**
 * Represents a graph using an adjacency list data structure.
 * It also includes a naive algorithm for comparison.
 */
public class adjacencyList 
{
    // The main adjacency list, where each index represents a vertex
    // and holds a list of Edge objects.
    public ArrayList<ArrayList<Edge>> adjList; 
    // A map to convert integer indexes to node labels (e.g., 0 -> "S").
    public HashMap<Integer, String> keyMap;
    // An array to store the shortest path distances.
    public int[] shortestPath;

    /**
     * Initializes a new adjacency list with a specified number of vertices.
     * @param vertexes The number of vertices in the graph.
     */
    public adjacencyList(int vertexes)
    {
        adjList = new ArrayList<ArrayList<Edge>>();
        for (int i = 0; i < vertexes; i ++)
        {
            ArrayList<Edge> temp = new ArrayList<Edge>();
            adjList.add(temp);
        }

        keyMap = new HashMap<Integer, String>();
        shortestPath = new int[vertexes];
    }

    /**
     * Adds a new directed edge to the graph.
     * @param u The source vertex.
     * @param v The destination vertex.
     * @param w The weight of the edge.
     */
    public void addEdge(int u, int v, int w)
    {
        Edge edge = new Edge(v, w);
        adjList.get(u).add(edge);
    }

    /**
     * Retrieves the list of edges from a specific vertex.
     * @param vertex The vertex to get edges from.
     * @return An ArrayList of Edge objects.
     */
    public ArrayList<Edge> getEdges(int vertex)
    {
        return adjList.get(vertex);
    }

    /**
     * Returns the total number of vertices in the graph.
     * @return The size of the adjacency list.
     */
    public int getSize()
    {
        return adjList.size();
    }

    /**
     * Generates a string representation of the edges from a given vertex.
     * @param vertex The vertex to represent.
     * @return A string showing the vertex and its connected edges.
     */
    public String edgesToString(int vertex)
    {
        String ret = "";
        ret+= keyMap.get(vertex) + ": ";
        ArrayList<Edge> edges = getEdges(vertex);
        for (int i = 0; i < edges.size(); i ++)
        {
            ret += edges.get(i).edgeToString(keyMap) + " ";
        }
        return ret; 
    }

    /**
     * Implements a naive approach to finding the shortest path using a depth-first traversal.
     * This approach is for comparison purposes and does not handle negative cycles correctly.
     * @param src The source vertex.
     */
    public void naiveApproach(int src)
    {
        HashSet<Integer> vertexMap = new HashSet<Integer>();
        // Initialize shortest path distances to infinity, except for the source.
        for (int i = 0; i < shortestPath.length; i ++)
        {
            shortestPath[i] = Integer.MAX_VALUE;
        }
        shortestPath[src] = 0; 
        vertexMap.add(src);
        depthSearchHelper(src, 0, vertexMap); 
    }

    /**
     * Helper function for the naive depth-first search approach.
     * It recursively explores the graph to find paths and update distances.
     * @param curr The current vertex in the traversal.
     * @param currWeight The cumulative weight to the current vertex.
     * @param vertexMap A set to keep track of visited vertices to avoid cycles.
     */
    public void depthSearchHelper(int curr, int currWeight, HashSet<Integer> vertexMap)
    {
        ArrayList<Edge> currEdges = adjList.get(curr);
        for (int i = 0; i < currEdges.size(); i ++)
        {
            Edge currEdge = currEdges.get(i);
            int currVertex = currEdge.vertex;
            if (!vertexMap.contains(currVertex))
            {
                int newWeight = currWeight + currEdge.weight;
                if (newWeight < shortestPath[currVertex])
                {
                    shortestPath[currVertex] = newWeight;
                }
                vertexMap.add(currVertex);
                depthSearchHelper(currVertex, newWeight, vertexMap);
            }
        }
        vertexMap.remove(curr);
    }
}
