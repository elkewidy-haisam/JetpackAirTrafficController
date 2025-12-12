package com.example.logging;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CityLogManager.java
 * by Haisam Elkewidy
 * 
 * Manages log files for each city's air traffic control system.
 * Handles jetpack movement logs, radar communications, weather broadcasts, and accident reports.
 */
public class CityLogManager {
    
    private Map<String, String> cityJetpackLogFiles;
    private Map<String, String> cityRadarLogFiles;
    private Map<String, String> cityWeatherLogFiles;
    private Map<String, String> cityAccidentLogFiles;
    
    /**
     * Constructor - initializes log file mappings for all cities
     */
    public CityLogManager() {
        initializeLogFileMaps();
        clearLogFiles();
    }
    
    /**
     * Initializes log file maps for each city
     */
    private void initializeLogFileMaps() {
        cityJetpackLogFiles = new HashMap<>();
        cityRadarLogFiles = new HashMap<>();
        cityWeatherLogFiles = new HashMap<>();
        cityAccidentLogFiles = new HashMap<>();
        
        String[] cities = {"New York", "Boston", "Houston", "Dallas"};
        for (String city : cities) {
            String cityCode = city.replace(" ", "").toLowerCase();
            cityJetpackLogFiles.put(city, cityCode + "_jetpack_movement_log.txt");
            cityRadarLogFiles.put(city, cityCode + "_radar_communications_log.txt");
            cityWeatherLogFiles.put(city, cityCode + "_weather_broadcast_log.txt");
            cityAccidentLogFiles.put(city, cityCode + "_accident_reports_log.txt");
        }
    }
    
    /**
     * Clears all log files when the application starts
     */
    private void clearLogFiles() {
        for (String city : cityJetpackLogFiles.keySet()) {
            try {
                // Clear jetpack movement log
                String jetpackFile = cityJetpackLogFiles.get(city);
                PrintWriter jetpackWriter = new PrintWriter(new FileWriter(jetpackFile, false));
                jetpackWriter.println("=== JETPACK MOVEMENT LOG ===");
                jetpackWriter.println("City: " + city);
                jetpackWriter.println("Application started: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                jetpackWriter.println("=".repeat(50));
                jetpackWriter.close();
                
                // Clear radar communications log
                String radarFile = cityRadarLogFiles.get(city);
                PrintWriter radarWriter = new PrintWriter(new FileWriter(radarFile, false));
                radarWriter.println("=== RADAR COMMUNICATIONS LOG ===");
                radarWriter.println("City: " + city);
                radarWriter.println("Application started: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                radarWriter.println("=".repeat(50));
                radarWriter.close();
                
                // Clear weather broadcast log
                String weatherFile = cityWeatherLogFiles.get(city);
                PrintWriter weatherWriter = new PrintWriter(new FileWriter(weatherFile, false));
                weatherWriter.println("=== WEATHER BROADCAST LOG ===");
                weatherWriter.println("City: " + city);
                weatherWriter.println("Application started: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                weatherWriter.println("=".repeat(50));
                weatherWriter.close();
                
                // Clear accident reports log
                String accidentFile = cityAccidentLogFiles.get(city);
                PrintWriter accidentWriter = new PrintWriter(new FileWriter(accidentFile, false));
                accidentWriter.println("=== ACCIDENT REPORTS LOG ===");
                accidentWriter.println("City: " + city);
                accidentWriter.println("Application started: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                accidentWriter.println("=".repeat(50));
                accidentWriter.close();
                
            } catch (IOException e) {
                System.err.println("Error clearing log files for " + city + ": " + e.getMessage());
            }
        }
        System.out.println("Log files cleared and initialized for all cities");
    }
    
    /**
     * Writes a message to the jetpack movement log file for a specific city
     */
    public void writeToJetpackLog(String city, String message) {
        String logFile = cityJetpackLogFiles.get(city);
        if (logFile == null) return;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
            writer.println(message);
        } catch (IOException e) {
            System.err.println("Error writing to jetpack log for " + city + ": " + e.getMessage());
        }
    }
    
    /**
     * Writes a message to the radar communications log file for a specific city
     */
    public void writeToRadarLog(String city, String message) {
        String logFile = cityRadarLogFiles.get(city);
        if (logFile == null) return;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
            writer.println(message);
        } catch (IOException e) {
            System.err.println("Error writing to radar log for " + city + ": " + e.getMessage());
        }
    }
    
    /**
     * Writes a message to the weather broadcast log file for a specific city
     */
    public void writeToWeatherLog(String city, String message) {
        String logFile = cityWeatherLogFiles.get(city);
        if (logFile == null) return;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
            writer.println(message);
        } catch (IOException e) {
            System.err.println("Error writing to weather log for " + city + ": " + e.getMessage());
        }
    }
    
    /**
     * Writes a message to the accident reports log file for a specific city
     */
    public void writeToAccidentLog(String city, String message) {
        String logFile = cityAccidentLogFiles.get(city);
        if (logFile == null) return;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
            writer.println(message);
        } catch (IOException e) {
            System.err.println("Error writing to accident log for " + city + ": " + e.getMessage());
        }
    }
    
    /**
     * Get radar log contents as a list of strings
     */
    public List<String> getRadarLog() {
        List<String> logEntries = new ArrayList<>();
        
        for (String city : cityRadarLogFiles.keySet()) {
            String logFile = cityRadarLogFiles.get(city);
            try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logEntries.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading radar log for " + city + ": " + e.getMessage());
            }
        }
        
        return logEntries;
    }
}

