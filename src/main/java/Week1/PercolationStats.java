import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private static final int TWO = 2;

    private static final double CONFIDENCE_95 = 1.96;
    private final transient int width;
    private final transient int trialsCount;
    private final transient double gridSites;

    private transient double[] fractions;

    private transient double meanValue;
    private transient double stdev;
    private transient double lo;
    private transient double hi;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n or trials <= 0");
        }

        width = n;
        trialsCount = trials;
        gridSites = n * n;
        fractions = new double[trialsCount];

        runMethod();
    }

    public double mean() {
        return meanValue;
    }

    public double stddev() {
        return stdev;
    }

    public double confidenceLo() {
        return lo;
    }

    public double confidenceHi() {
        return hi;
    }

    private void runMethod() {
        for (int i = 0; i < trialsCount; i++) {
            Percolation percolation = new Percolation(width);

            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, width + 1);
                int col = StdRandom.uniform(1, width + 1);

                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                }
            }

            fractions[i] = percolation.numberOfOpenSites() / gridSites;
        }

        meanValue = StdStats.mean(fractions);
        stdev = StdStats.stddev(fractions);

        double rootStdev = Math.sqrt(stdev);
        lo = meanValue - (CONFIDENCE_95 * rootStdev / Math.sqrt(trialsCount));
        hi = meanValue + (CONFIDENCE_95 * rootStdev / Math.sqrt(trialsCount));
    }

    public static void main(String[] args) {
        if (args.length == TWO) {
            int n = Integer.parseInt(args[0]);
            int t = Integer.parseInt(args[1]);
            Stopwatch sw = new Stopwatch();

            PercolationStats stats = new PercolationStats(n, t);
            System.err.println("Elapsed time: " + sw.elapsedTime());
            System.err.println("meanValue\t\t\t\t\t\t = " + stats.mean());
            System.err.println("stddev\t\t\t\t\t\t = " + stats.stddev());
            System.err.println("95% confidence interval\t\t\t\t = " + "[" + stats.confidenceLo() + ", "
                    + stats.confidenceHi() + "]");
        }
    }
}