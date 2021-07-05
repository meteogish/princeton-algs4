import java.awt.Color;
import java.util.LinkedList;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

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
        }
        else {
            insert(root, newNode, true);
        }
    }
    
    private void insert(Node currentNode, Node newNode, boolean isVerticalLevel)
    {
        //left or right rects only
        if (isVerticalLevel) {
            if (newNode.point.x() > currentNode.point.x()) {
                if (currentNode.right == null)
                {
                    newNode.rect = getRectangle(currentNode.point, 3);
                    currentNode.right = newNode;
                }
                else {
                    insert(currentNode.right, newNode, !isVerticalLevel);
                }
            }
            else {
                if (currentNode.left == null) {
                    newNode.rect = getRectangle(currentNode.point, 0);
                    currentNode.left = newNode;
                }
                else {
                    insert(currentNode.left, newNode, !isVerticalLevel);
                }
            }
        }
        //top or bottom rects only
        else {
            if (newNode.point.y() > currentNode.point.y()) {
                if (currentNode.right == null)
                {
                    newNode.rect = getRectangle(currentNode.point, 1);
                    currentNode.right = newNode;
                }
                else {
                    insert(currentNode.right, newNode, !isVerticalLevel);
                }
            }
            else {
                if (currentNode.left == null) {
                    newNode.rect = getRectangle(currentNode.point, 2);
                    currentNode.left = newNode;
                }
                else {
                    insert(currentNode.left, newNode, !isVerticalLevel);
                }
            }
        }
    }

    private RectHV getRectangle(Point2D point, int quarter) {
        //RectHV(double xmin, double ymin, double xmax, double ymax
        switch (quarter) {
            //left
            case 0:
                return new RectHV(0.0, 0.0, point.x(), 1.0);
            //top
            case 1:
                return new RectHV(0.0, point.y(), 1.0, 1.0);
            //bottom
            case 2:
                return new RectHV(0.0, 0.0, 1.0, point.y());
            //right
            default:
                return new RectHV(point.x(), 0.0, 1.0, 1.0);
        }
    }


    public boolean contains(Point2D p) // does the set contain point p?
    {
        return true;
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
                StdDraw.line(node.point.x(), 0.0, node.point.x(), 1.0);
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(0.0, node.point.y(), 1.0, node.point.y());
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
        return null;
    }

    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return null;
    }
}