package bellman.ford;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet; 

public class adjacencyList 
{
    public ArrayList<ArrayList<Edge>> adjList; 
    public HashMap<Integer, String> keyMap;
    public int[] shortestPath;

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

    public void addEdge(int u, int v, int w)
    {
        Edge edge = new Edge(v, w);
        adjList.get(u).add(edge);
    }

    public ArrayList<Edge> getEdges(int vertex)
    {
        return adjList.get(vertex);
    }

    public int getSize()
    {
        return adjList.size();
    }

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

    public void naiveApproach(int src)
    {
        HashSet<Integer> vertexMap = new HashSet<Integer>();
        for (int i = 0; i < shortestPath.length; i ++)
        {
            shortestPath[i] = Integer.MAX_VALUE;
        }
        shortestPath[src] = 0; 
        vertexMap.add(src);
        depthSearchHelper(src, 0, vertexMap); 
    }

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
