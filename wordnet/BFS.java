import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

public class BFS {

    private final int[] distTo;
    private final boolean[] marked;

    public BFS(Digraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        marked = new boolean[G.V()];
        distTo = new int[G.V()]; // length to nodes from v
    
        q.enqueue(s);
        marked[s] = true;
        distTo[s] = 0;
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    q.enqueue(w);
                    marked[w] = true;
                    distTo[w] = distTo[v] + 1;
                }
            }
        }
    }

    public BFS(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        marked = new boolean[G.V()];
        distTo = new int[G.V()];

        for (int s : sources) {
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    q.enqueue(w);
                    marked[w] = true;
                    distTo[w] = distTo[v] + 1;
                }
            }
        }
    }

    public boolean hasPath(int v) {
        return marked[v];
    }

    public int distTo(int v) {
        return distTo[v];
    }
}
