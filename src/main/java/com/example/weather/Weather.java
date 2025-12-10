package com.example.weather;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Weather.java
 * by Haisam Elkewidy
 * 
 * This class represents different kinds of weather along with varying degrees of severity.
 * 
 * Known properties:
 * - A Map or Dictionary for Different types of weathers and the category of their severity.
 * 
 * The possible values are as follows:
 * - Clear/Sunny, 1
 * - Partly Cloudy/Overcast, 1
 * - Light Rain/Drizzle, 1
 * - Light Snow/Flurries, 1
 * - Fog/Mist, 2
 * - Steady Rain, 2
 * - Showers/Thunder Showers, 2
 * - Windy Conditions, 2
 * 
 * The values 1 and 2 correspond to "everyday conditions, minimal risk" and
 * "noticeable impact, manageable disruptions" on the global weather severity scale.
 * 
 * Known methods:
 * - sendWeatherBroadcast(): sets the weather and global severity on the GUI for the Air Traffic Controller
 * - changeWeather(): changes the weather announcement to a different type depending on the situation
 */
public class Weather {
    
    // Weather severity constants
    public static final int SEVERITY_MINIMAL = 1;      // Everyday conditions, minimal risk
    public static final int SEVERITY_MANAGEABLE = 2;   // Noticeable impact, manageable disruptions
    public static final int SEVERITY_MODERATE = 3;     // Moderate risk, flight restrictions
    public static final int SEVERITY_SEVERE = 4;       // Severe conditions, flight hazardous
    public static final int SEVERITY_CRITICAL = 5;     // Critical conditions, no-fly zone
    
    // Debug flag - set to false to disable console output for better performance
    private static boolean VERBOSE_LOGGING = false;
    
    private Map<String, Integer> weatherTypes;
    private String currentWeather;
    private int currentSeverity;
    private double temperature; // in Fahrenheit
    private int windSpeed; // in mph
    private int visibility; // in miles
    private String weatherID;
    private long lastUpdated;
    
    /**
     * Default constructor - initializes with clear weather
     */
    public Weather() {
        this.weatherTypes = new HashMap<>();
        initializeWeatherTypes();
        this.currentWeather = "Clear/Sunny";
        this.currentSeverity = weatherTypes.get(currentWeather);
        this.temperature = 72.0;
        this.windSpeed = 5;
        this.visibility = 10;
        this.weatherID = "WEATHER-01";
        this.lastUpdated = System.currentTimeMillis();
    }
    
    /**
     * Parameterized constructor
     * 
     * @param weatherID Unique identifier for the weather system
     * @param initialWeather Initial weather condition
     */
    public Weather(String weatherID, String initialWeather) {
        this.weatherTypes = new HashMap<>();
        initializeWeatherTypes();
        this.weatherID = weatherID;
        
        if (weatherTypes.containsKey(initialWeather)) {
            this.currentWeather = initialWeather;
            this.currentSeverity = weatherTypes.get(initialWeather);
        } else {
            this.currentWeather = "Clear/Sunny";
            this.currentSeverity = SEVERITY_MINIMAL;
        }
        
        this.temperature = 72.0;
        this.windSpeed = 5;
        this.visibility = 10;
        this.lastUpdated = System.currentTimeMillis();
    }
    
    /**
     * Initializes all weather types with their severity levels
     */
    private void initializeWeatherTypes() {
        // Severity Level 1: Everyday conditions, minimal risk
        weatherTypes.put("Clear/Sunny", SEVERITY_MINIMAL);
        weatherTypes.put("Partly Cloudy", SEVERITY_MINIMAL);
        weatherTypes.put("Overcast", SEVERITY_MINIMAL);
        weatherTypes.put("Light Rain", SEVERITY_MINIMAL);
        weatherTypes.put("Drizzle", SEVERITY_MINIMAL);
        weatherTypes.put("Light Snow", SEVERITY_MINIMAL);
        weatherTypes.put("Flurries", SEVERITY_MINIMAL);
        
        // Severity Level 2: Noticeable impact, manageable disruptions
        weatherTypes.put("Fog", SEVERITY_MANAGEABLE);
        weatherTypes.put("Mist", SEVERITY_MANAGEABLE);
        weatherTypes.put("Steady Rain", SEVERITY_MANAGEABLE);
        weatherTypes.put("Showers", SEVERITY_MANAGEABLE);
        weatherTypes.put("Thunder Showers", SEVERITY_MANAGEABLE);
        weatherTypes.put("Windy Conditions", SEVERITY_MANAGEABLE);
    }
    
