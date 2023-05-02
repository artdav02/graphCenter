import java.util.*;

/** Container class to different classes, that makes the whole
 * set of classes one class formally.
 */
public class GraphTask {

   /**
    * Main method. Initializes a GraphTask object and runs the example.
    *
    * @param args command-line arguments (not used)
    */

   public static void main (String[] args) {
      GraphTask a = new GraphTask();
      a.run();
   }

   /**
    * Runs a series of test cases on random graphs to find their centers.
    * It generates graphs with different sizes and counts the execution time for the last case.
    */
   public void run() {
      // Test 1
      Graph g1 = new Graph("G1");
      g1.createRandomSimpleGraph(6, 9);
      System.out.println(g1);
      Vertex center1 = g1.findCenter();
      System.out.println("Center of the graph G1: " + center1);

      // Test 2
      Graph g2 = new Graph("G2");
      g2.createRandomSimpleGraph(10, 15);
      System.out.println(g2);
      Vertex center2 = g2.findCenter();
      System.out.println("Center of the graph G2: " + center2);

      // Test 3
      Graph g3 = new Graph("G3");
      g3.createRandomSimpleGraph(15, 20);
      System.out.println(g3);
      Vertex center3 = g3.findCenter();
      System.out.println("Center of the graph G3: " + center3);

      // Test 4
      Graph g4 = new Graph("G4");
      g4.createRandomSimpleGraph(20, 25);
      System.out.println(g4);
      Vertex center4 = g4.findCenter();
      System.out.println("Center of the graph G4: " + center4);

      // Test 5 - Large graph with 2000+ vertices
      Graph g5 = new Graph("G5");
      long startTime = System.currentTimeMillis();
      g5.createRandomSimpleGraph(2000, 3999);
      Vertex center5 = g5.findCenter();
      long endTime = System.currentTimeMillis();
      System.out.println("Center of the graph G5: " + center5);
      System.out.println("Execution time for the graph with 2000+ vertices: " + (endTime - startTime) + " milliseconds");

   }

   // TODO!!! add javadoc relevant to your problem
   class Vertex {

      private String id;
      private Vertex next;
      private Arc first;
      private int info = 0;
      // You can add more fields, if needed

      Vertex (String s, Vertex v, Arc e) {
         id = s;
         next = v;
         first = e;
      }

      Vertex (String s) {
         this (s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }

      public void setInfo(int info) {
         this.info = info;
      }

      public int getInfo() {
         return info;
      }

      // TODO!!! Your Vertex methods here!
   }


   /** Arc represents one arrow in the graph. Two-directional edges are
    * represented by two Arc objects (for both directions).
    */
   class Arc {

      private String id;
      private Vertex target;
      private Arc next;
      private int info = 0;
      // You can add more fields, if needed

      Arc (String s, Vertex v, Arc a) {
         id = s;
         target = v;
         next = a;
      }

      Arc (String s) {
         this (s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }

      // TODO!!! Your Arc methods here!
   }


   class Graph {

      private String id;
      private Vertex first;
      private int info = 0;
      // You can add more fields, if needed

      Graph (String s, Vertex v) {
         id = s;
         first = v;
      }

      Graph (String s) {
         this (s, null);
      }

      /**
       * Finds the eccentricity of a given vertex in the graph.
       * The eccentricity is the maximum distance from the vertex to any other vertex.
       *
       * @param v the vertex for which the eccentricity is to be found
       * @return the eccentricity of the vertex
       */
      public int findEccentricity(Vertex v) {
         bfs(v);
         int maxDist = 0;
         Vertex u = first;
         while (u != null) {
            maxDist = Math.max(maxDist, u.getInfo());
            u = u.next;
         }
         return maxDist;
      }

      /**
       * Finds the center of the graph.
       * The center is the vertex with the minimum eccentricity.
       *
       * @return the center vertex of the graph
       */
      public Vertex findCenter() {
         Vertex center = null;
         int minEccentricity = Integer.MAX_VALUE;

         Vertex v = first;
         while (v != null) {
            int ecc = findEccentricity(v);
            if (ecc < minEccentricity) {
               minEccentricity = ecc;
               center = v;
            }
            v = v.next;
         }
         return center;
      }

