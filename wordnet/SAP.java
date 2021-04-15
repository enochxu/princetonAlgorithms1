import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Digraph is null.");
        graph = new Digraph(G);
    }
 
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // use bfs 
        validateVertex(v);
        validateVertex(w);
        BFS vBFS = new BFS(graph, v);
        BFS wBFS = new BFS(graph, w);
        int ancestor = findAncestor(vBFS, wBFS);
        if (ancestor == -1)
            return -1;
        else
            return vBFS.distTo(ancestor) + wBFS.distTo(ancestor);
    }
 
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        BFS vBFS = new BFS(graph, v);
        BFS wBFS = new BFS(graph, w);
        return findAncestor(vBFS, wBFS);
    }
 
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        BFS vBFS = new BFS(graph, v);
        BFS wBFS = new BFS(graph, w);
        int ancestor = findAncestor(vBFS, wBFS);
        if (ancestor == -1)
            return -1;
        else
            return vBFS.distTo(ancestor) + wBFS.distTo(ancestor);
    }
 
    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        BFS vBFS = new BFS(graph, v);
        BFS wBFS = new BFS(graph, w);
        return findAncestor(vBFS, wBFS);
    }
    
    private int findAncestor(BFS vBFS, BFS wBFS) {
        int leastAncestor = -1;
        int leastDist = -1;
        for (int vertex = 0; vertex < graph.V(); vertex++) {
            if (vBFS.hasPath(vertex) && wBFS.hasPath(vertex)) {
                int dist = vBFS.distTo(vertex) + wBFS.distTo(vertex);
                if (leastDist == -1 || dist < leastDist) {
                    leastDist = dist;
                    leastAncestor = vertex;
                }                    
            }
        }
        return leastAncestor;
    }

    private void validateVertices(Iterable<Integer> v) {
        if (v == null)
            throw new IllegalArgumentException("Word is null.");
        for (int vertex : v) {
            if (vertex < 0 || vertex >= graph.V())
                throw new IllegalArgumentException("Vertex out of bounds.");
        }
    }

    private void validateVertex(int v) {
        if (v < 0)
            throw new IllegalArgumentException("Vertex index less than 0.");
        if (v >= graph.V())
            throw new IllegalArgumentException("Vertex index is greater than graph size.");
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
