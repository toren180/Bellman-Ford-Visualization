package bellman.ford;
import java.util.HashMap;

public class Edge 
{
    int weight; 
    int vertex; 

    public Edge(int v, int w)
    {
        weight = w; 
        vertex = v; 
    }

    public String edgeToString(HashMap<Integer,String> keyMap)
    {
        return "{" + keyMap.get(vertex) + "," + weight + "}";
    }


}
