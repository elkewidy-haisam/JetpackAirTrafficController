/**
 * Measures execution time for performance profiling and optimization analysis.
 * 
 * Purpose:
 * Provides simple start/stop timing functionality to measure elapsed time for operations, enabling
 * performance profiling of rendering loops, computation-heavy algorithms, and UI responsiveness.
 * Useful for identifying bottlenecks during development and validating optimization improvements.
 * 
 * Key Responsibilities:
 * - Capture start timestamp when timing begins
 * - Capture end timestamp when timing completes
 * - Calculate elapsed time in milliseconds
 * - Support multiple sequential timing measurements via reset
 * 
 * Interactions:
 * - Used in rendering pipelines (GridRenderer, JetPackFlightRenderer) to measure frame times
 * - Applied in collision detection to assess algorithm performance
 * - Invoked in map loading and parsing operations for load time analysis
 * - Referenced in UI update cycles to ensure responsiveness thresholds
 * 
 * Patterns & Constraints:
 * - Simple stopwatch pattern with mutable state
 * - Not thread-safe; requires external synchronization for concurrent use
 * - Millisecond precision suitable for high-level profiling; not for microsecond analysis
 * - Single-operation tracking; use multiple instances for parallel measurements
 * - No automatic reset; caller responsible for start/stop sequence
 * 
 * @author Haisam Elkewidy
 */

package com.example.utility;

public class PerformanceMonitor {
    // Timestamp in milliseconds when timing started
    private long startTime;
    // Timestamp in milliseconds when timing stopped
    private long endTime;

    /**
     * Starts the performance timer.
     * Captures current system time as start timestamp.
     */
    public void start() {
        // Record current system time in milliseconds as start point
        startTime = System.currentTimeMillis();
    }

    /**
     * Stops the performance timer.
     * Captures current system time as end timestamp.
     */
    public void stop() {
        // Record current system time in milliseconds as end point
        endTime = System.currentTimeMillis();
    }

    /**
     * Calculates elapsed time between start and stop.
     * 
     * @return Elapsed time in milliseconds
     */
    public long getElapsedTime() {
        // Calculate and return difference between end and start times
        return endTime - startTime;
    }
}
