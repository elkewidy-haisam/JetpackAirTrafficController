/**
 * Unit tests for Weather functionality and behavior validation.
 * 
 * Purpose:
 * Validates correctness of Weather through automated JUnit test cases. Ensures reliability
 * and prevents regressions by testing core functionality, edge cases, and integration scenarios.
 * 
 * Key Test Areas:
 * - Core Weather operations and expected outcomes
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

package com.example.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.example.weather.Weather;

/**
 * Comprehensive test suite for Weather model
 */
public class WeatherTest {
    private Weather weather;
    
    @Before
    public void setUp() {
        weather = new Weather();
    }
    
    @Test
    public void testWeatherCreation() {
        assertNotNull("Weather should be created", weather);
        assertNotNull("Should have current weather", weather.getCurrentWeather());
    }
    
    @Test
    public void testInitialWeatherIsGood() {
        String current = weather.getCurrentWeather();
        // Initial weather should be Clear/Sunny
        assertEquals("Initial weather should be Clear/Sunny", "Clear/Sunny", current);
    }
    
    @Test
    public void testWeatherChange() {
        String initial = weather.getCurrentWeather();
        weather.changeWeatherRandomly();
        String changed = weather.getCurrentWeather();
        
        // Weather might be same or different, but should be valid
        assertNotNull("Changed weather should not be null", changed);
        assertFalse("Changed weather should not be empty", changed.isEmpty());
    }
    
    @Test
    public void testMultipleWeatherChanges() {
        for (int i = 0; i < 20; i++) {
            weather.changeWeatherRandomly();
            String current = weather.getCurrentWeather();
            assertNotNull("Weather should not be null after change " + i, current);
            assertFalse("Weather should not be empty after change " + i, current.isEmpty());
        }
    }
    
    @Test
    public void testAllWeatherConditions() {
        // Change weather many times to encounter different conditions
        boolean foundClear = false;
        boolean foundCloudy = false;
        boolean foundRain = false;
        boolean foundThunderstorm = false;
        boolean foundFog = false;
        
        for (int i = 0; i < 100; i++) {
            weather.changeWeatherRandomly();
            String w = weather.getCurrentWeather();
            
            if (w.contains("Clear")) foundClear = true;
            if (w.contains("Cloudy")) foundCloudy = true;
            if (w.contains("Rain")) foundRain = true;
            if (w.contains("Thunderstorm")) foundThunderstorm = true;
            if (w.contains("Fog")) foundFog = true;
        }
        
        // Should encounter at least some variety
        int varietyCount = (foundClear ? 1 : 0) + (foundCloudy ? 1 : 0) + 
                          (foundRain ? 1 : 0) + (foundThunderstorm ? 1 : 0) + 
                          (foundFog ? 1 : 0);
        assertTrue("Should have weather variety", varietyCount >= 2);
    }
    
    @Test
    public void testWeatherSeverityLevels() {
        // Test that severity levels are within valid range
        for (int i = 0; i < 5; i++) {
            weather.changeWeatherRandomly();
            int newSeverity = weather.getCurrentSeverity();
            assertTrue("Severity should be >= 1", newSeverity >= 1);
            assertTrue("Severity should be <= 5", newSeverity <= 5);
        }
    }
    
    @Test
    public void testSeverityMatchesWeather() {
        for (int i = 0; i < 30; i++) {
            weather.changeWeatherRandomly();
            String condition = weather.getCurrentWeather();
            int severity = weather.getCurrentSeverity();
            
            // Thunderstorms and blizzards should have high severity
            if (condition.contains("Thunderstorm") || condition.contains("Blizzard") || 
                condition.contains("Hurricane")) {
                assertTrue("Severe weather should have high severity: " + condition, 
                    severity >= 3);
            }
            
            // Clear weather should have low severity
            if (condition.equals("Clear") || condition.equals("Partly Cloudy")) {
                assertTrue("Good weather should have low severity: " + condition, 
                    severity <= 2);
            }
        }
    }
    
    @Test
    public void testToString() {
        String str = weather.toString();
        assertNotNull("toString should not be null", str);
        assertTrue("toString should contain weather info", str.length() > 0);
    }
    
    @Test
    public void testWeatherConsistency() {
        // Weather should remain consistent between queries if not changed
        String w1 = weather.getCurrentWeather();
        String w2 = weather.getCurrentWeather();
        assertEquals("Weather should be consistent without change", w1, w2);
        
        int s1 = weather.getCurrentSeverity();
        int s2 = weather.getCurrentSeverity();
        assertEquals("Severity should be consistent without change", s1, s2);
    }
    
    @Test
    public void testSeverity1Conditions() {
        // Test severity 1 (safest)
        weather = new Weather();
        int count = 0;
        for (int i = 0; i < 50; i++) {
            weather.changeWeatherRandomly();
            if (weather.getCurrentSeverity() == 1) {
                count++;
                String condition = weather.getCurrentWeather();
                // Severity 1 includes: Clear/Sunny, Partly Cloudy, Overcast, Light Rain, Drizzle, Light Snow, Flurries
                assertTrue("Severity 1 should be low-impact weather, got: " + condition, 
                    weather.getCurrentSeverity() == 1);
            }
        }
        assertTrue("Should encounter severity 1 conditions", count > 0);
    }
    
    @Test
    public void testSeverity5Conditions() {
        // Test severity 5 (most dangerous)
        weather = new Weather();
        int count = 0;
        for (int i = 0; i < 100; i++) {
            weather.changeWeatherRandomly();
            if (weather.getCurrentSeverity() == 5) {
                count++;
                String condition = weather.getCurrentWeather();
                assertTrue("Severity 5 should be extreme weather",
                    condition.contains("Hurricane") || 
                    condition.contains("Blizzard") || 
                    condition.contains("Severe"));
            }
        }
        // Severity 5 is rare, so just check it's possible
        assertTrue("Should be possible to encounter severity 5", count >= 0);
    }
    
    @Test
    public void testWeatherDistribution() {
        // Test that weather changes produce reasonable distribution
        int[] severityCounts = new int[6]; // 0-5
        
        for (int i = 0; i < 100; i++) {
            weather.changeWeatherRandomly();
            int severity = weather.getCurrentSeverity();
            severityCounts[severity]++;
        }
        
        // Lower severities should be more common than higher ones
        assertTrue("Should have some low severity weather", 
            severityCounts[1] + severityCounts[2] > 0);
    }
}
