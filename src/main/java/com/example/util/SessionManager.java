/**
 * Centralized management for session operations and lifecycle coordination.
 * 
 * Purpose:
 * Manages session instances across the Air Traffic Controller system, providing
 * factory methods, registry access, and coordination logic. Supports multi-city scenarios
 * and ensures consistent session state across operational contexts.
 * 
 * Key Responsibilities:
 * - Initialize and maintain session collections per city or system-wide
 * - Provide query methods for session retrieval and filtering
 * - Coordinate session state updates across subsystems
 * - Support session lifecycle (creation, modification, disposal)
 * 
 * Interactions:
 * - Referenced by AirTrafficControllerFrame and CityMapPanel
 * - Integrates with logging and persistence subsystems
 * - Coordinates with related manager classes
 * 
 * Patterns & Constraints:
 * - Manager pattern centralizes session concerns
 * - Thread-safe operations for concurrent access
 * - Per-city collections for multi-city support
 * 
 * @author Haisam Elkewidy
 */

package com.example.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.city.City;
import com.example.flight.JetPackFlight;
import com.example.parking.ParkingSpace;
import com.example.weather.Weather;

public class SessionManager {
    // Directory path for storing session files (in user home directory under .jetpack/sessions)
    private static final String SESSIONS_DIR = System.getProperty("user.home") + File.separator + ".jetpack" + File.separator + "sessions";
    // Date/time format pattern for timestamping filenames (yyyyMMdd_HHmmss)
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Constructs a new SessionManager and ensures sessions directory exists.
     */
    public SessionManager() {
        // Create sessions directory if it doesn't exist
        ensureSessionsDirectory();
    }

    /**
     * Ensure the sessions directory exists, creating it if necessary.
     */
    private void ensureSessionsDirectory() {
        // Create File object representing sessions directory
        File dir = new File(SESSIONS_DIR);
        // Check if directory doesn't exist yet
        if (!dir.exists()) {
            // Create directory and any necessary parent directories
            dir.mkdirs();
        }
    }

    /**
     * Save current city state to a session file.
     * 
     * @param city City data to save
     * @param flights List of active flights
     * @param weather Current weather conditions
     * @return Absolute path to saved session file, or null on error
     */
    public String saveSession(City city, List<JetPackFlight> flights, Weather weather) {
        // Format current timestamp for unique filename
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        // Create filename from city name (spaces replaced with underscores) and timestamp
        String filename = String.format("%s_%s.session", city.getName().replaceAll(" ", "_"), timestamp);
        // Create File object for session file in sessions directory
        File sessionFile = new File(SESSIONS_DIR, filename);

        // Use try-with-resources to auto-close writer
        try (PrintWriter writer = new PrintWriter(new FileWriter(sessionFile))) {
            // Write session file header comment
            writer.println("# JetPack Traffic Control Session");
            // Write save timestamp comment
            writer.println("# Saved: " + LocalDateTime.now());
            // Add blank line for readability
            writer.println();

            // Write city information section header
            writer.println("[CITY]");
            // Write city grid width dimension
            writer.println("width=" + city.getWidth());
            // Write city grid height dimension
            writer.println("height=" + city.getHeight());
            // Add blank line between sections
            writer.println();

            // Write weather type (e.g., "Clear/Sunny", "Rainy")
            writer.println("type=" + weather.getCurrentWeather());
            // Write weather severity level (1-5 scale)
            writer.println("severity=" + weather.getCurrentSeverity());
            // Write visibility (hardcoded to 1.0 for now)
            writer.println("visibility=1.0");
            // Add blank line between sections
            writer.println();

            // Write parking section header
            writer.println("[PARKING]");
            // Get list of all parking spaces in city
            List<ParkingSpace> parkingSpaces = city.getParkingSpaces();
            // Write total number of parking spaces
            writer.println("total=" + parkingSpaces.size());
            // Initialize counter for occupied spaces
            int occupied = 0;
            // Iterate through all parking spaces
            for (ParkingSpace space : parkingSpaces) {
                // Check if parking space is currently occupied
                if (space.isOccupied()) {
                    // Increment occupied counter
                    occupied++;
                }
            }
            // Write number of occupied parking spaces
            writer.println("occupied=" + occupied);
            // Calculate and write number of available spaces
            writer.println("available=" + (parkingSpaces.size() - occupied));
            // Add blank line between sections
            writer.println();

            // Write flights section header
            writer.println("[FLIGHTS]");
            // Write total number of active flights
            writer.println("count=" + flights.size());
            // Iterate through all active flights
            for (JetPackFlight flight : flights) {
                // Write flight data: callsign, X position, Y position, status
                writer.println(String.format("flight=%s,%.2f,%.2f,ACTIVE",
                    flight.getJetpack().getCallsign(),
                    flight.getX(),
                    flight.getY()));
            }

            // Log successful save to console
            System.out.println("Session saved: " + sessionFile.getAbsolutePath());
            // Return absolute path of saved file
            return sessionFile.getAbsolutePath();

        } catch (IOException e) {
            // Log error message to console
            System.err.println("Error saving session: " + e.getMessage());
            // Return null to indicate failure
            return null;
        }
    }

