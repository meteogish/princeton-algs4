import java.util.Arrays;
import edu.princeton.cs.algs4.LinkedStack;

public class FastCollinearPoints {
    private final LineSegment[] lineSegments;

    public FastCollinearPoints(final Point[] points) // finds all line segments containing 4 points
    {
        if (points == null)
            throw new IllegalArgumentException("The points is null");
            
        final LinkedStack<LineSegment> segments = new LinkedStack<LineSegment>();
        final int size = points.length;
        Point[] sortedPoints = new Point[size];
        for (int i = 0; i < size; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("One of the points is null");

            sortedPoints[i] = points[i];
        }
        
        for (int i = 0; i < size; i++) {
            Point origin = points[i];
            Arrays.sort(sortedPoints, 0, size, origin.slopeOrder());
            
            int left = 1;

            while (left < size)
            {
                Point leftPoint = sortedPoints[left];
                
                if (leftPoint.compareTo(origin) == 0)
                    throw new IllegalArgumentException("The points array has duplicated points");

                int right = left + 1;

                double leftSlope = origin.slopeTo(leftPoint);
                boolean originIsInLeftLowerCorner = origin.compareTo(leftPoint) < 0;

                while (right < size)
                {
                    int compared = sortedPoints[right].compareTo(origin);
                    
                    if (compared == 0)
                        throw new IllegalArgumentException("The points array has duplicated points");
                    
                    double rightSlope = origin.slopeTo(sortedPoints[right]);
                    if (leftSlope != rightSlope)
                    {
                        break;
                    }

                    if (compared < 0)
                    {
                        originIsInLeftLowerCorner = false;
                    }

                    ++right;
                }

                if (right - left >= 3)
                {
                    if (originIsInLeftLowerCorner)
                    {
                        Arrays.sort(sortedPoints, left, right);
                        segments.push(new LineSegment(origin, sortedPoints[right - 1]));
                    }

                    left = right;
                }
                else
                {
                    ++left; 
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
 }