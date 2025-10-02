
package bellman.ford;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;           

import javax.swing.JFrame;               

public class App 
{
    public String getGreeting() {
        return "Hello World!";
    }
    public static void main(String[] args) throws InterruptedException {

        //Visual stuff
        JFrame frame = new JFrame("Graph Visualizer");
        GraphVisualizer panel = new GraphVisualizer();
        frame.add(panel);
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        adjacencyList adjList = new adjacencyList(6); //Creates graph using adj list
        adjList.addEdge(0,5,8);
        adjList.addEdge(0,1,10);
        adjList.addEdge(1,3,2);
        adjList.addEdge(2,1,1);
        adjList.addEdge(3,2,-2);
        adjList.addEdge(4,1,-4);
        adjList.addEdge(4,3,-1);
        adjList.addEdge(5,4,1);
        
        //Maps indexes to letters that represent nodes on the graph, mostly for visualization purposes
        adjList.keyMap.put(0,"S");
        adjList.keyMap.put(1,"A");
        adjList.keyMap.put(2,"B");
        adjList.keyMap.put(3,"C");
        adjList.keyMap.put(4,"D");
        adjList.keyMap.put(5,"E");

        //prints out adj list
        for (int i = 0; i < adjList.adjList.size(); i ++)
        {
            System.out.println(adjList.edgesToString(i));
        }

        System.out.println("\n\n Bellman Ford: " + Arrays.toString(bellmanFord(adjList,0, panel)));

        adjList.naiveApproach(0);
        System.out.println("Naive Approach: " + Arrays.toString(adjList.shortestPath));
    }

    //Returns shortest path from src node to every other node in the graph. If there is a negative cycle returns {-1}
    //Warning: changing src will mess it up, I had to modify it for the visualization and that involved setting a single node as src, sorry
    public static int[] bellmanFord(adjacencyList adjList, int src, GraphVisualizer graphVisualizer) throws InterruptedException
    {
        GraphVisualizer.pause(1000);
        graphVisualizer.updateNodeDistance("S", 0);

        int[] shortestPath = new int[adjList.getSize()]; //stores shortest path to every vertex from src

        for (int i = 0; i < shortestPath.length; i ++)
        {
            shortestPath[i] = Integer.MAX_VALUE;
        }

        shortestPath[src] = 0; //Set all shortest paths to infinity except for src

        int numCycles = 0; 
        boolean notComplete = true; 

        //Runs V - 1 times, and one extra to check for a negative cycle. Ends early if all shortest paths have been found. 
        while (numCycles < shortestPath.length && notComplete)
        {
            notComplete = false; 

            int index = 0; 
            while (index < shortestPath.length) //Runs E times. E = number of edges in the graph. 
            {
                if (shortestPath[index] < Integer.MAX_VALUE) //If 
                {
                    ArrayList<Edge> currEdges = adjList.getEdges(index); //List of curr vertex adjacent vertexes
                    for (int i = 0; i < currEdges.size(); i ++) //Iterates through currEdges
                    {
                        int currDist = shortestPath[index];
                        Edge currEdge = currEdges.get(i); 
                        int currVertex = currEdge.vertex; //currentAdjacentVertex
                        currDist += currEdge.weight; //currDist = distance it takes to reach currentAdjacent vertex through this path. 
                        graphVisualizer.highlightEdge(adjList.keyMap.get(index), adjList.keyMap.get(currVertex), Color.RED); //turns edge red when it is checked
                        GraphVisualizer.pause(2000);

                        if (currDist < shortestPath[currVertex]) //If the distance of the current path to the vertex is shorter than the current known shortest distance 
                        {
                            shortestPath[currVertex] = currDist; //Update shortest distance

                            //Visual stuff
                            graphVisualizer.updateNodeDistance(adjList.keyMap.get(currVertex), shortestPath[currVertex]);
                            graphVisualizer.highlightEdge(adjList.keyMap.get(index), adjList.keyMap.get(currVertex), Color.GREEN);
                            GraphVisualizer.pause(2000);
                            
                            notComplete = true; //Confirms that there is still shorter paths to be found, keeps while loop running
                        }

                        //More visual stuff
                        graphVisualizer.highlightEdge(adjList.keyMap.get(index), adjList.keyMap.get(currVertex), Color.BLACK);
                        //GraphVisualizer.pause(500);
                    }
                }
                index ++; 
            }

            numCycles++; 
            graphVisualizer.numCycles = numCycles + 1; //Tracks num of cycles for visual 
        }
        if (notComplete) //If edges are relaxed more than V - 1 times and there still a path getting shorter, a negative cycle exists 
        {
            int[] retArr = {-1};
            return retArr;
        }
        graphVisualizer.complete(); //Visual 
        return shortestPath; 
    }


}
