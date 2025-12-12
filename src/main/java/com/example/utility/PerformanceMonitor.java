/**
 * PerformanceMonitor.java
 * by Haisam Elkewidy
 *
 * Monitors performance metrics for the city simulation.
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
