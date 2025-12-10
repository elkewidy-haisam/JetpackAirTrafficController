/*
 * ConfigManager.java
 * Part of Jetpack Air Traffic Controller
 *
 * Handles loading and saving user preferences and application settings.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
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

    /** Ensure the configuration directory exists */
    private void ensureConfigDirectory() {
        File dir = new File(CONFIG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /** Load configuration from file */
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

    /** Save configuration to file */
    public void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            properties.store(fos, "JetPack Traffic Control System Configuration");
            System.out.println("Configuration saved to: " + configFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }

    /** Set default configuration values */
    private void setDefaults() {
        properties.setProperty("collision.criticalDistance", "50.0");
        properties.setProperty("collision.accidentDistance", "20.0");
        properties.setProperty("weather.alertSeverity", "4");
        properties.setProperty("parking.updateInterval", "2000");
        properties.setProperty("lastCity", "New York");
        properties.setProperty("grid.visible", "true");
    }

    // Getters
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

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}