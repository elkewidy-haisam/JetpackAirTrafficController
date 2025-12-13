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
 * Runs all unit tests for the JetPack Traffic Control System.
 * Use this class to execute all tests in a single run for continuous integration.
 */
@RunWith(Suite.class)  // Specify JUnit Suite runner to execute multiple test classes
@SuiteClasses({  // Define the list of test classes to include in this suite
    // Model tests - validate core data structures and business logic
    JetPackTest.class,         // Test JetPack model operations
    ParkingSpaceTest.class,    // Test parking space management
    WeatherTest.class,         // Test weather system functionality
    
    // Detection tests - validate collision and radar systems
    RadarTest.class,           // Test radar tracking and detection
    CollisionDetectorTest.class,  // Test collision detection algorithms
    
    // Utility tests - validate helper functions and calculations
    GeometryUtilsTest.class,   // Test geometric calculations
    WaterDetectorTest.class,   // Test water body detection
    
    // Flight tests - validate flight control and emergency systems
    FlightEmergencyHandlerTest.class,  // Test emergency handling logic
    
    // Original test - basic framework validation
    AppTest.class              // Test application bootstrap
})
public class AllTests {
    // This class remains empty, it is used only as a holder for the above annotations
    // JUnit Suite runner uses annotations to determine which tests to execute
}
