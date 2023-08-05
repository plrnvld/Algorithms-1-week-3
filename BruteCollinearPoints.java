import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class BruteCollinearPoints {

    LineSegment[] segments;
    int segSize;
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        segments = new LineSegment[10];
        segSize = 0;

        for (var i = 0; i < points.length; i++)
            for (var j = i + 1; j < points.length; j++)
                for (var k = j + 1; k < points.length; k++)
                    for (var m = k + 1; m < points.length; m++) {
                        var p = points[i];
                        var q = points[j];
                        var r = points[k];
                        var s = points[m];

                        var slope = normalizedSlope(p, q);

                        if (normalizedSlope(p, r) == slope && normalizedSlope(p, s) == slope) {
                            addSegment(p, q, r, s);
                        }
                    }
    }

    private void addSegment(Point p, Point q, Point r, Point s) {
        var points = new Point[] { p, q, r, s};
        Arrays.sort(points, p.slopeOrder());
        var minPoint = points[0];
        var maxPoint = points[3];
        var newSegment = new LineSegment(minPoint, maxPoint);

        if (segSize > segments.length + 1) {
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

        segments[segSize++] = newSegment;        
    }

    private double normalizedSlope(Point p0, Point p1) {
        return p0.compareTo(p1) < 0 ? p0.slopeTo(p1) : p1.slopeTo(p0);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segSize;
    }

    // the line segments
    public LineSegment[] segments() {
        var results = new LineSegment[segSize];

        for (var i = 0; i < segSize; i++)
            results[i] = segments[i];

        return results;
    }
}
