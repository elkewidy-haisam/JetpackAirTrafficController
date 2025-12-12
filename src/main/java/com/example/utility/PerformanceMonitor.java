/*
 * PerformanceMonitor.java
 * Part of Jetpack Air Traffic Controller
 *
 * Monitors performance metrics for the city simulation.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
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
