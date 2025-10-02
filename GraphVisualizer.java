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

public class GraphVisualizer extends JPanel 
{

    // Node positions (for layout)
    private final Map<String, Point> nodePositions = new HashMap<>();
    private final Map<String, Color> edgeColors = new HashMap<>();
    private Map<String, Integer> nodeDistances = new HashMap<>();
    public int numCycles = 1; 

    public GraphVisualizer() 
    {
        // Manually place nodes (x, y) coordinates
        setBackground(Color.WHITE);
        nodePositions.put("S", new Point(100, 100));
        nodePositions.put("E", new Point(300, 100));
        nodePositions.put("D", new Point(500, 100));
        nodePositions.put("A", new Point(150, 300));
        nodePositions.put("C", new Point(350, 300));
        nodePositions.put("B", new Point(250, 500));
        
        //Add in distances for each node to the NodeDistance map
        nodeDistances.put("S",0);
        nodeDistances.put("A",Integer.MAX_VALUE);
        nodeDistances.put("B",Integer.MAX_VALUE);
        nodeDistances.put("C",Integer.MAX_VALUE);
        nodeDistances.put("D",Integer.MAX_VALUE);
        nodeDistances.put("E",Integer.MAX_VALUE);

    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        
        super.paintComponent(g);
        //System.out.println("Calling paintcomponent");

        Graphics2D g2 = (Graphics2D) g;
        // Set font for the label
        g2.setFont(new Font("Arial", Font.PLAIN, 18)); // Medium size font

        // Set color for the label
        g2.setColor(Color.BLACK);

        // Draw label at the top-left
        String labelText = "Cycle # " + numCycles;
        g2.drawString(labelText, 70, 30); // (x=20, y=30)
        labelText = "Traversal Order: S > A > B > C > D > E";
        g2.drawString(labelText, 220, 30);
        g2.setStroke(new BasicStroke(2));
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        //drawSimpleArrow(g2,100, 100, 300, 200);

        // Draw edges (arrows)
        drawArrow(g2, "S", "A", 10);
        drawArrow(g2, "S", "E", 8);
        drawArrow(g2, "A", "C", 2);
        drawArrow(g2, "B", "A", 1);
        drawArrow(g2, "C", "B", -2);
        drawArrow(g2, "D", "A", -4);
        drawArrow(g2, "D", "C", -1);
        drawArrow(g2, "E", "D", 1);

        //drawSimpleArrow(g2, 100,100,150,30);

        for (String node : nodePositions.keySet()) 
        {
            Point p = nodePositions.get(node);
        
            // Draw node circle
            Color fillColor = Color.LIGHT_GRAY;
            g2.setColor(fillColor);
            g2.fillOval(p.x - 20, p.y - 20, 40, 40);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - 20, p.y - 20, 40, 40);
        
            // Prepare node label text
            String label = node;
            if (nodeDistances.containsKey(node)) 
            {
                int distance = nodeDistances.get(node);
                if (distance == Integer.MAX_VALUE) 
                {
                    //System.out.println("changing to infinity");
                    label += " (∞)";
                } else {
                    label += " (" + distance + ")";
                }
            }
        
            // Calculate label width
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
            // Center label horizontally, and draw it slightly BELOW the node

            int labelX = (p.x - labelWidth / 2) + offset;
            int labelY = p.y + 45; // 40 pixels below center (circle radius is 20)
            g2.setColor(Color.BLUE);
            g2.drawString(label, labelX, labelY);
        }
    }


    private void drawArrow(Graphics2D g2, String from, String to, int weight) 
    {
        //System.out.println("DRWAING ARROWS");

        // Get coordinates of the source and destination nodes
        Point p1 = nodePositions.get(from);
        Point p2 = nodePositions.get(to);
    
        // Calculate the angle between the nodes
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double angle = Math.atan2(dy, dx);
    
        // Shorten the start and end to avoid overlapping circles
        int shorten = 20;
        int startX = (int) (p1.x + shorten * Math.cos(angle));
        int startY = (int) (p1.y + shorten * Math.sin(angle));
        int endX = (int) (p2.x - shorten * Math.cos(angle));
        int endY = (int) (p2.y - shorten * Math.sin(angle));
    
        // Draw the line
        Color color = edgeColors.getOrDefault(from + "->" + to, Color.BLACK);
        //System.out.println(edgeColors.getOrDefault(from + "->" + to, Color.BLACK));
        g2.setColor(color);
        g2.drawLine(startX, startY, endX, endY);
        int offset = 0;

        //Offset 
        if((from.equals("D") && to.equals("C" ))||(from.equals("C") && to.equals("B")))
        {
            offset = -10; 
        }
        // Draw the weight label near the middle of the line
        int labelX = ((startX + endX) / 2) + offset;
        int labelY = (startY + endY) / 2 - 10;
        g2.drawString(String.valueOf(weight), labelX, labelY);

        // Define the arrowhead shape
        int arrowSize = 10;
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(0, 0);
        arrowHead.addPoint(-arrowSize, -arrowSize / 2);
        arrowHead.addPoint(-arrowSize, arrowSize / 2);
    
        // Create a new graphics context for the arrowhead
        Graphics2D g = (Graphics2D) g2.create();
        //AffineTransform tx = new AffineTransform();
        g2.setColor(color);
    
        // 💥 Place the arrow at (endX, endY), not the middle!
        g.translate(endX, endY);    // move to endpoint
        g.rotate(angle);        // rotate to line's angle
        g.fill(arrowHead);      // draw the arrowhead
        g.dispose();    
    }

    // Turns the entire graph green
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

    //Turns an edge to a specific color 
    public void highlightEdge(String from, String to, Color color) 
    {
        edgeColors.put(from + "->" + to, color);
        //System.out.println("setting color to green");
        repaint();
    }

    // Pauses graph
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

    //Updates distance in hashmap when shortestPath array is changed
    public void updateNodeDistance(String node, int distance) 
    {
        nodeDistances.put(node, distance);
        repaint();
    }

}