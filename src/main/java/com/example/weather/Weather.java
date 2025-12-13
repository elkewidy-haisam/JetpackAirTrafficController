/**
 * Simulates and manages weather conditions including classification, severity levels, and atmospheric parameters.
 * 
 * Purpose:
 * Models dynamic weather conditions that affect jetpack flight operations, assigning severity levels to
 * weather types (Clear/Sunny=1 to Thunderstorm=5) and tracking atmospheric parameters (temperature, wind
 * speed, visibility). Provides safety assessments for flight operations and broadcasts weather updates
 * through the radio system.
 * 
 * Key Responsibilities:
 * - Maintain current weather condition and severity level (1-5 scale)
 * - Track atmospheric parameters (temperature, wind speed, visibility)
 * - Classify weather types by operational severity (MINIMAL to CRITICAL)
 * - Provide flight safety assessments based on weather severity
 * - Support random weather changes for realistic simulation
 * - Generate formatted weather broadcasts for radio transmission
 * 
 * Interactions:
 * - Used by CityMapPanel to trigger flight groundings in severe weather
 * - Referenced by WeatherBroadcastPanel for UI display updates
 * - Broadcasts through Radio system to notify all jetpacks
 * - Affects FlightEmergencyHandler decisions for emergency landings
 * - Logged by CityLogManager for weather history tracking
 * 
 * Patterns & Constraints:
 * - Severity levels: 1 (safe) to 5 (critical - no flight operations)
 * - Immutable weather-to-severity mapping defined at initialization
 * - Random weather changes weighted by current conditions
 * - Thread-safe for reads; external synchronization for weather updates
 * - VERBOSE_LOGGING flag controls debug output for testing
 * 
 * @author Haisam Elkewidy
 */

