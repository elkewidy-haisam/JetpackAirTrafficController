package com.example.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigManager handles loading and saving user preferences and application settings
 */
public class ConfigManager {
    private static final String CONFIG_FILE = "jetpack_config.properties";
    private static final String CONFIG_DIR = System.getProperty("user.home") + File.separator + ".jetpack";
    private Properties properties;
    private File configFile;
    
    public ConfigManager() {
        properties = new Properties();
        configFile = new File(CONFIG_DIR, CONFIG_FILE);
        ensureConfigDirectory();
        loadConfig();
    }
    
    /**
     * Ensure the configuration directory exists
     */
    private void ensureConfigDirectory() {
        File dir = new File(CONFIG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    /**
     * Load configuration from file
     */
    public void loadConfig() {
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
                System.out.println("Configuration loaded from: " + configFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error loading configuration: " + e.getMessage());
                setDefaults();
            }
        } else {
            setDefaults();
        }
    }
    
    /**
     * Save configuration to file
     */
    public void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            properties.store(fos, "JetPack Traffic Control System Configuration");
            System.out.println("Configuration saved to: " + configFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }
    
    /**
     * Set default configuration values
     */
    private void setDefaults() {
        properties.setProperty("grid.visible", "false");
        properties.setProperty("performance.visible", "false");
        properties.setProperty("radar.updateInterval", "50");
        properties.setProperty("collision.warningDistance", "100.0");
        properties.setProperty("collision.criticalDistance", "50.0");
        properties.setProperty("collision.accidentDistance", "20.0");
        properties.setProperty("weather.alertSeverity", "4");
        properties.setProperty("parking.updateInterval", "2000");
        properties.setProperty("lastCity", "New York");
    }
    
    // Getters
    public boolean isGridVisible() {
        return Boolean.parseBoolean(properties.getProperty("grid.visible", "false"));
    }
    
    public boolean isPerformanceVisible() {
        return Boolean.parseBoolean(properties.getProperty("performance.visible", "false"));
    }
    
    public int getRadarUpdateInterval() {
        return Integer.parseInt(properties.getProperty("radar.updateInterval", "50"));
    }
    
    public double getCollisionWarningDistance() {
        return Double.parseDouble(properties.getProperty("collision.warningDistance", "100.0"));
    }
    
    public double getCollisionCriticalDistance() {
        return Double.parseDouble(properties.getProperty("collision.criticalDistance", "50.0"));
    }
    
    public double getCollisionAccidentDistance() {
        return Double.parseDouble(properties.getProperty("collision.accidentDistance", "20.0"));
    }
    
    public int getWeatherAlertSeverity() {
        return Integer.parseInt(properties.getProperty("weather.alertSeverity", "4"));
    }
    
    public int getParkingUpdateInterval() {
        return Integer.parseInt(properties.getProperty("parking.updateInterval", "2000"));
    }
    
    public String getLastCity() {
        return properties.getProperty("lastCity", "New York");
    }
    
    // Setters
    public void setGridVisible(boolean visible) {
        properties.setProperty("grid.visible", String.valueOf(visible));
    }
    
    public void setPerformanceVisible(boolean visible) {
        properties.setProperty("performance.visible", String.valueOf(visible));
    }
    
    public void setRadarUpdateInterval(int interval) {
        properties.setProperty("radar.updateInterval", String.valueOf(interval));
    }
    
    public void setCollisionWarningDistance(double distance) {
        properties.setProperty("collision.warningDistance", String.valueOf(distance));
    }
    
    public void setCollisionCriticalDistance(double distance) {
        properties.setProperty("collision.criticalDistance", String.valueOf(distance));
    }
    
    public void setCollisionAccidentDistance(double distance) {
        properties.setProperty("collision.accidentDistance", String.valueOf(distance));
    }
    
    public void setWeatherAlertSeverity(int severity) {
        properties.setProperty("weather.alertSeverity", String.valueOf(severity));
    }
    
    public void setParkingUpdateInterval(int interval) {
        properties.setProperty("parking.updateInterval", String.valueOf(interval));
    }
    
    public void setLastCity(String city) {
        properties.setProperty("lastCity", city);
    }
    
    /**
     * Get a custom property value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Set a custom property value
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}
