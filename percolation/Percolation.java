// FIX BACKWASH IF YOU HAVE TIME

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF grid;
    private boolean[] gridOpen;
    private int openCount;
    private final int gridSideLength;
    private final int virtualTopID;
    private final int virtualBottomID;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0.");
        }

        // +2 for virtual top and bottom sites.
        grid = new WeightedQuickUnionUF(n * n + 2);
        gridOpen = new boolean[n * n];
        gridSideLength = n;
        virtualTopID = n * n;
        virtualBottomID = n * n + 1;
        openCount = 0;
    }

    private int findIndex(int row, int col) {
        if (row <= 0 || row > gridSideLength || col <= 0 || col > gridSideLength) {
            throw new IllegalArgumentException("row and column must be in the range of 1 to n.");
        }

        return (gridSideLength * (row - 1) + (col - 1));
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int index = findIndex(row, col);
        if (!gridOpen[index]) {
            
            // start with neighbors on the same row. Check for edge cases.
            if ((col > 1) && gridOpen[index - 1]) {
                grid.union(index, index - 1);
            }
            // gridWidth - 1 because col indexes from zero.
            if ((col < gridSideLength) && gridOpen[index + 1]) {
                grid.union(index, index + 1);
            }
            // then neighbors on the same column.
            if ((row > 1) && gridOpen[index - gridSideLength]) {
                grid.union(index, index - gridSideLength);
            }
            if ((row < gridSideLength) && gridOpen[index + gridSideLength]) {
                grid.union(index, index + gridSideLength);
            }

            // connect to virtual sites
            if (row == 1) {
                grid.union(index, virtualTopID);
            } else if (row == gridSideLength) {
                grid.union(index, virtualBottomID);
            }

            gridOpen[index] = true;
            openCount++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return gridOpen[findIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return (grid.find(findIndex(row, col)) == grid.find(virtualTopID));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return (grid.find(virtualTopID) == grid.find(virtualBottomID));
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation test = new Percolation(5);
        test.open(2, 2);
        // test.open(2, 3); // virtualGrid.find(0)
        test.open(3, 2);
        test.open(0, 2);
        test.open(1, 2);
        test.open(4, 2);
        System.out.println(test.isOpen(0, 0));
        System.out.println(test.numberOfOpenSites());
        System.out.println(test.percolates());
    }
}