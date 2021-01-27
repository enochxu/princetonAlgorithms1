import java.lang.Math; 
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private final double[] openSiteCounts;
    private final int t;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and T must be greater than 0.");
        }

        t = trials;
        // float totalGridCount = n * n;
        openSiteCounts = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolationSimulation = new Percolation(n);
            while (!percolationSimulation.percolates()) {
                percolationSimulation.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }            
            openSiteCounts[i] = (double) (percolationSimulation.numberOfOpenSites()) / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(openSiteCounts);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(openSiteCounts);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (StdStats.mean(openSiteCounts) - 
                1.96 * StdStats.stddev(openSiteCounts) / Math.sqrt(t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (StdStats.mean(openSiteCounts) + 
                1.96 * StdStats.stddev(openSiteCounts) / Math.sqrt(t));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.printf("%-25s = %f%n", "mean", percolationStats.mean());
        System.out.printf("%-25s = %f%n", "stddev", percolationStats.stddev());
        System.out.printf("%-25s = [%f, %f]%n", "95% confidence interval", 
                            percolationStats.confidenceLo(), percolationStats.confidenceHi());
    }
}