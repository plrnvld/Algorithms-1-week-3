
/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private static final double EPSILON = 0.0000001;            

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
        if (that == null)
            throw new NullPointerException("[slopeTo] that cannot be null");

        if (this.compareTo(that) == 0)
            return Double.NEGATIVE_INFINITY;

        if (this.y == that.y) {
            return +0.0;        }

        if (this.x == that.x)
            return Double.POSITIVE_INFINITY;

        return ((double) that.y - this.y) / ((double) that.x - this.x);
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
        if (that == null)
            throw new NullPointerException("[compareTo] that cannot be null");

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
            if (p0 == null || p1 == null)
                throw new NullPointerException("[compare]");

            var slope0 = normalizedSlope(from, p0);
            var slope1 = normalizedSlope(from, p1);

            var delta = Math.abs(slope1 - slope0);
            if (delta < EPSILON)
                return 0;

            if (slope0 == Double.POSITIVE_INFINITY && slope1 == Double.POSITIVE_INFINITY 
                || slope0 == Double.NEGATIVE_INFINITY && slope1 == Double.NEGATIVE_INFINITY)
                return 0;

            return slope0 < slope1 ? -1 : 1;
        }
    }

    private static double normalizedSlope(Point p0, Point p1) {
        return p0.compareTo(p1) < 0 ? p0.slopeTo(p1) : p1.slopeTo(p0);
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
        Point p = new Point(7, 9);
        Point q = new Point(1, 5);
        Point r = new Point(4, 7);

        printCalc(p, q, "slope", p.slopeTo(q));
        printCalc(p, r, "slope", p.slopeTo(r));

        printCalc(p, r, "slope compare", new BySlope(p).compare(q, r));
    }

    private static void printCalc(Point p0, Point p1, String description, Object result) {
        var line = String.format("%s: %s and %s = %s", description, p0, p1, result);

        System.out.println(line);
    }
}
