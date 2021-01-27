import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class KdTree {

    private class Node {

        private final Point2D point;
        public final int depth;
        public Node left;
        public Node right;
        
        public Node(Point2D p, int depth) {            
            point = p;
            this.depth = depth;
        }

        public Point2D getValue() {
            return point;
        }

        public double getAxis() {
            if (depth % 2 == 0) { // vertical
                return point.x();
            } else { // horitzontal
                return point.y();
            }
        }
    }

    private int size;
    private Node head;

    // construct an empty set of points 
    public KdTree() {
        size = 0;
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set 
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNull(p);
        if (isEmpty()){
            head = new Node(p, 0);
        } else {
            Node current = head;
            Node prev = null;
            double pntAxis = p.x();

            while (current != null) {
                prev = current;
                if (current.getValue().equals(p))
                    return;
                
                if (current.depth % 2 == 0) {
                    pntAxis = p.x();
                } else {
                    pntAxis = p.y();
                }
                if (pntAxis < current.getAxis())
                    current = current.left;
                else
                    current = current.right;
            }
            current = new Node(p, prev.depth + 1);
            // bad practice using pntAxis like this
            if (pntAxis < prev.getAxis())
                prev.left = current;
            else
                prev.right = current;
        }
        
        size++;
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        checkNull(p);
        Node current = head;
        while (current != null) {
            if (p.equals(current.getValue()))
                return true;
            if (p.compareTo(current.getValue()) < 0)
                current = current.left;
            else
                current = current.right;
        }
        return false;
    }
    
    // draw all points to standard draw 
    public void draw() {
        Node current = head;
        draw(current, 0, 1);
    }

    private void draw(Node current, double minBound, double maxBound) {
        if (current == null)
            return;
        double x = current.getValue().x();
        double y = current.getValue().y();
        double bound = 0;
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(x, y);

        StdDraw.setPenRadius(0.005);
        if (current.depth % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x, minBound, x, maxBound);
            bound = x;
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(minBound, y, maxBound, y);
            bound = y;
        }

        if (current.left != null) {
            draw(current.left, 0, bound);
        }
        if (current.right != null) {
            draw(current.right, bound, 1);
        }
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        Node current = head;
        Queue<Point2D> rangePoints = new Queue<Point2D>();

        findPoints(current, rect, rangePoints);
        return rangePoints;
    }

    private void findPoints(Node current, RectHV rect, Queue<Point2D> rangePoints) {
        if (current == null)
            return;

        double min;
        double max;
        double val;
        if (current.depth % 2 == 0) { // odd
            min = rect.xmin();
            max = rect.xmax();
            val = current.getValue().x();
        } else {
            min = rect.ymin();
            max = rect.ymax();
            val = current.getValue().y();
        }

        if (min < val) {
            findPoints(current.left, rect, rangePoints);
        }
        if (max > val) {
            findPoints(current.right, rect, rangePoints);
        } 
        if (rect.contains(current.getValue())) {
            rangePoints.enqueue(current.getValue());
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        checkNull(p);
        // System.out.println(head.left);
        return findNearest(head, p);
    }


    private Point2D findNearest(Node current, Point2D p) {
        if (current == null)
            return null;

        Point2D nearest = current.getValue();
        Double dist1D;
        if (current.depth % 2 == 0)
            dist1D = p.x() - current.getValue().x();
        else
            dist1D = p.y() - current.getValue().y();

        Point2D next;
        // System.out.println(dist1D);
        // current best
        if (dist1D < 0)
            next = findNearest(current.left, p);        
        else
            next = findNearest(current.right, p);

        if (next != null && (nearest.distanceSquaredTo(p) > next.distanceSquaredTo(p)))
            nearest = next;
        
        if (nearest.distanceTo(p) > Math.abs(dist1D)) { // other possibility
            if (dist1D < 0)
                next = findNearest(current.right, p);        
            else
                next = findNearest(current.left, p);

            if (next != null && nearest.distanceSquaredTo(p) > next.distanceSquaredTo(p))
                nearest = next;
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
