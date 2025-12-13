/**
 * Main entry point for the Jetpack Air Traffic Controller application.
 * 
 * Purpose:
 * Bootstraps the entire application by initializing the Swing-based GUI and processing command-line
 * arguments for customization. Serves as the orchestrator that configures system look-and-feel,
 * launches the main application frame (AirTrafficControllerFrame), and provides CLI support for
 * pre-selecting cities or displaying version information.
 * 
 * Key Responsibilities:
 * - Parse and validate command-line arguments (--help, --version, --city=<name>)
 * - Set system look-and-feel for native UI appearance
 * - Initialize and display the main AirTrafficControllerFrame on the Event Dispatch Thread
 * - Handle graceful startup errors with user-friendly dialogs
 * - Support headless/test mode detection to prevent GUI startup during automated tests
 * 
 * Interactions:
 * - Creates and configures AirTrafficControllerFrame (primary UI component)
 * - Uses SwingUtilities for thread-safe GUI initialization
 * - Integrates with UIManager for cross-platform look-and-feel
 * 
 * Patterns & Constraints:
 * - Follows standard Java application main() entry point pattern
 * - Ensures GUI operations run on EDT via SwingUtilities.invokeLater()
 * - Checks for test environment flag to avoid GUI conflicts during testing
 * - Minimal dependencies; focuses solely on application bootstrap
 * 
 * @author Haisam Elkewidy
 */

package com.example;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.example.ui.frames.AirTrafficControllerFrame;

public class App {
    
    private static final String APP_NAME = "Air Traffic Controller";
    private static final String VERSION = "2.0";
    
    public static void main(String[] args) {
        // Check for test environment flag to prevent GUI conflicts during automated tests
        if (Boolean.getBoolean("test.env")) {  // System property "test.env" set to true
            System.out.println("Test environment detected: Skipping GUI startup.");  // Log test mode detection
            return;  // Exit early without launching GUI
        }
        AppConfig config = parseArguments(args);  // Parse command-line arguments for customization
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  // Apply native OS look-and-feel for better UX
        } catch (Exception e) {
            System.err.println("Warning: Could not set system look and feel");  // Non-fatal: continue with default look
        }
        SwingUtilities.invokeLater(() -> {  // Ensure GUI initialization happens on Event Dispatch Thread
            try {
                System.out.println("=".repeat(50));  // Print header separator (50 equals signs)
                System.out.println("🚀 " + APP_NAME + " v" + VERSION);  // Display application name and version with emoji
                System.out.println("=".repeat(50));  // Print footer separator
                System.out.println("Starting application...");  // Log startup initiation
                if (config.city != null) {  // Check if user pre-selected a city via CLI
                    System.out.println("Pre-selected city: " + config.city);  // Log the pre-selected city name
                }
                System.out.println("Initializing GUI...");  // Log GUI initialization start
                AirTrafficControllerFrame frame = new AirTrafficControllerFrame();  // Create main application window
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit application when window is closed
                frame.setSize(800, 600);  // Set initial window dimensions (width x height)
                frame.setTitle(APP_NAME + " v" + VERSION);  // Set window title bar text
                frame.setLocationRelativeTo(null);  // Center window on screen
                frame.setVisible(true);  // Make window visible to user
                System.out.println("✅ Application started successfully!");  // Log successful startup with checkmark
                System.out.println("=".repeat(50));  // Print final separator
            } catch (Exception e) {
                System.err.println("❌ Error starting application:");  // Log startup failure with X emoji
                e.printStackTrace();  // Print full stack trace to stderr for debugging
                JOptionPane.showMessageDialog(null,  // Show error dialog to user (no parent window)
                    "Failed to start application:\n" + e.getMessage(),  // Error message text
                    "Startup Error",  // Dialog title
                    JOptionPane.ERROR_MESSAGE);  // Error icon type
                System.exit(1);  // Exit with error code 1 to indicate failure
            }
        });
    }
    
    /**
     * Parse command-line arguments
     */
    private static AppConfig parseArguments(String[] args) {
        AppConfig config = new AppConfig();  // Create new configuration object with defaults
        
        for (int i = 0; i < args.length; i++) {  // Iterate through all command-line arguments
            String arg = args[i];  // Get current argument string
            
            if (arg.equals("--help") || arg.equals("-h")) {  // Check for help flag (long or short form)
                printHelp();  // Display usage information to console
                System.exit(0);  // Exit gracefully after showing help
            } else if (arg.equals("--version") || arg.equals("-v")) {  // Check for version flag
                System.out.println(APP_NAME + " version " + VERSION);  // Print version info
                System.exit(0);  // Exit after displaying version
            } else if (arg.startsWith("--city=")) {  // Check for city pre-selection argument
                config.city = arg.substring(7);  // Extract city name (skip "--city=" prefix of length 7)
            }
        }
        
        return config;  // Return parsed configuration object
    }
    
    /**
     * Print help information
     */
    private static void printHelp() {
        System.out.println(APP_NAME + " v" + VERSION);  // Print application name and version header
        System.out.println("\nUsage: java -cp target/classes com.example.App [OPTIONS]");  // Show basic command syntax
        System.out.println("\nOptions:");  // Section header for available options
        System.out.println("  --help, -h          Show this help message");  // Help flag description
        System.out.println("  --version, -v       Show version information");  // Version flag description
        System.out.println("  --city=<name>       Pre-select city (New York, Boston, Houston, Dallas)");  // City pre-selection syntax
        System.out.println("\nExample:");  // Section header for usage example
        System.out.println("  java -cp target/classes com.example.App --city=\"New York\"");  // Show concrete usage example
    }
    
    /**
     * Configuration class for application settings
     */
    private static class AppConfig {
        String city = null;
    }
}
