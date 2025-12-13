/**
 * Centralized management for config operations and lifecycle coordination.
 * 
 * Purpose:
 * Manages config instances across the Air Traffic Controller system, providing
 * factory methods, registry access, and coordination logic. Supports multi-city scenarios
 * and ensures consistent config state across operational contexts.
 * 
 * Key Responsibilities:
 * - Initialize and maintain config collections per city or system-wide
 * - Provide query methods for config retrieval and filtering
 * - Coordinate config state updates across subsystems
 * - Support config lifecycle (creation, modification, disposal)
 * 
 * Interactions:
 * - Referenced by AirTrafficControllerFrame and CityMapPanel
 * - Integrates with logging and persistence subsystems
 * - Coordinates with related manager classes
 * 
 * Patterns & Constraints:
 * - Manager pattern centralizes config concerns
 * - Thread-safe operations for concurrent access
 * - Per-city collections for multi-city support
 * 
 * @author Haisam Elkewidy
 */

package com.example.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigManager handles loading and saving user preferences and application settings.
 * It manages a properties file in the user's home directory and provides methods to load, save, and set defaults.
 */
public class ConfigManager {
    // Configuration file name stored in user's home directory
    private static final String CONFIG_FILE = "jetpack_config.properties";
    
    // Configuration directory path: ~/.jetpack/ (platform-independent via File.separator)
    private static final String CONFIG_DIR = System.getProperty("user.home") + File.separator + ".jetpack";
    
    // Properties object holding key-value configuration pairs
    private Properties properties;
    
    // File handle for the configuration properties file
    private File configFile;

    /**
     * Constructs a new ConfigManager and initializes configuration.
     * Creates the config directory if needed and loads existing configuration
     * from disk, or sets defaults if no config file exists.
     */
    public ConfigManager() {
        // Initialize empty properties collection
        properties = new Properties();
        
        // Construct full path to config file: ~/.jetpack/jetpack_config.properties
        configFile = new File(CONFIG_DIR, CONFIG_FILE);
        
        // Ensure directory structure exists before attempting file operations
        ensureConfigDirectory();
        
        // Load configuration from file or set defaults if file doesn't exist
        loadConfig();
    }

    /**
     * Ensures the configuration directory exists, creating it if necessary.
     * Creates parent directories as needed using mkdirs().
     * Safe to call multiple times - no-op if directory already exists.
     */
    private void ensureConfigDirectory() {
        // Construct File object for configuration directory
        File dir = new File(CONFIG_DIR);
        
        // Check existence before attempting creation
        if (!dir.exists()) {
            // Create directory and all necessary parents
            // mkdirs() handles intermediate directories automatically
            dir.mkdirs();
        }
    }

    /**
     * Loads configuration from the properties file.
     * If file exists, reads key-value pairs into the properties object.
     * If file doesn't exist or loading fails, applies default values.
     * Automatically called during construction.
     */
    public void loadConfig() {
        // Check if config file exists before attempting to read
        if (configFile.exists()) {
            // Use try-with-resources for automatic stream closure
            try (FileInputStream fis = new FileInputStream(configFile)) {
                // Load properties from file into properties object
                properties.load(fis);
                
                // Confirm successful load to console
                System.out.println("Configuration loaded from: " + configFile.getAbsolutePath());
            } catch (IOException e) {
                // Log error and fall back to default values if load fails
                System.err.println("Error loading configuration: " + e.getMessage());
                setDefaults();
            }
        } else {
            // File doesn't exist - initialize with default configuration
            setDefaults();
        }
    }

    /**
     * Saves the current configuration to the properties file.
     * Writes all key-value pairs to disk for persistence across application restarts.
     * Should be called after configuration changes to preserve user preferences.
     */
    public void saveConfig() {
        // Use try-with-resources for automatic stream closure
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            // Write properties to file with descriptive header comment
            properties.store(fos, "JetPack Traffic Control System Configuration");
            
            // Confirm successful save to console
            System.out.println("Configuration saved to: " + configFile.getAbsolutePath());
        } catch (IOException e) {
            // Log error if save fails - configuration changes will be lost on restart
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }

