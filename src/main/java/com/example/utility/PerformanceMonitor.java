/**
 * PerformanceMonitor.java
 * by Haisam Elkewidy
 *
 * This class handles PerformanceMonitor functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - startTime (long)
 *   - endTime (long)
 *
 * Methods:
 *   - start()
 *   - stop()
 *
 */

package com.example.utility;

public class PerformanceMonitor {
    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        endTime = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return endTime - startTime;
    }
}
