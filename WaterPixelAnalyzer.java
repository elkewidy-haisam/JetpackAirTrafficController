/**
 * Diagnostic utility for analyzing water pixel distribution in city map images.
 * 
 * Purpose:
 * Standalone command-line tool that processes city map PNG files to identify and quantify water
 * pixels based on color analysis. Helps validate map assets, tune water detection algorithms, and
 * ensure accurate water/land boundary representation for flight path planning and collision avoidance.
 * 
 * Key Responsibilities:
 * - Load city map images from resources (boston, newyorkcity, dallas, houston)
 * - Sample pixel colors at regular intervals for performance efficiency
 * - Classify pixels as water or land based on RGB thresholds
 * - Generate statistical reports (pixel counts, percentages, color distributions)
 * - Identify potential water detection edge cases for algorithm refinement
 * 
 * Interactions:
 * - Reads PNG images via ImageIO from src/main/java/maps/
 * - Produces console output with analysis results
 * - Informs WaterDetector utility configuration
 * - Supports map asset validation during development
 * 
 * Patterns & Constraints:
 * - Standalone main() method; no integration with runtime application
 * - Sampling strategy (every 10th pixel) balances accuracy and performance
 * - RGB color-based heuristics; no advanced computer vision techniques
 * - No dependencies on AWT/Swing beyond BufferedImage
 * - Intended for development/testing; not deployed in production builds
 * 
 * @author Haisam Elkewidy
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WaterPixelAnalyzer {
    /**
     * Main entry point for the water pixel analyzer diagnostic tool.
     * Iterates through predefined city map images and analyzes water distribution.
     *
     * @param args Command-line arguments (unused; cities are hardcoded)
     */
    public static void main(String[] args) {
        // Array of city names matching PNG filenames in maps directory
        String[] cities = {"boston", "newyorkcity", "dallas", "houston"};
        
        // Process each city map sequentially
        for (String city : cities) {
            try {
                // Construct relative path to city map PNG file
                String imagePath = "src/main/java/maps/" + city + ".png";
                File imageFile = new File(imagePath);
                
                // Check if the map file exists before attempting to read
                if (!imageFile.exists()) {
                    System.out.println("Image not found: " + imagePath);
                    continue; // Skip to next city if file missing
                }
                
                // Load PNG image into memory as BufferedImage
                BufferedImage img = ImageIO.read(imageFile);
                // Perform water pixel analysis on the loaded image
                analyzeWater(img, city);
                
            } catch (IOException e) {
                // Handle file read errors gracefully without crashing
                System.out.println("Error reading " + city + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Analyzes water pixel distribution in a city map image.
     * Uses color-based heuristics to classify pixels as water or land.
     *
     * @param img BufferedImage containing the city map
     * @param city Name of the city for report labeling
     */
    private static void analyzeWater(BufferedImage img, String city) {
        // Extract image dimensions for iteration bounds
        int width = img.getWidth();
        int height = img.getHeight();
        
        // Print analysis header with city name
        System.out.println("\n=== " + city.toUpperCase() + " ===");
        System.out.println("Image size: " + width + "x" + height);
        
        // Initialize counters for water and land pixel classification
        int waterPixels = 0;  // Count of pixels classified as water
        int landPixels = 0;   // Count of pixels classified as land
        
        // Iterate through image in 10-pixel steps for performance (sampling strategy)
        for (int y = 0; y < height; y += 10) {
            for (int x = 0; x < width; x += 10) {
                // Read pixel color as packed 32-bit ARGB integer
                int rgb = img.getRGB(x, y);
                // Extract blue channel (bits 0-7)
                int blue = rgb & 0xFF;
                // Extract green channel (bits 8-15)
                int green = (rgb >> 8) & 0xFF;
                // Extract red channel (bits 16-23)
                int red = (rgb >> 16) & 0xFF;
                
                // Classify as water if blue dominates red and green by at least 20 units
                if (blue > red + 20 && blue > green + 20) {
                    waterPixels++;  // Increment water count
                } else {
                    landPixels++;   // Otherwise classify as land
                }
            }
        }
        
        // Print classification results
        System.out.println("Water pixels: " + waterPixels);
        System.out.println("Land pixels: " + landPixels);
        // Calculate and display water coverage percentage
        System.out.println("Water percentage: " + (100.0 * waterPixels / (waterPixels + landPixels)) + "%");
    }
}