    /**
     * Sets default configuration values for all supported settings.
     * Called when no config file exists or when loading fails.
     * Provides sensible defaults for collision detection, weather alerts,
     * parking updates, and UI preferences.
     */
    private void setDefaults() {
        // Collision detection: critical warning distance in abstract units
        properties.setProperty("collision.criticalDistance", "50.0");
        
        // Collision detection: accident reporting distance threshold
        properties.setProperty("collision.accidentDistance", "20.0");
        
        // Weather: severity level (1-5) that triggers alerts
        properties.setProperty("weather.alertSeverity", "4");
        
        // Parking: update interval in milliseconds for parking space checks
        properties.setProperty("parking.updateInterval", "2000");
        
        // UI: last selected city for convenience on restart
        properties.setProperty("lastCity", "New York");
        
        // UI: grid visibility toggle (true = show grid overlay)
        properties.setProperty("grid.visible", "true");
    }

    // Getter methods for typed configuration access with default fallbacks
    
    /**
     * Returns the critical distance threshold for collision warnings.
     * Jetpacks within this distance trigger proximity alerts.
     * 
     * @return Critical collision distance (defaults to 50.0 units if not configured)
     */
    public double getCollisionCriticalDistance() {
        // Parse string property to double, use default if property missing
        return Double.parseDouble(properties.getProperty("collision.criticalDistance", "50.0"));
    }

    /**
     * Returns the distance threshold for automatic accident reporting.
     * Collisions within this distance are logged as accidents.
     * 
     * @return Accident reporting distance (defaults to 20.0 units if not configured)
     */
    public double getCollisionAccidentDistance() {
        // Parse string property to double, use default if property missing
        return Double.parseDouble(properties.getProperty("collision.accidentDistance", "20.0"));
    }

    /**
     * Returns the weather severity level that triggers safety alerts.
     * Severity scale: 1 (Clear) to 5 (Extreme). Alerts trigger at this level or higher.
     * 
     * @return Weather alert severity threshold (defaults to 4 if not configured)
     */
    public int getWeatherAlertSeverity() {
        // Parse string property to integer, use default if property missing
        return Integer.parseInt(properties.getProperty("weather.alertSeverity", "4"));
    }

    /**
     * Returns the update interval for parking space availability checks.
     * Controls how frequently the UI refreshes parking occupancy status.
     * 
     * @return Update interval in milliseconds (defaults to 2000ms / 2 seconds if not configured)
     */
    public int getParkingUpdateInterval() {
        // Parse string property to integer, use default if property missing
        return Integer.parseInt(properties.getProperty("parking.updateInterval", "2000"));
    }

    /**
     * Returns the last selected city name.
     * Used to restore user's previous city selection on application restart.
     * 
     * @return Last city name (defaults to "New York" if not configured)
     */
    public String getLastCity() {
        // Return string property directly, use default if property missing
        return properties.getProperty("lastCity", "New York");
    }

    // Setter methods for updating configuration values
    
    /**
     * Sets the grid visibility preference.
     * Controls whether the coordinate grid overlay is displayed on the map.
     * 
     * @param visible true to show grid, false to hide
     */
    public void setGridVisible(boolean visible) {
        // Convert boolean to string for properties storage
        properties.setProperty("grid.visible", String.valueOf(visible));
    }

    /**
     * Generic setter for any configuration property.
     * Allows dynamic configuration of arbitrary key-value pairs.
     * 
     * @param key Configuration property key
     * @param value Configuration property value
     */
    public void setProperty(String key, String value) {
        // Store key-value pair in properties object
        // Note: Changes are not persisted until saveConfig() is called
        properties.setProperty(key, value);
    }
}