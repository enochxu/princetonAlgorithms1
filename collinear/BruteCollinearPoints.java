import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {

    private final LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("Array is null");
        checkPointNull(points);

        // maximum amount of Collinear Point sets possible
        ArrayList<LineSegment> segmentList = new ArrayList<LineSegment>();
        Point[] pointsArray = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsArray);
        checkDuplicate(pointsArray);
        // Point[] sortedPoints = insertionSort(points);
        // checks if slopes are equal to find collinear points
        for (int p = 0; p < pointsArray.length; p++) {

            for (int q = p + 1; q < pointsArray.length; q++) {
                double slopeToQ = pointsArray[p].slopeTo(pointsArray[q]);

                for (int r = q + 1; r < pointsArray.length; r++) {
                    double slopeToR = pointsArray[q].slopeTo(pointsArray[r]);

                    for (int s = r + 1; s < pointsArray.length; s++) {
                        double slopeToS = pointsArray[r].slopeTo(pointsArray[s]);
                        if (slopeToQ == slopeToR && slopeToR == slopeToS) {
                            LineSegment segment = new LineSegment(pointsArray[p], pointsArray[s]);
                            segmentList.add(segment);
                        }
                    }
                }
            }
        }
        segments = segmentList.toArray(new LineSegment[segmentList.size()]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }

    private void checkPointNull(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("Array contains null points.");
        }
    }

    private void checkDuplicate(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0)
                throw new IllegalArgumentException("Detected duplicate points.");
        }
    }

    // private Point[] insertionSort(Point[] points) {
    // for (int i = 0; i < points.length; i++) {

    // int j = i;
    // // exchange if smaller
    // while (j > 0 && points[j].compareTo(points[j - 1]) < 0) {
    // Point temp = points[j];
    // points[j] = points[j - 1];
    // points[j - 1] = temp;
    // j--;
    // }
    // }
    // return points;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}