import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.HashMap;
import javax.swing.*;
// import .Node;
// import .Edge;

@SuppressWarnings("serial")
public class DrawGraph extends JPanel {

   private static final int PREF_W = 800; //Preferred width
   private static final int PREF_H = 650; //Preferred height
   private static final int BORDER_GAP = 30;  //Gap from screen border
   private static final Color TREE_COLOR = Color.green; //Color for tree edge
   private static final Color GRAPH_COLOR = Color.gray; //Color for graph edge
   private static final Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180); //Color of graph node
   private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
   private static final int GRAPH_POINT_WIDTH = 12;
    private final HashMap<Node, double[]> nodes_dir;
    private Edge edges[];
   private Node nodes[];
   private int n;
   private int cnt_edge;
   private static int hor_mul;
   private static int ver_mul;

   public DrawGraph(Edge edges[], Node nodes[], int n, int cnt_edge) {
      this.edges = edges;
      this.nodes = nodes;
      this.nodes_dir = new HashMap<>();
      this.n = n;
      this.cnt_edge = cnt_edge;
      this.hor_mul=(1000/n);
      this.ver_mul=(700/n);

      for(int i = 1; i <= cnt_edge; i++) {
          Edge e = edges[i];
          if(!nodes_dir.containsKey(e.start_node)) {
              nodes_dir.put(e.start_node, new double[3]);
              nodes_dir.get(e.start_node)[2] = 0;
          }

          if(!nodes_dir.containsKey(e.end_node)) {
              nodes_dir.put(e.end_node, new double[3]);
              nodes_dir.get(e.end_node)[2] = 0;
          }

          int x = e.start_node.x - e.end_node.x;
          int y = e.start_node.y - e.end_node.y;

          double dirx = x / Math.sqrt(x*x + y * y);
          double diry = y / Math.sqrt(x*x + y * y);

          double temp[] = nodes_dir.get(e.start_node);
          temp[0] = (temp[0] * temp[2] + dirx)/ (temp[2] + 1);
          temp[1] = (temp[1] * temp[2] + diry)/ (temp[2] + 1);
          temp[2]++;

          temp = nodes_dir.get(e.end_node);
          temp[0] = (temp[0] * temp[2] - dirx)/ (temp[2] + 1);
          temp[1] = (temp[1] * temp[2] - diry)/ (temp[2] + 1);
          temp[2]++;
      }
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // create x and y axes
//      g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
//      g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

      // create hatch marks for y axis.
//      for (int i = 0; i < n; i++) {
//         int x0 = BORDER_GAP;
//         int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
//         int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / n + BORDER_GAP);
//         int y1 = y0;
//         g2.drawLine(x0, y0, x1, y1);
//      }

      // and for x axis
//      for (int i = 0; i < n; i++) {
//         int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / n + BORDER_GAP;
//         int x1 = x0;
//         int y0 = getHeight() - BORDER_GAP;
//         int y1 = y0 - GRAPH_POINT_WIDTH;
//         g2.drawLine(x0, y0, x1, y1);
//      }

      Stroke oldStroke = g2.getStroke();

      //Draw the edges on the graph;
      g2.setStroke(GRAPH_STROKE);
      for (int i = 1; i <=cnt_edge; i++) {
          Edge current_edge = edges[i];

          int x1 = current_edge.start_node.x;
          int y1 = current_edge.start_node.y;
          int x2 = current_edge.end_node.x;
          int y2 = current_edge.end_node.y;



          int dx = hor_mul*(x2 - x1);
          int dy = ver_mul*(y2 - y1);
          double slope = dx == 0 ? dy * 100000 : dy/dx;

          int mx = (hor_mul*(x1 + x2)/2) + (int)(-slope * 20 * dx/Math.sqrt(dx * dx + dy * dy));
          int my = (ver_mul*(y1 + y2)/2) + (int)(-slope * 20 * dy/Math.sqrt(dx * dx + dy * dy));

          String label = Long.toString(current_edge.weight);
          g2.setColor(Color.red);
          g2.drawString(label, mx, my);

          if(current_edge.present_in_MST==false){
              g2.setColor(GRAPH_COLOR);
          }
          else {
              g2.setColor(TREE_COLOR);
          }

          g2.drawLine(hor_mul*x1, ver_mul*y1, hor_mul*x2,ver_mul*y2);
      }

       for (int i = 1; false && i <=cnt_edge; i++) {
           Edge current_edge = edges[i];
          if(current_edge.present_in_MST==true){
        	  int x1 = current_edge.start_node.x;
              int y1 = current_edge.start_node.y;
              int x2 = current_edge.end_node.x;
              int y2 = current_edge.end_node.y;

              g2.drawLine(hor_mul*x1, ver_mul*y1, hor_mul*x2,ver_mul*y2);
          }
        }

      //Draw the vertices on the graph
      g2.setStroke(oldStroke);
      g2.setColor(GRAPH_POINT_COLOR);
      for (int i = 1; i <=n; i++) {
         int x = hor_mul*nodes[i].x - GRAPH_POINT_WIDTH/2;
         int y = ver_mul*nodes[i].y - GRAPH_POINT_WIDTH/2;
         int ovalW = GRAPH_POINT_WIDTH;
         int ovalH = GRAPH_POINT_WIDTH;
         g2.fillOval(x, y, ovalW, ovalH);
         String label = Integer.toString(i);
         double dir[] = nodes_dir.get(nodes[i]);
         double dirx = dir[0] / Math.sqrt(dir[0] * dir[0] + dir[1] * dir[1]);
         double diry = dir[1] / Math.sqrt(dir[0] * dir[0] + dir[1] * dir[1]);
         g2.drawString(label, (int)(x + 15 * dirx), (int)(y + 15 * diry));
      }
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(PREF_W, PREF_H);
   }
 
   
}