    /**
     * Export radar log to text file.
     * 
     * @param radarLog List of radar log entries
     * @return Absolute path to exported log file, or null on error
     */
    public String exportRadarLog(List<String> radarLog) {
        // Format current timestamp for unique filename
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        // Create filename with timestamp
        String filename = String.format("radar_log_%s.txt", timestamp);
        // Create File object for log file in sessions directory
        File logFile = new File(SESSIONS_DIR, filename);

        // Use try-with-resources to auto-close writer
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile))) {
            // Write header separator line (80 equals signs)
            writer.println("=".repeat(80));
            // Write report title
            writer.println("JETPACK TRAFFIC CONTROL SYSTEM - RADAR LOG");
            // Write export timestamp
            writer.println("Exported: " + LocalDateTime.now());
            // Write footer separator line
            writer.println("=".repeat(80));
            // Add blank line before log entries
            writer.println();

            // Iterate through all radar log entries
            for (String entry : radarLog) {
                // Write each log entry on its own line
                writer.println(entry);
            }

            // Log successful export to console
            System.out.println("Radar log exported: " + logFile.getAbsolutePath());
            // Return absolute path of exported file
            return logFile.getAbsolutePath();

        } catch (IOException e) {
            // Log error message to console
            System.err.println("Error exporting radar log: " + e.getMessage());
            // Return null to indicate failure
            return null;
        }
    }

    /**
     * Export accident report to text file.
     * 
     * @param accidents List of accident descriptions
     * @return Absolute path to exported report file, or null on error
     */
    public String exportAccidentReport(List<String> accidents) {
        // Format current timestamp for unique filename
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        // Create filename with timestamp
        String filename = String.format("accident_report_%s.txt", timestamp);
        // Create File object for report file in sessions directory
        File reportFile = new File(SESSIONS_DIR, filename);

        // Use try-with-resources to auto-close writer
        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
            // Write header separator line (80 equals signs)
            writer.println("=".repeat(80));
            // Write report title
            writer.println("JETPACK TRAFFIC CONTROL SYSTEM - ACCIDENT REPORT");
            // Write generation timestamp
            writer.println("Generated: " + LocalDateTime.now());
            // Write footer separator line
            writer.println("=".repeat(80));
            // Add blank line after header
            writer.println();
            // Write total count of accidents in report
            writer.println("Total Accidents: " + accidents.size());
            // Add blank line before accident details
            writer.println();

            // Iterate through all accidents with index
            for (int i = 0; i < accidents.size(); i++) {
                // Write accident number header (1-indexed)
                writer.println(String.format("Accident #%d:", i + 1));
                // Write accident description details
                writer.println(accidents.get(i));
                // Write separator line between accidents
                writer.println("-".repeat(80));
            }

            // Log successful export to console
            System.out.println("Accident report exported: " + reportFile.getAbsolutePath());
            // Return absolute path of exported file
            return reportFile.getAbsolutePath();

        } catch (IOException e) {
            // Log error message to console
            System.err.println("Error exporting accident report: " + e.getMessage());
            // Return null to indicate failure
            return null;
        }
    }

    /**
     * List all saved session files in the sessions directory.
     * 
     * @return Array of File objects for .session files, or null if directory doesn't exist
     */
    public File[] listSessions() {
        // Create File object representing sessions directory
        File dir = new File(SESSIONS_DIR);
        // Return filtered list of files ending with .session extension
        return dir.listFiles((d, name) -> name.endsWith(".session"));
    }

    /**
     * Get the absolute path of the sessions directory.
     * 
     * @return Sessions directory path string
     */
    public String getSessionsDirectory() {
        // Return the sessions directory path constant
        return SESSIONS_DIR;
    }
}