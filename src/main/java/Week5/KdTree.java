import java.util.LinkedList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private static class Node {
        public Point2D point;
        public RectHV rect;
        public Node left;
        public Node right;
    }

    private Node root;

    private int count;

    public KdTree() // construct an empty set of points
    {
        root = null;
        count = 0;
    }

    public boolean isEmpty() // is the set empty?
    {
        return count == 0;
    }

    public int size() // number of points in the set
    {
        return count;
    }

    public void insert(Point2D p) // add the point to the set (if it is not already in the set)
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        
        Node newNode = new Node();
        newNode.point = p;
        newNode.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        newNode.left = null;
        newNode.right = null;

        if (root == null)
        {
            root = newNode;
            ++count;
        }
        else {
            insert(root, newNode, true);
        }
    }
    
    private void insert(Node currentNode, Node newNode, boolean isVerticalLevel)
    {
        if (currentNode.point.equals(newNode.point)) {
            return;
        }

        int compareResult = 0;
        int rightRectangleType = 0;
        int leftRectangleType = 0;
        if (isVerticalLevel) {
            compareResult = Double.compare(newNode.point.x(), currentNode.point.x());
            // left or right rects only
            rightRectangleType = 3;
            leftRectangleType = 0;
        }
        else {
            compareResult = Double.compare(newNode.point.y(), currentNode.point.y());
            // top or bottom rects only
            rightRectangleType = 1;
            leftRectangleType = 2;
        }
        
        if (compareResult < 0) {
            if (currentNode.left == null) {
                newNode.rect = getRectangle(currentNode.point, currentNode.rect, leftRectangleType);
                currentNode.left = newNode;
                ++count;
            }
            else {
                insert(currentNode.left, newNode, !isVerticalLevel);
            }
        }
        else {
            if (currentNode.right == null)
            {
                newNode.rect = getRectangle(currentNode.point, currentNode.rect, rightRectangleType);
                currentNode.right = newNode;
                ++count;
            }
            else {
                insert(currentNode.right, newNode, !isVerticalLevel);
            }
        }
    }

    private RectHV getRectangle(Point2D point, RectHV enclosingRect, int quarter) {
        switch (quarter) {
            // left
            case 0:
                return new RectHV(enclosingRect.xmin(), enclosingRect.ymin(), point.x(), enclosingRect.ymax());
            // top
            case 1:
                return new RectHV(enclosingRect.xmin(), point.y(), enclosingRect.xmax(), enclosingRect.ymax());
            // bottom
            case 2:
                return new RectHV(enclosingRect.xmin(), enclosingRect.ymin(), enclosingRect.xmax(), point.y());
            // right
            default:
                return new RectHV(point.x(), enclosingRect.ymin(), enclosingRect.xmax(), enclosingRect.ymax());
        }
    }


    public boolean contains(Point2D p) // does the set contain point p?
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return containsRec(p, root);
    }
    
    private boolean containsRec(Point2D p, Node node) {
        if (node == null) {
            return false;
        }
        
        if (!node.rect.contains(p)) {
            return false;
        }
        
        return node.point.equals(p) || containsRec(p, node.left) || containsRec(p, node.right);
    }

    public void draw() // draw all points to standard draw
    {
        drawNode(root, true);
    }
    
    private void drawNode(Node node, boolean isVerticalLevel) {
        if (node != null) {
            StdDraw.setPenRadius(0.01);
            node.point.draw();
            StdDraw.setPenRadius();
            if (isVerticalLevel) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
            }

            StdDraw.setPenColor();
            drawNode(node.left, !isVerticalLevel);
            drawNode(node.right, !isVerticalLevel);
        }
    }

    public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        LinkedList<Point2D> found = new LinkedList<>();
        return rangeRec(found, rect, root);
    }
    
    private LinkedList<Point2D> rangeRec(LinkedList<Point2D> agg, RectHV rect, Node node) 
    {
        if (node == null) {
            return agg;
        }
        
        if (!node.rect.intersects(rect)) {
            return agg;
        }

        if (rect.contains(node.point)) {
            agg.add(node.point);
        }
        
        LinkedList<Point2D> leftResult = rangeRec(agg, rect, node.left);
        return rangeRec(leftResult, rect, node.right);
    }

    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (root != null) {
            return nearestRec(p, root.point, root, 1);
        }
        return null;
    }
    
    private Point2D nearestRec(Point2D target, Point2D bestSoFar, Node node, int level)
    {
        if (node == null) {
            return bestSoFar;
        }
        boolean isVerticalLevel = level % 2 == 1;
        StdOut.printf("level %d\n", level);
        double distanceToBest = bestSoFar.distanceSquaredTo(target);

        if (distanceToBest > node.point.distanceSquaredTo(bestSoFar)) {
            bestSoFar = node.point;
        }

        StdOut.printf("bestsofar: %f, %f\n", bestSoFar.x(), bestSoFar.y());
        
        boolean shouldGoLeft = isVerticalLevel 
            ? node.point.x() > target.x()
            : node.point.y() > target.y();
        
        double distanceToLeftRect = node.left == null ? Double.POSITIVE_INFINITY : node.left.rect.distanceSquaredTo(target);
        double distanceToRightRect = node.right == null ? Double.POSITIVE_INFINITY : node.right.rect.distanceSquaredTo(target);
        StdOut.printf("distanceToLeftRect: %f, distanceToRightRect: %f\n", distanceToLeftRect, distanceToRightRect);
        if (shouldGoLeft) {
            StdOut.printf("distance to left rect is smaller: %f\n", distanceToLeftRect);
            Point2D newBestSoFar = nearestRec(target, bestSoFar, node.left, level + 1);
            StdOut.printf("newer bestsofar from left rect: %f, %f\n", newBestSoFar.x(), newBestSoFar.y());
            double newBestSoFarDistance = newBestSoFar.distanceTo(target);
            double rightRectDist = node.right != null ? node.right.rect.distanceSquaredTo(target) : Double.POSITIVE_INFINITY;
            if (newBestSoFarDistance > rightRectDist) {
                return nearestRec(target, newBestSoFar, node.right, level + 1);
            }
            return newBestSoFar;
        } else {
            StdOut.printf("distance to right rect is smaller: %f\n", distanceToRightRect);
            Point2D newBestSoFar = nearestRec(target, bestSoFar, node.right, level + 1);
            StdOut.printf("newer bestsofar from right rect: %f, %f\n", newBestSoFar.x(), newBestSoFar.y());
            double newBestSoFarDistance = newBestSoFar.distanceTo(target);
            double leftRectDist = node.left != null ? node.left.rect.distanceSquaredTo(target) : Double.POSITIVE_INFINITY;
            if (newBestSoFarDistance > leftRectDist){
                return nearestRec(target, newBestSoFar, node.left, level + 1);
            }
            return newBestSoFar;
        }
    }
}