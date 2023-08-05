
/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x; // x-coordinate of this point
    private final int y; // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (this.compareTo(that) == 0)
            return Double.NEGATIVE_INFINITY;

        if (this.y == that.y) {
            return +0.0;
        }            

        if (this.x == that.x)
            return Double.POSITIVE_INFINITY;

        return ((double)that.y - this.y) / ((double)that.x - this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        if (this.y < that.y)
            return -1;
        if (this.y > that.y)
            return 1;
        if (this.x < that.x)
            return -1;
        if (this.x > that.x)
            return 1;

        return 0;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new BySlope(this);
    }

    private static class BySlope implements Comparator<Point> {
        Point from;

        public BySlope(Point from) {
            this.from = from;
        }

        public int compare(Point p0, Point p1) {
            var slope0 = from.slopeTo(p0);
            var slope1 = from.slopeTo(p1);

            if (slope0 < slope1)
                return -1;
            if (slope0 >= slope1)
                return 1;

            return 0;
        }
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        {
            Point p0 = new Point(0, 0);
            Point p1 = new Point(1, 1);
            printCalc(p0, p1, "slope", p0.slopeTo(p1));
        }

        {
            Point p0 = new Point(0, 0);
            Point p1 = new Point(1, 2);
            printCalc(p0, p1, "slope", p0.slopeTo(p1));
        }

        {
            Point p0 = new Point(0, 0);
            Point p1 = new Point(0, 2);
            printCalc(p0, p1, "slope", p0.slopeTo(p1));
        }

        {
            Point p0 = new Point(0, 0);
            Point p1 = new Point(0, 0);
            printCalc(p0, p1, "slope", p0.slopeTo(p1));
        }

        {
            Point p0 = new Point(0, 0);
            Point p1 = new Point(0, 0);
            printCalc(p0, p1, "compareTo", p0.compareTo(p1));
        }

        {
            Point origin = new Point(0, 0);
            Point fifteen = new Point(10, 15);
            printCalc(origin, fifteen, "slope", origin.slopeTo(fifteen));

            Point minusTwenty = new Point(10, -20);
            printCalc(origin, minusTwenty, "slope", origin.slopeTo(minusTwenty));

            Point six = new Point(10, 6);
            printCalc(origin, six, "slope", origin.slopeTo(six));

            Point eleven = new Point(10, 11);
            printCalc(origin, eleven, "slope", origin.slopeTo(eleven));

            Point[] points = new Point[] { fifteen, minusTwenty, six, eleven };

            Arrays.sort(points, origin.slopeOrder());

            for (var p : points)
                System.out.println("> " + p);

            System.out.println();
            {
                Point p0 = new Point(0, 0);
                Point p1 = new Point(6, 8);
                printCalc(p0, p1, "slope", p0.slopeTo(p1));
            }
        }
    }

    static void printCalc(Point p0, Point p1, String description, Object result) {
        var line = String.format("%s: %s and %s = %s", description, p0, p1, result);

        System.out.println(line);
    }
}