      /**
       * Performs Breadth-First Search (BFS) on the graph, starting from the given vertex.
       * Updates the 'info' field of each vertex with the shortest distance from the starting vertex.
       *
       * @param start the starting vertex for the BFS traversal
       */
      public void bfs(Vertex start) {
         for (Vertex v = first; v != null; v = v.next) {
            v.setInfo(Integer.MAX_VALUE);
         }
         start.setInfo(0);

         Queue<Vertex> queue = new LinkedList<>();
         queue.add(start);

         while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            int currentDist = current.getInfo();
            Arc arc = current.first;
            while (arc != null) {
               Vertex target = arc.target;
               if (target.getInfo() == Integer.MAX_VALUE) {
                  target.setInfo(currentDist + 1);
                  queue.add(target);
               }
               arc = arc.next;
            }
         }
      }

      @Override
      public String toString() {
         String nl = System.getProperty ("line.separator");
         StringBuffer sb = new StringBuffer (nl);
         sb.append (id);
         sb.append (nl);
         Vertex v = first;
         while (v != null) {
            sb.append (v.toString());
            sb.append (" -->");
            Arc a = v.first;
            while (a != null) {
               sb.append (" ");
               sb.append (a.toString());
               sb.append (" (");
               sb.append (v.toString());
               sb.append ("->");
               sb.append (a.target.toString());
               sb.append (")");
               a = a.next;
            }
            sb.append (nl);
            v = v.next;
         }
         return sb.toString();
      }

      public Vertex createVertex (String vid) {
         Vertex res = new Vertex (vid);
         res.next = first;
         first = res;
         return res;
      }

      public Arc createArc (String aid, Vertex from, Vertex to) {
         Arc res = new Arc (aid);
         res.next = from.first;
         from.first = res;
         res.target = to;
         return res;
      }

      /**
       * Create a connected undirected random tree with n vertices.
       * Each new vertex is connected to some random existing vertex.
       * @param n number of vertices added to this graph
       */
      public void createRandomTree (int n) {
         if (n <= 0)
            return;
         Vertex[] varray = new Vertex [n];
         for (int i = 0; i < n; i++) {
            varray [i] = createVertex ("v" + String.valueOf(n-i));
            if (i > 0) {
               int vnr = (int)(Math.random()*i);
               createArc ("a" + varray [vnr].toString() + "_"
                       + varray [i].toString(), varray [vnr], varray [i]);
               createArc ("a" + varray [i].toString() + "_"
                       + varray [vnr].toString(), varray [i], varray [vnr]);
            } else {}
         }
      }

      /**
       * Create an adjacency matrix of this graph.
       * Side effect: corrupts info fields in the graph
       * @return adjacency matrix
       */
      public int[][] createAdjMatrix() {
         info = 0;
         Vertex v = first;
         while (v != null) {
            v.info = info++;
            v = v.next;
         }
         int[][] res = new int [info][info];
         v = first;
         while (v != null) {
            int i = v.info;
            Arc a = v.first;
            while (a != null) {
               int j = a.target.info;
               res [i][j]++;
               a = a.next;
            }
            v = v.next;
         }
         return res;
      }

      /**
       * Creates a connected simple (undirected, no loops, no multiple
       * arcs) random graph with n vertices and m edges.
       *
       * @param n number of vertices
       * @param m number of edges
       * @throws IllegalArgumentException if the number of vertices or edges is invalid
       */
      public void createRandomSimpleGraph (int n, int m) {
         if (n <= 0)
            return;
         if (n > 2500)
            throw new IllegalArgumentException ("Too many vertices: " + n);
         if (m < n-1 || m > n*(n-1)/2)
            throw new IllegalArgumentException
                    ("Impossible number of edges: " + m);
         first = null;
         createRandomTree (n);       // n-1 edges created here
         Vertex[] vert = new Vertex [n];
         Vertex v = first;
         int c = 0;
         while (v != null) {
            vert[c++] = v;
            v = v.next;
         }
         int[][] connected = createAdjMatrix();
         int edgeCount = m - n + 1;  // remaining edges
         while (edgeCount > 0) {
            int i = (int)(Math.random()*n);  // random source
            int j = (int)(Math.random()*n);  // random target
            if (i==j)
               continue;  // no loops
            if (connected [i][j] != 0 || connected [j][i] != 0)
               continue;  // no multiple edges
            Vertex vi = vert [i];
            Vertex vj = vert [j];
            createArc ("a" + vi.toString() + "_" + vj.toString(), vi, vj);
            connected [i][j] = 1;
            createArc ("a" + vj.toString() + "_" + vi.toString(), vj, vi);
            connected [j][i] = 1;
            edgeCount--;  // a new edge happily created
         }
      }

      // TODO!!! Your Graph methods here! Probably your solution belongs here.
   }

}