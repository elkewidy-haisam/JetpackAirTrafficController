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
