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

    public boolean isSafeToFly() {
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