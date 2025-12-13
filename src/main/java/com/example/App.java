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
        // Prevent GUI startup in test environment
        if (Boolean.getBoolean("test.env")) {
            System.out.println("Test environment detected: Skipping GUI startup.");
            return;
        }
        AppConfig config = parseArguments(args);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Warning: Could not set system look and feel");
        }
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("=".repeat(50));
                System.out.println("🚀 " + APP_NAME + " v" + VERSION);
                System.out.println("=".repeat(50));
                System.out.println("Starting application...");
                if (config.city != null) {
                    System.out.println("Pre-selected city: " + config.city);
                }
                System.out.println("Initializing GUI...");
                AirTrafficControllerFrame frame = new AirTrafficControllerFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setTitle(APP_NAME + " v" + VERSION);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                System.out.println("✅ Application started successfully!");
                System.out.println("=".repeat(50));
            } catch (Exception e) {
                System.err.println("❌ Error starting application:");
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Failed to start application:\n" + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
    
    /**
     * Parse command-line arguments
     */
    private static AppConfig parseArguments(String[] args) {
        AppConfig config = new AppConfig();
        
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            
            if (arg.equals("--help") || arg.equals("-h")) {
                printHelp();
                System.exit(0);
            } else if (arg.equals("--version") || arg.equals("-v")) {
                System.out.println(APP_NAME + " version " + VERSION);
                System.exit(0);
            } else if (arg.startsWith("--city=")) {
                config.city = arg.substring(7);
            }
        }
        
        return config;
    }
    
    /**
     * Print help information
     */
    private static void printHelp() {
        System.out.println(APP_NAME + " v" + VERSION);
        System.out.println("\nUsage: java -cp target/classes com.example.App [OPTIONS]");
        System.out.println("\nOptions:");
        System.out.println("  --help, -h          Show this help message");
        System.out.println("  --version, -v       Show version information");
        System.out.println("  --city=<name>       Pre-select city (New York, Boston, Houston, Dallas)");
        System.out.println("\nExample:");
        System.out.println("  java -cp target/classes com.example.App --city=\"New York\"");
    }
    
    /**
     * Configuration class for application settings
     */
    private static class AppConfig {
        String city = null;
    }
}
