import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {
    
    private final LineSegment[] segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // check corner cases.
        if (points == null) {
            throw new IllegalArgumentException("Array is null.");
        }
        
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);
        checkExceptions(pointsCopy);

        Point[] sortedPoints = Arrays.copyOf(pointsCopy, pointsCopy.length);
        ArrayList<LineSegment> segmentList = new ArrayList<LineSegment>();   
        for (int lo = 0; lo < pointsCopy.length - 2; lo++) {
            Point p = sortedPoints[lo]; // base comparison point. Must be smallest
            Arrays.sort(pointsCopy, lo, pointsCopy.length, p.slopeOrder());
            segmentList.addAll(findSegments(pointsCopy, p, lo));
        }
        segments = segmentList.toArray(new LineSegment[segmentList.size()]);         
    }    

    private ArrayList<LineSegment> findSegments(Point[] sortedPoints, Point p, int lo) {
        // initial point to find slope with (2nd collinear point, given lo is the first)
        // i is initial point (two collinear points found)
        int i = lo + 1;  
        ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
        while (i < sortedPoints.length - 2) {
            double slope = p.slopeTo(sortedPoints[i]);
            Point endpoint = sortedPoints[i];
            int k = 1;
            while (i + k < sortedPoints.length && p.slopeTo(sortedPoints[i + k]) == slope) {
                if (sortedPoints[i + k].compareTo(endpoint) > 0)
                    endpoint = sortedPoints[i + k];
                k++;
            }
            // 3 points besides p
            if (k >= 3) {
                // sortedPoints[i + k - 1]
                // gotta sort to ensure the biggest one is last. Java sort is destructive.
                LineSegment segment = new LineSegment(sortedPoints[lo], endpoint);
                Boolean repeat = false;
                int j = 1;
                while (!repeat && j <= lo) {
                    if (p.slopeTo(sortedPoints[lo - j]) != slope)
                        j++;
                    else
                        repeat = true;
                }
                if (!repeat)
                    segments.add(segment);     
            }
            i = i + k;
        }
        return segments;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }
        
    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }                

    private void checkExceptions(Point[] points) {
        if (points[0] == null)
            throw new IllegalArgumentException("Array contains null points.");
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0)
                throw new IllegalArgumentException("Detected duplicate points.");
            if (points[i + 1] == null)
                throw new IllegalArgumentException("Array contains null points.");
        }
    }
    
    // private class Merge {
    //     private static Comparable[] aux;

    //     public void sort(Comparable[] a) {
    //         int N = a.length;
    //         aux = new Comparable[N];

    //         for (int size = 1; size < N; size = size + size) {
    //             for (int lo = 0; lo < N - size; lo += size + size) {
    //                 merge(a, lo, lo + size - 1, Math.min(lo + size + size - 1, N - 1));
    //             }
    //         }

    //     }

    //     public void merge(Comparable[] a, lo, mid, hi) {
    //         int i = lo;
    //         int j = mid + 1;

    //         for (int k = lo; i <= hi; i++) {
    //             aux[k] = a[k];
    //         }

    //         for (int k = )
    //             if (i > j) 

    //     }
    // }
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}