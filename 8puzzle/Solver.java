import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private class SearchNode implements Comparable<SearchNode> {

        private final int moves; // G cost/distance form starting node.
        private final int manhattan; // H cost/distance from end node.
        private final int priority; // F cost
        private final Board board;
        private final SearchNode previous;

        SearchNode(SearchNode previous, Board board, int moves) {
            this.previous = previous;
            this.board = board;
            this.moves = moves;
            manhattan = board.manhattan();
            priority = this.moves + manhattan;
        }

        @Override
        public int compareTo(SearchNode that) {
            if (this.priority == that.priority)
                return this.manhattan - that.manhattan;
            else
                return this.priority - that.priority;
        }
    }

    private int moves;
    private SearchNode minNode;
    private Board goal;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) 
            throw new IllegalArgumentException("Initial argument is null");

        Board twin = initial.twin();

        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPq = new MinPQ<SearchNode>();

        SearchNode current = null;
        pq.insert(new SearchNode(current, initial, 0));

        SearchNode twinCurrent = null;
        twinPq.insert(new SearchNode(twinCurrent, twin, 0));

        // pre-calculate goal
        int sideLength = initial.dimension();

        int[][] grid = new int[sideLength][sideLength];
        int k = 1 ;
        for (int i = 0; i < sideLength; i++)
            for (int j = 0; j < sideLength; j++) {
            grid[i][j] = k;
            k++;
        }
        grid[sideLength - 1][sideLength - 1] = 0;

        goal = new Board(grid);
        
        // Board current = initial;
        while (!pq.min().board.equals(goal) && !twinPq.min().board.equals(goal)) {
            current = pq.delMin();
            for (Board neighbor : current.board.neighbors()) {
                if (current.moves == 0 || !current.previous.board.equals(neighbor))
                    pq.insert(new SearchNode(current, neighbor, current.moves + 1));
            }
            twinCurrent = twinPq.delMin();
            for (Board neighbor : twinCurrent.board.neighbors()) {
                if (twinCurrent.moves == 0 || !twinCurrent.previous.board.equals(neighbor))
                    twinPq.insert(new SearchNode(twinCurrent, neighbor, twinCurrent.moves + 1));
            }
        }
        if (pq.min().board.equals(goal)) {
            minNode = pq.min();
            moves = minNode.moves;
        } else {
            minNode = null;
            moves = -1;
        }
    }
    

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return minNode != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;

        SearchNode current = minNode;
        Stack<Board> solutionSeq = new Stack<Board>();
    
        for (int i = 0; i <= moves; i++) {
            solutionSeq.push(current.board);
            current = current.previous;
        }
        return solutionSeq;
    }

    // test client (see below) 
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}