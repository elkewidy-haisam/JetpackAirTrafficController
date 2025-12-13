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
    // Directory path for session storage in user's home directory under .jetpack/sessions
    // Platform-independent path construction using File.separator for Windows/Unix compatibility
    private static final String SESSIONS_DIR = System.getProperty("user.home") + File.separator + ".jetpack" + File.separator + "sessions";
    
    // Timestamp format for session filenames: YYYYMMDD_HHMMSS (e.g., 20241215_143022)
    // Sortable format ensures chronological file ordering in directory listings
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Constructs a new SessionManager and ensures the sessions directory exists.
     * Automatically creates the directory structure if it doesn't exist,
     * preventing IO errors during session save operations.
     */
    public SessionManager() {
        // Initialize directory structure before any save operations
        ensureSessionsDirectory();
    }

    /**
     * Ensures the sessions directory exists, creating it if necessary.
     * Creates parent directories as needed (.jetpack and .jetpack/sessions).
     * Safe to call multiple times - no-op if directory already exists.
     */
    private void ensureSessionsDirectory() {
        // Construct File object representing the sessions directory
        File dir = new File(SESSIONS_DIR);
        
        // Check if directory exists before attempting creation
        if (!dir.exists()) {
            // Create directory and all necessary parent directories
            // mkdirs() creates intermediate directories if needed
            dir.mkdirs();
        }
    }

    /**
     * Saves the current city state to a persistent session file.
     * Captures complete snapshot including city dimensions, weather, parking, and active flights.
     * Generates timestamped filename for historical tracking and session management.
     * 
     * @param city The City object containing dimension and parking data
     * @param flights List of active JetPackFlight objects to persist
     * @param weather Current Weather state to capture
     * @return Absolute path to saved session file, or null if save failed
     */
    public String saveSession(City city, List<JetPackFlight> flights, Weather weather) {
        // Generate timestamp for unique filename (e.g., "20241215_143022")
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        
        // Create filename: CityName_TIMESTAMP.session (spaces replaced with underscores)
        // Example: "New_York_20241215_143022.session"
        String filename = String.format("%s_%s.session", city.getName().replaceAll(" ", "_"), timestamp);
        
        // Construct full path to session file in sessions directory
        File sessionFile = new File(SESSIONS_DIR, filename);

        // Use try-with-resources to ensure writer is closed even if exception occurs
        try (PrintWriter writer = new PrintWriter(new FileWriter(sessionFile))) {
            // Write session file header with metadata
            writer.println("# JetPack Traffic Control Session");
            writer.println("# Saved: " + LocalDateTime.now());
            writer.println();

            // [CITY] section: capture city dimensions for boundary validation on load
            writer.println("[CITY]");
            writer.println("width=" + city.getWidth());
            writer.println("height=" + city.getHeight());
            writer.println();

            // [WEATHER] section (implied): capture atmospheric conditions affecting flight safety
            writer.println("type=" + weather.getCurrentWeather());
            writer.println("severity=" + weather.getCurrentSeverity());
            writer.println("visibility=1.0");  // Default visibility value
            writer.println();

            // [PARKING] section: capture parking space utilization statistics
            writer.println("[PARKING]");
            List<ParkingSpace> parkingSpaces = city.getParkingSpaces();
            writer.println("total=" + parkingSpaces.size());
            
            // Count occupied spaces for statistics
            int occupied = 0;
            for (ParkingSpace space : parkingSpaces) {
                if (space.isOccupied()) {
                    occupied++;
                }
            }
            
            // Write parking statistics
            writer.println("occupied=" + occupied);
            writer.println("available=" + (parkingSpaces.size() - occupied));
            writer.println();

            // [FLIGHTS] section: capture all active flight positions and states
            writer.println("[FLIGHTS]");
            writer.println("count=" + flights.size());
            
            // Write each flight as: callsign,x,y,status
            for (JetPackFlight flight : flights) {
                writer.println(String.format("flight=%s,%.2f,%.2f,ACTIVE",
                    flight.getJetpack().getCallsign(),  // Aircraft identifier
                    flight.getX(),                      // Current X position
                    flight.getY()));                    // Current Y position
            }

            // Confirm successful save to console
            System.out.println("Session saved: " + sessionFile.getAbsolutePath());
            return sessionFile.getAbsolutePath();

        } catch (IOException e) {
            // Log error and return null to indicate save failure
            System.err.println("Error saving session: " + e.getMessage());
            return null;
        }
    }

    /**
     * Exports radar communication log to a timestamped text file.
     * Preserves chronological record of all radar events including position updates,
     * tracking activities, and collision warnings for audit and analysis.
     * 
     * @param radarLog List of radar log entry strings to export
     * @return Absolute path to exported log file, or null if export failed
     */
    public String exportRadarLog(List<String> radarLog) {
        // Generate timestamp for unique filename
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        
        // Create filename: radar_log_TIMESTAMP.txt
        String filename = String.format("radar_log_%s.txt", timestamp);
        File logFile = new File(SESSIONS_DIR, filename);

        // Use try-with-resources for automatic writer cleanup
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile))) {
            // Write report header with decorative border
            writer.println("=".repeat(80));
            writer.println("JETPACK TRAFFIC CONTROL SYSTEM - RADAR LOG");
            writer.println("Exported: " + LocalDateTime.now());
            writer.println("=".repeat(80));
            writer.println();

            // Write each radar log entry on a separate line
            // Entries should already be formatted with timestamps and event details
            for (String entry : radarLog) {
                writer.println(entry);
            }

            // Confirm successful export to console
            System.out.println("Radar log exported: " + logFile.getAbsolutePath());
            return logFile.getAbsolutePath();

        } catch (IOException e) {
            // Log error and return null to indicate export failure
            System.err.println("Error exporting radar log: " + e.getMessage());
            return null;
        }
    }

    /**
     * Exports accident incidents to a formatted report file.
     * Generates comprehensive accident summary with numbering, details, and statistics
     * for safety review, regulatory compliance, and trend analysis.
     * 
     * @param accidents List of accident description strings to include in report
     * @return Absolute path to exported report file, or null if export failed
     */
    public String exportAccidentReport(List<String> accidents) {
        // Generate timestamp for unique filename
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        
        // Create filename: accident_report_TIMESTAMP.txt
        String filename = String.format("accident_report_%s.txt", timestamp);
        File reportFile = new File(SESSIONS_DIR, filename);

        // Use try-with-resources for automatic writer cleanup
        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
            // Write report header with decorative border
            writer.println("=".repeat(80));
            writer.println("JETPACK TRAFFIC CONTROL SYSTEM - ACCIDENT REPORT");
            writer.println("Generated: " + LocalDateTime.now());
            writer.println("=".repeat(80));
            writer.println();
            
            // Include total accident count for quick assessment
            writer.println("Total Accidents: " + accidents.size());
            writer.println();

            // Write each accident with sequential numbering and separator
            for (int i = 0; i < accidents.size(); i++) {
                // Number accidents starting from 1 for human readability
                writer.println(String.format("Accident #%d:", i + 1));
                
                // Write accident details (should include location, type, severity, timestamp)
                writer.println(accidents.get(i));
                
                // Add separator line between accidents for visual clarity
                writer.println("-".repeat(80));
            }

            // Confirm successful export to console
            System.out.println("Accident report exported: " + reportFile.getAbsolutePath());
            return reportFile.getAbsolutePath();

        } catch (IOException e) {
            // Log error and return null to indicate export failure
            System.err.println("Error exporting accident report: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lists all saved session files in the sessions directory.
     * Filters for files with .session extension to exclude other file types.
     * Returns null if directory doesn't exist or is empty.
     * 
     * @return Array of session File objects, or null if none found
     */
    public File[] listSessions() {
        // Construct directory path
        File dir = new File(SESSIONS_DIR);
        
        // List files matching .session extension using lambda filter
        // Returns null if directory doesn't exist or contains no matching files
        return dir.listFiles((d, name) -> name.endsWith(".session"));
    }

    /**
     * Returns the absolute path to the sessions storage directory.
     * Useful for displaying to users or constructing custom file paths.
     * 
     * @return String path to sessions directory (e.g., "/home/user/.jetpack/sessions")
     */
    public String getSessionsDirectory() {
        // Return the constant directory path
        return SESSIONS_DIR;
    }
}