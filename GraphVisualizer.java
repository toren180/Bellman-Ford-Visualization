package bellman.ford;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics; 
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 * The GraphVisualizer class is a JPanel that handles the graphical representation of the graph.
 * It uses JSwing to draw nodes, edges, and update the display based on the algorithm's state.
 */
public class GraphVisualizer extends JPanel 
{

    // A map to store the (x, y) coordinates for each node to control the layout.
    private final Map<String, Point> nodePositions = new HashMap<>();
    // A map to store the color of each edge, used for highlighting.
    private final Map<String, Color> edgeColors = new HashMap<>();
    // A map to store the current shortest distance to each node.
    private Map<String, Integer> nodeDistances = new HashMap<>();
    // A counter to track the number of cycles completed by the Bellman-Ford algorithm.
    public int numCycles = 1; 

    /**
     * Constructs the GraphVisualizer and initializes the node positions and distances.
     */
    public GraphVisualizer() 
    {
        setBackground(Color.WHITE);
        // Manually place nodes for a clear, readable graph layout.
        nodePositions.put("S", new Point(100, 100));
        nodePositions.put("E", new Point(300, 100));
        nodePositions.put("D", new Point(500, 100));
        nodePositions.put("A", new Point(150, 300));
        nodePositions.put("C", new Point(350, 300));
        nodePositions.put("B", new Point(250, 500));
        
        // Initialize distances for each node. The source node "S" is 0,
        // while all other nodes are set to infinity (Integer.MAX_VALUE).
        nodeDistances.put("S",0);
        nodeDistances.put("A",Integer.MAX_VALUE);
        nodeDistances.put("B",Integer.MAX_VALUE);
        nodeDistances.put("C",Integer.MAX_VALUE);
        nodeDistances.put("D",Integer.MAX_VALUE);
        nodeDistances.put("E",Integer.MAX_VALUE);

    }