package com.example.weather;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Weather {
    /** Severity level 1: Safe for all flight operations (Clear, Partly Cloudy, Light Rain, etc.) */
    public static final int SEVERITY_MINIMAL = 1;
    /** Severity level 2: Manageable conditions requiring pilot awareness (Fog, Steady Rain, Windy) */
    public static final int SEVERITY_MANAGEABLE = 2;
    /** Severity level 3: Moderate conditions requiring caution (Heavy Rain, Strong Winds) */
    public static final int SEVERITY_MODERATE = 3;
    /** Severity level 4: Severe conditions requiring restricted operations (Thunderstorms) */
    public static final int SEVERITY_SEVERE = 4;
    /** Severity level 5: Critical conditions - all flight operations must cease immediately */
    public static final int SEVERITY_CRITICAL = 5;
    /** Flag to control debug output - set to false in production to reduce console noise */
    private static final boolean VERBOSE_LOGGING = false;

    /** Maps weather condition names (e.g., "Clear/Sunny") to their severity levels (1-5) */
    private final Map<String, Integer> weatherTypes;
    /** The current weather condition name (e.g., "Thunderstorm", "Clear/Sunny") */
    private String currentWeather;
    /** The current severity level (1-5) corresponding to currentWeather */
    private int currentSeverity;
    /** Current temperature in degrees Fahrenheit */
    private double temperature;
    /** Current wind speed in miles per hour */
    private int windSpeed;
    /** Current visibility distance in miles */
    private int visibility;
    /** Unique identifier for this Weather instance (e.g., "WEATHER-01" for city-specific tracking) */
    private final String weatherID;
    /** Timestamp (milliseconds since epoch) of last weather update */
    private long lastUpdated;

    /**
     * Default constructor initializing weather to pleasant conditions.
     * Sets weather to "Clear/Sunny" with default ID "WEATHER-01".
     */
    public Weather() {
        this.weatherTypes = new HashMap<>();      // Create empty map for weather-to-severity mappings
        initializeWeatherTypes();                  // Populate map with all known weather types
        this.currentWeather = "Clear/Sunny";       // Start with clear conditions
        this.currentSeverity = weatherTypes.get(currentWeather);  // Get severity for Clear/Sunny (=1)
        this.temperature = 72.0;                   // Set comfortable temperature (72°F)
        this.windSpeed = 5;                        // Set light wind (5 mph)
        this.visibility = 10;                      // Set excellent visibility (10 miles)
        this.weatherID = "WEATHER-01";             // Assign default weather station ID
        this.lastUpdated = System.currentTimeMillis();  // Record initialization timestamp
    }

    /**
     * Constructor with custom weather ID and initial condition.
     * Validates initialWeather against known types, defaults to Clear/Sunny if invalid.
     * 
     * @param weatherID unique identifier for this weather instance
     * @param initialWeather the starting weather condition name
     */
    public Weather(String weatherID, String initialWeather) {
        this.weatherTypes = new HashMap<>();      // Create empty map for weather-to-severity mappings
        initializeWeatherTypes();                  // Populate map with all known weather types
        this.weatherID = weatherID;                // Store the provided weather station ID
        if (weatherTypes.containsKey(initialWeather)) {  // Check if initialWeather is valid
            this.currentWeather = initialWeather;  // Use provided weather condition
            this.currentSeverity = weatherTypes.get(initialWeather);  // Get severity from map
        } else {  // If invalid weather type provided
            this.currentWeather = "Clear/Sunny";   // Default to safe condition
            this.currentSeverity = SEVERITY_MINIMAL;  // Set lowest severity level
        }
        this.temperature = 72.0;                   // Set comfortable temperature (72°F)
        this.windSpeed = 5;                        // Set light wind (5 mph)
        this.visibility = 10;                      // Set excellent visibility (10 miles)
        this.lastUpdated = System.currentTimeMillis();  // Record initialization timestamp
    }

    /**
     * Populates weatherTypes map with all recognized weather conditions and their severity levels.
     * Groups conditions into severity categories from MINIMAL (1) to CRITICAL (5).
     */
    private void initializeWeatherTypes() {
        // SEVERITY_MINIMAL (1): Safe conditions for all flight operations
        weatherTypes.put("Clear/Sunny", SEVERITY_MINIMAL);       // Perfect visibility, no precipitation
        weatherTypes.put("Partly Cloudy", SEVERITY_MINIMAL);     // Some clouds, still safe
        weatherTypes.put("Overcast", SEVERITY_MINIMAL);          // Cloud cover but no precipitation
        weatherTypes.put("Light Rain", SEVERITY_MINIMAL);        // Minor precipitation, manageable
        weatherTypes.put("Drizzle", SEVERITY_MINIMAL);           // Very light precipitation
        weatherTypes.put("Light Snow", SEVERITY_MINIMAL);        // Minor snow, not accumulating fast
        weatherTypes.put("Flurries", SEVERITY_MINIMAL);          // Intermittent light snow

        // SEVERITY_MANAGEABLE (2): Requires pilot awareness and caution
        weatherTypes.put("Fog", SEVERITY_MANAGEABLE);            // Reduced visibility concern
        weatherTypes.put("Mist", SEVERITY_MANAGEABLE);           // Slight visibility reduction
        weatherTypes.put("Steady Rain", SEVERITY_MANAGEABLE);    // Consistent precipitation
        weatherTypes.put("Showers", SEVERITY_MANAGEABLE);        // Intermittent heavier rain
        weatherTypes.put("Thunder Showers", SEVERITY_MANAGEABLE);// Rain with lightning nearby
        weatherTypes.put("Windy Conditions", SEVERITY_MANAGEABLE);// Strong winds affecting control
    }

    /**
     * Generates and returns a formatted weather broadcast message.
     * Includes all atmospheric parameters, flight status, and safety recommendations.
     * Optionally outputs to console if VERBOSE_LOGGING is enabled.
     * 
     * @return formatted weather broadcast string ready for radio transmission
     */
    public String sendWeatherBroadcast() {
        String severityDescription = getSeverityDescription(currentSeverity);  // Get human-readable severity
        StringBuilder broadcast = new StringBuilder();  // Create builder for efficient string construction
        broadcast.append("\n*** WEATHER BROADCAST ***\n");  // Add header
        broadcast.append("Weather ID: ").append(weatherID).append("\n");  // Include weather station ID
        broadcast.append("Current Conditions: ").append(currentWeather).append("\n");  // Add weather type
        broadcast.append("Severity Level: ").append(currentSeverity).append(" - ").append(severityDescription).append("\n");  // Add severity with description
        broadcast.append("Temperature: ").append(String.format("%.1f", temperature)).append("°F\n");  // Add temp with 1 decimal
        broadcast.append("Wind Speed: ").append(windSpeed).append(" mph\n");  // Add wind speed
        broadcast.append("Visibility: ").append(visibility).append(" miles\n");  // Add visibility distance
        broadcast.append("Flight Status: ").append(getFlightStatus()).append("\n");  // Add flight safety assessment
        broadcast.append("Recommendations: ").append(getRecommendations()).append("\n");  // Add pilot recommendations
        broadcast.append("Last Updated: ").append(new java.util.Date(lastUpdated)).append("\n");  // Add timestamp
        broadcast.append("*************************\n");  // Add footer
        String message = broadcast.toString();  // Convert StringBuilder to String
        if (VERBOSE_LOGGING) {  // Check if debug output is enabled
            System.out.println(message);  // Output to console for debugging
        }
        return message;  // Return formatted broadcast string
    }

    /**
     * Changes the current weather to a new condition.
     * Validates the weather type, updates severity, adjusts atmospheric parameters,
     * and broadcasts the change. Silently ignores invalid weather types.
     * 
     * @param newWeather the name of the new weather condition to apply
     */
    public void changeWeather(String newWeather) {
        if (!weatherTypes.containsKey(newWeather)) {  // Validate weather type exists
            return;  // Silently abort if invalid weather type
        }
        String oldWeather = this.currentWeather;  // Store previous weather for logging
        int oldSeverity = this.currentSeverity;   // Store previous severity for comparison
        this.currentWeather = newWeather;         // Update to new weather condition
        this.currentSeverity = weatherTypes.get(newWeather);  // Look up and set new severity
        this.lastUpdated = System.currentTimeMillis();  // Record timestamp of change
        adjustWeatherParameters();  // Update temperature, wind, visibility for new weather
        if (VERBOSE_LOGGING) {  // Check if debug output is enabled
            System.out.println("\n*** WEATHER CHANGE ***");  // Log header
            System.out.println("Previous: " + oldWeather + " (Severity " + oldSeverity + ")");  // Log old state
            System.out.println("Current: " + currentWeather + " (Severity " + currentSeverity + ")");  // Log new state
            if (currentSeverity > oldSeverity) {  // Check if weather worsened
                System.out.println("WARNING: Weather conditions have deteriorated!");  // Alert on deterioration
            } else if (currentSeverity < oldSeverity) {  // Check if weather improved
                System.out.println("Weather conditions have improved.");  // Note improvement
            }
            System.out.println("**********************\n");  // Log footer
        }
        sendWeatherBroadcast();  // Broadcast updated weather to all listeners
    }

    /**
     * Randomly changes the weather to any registered weather type.
     * Useful for simulation and testing dynamic weather scenarios.
     */
    public void changeWeatherRandomly() {
        Random random = new Random();  // Create random number generator
        String[] weatherArray = weatherTypes.keySet().toArray(new String[weatherTypes.size()]);  // Convert map keys to array
        String randomWeather = weatherArray[random.nextInt(weatherArray.length)];  // Pick random weather from array
        changeWeather(randomWeather);  // Apply the randomly selected weather
    }

    /**
     * Adjusts atmospheric parameters (temperature, wind speed, visibility) based on current weather.
     * Each weather type has realistic ranges with randomization for variety.
     * Called after weather changes to update conditions appropriately.
     */
    private void adjustWeatherParameters() {
        Random random = new Random();  // Create random number generator for variation
        switch (currentWeather) {  // Select parameter ranges based on weather type
            case "Clear/Sunny":  // Perfect weather conditions
                temperature = 70 + random.nextInt(20);  // 70-89°F (pleasant to warm)
                windSpeed = 3 + random.nextInt(7);      // 3-9 mph (light breeze)
                visibility = 10;                        // 10 miles (maximum visibility)
                break;
            case "Partly Cloudy":  // Moderate cloud cover
            case "Overcast":       // Full cloud cover
                temperature = 60 + random.nextInt(15);  // 60-74°F (moderate temps)
                windSpeed = 5 + random.nextInt(10);     // 5-14 mph (light to moderate wind)
                visibility = 8 + random.nextInt(3);     // 8-10 miles (good visibility)
                break;
            case "Light Rain":  // Light precipitation
            case "Drizzle":     // Very light rain
                temperature = 50 + random.nextInt(15);  // 50-64°F (cool conditions)
                windSpeed = 8 + random.nextInt(12);     // 8-19 mph (moderate wind)
                visibility = 5 + random.nextInt(4);     // 5-8 miles (reduced visibility)
                break;
            case "Light Snow":  // Light snow conditions
            case "Flurries":    // Intermittent snow
                temperature = 25 + random.nextInt(15);  // 25-39°F (below freezing to near freezing)
                windSpeed = 10 + random.nextInt(10);    // 10-19 mph (moderate wind)
                visibility = 4 + random.nextInt(4);     // 4-7 miles (snow reduces visibility)
                break;
            case "Fog":  // Heavy fog conditions
            case "Mist": // Light fog
                temperature = 45 + random.nextInt(20);  // 45-64°F (cool to moderate)
                windSpeed = 2 + random.nextInt(5);      // 2-6 mph (light wind, fog forms in calm)
                visibility = 1 + random.nextInt(3);     // 1-3 miles (severely reduced visibility)
                break;
            case "Steady Rain":  // Continuous rainfall
                temperature = 45 + random.nextInt(20);  // 45-64°F (cool to moderate)
                windSpeed = 15 + random.nextInt(15);    // 15-29 mph (strong wind)
                visibility = 2 + random.nextInt(4);     // 2-5 miles (poor visibility)
                break;
            case "Showers":       // Intermittent heavy rain
            case "Thunder Showers": // Rain with lightning
                temperature = 55 + random.nextInt(20);  // 55-74°F (moderate temps)
                windSpeed = 20 + random.nextInt(20);    // 20-39 mph (very strong wind)
                visibility = 2 + random.nextInt(3);     // 2-4 miles (very poor visibility)
                break;
            case "Windy Conditions":  // High winds
                temperature = 50 + random.nextInt(25);  // 50-74°F (wide temperature range)
                windSpeed = 25 + random.nextInt(25);    // 25-49 mph (dangerous wind speeds)
                visibility = 6 + random.nextInt(5);     // 6-10 miles (moderate visibility)
                break;
            default:  // Fallback for unknown weather types
                temperature = 65;   // Default moderate temperature
                windSpeed = 10;     // Default moderate wind
                visibility = 10;    // Default good visibility
        }
    }

    /**
     * Returns a human-readable description for a given severity level.
     * 
     * @param severity the severity level (1-5)
     * @return descriptive string explaining the severity impact
     */
    private String getSeverityDescription(int severity) {
        switch (severity) {  // Map severity number to description
            case SEVERITY_MINIMAL: return "Everyday conditions, minimal risk";        // Level 1
            case SEVERITY_MANAGEABLE: return "Noticeable impact, manageable disruptions";  // Level 2
            case SEVERITY_MODERATE: return "Moderate risk, flight restrictions";      // Level 3
            case SEVERITY_SEVERE: return "Severe conditions, flight hazardous";       // Level 4
            case SEVERITY_CRITICAL: return "Critical conditions, no-fly zone";        // Level 5
            default: return "Unknown severity";  // Fallback for invalid severity
        }
    }

    /**
     * Returns the current flight operational status based on weather severity.
     * Provides guidance for air traffic control decisions.
     * 
     * @return operational status string for current severity
     */
    private String getFlightStatus() {
        switch (currentSeverity) {  // Map severity to flight status
            case SEVERITY_MINIMAL: return "Normal operations";              // Level 1 - all clear
            case SEVERITY_MANAGEABLE: return "Caution advised";             // Level 2 - be aware
            case SEVERITY_MODERATE: return "Flight restrictions in effect"; // Level 3 - limited ops
            case SEVERITY_SEVERE: return "Flight operations suspended";     // Level 4 - ground all
            default: return "No-fly zone declared";  // Level 5 - absolute prohibition
        }
    }

    /**
     * Returns pilot recommendations based on current weather severity.
     * Provides actionable guidance for flight safety decisions.
     * 
     * @return recommendation string for current severity level
     */
    private String getRecommendations() {
        switch (currentSeverity) {  // Select recommendations based on severity
            case SEVERITY_MINIMAL: return "Safe to fly. Standard precautions apply.";           // Level 1 - normal ops
            case SEVERITY_MANAGEABLE: return "Exercise increased caution. Monitor conditions closely.";  // Level 2 - heightened awareness
            case SEVERITY_MODERATE: return "Experienced pilots only. Avoid unnecessary flights.";  // Level 3 - restricted
            case SEVERITY_SEVERE: return "Ground all aircraft. Emergency operations only.";      // Level 4 - suspend ops
            default: return "All flights prohibited. Seek immediate shelter.";  // Level 5 - total prohibition
        }
    }

    /**
     * Determines if conditions are safe for jetpack flight operations.
     * Checks severity level, visibility, and wind speed against safety thresholds.
     * 
     * @return true if safe to fly, false if conditions are hazardous
     */
    public boolean isSafeToFly() {
        // Return true only if ALL safety conditions are met:
        return currentSeverity <= SEVERITY_MANAGEABLE  // Severity must be 1 or 2 (not 3+)
                && visibility >= 3                      // Visibility must be at least 3 miles
                && windSpeed < 30;                      // Wind speed must be under 30 mph
    }

    /**
     * Returns a defensive copy of all weather types and their severity mappings.
     * Protects internal state from external modification.
     * 
     * @return new HashMap with weather type to severity level mappings
     */
    public Map<String, Integer> getWeatherTypes() {
        return new HashMap<>(weatherTypes);  // Return copy to prevent external modification
    }

    /**
     * Returns the current weather condition name.
     * @return string representing current weather (e.g., "Clear/Sunny", "Thunderstorm")
     */
    public String getCurrentWeather() {
        return currentWeather;  // Return current weather type
    }

    /**
     * Returns the current weather severity level.
     * @return severity integer (1-5 scale)
     */
    public int getCurrentSeverity() {
        return currentSeverity;  // Return current severity value
    }

    /**
     * Returns the current temperature.
     * @return temperature in degrees Fahrenheit
     */
    public double getTemperature() {
        return temperature;  // Return temperature value
    }

    /**
     * Sets the temperature to a specific value.
     * @param temperature the new temperature in degrees Fahrenheit
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;  // Update temperature field
    }

    /**
     * Returns the current wind speed.
     * @return wind speed in miles per hour
     */
    public int getWindSpeed() {
        return windSpeed;  // Return wind speed value
    }

    /**
     * Sets the wind speed to a specific value.
     * @param windSpeed the new wind speed in miles per hour
     */
    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;  // Update wind speed field
    }

    /**
     * Returns the current visibility distance.
     * @return visibility in miles
     */
    public int getVisibility() {
        return visibility;  // Return visibility value
    }

    /**
     * Sets the visibility to a specific distance.
     * @param visibility the new visibility distance in miles
     */
    public void setVisibility(int visibility) {
        this.visibility = visibility;  // Update visibility field
    }

    /**
     * Returns the unique weather station identifier.
     * @return weather ID string
     */
    public String getWeatherID() {
        return weatherID;  // Return immutable weather ID
    }

    /**
     * Returns the timestamp of the last weather update.
     * @return milliseconds since epoch of last update
     */
    public long getLastUpdated() {
        return lastUpdated;  // Return last update timestamp
    }

    /**
     * Returns a formatted string representation of this Weather object.
     * Includes all key atmospheric parameters and flight safety status.
     * 
     * @return comprehensive weather state string
     */
    @Override
    public String toString() {
        // Build multi-field string representation
        return "Weather{" +
                "weatherID='" + weatherID + '\'' +                              // Include weather station ID
                ", currentWeather='" + currentWeather + '\'' +                  // Include weather type
                ", severity=" + currentSeverity +                                // Include severity level
                ", temperature=" + String.format("%.1f", temperature) + "°F" +  // Include temperature with 1 decimal
                ", windSpeed=" + windSpeed + " mph" +                           // Include wind speed
                ", visibility=" + visibility + " miles" +                       // Include visibility
                ", safeToFly=" + isSafeToFly() +                               // Include safety assessment
                '}';
    }
}