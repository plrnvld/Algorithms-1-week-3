import java.util.Arrays;

public class BruteCollinearPoints {

    private static final int START_SIZE = 10;

    private LineSegment[] segments;
    private int nextIndex;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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

        segments = new LineSegment[START_SIZE];
        nextIndex = 0;

        for (var i = 0; i < points.length; i++)
            for (var j = i + 1; j < points.length; j++)
                for (var k = j + 1; k < points.length; k++)
                    for (var m = k + 1; m < points.length; m++) {
                        var p = points[i];
                        var q = points[j];
                        var r = points[k];
                        var s = points[m];

                        var comparer = p.slopeOrder();
                        if (comparer.compare(q, r) == 0 && comparer.compare(r, s) == 0)
                            addSegment(p, q, r, s);
                    }
    }

    private void addSegment(Point p, Point q, Point r, Point s) {
        var points = new Point[] { p, q, r, s };
        Arrays.sort(points);
        var minPoint = points[0];
        var maxPoint = points[3];
        var newSegment = new LineSegment(minPoint, maxPoint);
        var index = nextIndex++;

        if (index >= segments.length) {
            var newSize = Integer.MAX_VALUE / 2 < segments.length
                    ? Integer.MAX_VALUE
                    : segments.length * 2;

            var newArray = new LineSegment[newSize];
            for (var i = 0; i < segments.length; i++) {
                newArray[i] = segments[i];
                segments[i] = null;
            }

            segments = newArray;
        }

        segments[index] = newSegment;
    }

    // the number of line segments
    public int numberOfSegments() {
        return nextIndex;
    }

    // the line segments
    public LineSegment[] segments() {
        var results = new LineSegment[nextIndex];

        for (var i = 0; i < nextIndex; i++)
            results[i] = segments[i];

        return results;
    }
}
