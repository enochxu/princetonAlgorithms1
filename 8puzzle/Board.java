import java.util.Arrays;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;

public class Board {
    
    private final int[][] grid;
    private final int gridSize;
    // private int[][] goal;
    private int blankRow;
    private int blankCol;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        grid = copy(tiles);
        gridSize = grid.length;

        Boolean blankFound = false; // would for loop + break statement also work?
        int i = 0;
        int j = 0;
        while (!blankFound && i < gridSize) {
            while (!blankFound && j < gridSize) {
                if (grid[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                    blankFound = true;
                } else {
                    j++;
                }                    
            }
            i++;
            j = 0;
        }

        if (!blankFound)
            throw new IllegalArgumentException("No blank element found.");
    }
                                           
    // string representation of this board
    public String toString() {
        StringBuilder boardRep = new StringBuilder().append(gridSize);
        for (int i = 0; i < gridSize; i++) {
            boardRep.append("\n");
            for (int j = 0; j < gridSize; j++) {
                boardRep.append(" " + grid[i][j]);
            }
        }
        return boardRep.toString();
    }

    // board dimension n
    public int dimension() {
        return gridSize;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (grid[row][col] != row * gridSize + col + 1
                    && grid[row][col] != 0)
                    hamming++;                 
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        int goalRow;
        int goalCol;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (grid[row][col] == 0) {
                    goalRow = gridSize - 1;
                    goalCol = gridSize - 1;
                } else {
                    goalRow = (grid[row][col] - 1) / gridSize;
                    if (grid[row][col] % gridSize == 0)
                        goalCol = gridSize - 1; // last column
                    else
                        goalCol = (grid[row][col] % gridSize) - 1;
                    manhattan += Math.abs(goalRow - row) + Math.abs(goalCol - col);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (row == gridSize - 1 && col == gridSize - 1)
                    return grid[row][col] == 0;
                else if (grid[row][col] != row * gridSize + col + 1)
                    return false;
            }
        }

        // probably won't get here.
        return true;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null) 
            return false;
        if (y.getClass() != this.getClass()) 
            return false;
        Board that = (Board) y;
        if (Arrays.deepEquals(this.grid, that.grid)) 
            return true;

        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<Board>();
        if (blankRow > 0)
            neighbors.push(new Board(swap(blankRow, blankCol, blankRow - 1, blankCol)));
        if (blankCol > 0)
            neighbors.push(new Board(swap(blankRow, blankCol, blankRow, blankCol - 1)));
        if (blankRow < gridSize - 1)
            neighbors.push(new Board(swap(blankRow, blankCol, blankRow + 1, blankCol)));
        if (blankCol < gridSize - 1)
            neighbors.push(new Board(swap(blankRow, blankCol, blankRow, blankCol + 1)));    
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col + 1 < gridSize; col++) { // col + 1 to ensure swap is possible
                if (row != blankRow || (col != blankCol && col + 1 != blankCol)) {
                    return new Board(swap(row, col, row, col + 1));
                }
            }
        }
        throw new RuntimeException("Could not find twin.");
    }

    private int[][] copy(int[][] board) {
        int[][] copy = new int[board.length][board[0].length];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                copy[row][col] = board[row][col];
            }
        }
        return copy;
    }

    private int[][] swap(int row1, int col1, int row2, int col2) {
        
        int[][] swapped = copy(grid);
        int temp = swapped[row1][col1];
        swapped[row1][col1] = swapped[row2][col2];
        // System.out.println(board[row1][col1]);
        swapped[row2][col2] = temp;
        // System.out.println(board[row1][col1]);
        
        return swapped;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        System.out.println(initial.manhattan());
        System.out.println(initial.twin().toString());
        for (Board neighbor : initial.neighbors()) {
            System.out.println(neighbor.toString());
        }
    }

}