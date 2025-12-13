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