    /**
     * This method is called by the JSwing framework to render the graph.
     * It draws the nodes, edges, and text labels.
     * @param g The Graphics object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        
        // Set font and color for the informational labels at the top of the window.
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.setColor(Color.BLACK);

        // Draw labels showing the current cycle number and the traversal order.
        String labelText = "Cycle # " + numCycles;
        g2.drawString(labelText, 70, 30);
        labelText = "Traversal Order: S > A > B > C > D > E";
        g2.drawString(labelText, 220, 30);
        g2.setStroke(new BasicStroke(2));
        g2.setFont(new Font("Arial", Font.BOLD, 14));

        // Draw all the predefined edges (arrows) for the graph.
        drawArrow(g2, "S", "A", 10);
        drawArrow(g2, "S", "E", 8);
        drawArrow(g2, "A", "C", 2);
        drawArrow(g2, "B", "A", 1);
        drawArrow(g2, "C", "B", -2);
        drawArrow(g2, "D", "A", -4);
        drawArrow(g2, "D", "C", -1);
        drawArrow(g2, "E", "D", 1);

        // Iterate through each node and draw its circle and label.
        for (String node : nodePositions.keySet()) 
        {
            Point p = nodePositions.get(node);
        
            // Draw the node circle (light gray with a black outline).
            Color fillColor = Color.LIGHT_GRAY;
            g2.setColor(fillColor);
            g2.fillOval(p.x - 20, p.y - 20, 40, 40);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - 20, p.y - 20, 40, 40);
        
            // Prepare the label text, including the current shortest distance.
            String label = node;
            if (nodeDistances.containsKey(node)) 
            {
                int distance = nodeDistances.get(node);
                if (distance == Integer.MAX_VALUE) 
                {
                    label += " (∞)";
                } else {
                    label += " (" + distance + ")";
                }
            }
        
            // Center the label horizontally and position it below the node.
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(label);
            int offset = 0; 
            if (node == "S")
            {
                offset = -10; 
            }
            if (node == "C")
            {
                offset = 5; 
            }
            if (node == "A")
            {
                offset = -5; 
            }
            int labelX = (p.x - labelWidth / 2) + offset;
            int labelY = p.y + 45;
            g2.setColor(Color.BLUE);
            g2.drawString(label, labelX, labelY);
        }
    }

    /**
     * Draws an arrow representing a directed edge between two nodes.
     * It also draws the edge's weight and handles color highlighting.
     * @param g2 The Graphics2D object for drawing.
     * @param from The starting node's key.
     * @param to The ending node's key.
     * @param weight The weight of the edge.
     */
    private void drawArrow(Graphics2D g2, String from, String to, int weight) 
    {
        // Get coordinates of the source and destination nodes
        Point p1 = nodePositions.get(from);
        Point p2 = nodePositions.get(to);
    
        // Calculate the angle between the nodes to correctly orient the arrow.
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double angle = Math.atan2(dy, dx);
    
        // Shorten the start and end of the line so it doesn't overlap the node circles.
        int shorten = 20;
        int startX = (int) (p1.x + shorten * Math.cos(angle));
        int startY = (int) (p1.y + shorten * Math.sin(angle));
        int endX = (int) (p2.x - shorten * Math.cos(angle));
        int endY = (int) (p2.y - shorten * Math.sin(angle));
    
        // Get the current color for the edge from the edgeColors map.
        Color color = edgeColors.getOrDefault(from + "->" + to, Color.BLACK);
        g2.setColor(color);
        g2.drawLine(startX, startY, endX, endY);
        int offset = 0;

        // Apply a small offset to the weight label for better readability on specific edges.
        if((from.equals("D") && to.equals("C" ))||(from.equals("C") && to.equals("B")))
        {
            offset = -10; 
        }
        // Draw the weight label near the middle of the line.
        int labelX = ((startX + endX) / 2) + offset;
        int labelY = (startY + endY) / 2 - 10;
        g2.drawString(String.valueOf(weight), labelX, labelY);

        // Define the arrowhead shape.
        int arrowSize = 10;
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(0, 0);
        arrowHead.addPoint(-arrowSize, -arrowSize / 2);
        arrowHead.addPoint(-arrowSize, arrowSize / 2);
    
        // Create a new graphics context for the arrowhead to apply rotation and translation.
        Graphics2D g = (Graphics2D) g2.create();
        g2.setColor(color);
    
        // Translate and rotate the graphics context to draw the arrowhead at the correct position and angle.
        g.translate(endX, endY);
        g.rotate(angle);
        g.fill(arrowHead);
        g.dispose();    
    }

    /**
     * Highlights all edges in green to indicate the algorithm is complete and the final paths are found.
     */
    public void complete()
    {
        highlightEdge("S", "A", Color.GREEN);
        highlightEdge("S", "E", Color.GREEN);
        highlightEdge("A", "C", Color.GREEN);
        highlightEdge("B", "A", Color.GREEN);
        highlightEdge("C", "B", Color.GREEN);
        highlightEdge("D", "A", Color.GREEN);
        highlightEdge("D", "C", Color.GREEN);
        highlightEdge("E", "D", Color.GREEN);
    }

    /**
     * Changes the color of a specific edge and triggers a repaint to update the display.
     * @param from The starting node's key.
     * @param to The ending node's key.
     * @param color The new color for the edge.
     */
    public void highlightEdge(String from, String to, Color color) 
    {
        edgeColors.put(from + "->" + to, color);
        repaint();
    }

    /**
     * Pauses the program's execution for a specified number of milliseconds.
     * This is used to slow down the visualization for better viewing.
     * @param milliseconds The duration to pause in milliseconds.
     */
    public static void pause(int milliseconds) 
    {
        try 
        {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) 
        {
            e.printStackTrace();
            Thread.currentThread().interrupt(); 
        }
    }

    /**
     * Updates the displayed distance for a specific node and repaints the graph.
     * @param node The node's key to update.
     * @param distance The new shortest distance value.
     */
    public void updateNodeDistance(String node, int distance) 
    {
        nodeDistances.put(node, distance);
        repaint();
    }

}