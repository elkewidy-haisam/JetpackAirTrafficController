/*
 * WaterDetector.java
 * Part of Jetpack Air Traffic Controller
 *
 * Utility for detecting water regions in the city grid.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */
package com.example.utility;

public class WaterDetector {
    public WaterDetector(String resourcePath) {}

    public boolean isWater(double x, double y) {
        // Dummy logic: treat y < 0 as water for demonstration
        return y < 0;
    }
}
