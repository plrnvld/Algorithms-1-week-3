import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {

    private LinkedList<MinMaxPoint> minMaxPoints;
    private boolean hasSegments;
    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        validatePoints(points);

        hasSegments = false;
        minMaxPoints = new LinkedList<>();

        for (var i = 0; i < points.length; i++) {
            var point = points[i];
            var otherPointsSorted = otherPointsSortedBySlope(point, points);
            findSegments(point, otherPointsSorted);
        }
    }

    private void validatePoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();

        for (var p : points)
            if (p == null)
                throw new IllegalArgumentException();

        var dupCheckPoints = new Point[points.length];
        for (var i = 0; i < points.length; i++)
            dupCheckPoints[i] = points[i];

        Arrays.sort(dupCheckPoints);

        for (var i = 1; i < dupCheckPoints.length; i++)
            if (dupCheckPoints[i - 1].compareTo(dupCheckPoints[i]) == 0)
                throw new IllegalArgumentException("Duplicate points");
    }

    private void findSegments(Point origin, Point[] otherPointsSortedBySlope) {
        var i = 0;
        while (i < otherPointsSortedBySlope.length) {
            var sameSlopePoints = pointsWithSameSlope(origin, otherPointsSortedBySlope, i);
            if (sameSlopePoints.length >= 4) {
                Arrays.sort(sameSlopePoints);
                var minPoint = sameSlopePoints[0];
                var maxPoint = sameSlopePoints[sameSlopePoints.length - 1];
                addMinMaxPoint(minPoint, maxPoint);
            }

            i += sameSlopePoints.length;
        }
    }

    private Point[] pointsWithSameSlope(Point origin, Point[] otherPointsSortedBySlope, int start) {
        var slopeComparator = origin.slopeOrder();
        var startPoint = otherPointsSortedBySlope[start];
        var next = start + 1;

        while (next < otherPointsSortedBySlope.length
                && slopeComparator.compare(startPoint, otherPointsSortedBySlope[next]) == 0)
            next++;

        var sameSlope = Arrays.copyOfRange(otherPointsSortedBySlope, start, next);
        var allPointsWithSameSlope = new Point[sameSlope.length + 1];
        allPointsWithSameSlope[0] = origin;

        for (var i = 0; i < sameSlope.length; i++)
            allPointsWithSameSlope[i + 1] = sameSlope[i];

        return allPointsWithSameSlope;
    }

    private Point[] otherPointsSortedBySlope(Point origin, Point[] allPoints) {
        var next = 0;
        var otherPoints = new Point[allPoints.length - 1];
        for (var i = 0; i < allPoints.length; i++) {
            var curr = allPoints[i];
            if (curr.compareTo(origin) != 0)
                otherPoints[next++] = curr;
        }

        Arrays.sort(otherPoints, origin.slopeOrder());

        return otherPoints;
    }

    private void addMinMaxPoint(Point p0, Point p1) {
        var minMaxPoint = new MinMaxPoint(p0, p1);
        minMaxPoints.push(minMaxPoint);
    }

    // the number of line segments
    public int numberOfSegments() {
        createSegments();

        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        createSegments();

        return Arrays.copyOf(segments, segments.length);
    }

    private void createSegments() {
        if (hasSegments)
            return;

        hasSegments = true;

        if (minMaxPoints.size() == 0) {
            segments = new LineSegment[0];
            return;
        }

        var arrayWithMinMaxPoints = minMaxPoints.toArray(new MinMaxPoint[minMaxPoints.size()]);

        Arrays.sort(arrayWithMinMaxPoints);

        var dedupped = new LinkedList<MinMaxPoint>();
        dedupped.push(arrayWithMinMaxPoints[0]);

        for (var i = 1; i < arrayWithMinMaxPoints.length; i++) {
            var curr = arrayWithMinMaxPoints[i];
            var prev = arrayWithMinMaxPoints[i - 1];

            if (curr.compareTo(prev) != 0)
                dedupped.push(curr);
        }

        var newSegments = new LineSegment[dedupped.size()];
        var j = 0;
        for (var curr : dedupped) 
            newSegments[j++] = new LineSegment(curr.minPoint, curr.maxPoint);

        segments = newSegments;
    }

    private class MinMaxPoint implements Comparable<MinMaxPoint> {
        public Point minPoint;
        public Point maxPoint;

        MinMaxPoint(Point p0, Point p1) {
            if (p0.compareTo(p1) < 0) {
                minPoint = p0;
                maxPoint = p1;
            } else {
                minPoint = p1;
                maxPoint = p0;
            }
        }

        public int compareTo(MinMaxPoint that) {
            if (that == null)
                throw new NullPointerException("[compareTo] that cannot be null");

            var compMin = minPoint.compareTo(that.minPoint);
            var compMax = maxPoint.compareTo(that.maxPoint);
            if (compMin != 0)
                return compMin;

            return compMax;
        }
    }

    public static void main(String[] args) {
        // (1000, 17000) -> (13000, 17000) -> (17000, 17000) -> (29000, 17000)
        Point p = new Point(1000, 17000);
        Point q = new Point(13000, 17000);
        Point r = new Point(17000, 17000);
        Point s = new Point(29000, 17000);

        var comparer = p.slopeOrder();

        System.out.println(comparer.compare(q, r));
        System.out.println(comparer.compare(r, s));


        var collinear = new FastCollinearPoints(new Point[] { r, s, q});
        System.out.println(collinear.numberOfSegments());

        for (var line : collinear.segments())
            System.out.println(line);
    }
}
