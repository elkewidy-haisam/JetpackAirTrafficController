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
    private static final String ACCIDENTS_DIR = System.getProperty("user.home") + File.separator + ".jetpack" + File.separator + "accidents";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public AccidentReporter() {
        ensureAccidentsDirectory();
    }

    private void ensureAccidentsDirectory() {
        File dir = new File(ACCIDENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Save accident report to a file
     */
    public String saveAccidentReport(List<String> accidents) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String filename = String.format("accident_report_%s.txt", timestamp);
        File reportFile = new File(ACCIDENTS_DIR, filename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
            writer.println("=".repeat(80));
            writer.println("JETPACK TRAFFIC CONTROL SYSTEM - ACCIDENT REPORT");
            writer.println("Generated: " + LocalDateTime.now());
            writer.println("=".repeat(80));
            writer.println();
            writer.println("Total Accidents: " + accidents.size());
            writer.println();

            for (int i = 0; i < accidents.size(); i++) {
                writer.println(String.format("Accident #%d:", i + 1));
                writer.println(accidents.get(i));
                writer.println("-".repeat(80));
            }

            System.out.println("Accident report saved: " + reportFile.getAbsolutePath());
            return reportFile.getAbsolutePath();

        } catch (IOException e) {
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
