import java.util.LinkedList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> points;

    public PointSET() // construct an empty set of points
    {
        points = new SET<Point2D>();
    }

    public boolean isEmpty() // is the set empty?
    {
        return points.isEmpty();
    }

    public int size() // number of points in the set
    {
        return points.size();
    }

    public void insert(Point2D p) // add the point to the set (if it is not already in the set)
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        
        points.add(p);
    }

    public boolean contains(Point2D p) // does the set contain point p?
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return points.contains(p);
    }

    public void draw() // draw all points to standard draw
    {
        for (Point2D p : points) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        LinkedList<Point2D> found = new LinkedList<>();

        for (Point2D p : points) {
            if (rect.contains(p)) {
                found.add(p);
            }
        }
        return found;
    }

    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        
        Point2D found = null;
        double latestDist = Double.MAX_VALUE;

        for (Point2D point : points) {
            double d = point.distanceTo(p);
            if (d < latestDist) {
                latestDist = d;
                found = point;
            }
        }
        
        return found;
    }
}