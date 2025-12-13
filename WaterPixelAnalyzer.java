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
    public static void main(String[] args) {
        String[] cities = {"boston", "newyorkcity", "dallas", "houston"};
        
        for (String city : cities) {
            try {
                String imagePath = "src/main/java/maps/" + city + ".png";
                File imageFile = new File(imagePath);
                
                if (!imageFile.exists()) {
                    System.out.println("Image not found: " + imagePath);
                    continue;
                }
                
                BufferedImage img = ImageIO.read(imageFile);
                analyzeWater(img, city);
                
            } catch (IOException e) {
                System.out.println("Error reading " + city + ": " + e.getMessage());
            }
        }
    }
    
    private static void analyzeWater(BufferedImage img, String city) {
        int width = img.getWidth();
        int height = img.getHeight();
        
        System.out.println("\n=== " + city.toUpperCase() + " ===");
        System.out.println("Image size: " + width + "x" + height);
        
        // Sample water detection - check common blue colors
        int waterPixels = 0;
        int landPixels = 0;
        
        // Sample every 10th pixel
        for (int y = 0; y < height; y += 10) {
            for (int x = 0; x < width; x += 10) {
                int rgb = img.getRGB(x, y);
                int blue = rgb & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int red = (rgb >> 16) & 0xFF;
                
                // Water detection: blue > red and blue > green
                if (blue > red + 20 && blue > green + 20) {
                    waterPixels++;
                } else {
                    landPixels++;
                }
            }
        }
        
        System.out.println("Water pixels: " + waterPixels);
        System.out.println("Land pixels: " + landPixels);
        System.out.println("Water percentage: " + (100.0 * waterPixels / (waterPixels + landPixels)) + "%");
    }
}