    /**
     * Sets the weather and global severity on the GUI for the Air Traffic Controller
     * 
     * @return Weather broadcast message
     */
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
     * Changes the weather announcement to a different type depending on the situation
     * 
     * @param newWeather New weather condition
     */
    public void changeWeather(String newWeather) {
        if (!weatherTypes.containsKey(newWeather)) {
            // System.out.println("ERROR: Unknown weather type: " + newWeather);
            // System.out.println("Available weather types: " + weatherTypes.keySet());
            return;
        }
        
        String oldWeather = this.currentWeather;
        int oldSeverity = this.currentSeverity;
        
        this.currentWeather = newWeather;
        this.currentSeverity = weatherTypes.get(newWeather);
        this.lastUpdated = System.currentTimeMillis();
        
        // Adjust temperature, wind, and visibility based on weather type
        adjustWeatherParameters();
        
        if (VERBOSE_LOGGING) {
            System.out.println("\n*** WEATHER CHANGE ***");
            System.out.println("Previous: " + oldWeather + " (Severity " + oldSeverity + ")");
            System.out.println("Current: " + currentWeather + " (Severity " + currentSeverity + ")");
            
            if (currentSeverity > oldSeverity) {
                System.out.println("WARNING: Weather conditions have deteriorated!");
            } else if (currentSeverity < oldSeverity) {
                System.out.println("Weather conditions have improved.");
            }
            
            System.out.println("**********************\n");
        }
        
        sendWeatherBroadcast();
    }
    
    /**
     * Changes weather to a random condition
     */
    public void changeWeatherRandomly() {
        Random random = new Random();
        String[] weatherArray = weatherTypes.keySet().toArray(new String[0]);
        String randomWeather = weatherArray[random.nextInt(weatherArray.length)];
        changeWeather(randomWeather);
    }
    
    /**
     * Adjusts weather parameters based on current weather type
     */
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
    
    /**
     * Gets severity description based on level
     * 
     * @param severity Severity level
     * @return Description string
     */
    private String getSeverityDescription(int severity) {
        switch (severity) {
            case SEVERITY_MINIMAL:
                return "Everyday conditions, minimal risk";
            case SEVERITY_MANAGEABLE:
                return "Noticeable impact, manageable disruptions";
            case SEVERITY_MODERATE:
                return "Moderate risk, flight restrictions";
            case SEVERITY_SEVERE:
                return "Severe conditions, flight hazardous";
            case SEVERITY_CRITICAL:
                return "Critical conditions, no-fly zone";
            default:
                return "Unknown severity";
        }
    }
    
    /**
     * Gets flight status based on current weather
     * 
     * @return Flight status string
     */
    private String getFlightStatus() {
        if (currentSeverity == SEVERITY_MINIMAL) {
            return "Normal operations";
        } else if (currentSeverity == SEVERITY_MANAGEABLE) {
            return "Caution advised";
        } else if (currentSeverity == SEVERITY_MODERATE) {
            return "Flight restrictions in effect";
        } else if (currentSeverity == SEVERITY_SEVERE) {
            return "Flight operations suspended";
        } else {
            return "No-fly zone declared";
        }
    }
    
    /**
     * Gets recommendations based on current weather
     * 
     * @return Recommendations string
     */
    private String getRecommendations() {
        if (currentSeverity == SEVERITY_MINIMAL) {
            return "Safe to fly. Standard precautions apply.";
        } else if (currentSeverity == SEVERITY_MANAGEABLE) {
            return "Exercise increased caution. Monitor conditions closely.";
        } else if (currentSeverity == SEVERITY_MODERATE) {
            return "Experienced pilots only. Avoid unnecessary flights.";
        } else if (currentSeverity == SEVERITY_SEVERE) {
            return "Ground all aircraft. Emergency operations only.";
        } else {
            return "All flights prohibited. Seek immediate shelter.";
        }
    }
    
    /**
     * Checks if it's safe to fly
     * 
     * @return true if safe, false otherwise
     */
    public boolean isSafeToFly() {
        return currentSeverity <= SEVERITY_MANAGEABLE && visibility >= 3 && windSpeed < 30;
    }
    
    /**
     * Gets all available weather types
     * 
     * @return Map of weather types and severities
     */
    public Map<String, Integer> getWeatherTypes() {
        return new HashMap<>(weatherTypes);
    }
    
    // Getters and Setters
    
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
