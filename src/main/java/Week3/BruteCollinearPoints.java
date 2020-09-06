import java.util.Arrays;

import edu.princeton.cs.algs4.LinkedStack;

public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;

    public BruteCollinearPoints(final Point[] points) // finds all line segments containing 4 points
    {
        if (points == null)
            throw new IllegalArgumentException("The points is null");
        
        final int size = points.length;
        
        Point[] sortedPoints = new Point[size];
        for (int i = 0; i < size; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("One of the points is null");

            sortedPoints[i] = points[i];
        }
        
        Arrays.sort(sortedPoints);
        
        final LinkedStack<LineSegment> segments = new LinkedStack<LineSegment>();

        for (int i = 0; i < size; i++)
            for (int j = i + 1; j < size; j++) {
                final Point start = sortedPoints[i];
                
                if (start.compareTo(sortedPoints[j]) == 0)
                    throw new IllegalArgumentException("The points array has duplicated points");

                final double first = slope(start, sortedPoints[j]);

                for (int k = j + 1; k < size - 1; k++) {
                    final double second = slope(sortedPoints[j], sortedPoints[k]);

                    if (first != second)
                        continue;

                    for (int m = k + 1; m < size; m++) {
                        final Point end = sortedPoints[m];
                        final double third = slope(sortedPoints[k], end);

                        if (second == third) {
                            segments.push(new LineSegment(start, end));
                        }
                    }
                }
            }

        lineSegments = new LineSegment[segments.size()];

        int i = 0;
        while (!segments.isEmpty()) {
            lineSegments[i] = segments.pop();
            i++;
        }
    }

    public int numberOfSegments() // the number of line segments
    {
        return lineSegments.length;
    }

    public LineSegment[] segments() // the line segments
    {
        LineSegment[] segments = new LineSegment[lineSegments.length];
        for (int i = 0; i < lineSegments.length; i++) {
            segments[i] = lineSegments[i];
        }
        return segments;
    }

    private double slope(Point point1, Point point2) {
        return point1.slopeTo(point2);
    }
 }