## Bellman-Ford Visualization DSA Final Project - Toren Snyder

### Description:
Implements the Bellman-Ford algorithm in Java and visualizes it using Jswing. 
As the algorithm runs and the shortest paths to vertices are calculated, the display is updated. 
Edges change turn red when relaxed and green when a shorter path is found. 
Also contains an implementation of the naive approach using depth-first traversal. 

### Files:
- App.java: Contains Bellman-Ford and Main. 
- GraphVisualizer.java: Contains the Jswing visualization. 
- Edge.java: Contains the Edge class, which represents edges in the graph. 
- adjacencyList.java: Contains the graph represented by an adjacencyList that holds Edge objects, and a naive algorithm. 

### To Run:
1. Run the main class (App)
2. A window will open displaying the graph.
3. The Bellman-Ford algorithm will start automatically. 
   Relaxed edges will be highlighted in red, then green if they contain a shorter path to a node. 
   Labels beneath the node update show the shortest known distances to the node, taken from the shortestPath array. 
   Labels at the top show the # of Cycles and the traversal path the algorithm takes. 

### Notes:
- Changing the src Node on Bellman-Ford will likely disrupt the visuals. The visuals were designed with S as the source. 

### Video

https://github.com/user-attachments/assets/ef29a842-f51b-41b3-8554-22bbeb14276b

