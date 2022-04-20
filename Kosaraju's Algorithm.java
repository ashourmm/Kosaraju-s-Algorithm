public class DirectedGraph {
    
    private final int V;  // number of vertices
    private int E;        // number of edges
    
    // A Bag is a LinkedList with constant time addtion of elements
    // and with constant time iteration over its elements
    private Bag<Integer>[] adj; // adjacency lists

    
    // These data structures will be used for the methods that will
    // count the number of strongly connected components
    
    //Specifically these structures are for the depthFirstOrdering method
    private boolean[] marked;
    private Queue<Integer> pre;
    private Queue<Integer> post;
    private Stack<Integer> reversePost;

    //And these are for Kosaraju's algorithm
    boolean[] reached; // Reached vertices
    int[] id;          // Component identifiers
    int count;         // Number of strongly connected components

    public DirectedGraph(int V) 
    {
        
        marked = new boolean[V];
        pre = new Queue<Integer>();
        post = new Queue<Integer>();
        reversePost = new Stack<Integer>();
        reached = new boolean [V]; 
        id = new int[V];
        count = 0;
        
        this.V = V;
        this.E = 0;
        Bag<Integer>[] adj = new Bag[V]; // Create array of lists,
        
        for (int v = 0; v < V; v++) 
        {  
            adj[v] = new Bag<Integer>(); // Initialize all lists to empty.

        }
    }

    public DirectedGraph(In in)
    {
        this(in.readInt()); // Read V and construct this graph,
        int E = in.readInt(); // Read E,
        for (int i = 0; i < E; i++) {     // To add an edge. 
            int v = in.readInt();         // Read a vertex,
            int w = in.readInt();         // read another vertex,
            addEdge(v, w);                // and add edge connecting them.
        }
    }

    public int V() { return V; } // Return number of vertices 

    public int E() { return E; } // Return number of edges

    public void addEdge(int v, int w) {
        adj[v].add(w); // Add w to v’s list
        E++;
    }

    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    
    public DirectedGraph reverse() //Reverses the direction of edges in a DiretedGraph
     {
        DirectedGraph R = new DirectedGraph(V);
        for (int v = 0; v < V; v++) {
           for (int w : adj(v)) {    //Find all the edges,
              R.addEdge(w, v);       //Add the edge in reverse order.
            }
        }
        return R;
     }
    
    /*
    The idea is that DFS will visit each vertex one time.
    If we save the order in which the vertices are given to firstDFS() in a Queue, 
    then iterate through that Queue, we can visit all the graph vertices, in the inherent 
    order determined by the original graph structure and by whether we do the save before 
    or after the recursive calls.
    This precursor step will be useful for implementing Kosaraju's algorithm.
    */

    public void depthFirstOrdering(DirectedGraph G)
    {
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                firstDFS(G, v);
            }
        }
    }

    private void firstDFS(DirectedGraph G, int v)
    {
       pre.enqueue(v);       // This tracks vertices are they visited in their natural order
       marked[v] = true;
       for (int w : G.adj(v)) {
          if (!marked[w])
             {firstDFS(G, w);}
       post.enqueue(v);       // This tracks vertices as they marked done
       reversePost.push(v);   // This tracks the post Queue in reverse order
       }
   }

   /*
    Given a DirectedGraph G, we use depthFirstOrdering to compute the,
    reverse postorder of the original grpah's reverse as given by the reverse() method.
    Then we run standard DFS on the given graph,  but consider the unmarked vertices in, 
    the order just computed instead of the standard numerical order.
    From here, all vertices reached when secondDFS() is called,
    are in a strong component, so we count them as a strongly connected component.
   */

    public int Kosaraju(DirectedGraph G) 
    {                            
        depthFirstOrdering(G.reverse());
        
        for (int s : order.reversePost()) {
          if ( reached[s])
          {  secondDFS(G, s); count++;  }
        }
        return count;
    }

    private void secondDFS(DirectedGraph G, int v) 
    {
        reached[v] = true;
        id[v] = count;
        for (int w : G.adj(v)) 
        {
            if ( reached[w]) {secondDFS(G, w);}
        } 
    }

    public static void main(String[] args) { }

}
