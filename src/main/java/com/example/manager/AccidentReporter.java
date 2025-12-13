/**
 * AccidentReporter component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides accidentreporter functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core accidentreporter operations
 * - Maintain necessary state for accidentreporter functionality
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

package com.example.manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AccidentReporter {
    // Directory path for accident reports: ~/.jetpack/accidents/
    private static final String ACCIDENTS_DIR = System.getProperty("user.home") + File.separator + ".jetpack" + File.separator + "accidents";
    
    // Timestamp format for report filenames: YYYYMMDD_HHMMSS
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Constructs a new AccidentReporter and ensures directory structure exists.
     * Creates accidents directory if needed to prevent IO errors during report generation.
     */
    public AccidentReporter() {
        // Initialize directory structure before any file operations
        ensureAccidentsDirectory();
    }

    /**
     * Ensures the accidents directory exists, creating it if necessary.
     * Creates parent directories as needed (.jetpack and .jetpack/accidents).
     */
    private void ensureAccidentsDirectory() {
        // Construct File object for accidents directory
        File dir = new File(ACCIDENTS_DIR);
        
        // Check existence before attempting creation
        if (!dir.exists()) {
            // Create directory and all necessary parents
            dir.mkdirs();
        }
    }

    /**
     * Saves accident report to a timestamped file in accidents directory.
     * Generates formatted report with accident count, details, and separators.
     * Returns file path on success or null on IO error.
     * 
     * @param accidents List of accident description strings to include in report
     * @return Absolute path to saved report file, or null if save failed
     */
    public String saveAccidentReport(List<String> accidents) {
        // Generate timestamp for unique filename
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        
        // Create filename: accident_report_TIMESTAMP.txt
        String filename = String.format("accident_report_%s.txt", timestamp);
        File reportFile = new File(ACCIDENTS_DIR, filename);

        // Use try-with-resources for automatic writer cleanup
        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
            // Write report header with decorative border
            writer.println("=".repeat(80));
            writer.println("JETPACK TRAFFIC CONTROL SYSTEM - ACCIDENT REPORT");
            writer.println("Generated: " + LocalDateTime.now());
            writer.println("=".repeat(80));
            writer.println();
            
            // Include total count for quick assessment
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

            // Confirm successful save to console
            System.out.println("Accident report saved: " + reportFile.getAbsolutePath());
            return reportFile.getAbsolutePath();

        } catch (IOException e) {
            // Log error and return null to indicate save failure
            System.err.println("Error saving accident report: " + e.getMessage());
            return null;
        }
    }

    /**
     * List all saved accident reports
     */
    public File[] listAccidentReports() {
        File dir = new File(ACCIDENTS_DIR);
        return dir.listFiles((d, name) -> name.startsWith("accident_report_") && name.endsWith(".txt"));
    }

    /**
     * Get accidents directory path
     */
    public String getAccidentsDirectory() {
        return ACCIDENTS_DIR;
    }
}
