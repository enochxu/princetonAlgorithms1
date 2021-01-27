import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class PointSET {

    private final SET<Point2D> set;

    // construct an empty set of points 
    public PointSET() {
        set = new SET<Point2D>();
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set 
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        set.add(p);
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        return set.contains(p);
    }
    
    // draw all points to standard draw 
    public void draw() {
        for (Point2D point : set) {
            StdDraw.point(point.x(), point.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        Queue<Point2D> points = new Queue<Point2D>();
        for (Point2D point : set) {
            if (rect.contains(point))
                points.enqueue(point);
        }
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        checkNull(p);
        Point2D nearest = null;
        double minDist = Double.POSITIVE_INFINITY;
        for (Point2D point : set) {
            double dist = p.distanceTo(point);
            if (dist < minDist) {
                nearest = point;
                minDist = dist;
            }
        }
        return nearest;
    }

    private void checkNull(Object obj) {
        if (obj == null)
            throw new IllegalArgumentException("Point is null.");
    }
 
    // unit testing of the methods (optional) 
    // public static void main(String[] args) {

    // }
 }