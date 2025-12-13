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
    // Directory path for storing accident reports (in user home directory under .jetpack/accidents)
    private static final String ACCIDENTS_DIR = System.getProperty("user.home") + File.separator + ".jetpack" + File.separator + "accidents";
    // Date/time format pattern for timestamping filenames (yyyyMMdd_HHmmss)
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Constructs a new AccidentReporter and ensures accidents directory exists.
     */
    public AccidentReporter() {
        // Create accidents directory if it doesn't exist
        ensureAccidentsDirectory();
    }

    /**
     * Ensures the accidents directory exists, creating it if necessary.
     */
    private void ensureAccidentsDirectory() {
        // Create File object representing accidents directory
        File dir = new File(ACCIDENTS_DIR);
        // Check if directory doesn't exist yet
        if (!dir.exists()) {
            // Create directory and any necessary parent directories
            dir.mkdirs();
        }
    }

    /**
     * Saves accident report to a timestamped file.
     * 
     * @param accidents List of accident descriptions to save
     * @return Absolute path to saved report file, or null on error
     */
    public String saveAccidentReport(List<String> accidents) {
        // Format current timestamp for unique filename
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        // Create filename with timestamp
        String filename = String.format("accident_report_%s.txt", timestamp);
        // Create File object for report file in accidents directory
        File reportFile = new File(ACCIDENTS_DIR, filename);

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

            // Log successful save to console
            System.out.println("Accident report saved: " + reportFile.getAbsolutePath());
            // Return absolute path of saved file
            return reportFile.getAbsolutePath();

        } catch (IOException e) {
            // Log error message to console
            System.err.println("Error saving accident report: " + e.getMessage());
            // Return null to indicate failure
            return null;
        }
    }

    /**
     * Lists all saved accident report files in the accidents directory.
     * 
     * @return Array of File objects for accident reports, or null if directory doesn't exist
     */
    public File[] listAccidentReports() {
        // Create File object representing accidents directory
        File dir = new File(ACCIDENTS_DIR);
        // Return filtered list of files starting with "accident_report_" and ending with ".txt"
        return dir.listFiles((d, name) -> name.startsWith("accident_report_") && name.endsWith(".txt"));
    }

    /**
     * Gets the absolute path of the accidents directory.
     * 
     * @return Accidents directory path string
     */
    public String getAccidentsDirectory() {
        // Return the accidents directory path constant
        return ACCIDENTS_DIR;
    }
}
