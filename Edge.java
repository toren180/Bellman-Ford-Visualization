package bellman.ford;
import java.util.HashMap;

/**
 * Represents a directed edge in the graph.
 * It holds the destination vertex and the weight of the edge.
 */
public class Edge 
{
    int weight; 
    int vertex; 

    /**
     * Constructs a new Edge.
     * @param v The destination vertex.
     * @param w The weight of the edge.
     */
    public Edge(int v, int w)
    {
        weight = w; 
        vertex = v; 
    }

    /**
     * Generates a string representation of the edge.
     * @param keyMap A map to convert the integer vertex to its string label.
     * @return A string in the format "{vertex_label, weight}".
     */
    public String edgeToString(HashMap<Integer,String> keyMap)
    {
        return "{" + keyMap.get(vertex) + "," + weight + "}";
    }
}
