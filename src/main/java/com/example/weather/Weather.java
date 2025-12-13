/**
 * Dynamic weather system that simulates and manages atmospheric conditions affecting jetpack flight safety.
 * 
 * Purpose:
 * Models real-time weather conditions with severity-based classification to determine flight safety
 * and trigger operational responses. Provides weather broadcasts to air traffic controllers and pilots,
 * manages weather transitions, and evaluates flight safety based on current conditions. Supports
 * multi-city operations with independent weather instances per location.
 * 
 * Key Responsibilities:
 * - Maintain current weather state (condition, severity, temperature, wind, visibility)
 * - Classify weather conditions into 5 severity levels (MINIMAL to CRITICAL)
 * - Generate randomized weather changes simulating natural atmospheric transitions
 * - Broadcast weather information to operators and pilots via formatted messages
 * - Evaluate flight safety based on severity thresholds (severity 4+ grounds flights)
 * - Track weather update timestamps for data freshness validation
 * - Provide detailed weather reports with temperature, wind speed, and visibility data
 * 
 * Interactions:
 * - Monitored by CityMapWeatherManager for UI display updates
 * - Consulted by FlightHazardMonitor to determine if flights should be grounded
 * - Broadcasts sent to Radio system for pilot notifications
 * - Weather changes logged to city-specific weather broadcast logs
 * - Used by AirTrafficControllerFrame to display current conditions
 * - Integrated with DayTime for complete environmental simulation
 * 
 * Patterns & Constraints:
 * - Severity-based classification system (1=Clear to 5=Thunderstorm/Blizzard)
 * - Weather types mapped to severity levels via internal HashMap
 * - Random weather transitions simulate realistic atmospheric changes
 * - Immutable weatherID ensures instance tracking across system
 * - Thread-safe if accessed from single city context (not synchronized for multi-thread)
 * - Weather conditions affect flight operations: severity 4+ triggers emergency landings
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

    public void changeWeather(String newWeather) {
        if (!weatherTypes.containsKey(newWeather)) {
            return;
        }
        String oldWeather = this.currentWeather;
        int oldSeverity = this.currentSeverity;
        this.currentWeather = newWeather;
        this.currentSeverity = weatherTypes.get(newWeather);
        this.lastUpdated = System.currentTimeMillis();
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

    public void changeWeatherRandomly() {
        Random random = new Random();
        String[] weatherArray = weatherTypes.keySet().toArray(new String[weatherTypes.size()]);
        String randomWeather = weatherArray[random.nextInt(weatherArray.length)];
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