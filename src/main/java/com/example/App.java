package com.example;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.example.ui.frames.AirTrafficControllerFrame;

/**
 * App.java
 * Main entry point for Air Traffic Controller application
 * Supports command-line arguments for customization
 */
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
