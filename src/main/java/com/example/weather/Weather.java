/**
 * Weather component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides weather functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core weather operations
 * - Maintain necessary state for weather functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
 * 
 * @author Haisam Elkewidy
 */

package com.example.weather;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Weather {
    public static final int SEVERITY_MINIMAL = 1;
    public static final int SEVERITY_MANAGEABLE = 2;
    public static final int SEVERITY_MODERATE = 3;
    public static final int SEVERITY_SEVERE = 4;
    public static final int SEVERITY_CRITICAL = 5;
    private static final boolean VERBOSE_LOGGING = false;

    private final Map<String, Integer> weatherTypes;
    private String currentWeather;
    private int currentSeverity;
    private double temperature;
    private int windSpeed;
    private int visibility;
    private final String weatherID;
    private long lastUpdated;

    /**
     * Constructs a Weather object with default clear conditions.
     * Initializes with safe flying conditions and default weather ID.
     */
    public Weather() {
        // Initialize weather type mapping
        this.weatherTypes = new HashMap<>();
        initializeWeatherTypes();
        
        // Start with clear, safe flying conditions
        this.currentWeather = "Clear/Sunny";
        this.currentSeverity = weatherTypes.get(currentWeather);
        
        // Set default atmospheric parameters for clear day
        this.temperature = 72.0;  // Comfortable room temperature
        this.windSpeed = 5;       // Light breeze
        this.visibility = 10;     // Excellent visibility
        
        // Assign default weather system identifier
        this.weatherID = "WEATHER-01";
        
        // Capture initialization timestamp
        this.lastUpdated = System.currentTimeMillis();
    }

    /**
     * Constructs a Weather object with specified ID and initial conditions.
     * Validates initial weather type, defaulting to clear if invalid.
     * 
     * @param weatherID Unique identifier for this weather system
     * @param initialWeather Initial weather condition description
     */
    public Weather(String weatherID, String initialWeather) {
        // Initialize weather type mapping
        this.weatherTypes = new HashMap<>();
        initializeWeatherTypes();
        
        // Store weather system identifier
        this.weatherID = weatherID;
        
        // Set initial weather with validation
        if (weatherTypes.containsKey(initialWeather)) {
            // Use provided weather if valid
            this.currentWeather = initialWeather;
            this.currentSeverity = weatherTypes.get(initialWeather);
        } else {
            // Default to safe conditions if invalid weather specified
            this.currentWeather = "Clear/Sunny";
            this.currentSeverity = SEVERITY_MINIMAL;
        }
        
        // Set default atmospheric parameters
        this.temperature = 72.0;
        this.windSpeed = 5;
        this.visibility = 10;
        
        // Capture initialization timestamp
        this.lastUpdated = System.currentTimeMillis();
    }

    private void initializeWeatherTypes() {
        weatherTypes.put("Clear/Sunny", SEVERITY_MINIMAL);
        weatherTypes.put("Partly Cloudy", SEVERITY_MINIMAL);
        weatherTypes.put("Overcast", SEVERITY_MINIMAL);
        weatherTypes.put("Light Rain", SEVERITY_MINIMAL);
        weatherTypes.put("Drizzle", SEVERITY_MINIMAL);
        weatherTypes.put("Light Snow", SEVERITY_MINIMAL);
        weatherTypes.put("Flurries", SEVERITY_MINIMAL);

        weatherTypes.put("Fog", SEVERITY_MANAGEABLE);
        weatherTypes.put("Mist", SEVERITY_MANAGEABLE);
        weatherTypes.put("Steady Rain", SEVERITY_MANAGEABLE);
        weatherTypes.put("Showers", SEVERITY_MANAGEABLE);
        weatherTypes.put("Thunder Showers", SEVERITY_MANAGEABLE);
        weatherTypes.put("Windy Conditions", SEVERITY_MANAGEABLE);
    }

    public String sendWeatherBroadcast() {
        String severityDescription = getSeverityDescription(currentSeverity);
        StringBuilder broadcast = new StringBuilder();
        broadcast.append("\n*** WEATHER BROADCAST ***\n");
        broadcast.append("Weather ID: ").append(weatherID).append("\n");
        broadcast.append("Current Conditions: ").append(currentWeather).append("\n");
        broadcast.append("Severity Level: ").append(currentSeverity).append(" - ").append(severityDescription).append("\n");
        broadcast.append("Temperature: ").append(String.format("%.1f", temperature)).append("°F\n");
        broadcast.append("Wind Speed: ").append(windSpeed).append(" mph\n");
        broadcast.append("Visibility: ").append(visibility).append(" miles\n");
        broadcast.append("Flight Status: ").append(getFlightStatus()).append("\n");
        broadcast.append("Recommendations: ").append(getRecommendations()).append("\n");
        broadcast.append("Last Updated: ").append(new java.util.Date(lastUpdated)).append("\n");
        broadcast.append("*************************\n");
        String message = broadcast.toString();
        if (VERBOSE_LOGGING) {
            System.out.println(message);
        }
        return message;
    }

    /**
     * Changes weather to specified condition if valid.
     * Updates severity, adjusts parameters, and broadcasts change.
     * Silently ignores invalid weather types.
     * 
     * @param newWeather New weather condition string
     */
    public void changeWeather(String newWeather) {
        // Validate weather type exists in mapping
        if (!weatherTypes.containsKey(newWeather)) {
            return;  // Silently ignore invalid weather types
        }
        
        // Store previous state for comparison logging
        String oldWeather = this.currentWeather;
        int oldSeverity = this.currentSeverity;
        
        // Update weather condition and severity
        this.currentWeather = newWeather;
        this.currentSeverity = weatherTypes.get(newWeather);
        
        // Update timestamp to track when change occurred
        this.lastUpdated = System.currentTimeMillis();
        
        // Adjust temperature, wind, visibility based on new conditions
        adjustWeatherParameters();
        
        // Log weather change if verbose logging enabled
        if (VERBOSE_LOGGING) {
            System.out.println("\n*** WEATHER CHANGE ***");
            System.out.println("Previous: " + oldWeather + " (Severity " + oldSeverity + ")");
            System.out.println("Current: " + currentWeather + " (Severity " + currentSeverity + ")");
            
            // Alert on deteriorating conditions
            if (currentSeverity > oldSeverity) {
                System.out.println("WARNING: Weather conditions have deteriorated!");
            } else if (currentSeverity < oldSeverity) {
                System.out.println("Weather conditions have improved.");
            }
            System.out.println("**********************\n");
        }
        
        // Broadcast new weather to all systems
        sendWeatherBroadcast();
    }

    /**
     * Changes weather to a random condition from available types.
     * Useful for simulation and testing scenarios.
     * Selects uniformly from all defined weather conditions.
     */
    public void changeWeatherRandomly() {
        // Create random number generator
        Random random = new Random();
        
        // Convert weather types keyset to array for indexing
        String[] weatherArray = weatherTypes.keySet().toArray(new String[weatherTypes.size()]);
        
        // Select random weather from available types
        String randomWeather = weatherArray[random.nextInt(weatherArray.length)];
        
        // Apply selected weather using standard change method
        changeWeather(randomWeather);
    }

    private void adjustWeatherParameters() {
        Random random = new Random();
        switch (currentWeather) {
            case "Clear/Sunny":
                temperature = 70 + random.nextInt(20);
                windSpeed = 3 + random.nextInt(7);
                visibility = 10;
                break;
            case "Partly Cloudy":
            case "Overcast":
                temperature = 60 + random.nextInt(15);
                windSpeed = 5 + random.nextInt(10);
                visibility = 8 + random.nextInt(3);
                break;
            case "Light Rain":
            case "Drizzle":
                temperature = 50 + random.nextInt(15);
                windSpeed = 8 + random.nextInt(12);
                visibility = 5 + random.nextInt(4);
                break;
            case "Light Snow":
            case "Flurries":
                temperature = 25 + random.nextInt(15);
                windSpeed = 10 + random.nextInt(10);
                visibility = 4 + random.nextInt(4);
                break;
            case "Fog":
            case "Mist":
                temperature = 45 + random.nextInt(20);
                windSpeed = 2 + random.nextInt(5);
                visibility = 1 + random.nextInt(3);
                break;
            case "Steady Rain":
                temperature = 45 + random.nextInt(20);
                windSpeed = 15 + random.nextInt(15);
                visibility = 2 + random.nextInt(4);
                break;
            case "Showers":
            case "Thunder Showers":
                temperature = 55 + random.nextInt(20);
                windSpeed = 20 + random.nextInt(20);
                visibility = 2 + random.nextInt(3);
                break;
            case "Windy Conditions":
                temperature = 50 + random.nextInt(25);
                windSpeed = 25 + random.nextInt(25);
                visibility = 6 + random.nextInt(5);
                break;
            default:
                temperature = 65;
                windSpeed = 10;
                visibility = 10;
        }
    }

    private String getSeverityDescription(int severity) {
        switch (severity) {
            case SEVERITY_MINIMAL: return "Everyday conditions, minimal risk";
            case SEVERITY_MANAGEABLE: return "Noticeable impact, manageable disruptions";
            case SEVERITY_MODERATE: return "Moderate risk, flight restrictions";
            case SEVERITY_SEVERE: return "Severe conditions, flight hazardous";
            case SEVERITY_CRITICAL: return "Critical conditions, no-fly zone";
            default: return "Unknown severity";
        }
    }

    private String getFlightStatus() {
        switch (currentSeverity) {
            case SEVERITY_MINIMAL: return "Normal operations";
            case SEVERITY_MANAGEABLE: return "Caution advised";
            case SEVERITY_MODERATE: return "Flight restrictions in effect";
            case SEVERITY_SEVERE: return "Flight operations suspended";
            default: return "No-fly zone declared";
        }
    }

    private String getRecommendations() {
        switch (currentSeverity) {
            case SEVERITY_MINIMAL: return "Safe to fly. Standard precautions apply.";
            case SEVERITY_MANAGEABLE: return "Exercise increased caution. Monitor conditions closely.";
            case SEVERITY_MODERATE: return "Experienced pilots only. Avoid unnecessary flights.";
            case SEVERITY_SEVERE: return "Ground all aircraft. Emergency operations only.";
            default: return "All flights prohibited. Seek immediate shelter.";
        }
    }

    /**
     * Determines if current conditions are safe for flight operations.
     * Evaluates severity, visibility, and wind speed against safety thresholds.
     * Used by ATC for flight approval decisions.
     * 
     * @return true if conditions meet all safety criteria, false otherwise
     */
    public boolean isSafeToFly() {
        // Check all safety criteria must be met:
        // 1. Severity at or below manageable level (1-2)
        // 2. Visibility at least 3 miles
        // 3. Wind speed below 30 mph
        return currentSeverity <= SEVERITY_MANAGEABLE && visibility >= 3 && windSpeed < 30;
    }

    public Map<String, Integer> getWeatherTypes() {
        return new HashMap<>(weatherTypes);
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public int getCurrentSeverity() {
        return currentSeverity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getWeatherID() {
        return weatherID;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "weatherID='" + weatherID + '\'' +
                ", currentWeather='" + currentWeather + '\'' +
                ", severity=" + currentSeverity +
                ", temperature=" + String.format("%.1f", temperature) + "°F" +
                ", windSpeed=" + windSpeed + " mph" +
                ", visibility=" + visibility + " miles" +
                ", safeToFly=" + isSafeToFly() +
                '}';
    }
}