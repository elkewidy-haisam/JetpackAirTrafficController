/**
 * Unit tests for Alls functionality and behavior validation.
 * 
 * Purpose:
 * Validates correctness of Alls through automated JUnit test cases. Ensures reliability
 * and prevents regressions by testing core functionality, edge cases, and integration scenarios.
 * 
 * Key Test Areas:
 * - Core Alls operations and expected outcomes
 * - Edge cases and boundary condition handling
 * - Error scenarios and exception handling
 * - Integration with related components
 * 
 * Patterns & Constraints:
 * - JUnit framework for test execution and assertions
 * - Isolated test methods minimize inter-test dependencies
 * - Setup/teardown methods manage test state lifecycle
 * 
 * @author Haisam Elkewidy
 */

package com.example;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.example.detection.CollisionDetectorTest;
import com.example.detection.RadarTest;
import com.example.flight.FlightEmergencyHandlerTest;
import com.example.model.JetPackTest;
import com.example.model.ParkingSpaceTest;
import com.example.model.WeatherTest;
import com.example.utility.GeometryUtilsTest;
import com.example.utility.WaterDetectorTest;

/**
 * Comprehensive Test Suite Runner
 * Runs all unit tests for the JetPack Traffic Control System
 */
@RunWith(Suite.class)
@SuiteClasses({
    // Model tests
    JetPackTest.class,
    ParkingSpaceTest.class,
    WeatherTest.class,
    
    // Detection tests
    RadarTest.class,
    CollisionDetectorTest.class,
    
    // Utility tests
    GeometryUtilsTest.class,
    WaterDetectorTest.class,
    
    // Flight tests
    FlightEmergencyHandlerTest.class,
    
    // Original test
    AppTest.class
})
public class AllTests {
    // This class remains empty, it is used only as a holder for the above annotations
}
