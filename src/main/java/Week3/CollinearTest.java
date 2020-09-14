import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class CollinearTest {
    public static void main(String[] args) {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32_768);
        StdDraw.setYscale(0, 32_768);
        StdDraw.setPenRadius(0.01);  // make the points a bit larger
    
        // read in the input
        String filename = "https://coursera.cs.princeton.edu/algs4/assignments/collinear/files/input300.txt";
        // String filename = "https://coursera.cs.princeton.edu/algs4/assignments/collinear/files/equidistant.txt";
        // String filename = "https://coursera.cs.princeton.edu/algs4/assignments/collinear/files/inarow.txt";

        // String filename = "/Users/yevhenii/Documents/Repositories/algs4/week2/src/main/java/week3/duplicates.txt";

        In in = new In(filename);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);  // make the points a bit larger
            p.draw();
            points[i] = p;
        }
    
        StdDraw.setPenRadius(0.005);  // make the points a bit larger
        Arrays.sort(points);

        FastCollinearPoints fast = new FastCollinearPoints(points);

        // BruteCollinearPoints fast = new BruteCollinearPoints(points);
    
        LineSegment[] segments = fast.segments();
        System.out.println("Count of segments: " + segments.length);

        for (int i = 0; i < segments.length; i++) {
            LineSegment segment = segments[i];
            StdOut.println(segment);
            segment.draw();
            StdDraw.show();
        }
        StdDraw.show();
    
    }
}
