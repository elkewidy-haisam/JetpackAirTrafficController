/**
 * Duplicate/alias of com.example.accident.AccidentReporter for organizational purposes.
 * 
 * Purpose:
 * Manager package version providing identical accident reporting functionality. May represent a different
 * organizational layer or be consolidated with accident package version in future refactoring.
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
    /** Directory path for storing accident report files in user's home directory */
    private static final String ACCIDENTS_DIR = System.getProperty("user.home") + File.separator + ".jetpack" + File.separator + "accidents";
    /** Date/time format for report filenames (yyyyMMdd_HHmmss) */
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Constructs a new AccidentReporter.
     * Ensures the accidents directory exists on initialization.
     */
    public AccidentReporter() {
        ensureAccidentsDirectory();  // Create accidents directory if it doesn't exist
    }

    /**
     * Ensures the accidents directory exists, creating it if necessary.
     * Creates full directory path including parent directories.
     */
    private void ensureAccidentsDirectory() {
        File dir = new File(ACCIDENTS_DIR);  // Create File object for directory
        if (!dir.exists()) {  // Check if directory doesn't exist
            dir.mkdirs();  // Create directory and any necessary parent directories
        }
    }

    /**
     * Saves accident report to a timestamped file.
     * Creates formatted report with header, accident count, and details.
     * 
     * @param accidents List of accident description strings to include in report
     * @return absolute path to saved report file, or null if save failed
     */
    public String saveAccidentReport(List<String> accidents) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);  // Format current time for filename
        String filename = String.format("accident_report_%s.txt", timestamp);  // Create timestamped filename
        File reportFile = new File(ACCIDENTS_DIR, filename);  // Create File object for report

        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {  // Open file writer with auto-close
            writer.println("=".repeat(80));  // Print header separator (80 equals signs)
            writer.println("JETPACK TRAFFIC CONTROL SYSTEM - ACCIDENT REPORT");  // Print report title
            writer.println("Generated: " + LocalDateTime.now());  // Print generation timestamp
            writer.println("=".repeat(80));  // Print header separator
            writer.println();  // Blank line
            writer.println("Total Accidents: " + accidents.size());  // Print accident count
            writer.println();  // Blank line

            // Iterate through all accidents and write numbered entries
            for (int i = 0; i < accidents.size(); i++) {
                writer.println(String.format("Accident #%d:", i + 1));  // Print accident number (1-indexed)
                writer.println(accidents.get(i));  // Print accident details
                writer.println("-".repeat(80));  // Print separator line (80 dashes)
            }

            System.out.println("Accident report saved: " + reportFile.getAbsolutePath());  // Log success message
            return reportFile.getAbsolutePath();  // Return saved file path

        } catch (IOException e) {  // Catch file I/O errors
            System.err.println("Error saving accident report: " + e.getMessage());  // Log error message
            return null;  // Return null to indicate failure
        }
    }

    /**
     * Lists all saved accident report files.
     * Filters for files matching accident report naming pattern.
     * 
     * @return array of File objects for accident reports, or empty array if none found
     */
    public File[] listAccidentReports() {
        File dir = new File(ACCIDENTS_DIR);  // Create File object for accidents directory
        // List files matching accident report pattern (starts with "accident_report_" and ends with ".txt")
        return dir.listFiles((d, name) -> name.startsWith("accident_report_") && name.endsWith(".txt"));
    }

    /**
     * Gets the accidents directory path.
     * Returns the full path where accident reports are stored.
     * 
     * @return absolute path to accidents directory
     */
    public String getAccidentsDirectory() {
        return ACCIDENTS_DIR;  // Return directory path constant
    }
}
