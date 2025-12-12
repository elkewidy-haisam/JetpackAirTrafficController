/**
 * GeometryUtils.java
 * by Haisam Elkewidy
 *
 * This class handles GeometryUtils functionality in the Air Traffic Controller system.
 *
 * Methods:
 *   - distance(x1, y1, x2, y2)
 *   - calculateDistance(x1, y1, x2, y2)
 *   - calculateDistance(x1, y1, x2, y2)
 *
 */

package com.example.utility;

public class GeometryUtils {
    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double calculateDistance(int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
