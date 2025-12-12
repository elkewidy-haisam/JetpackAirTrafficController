# Air Traffic Controller - Feature Guide

## 🚀 Overview

This Air Traffic Controller application now features **dual map visualization modes**:

1. **Interactive OpenStreetMap** - Real-time animated jetpack tracking with JXMapViewer2
2. **Realistic Satellite View** - High-resolution satellite imagery with jetpack overlays

## 🗺️ Map Visualization Modes

### Interactive Map (Default)
When you first select a city, you'll see the **interactive OpenStreetMap view**:

- ✅ **Real OpenStreetMap tiles** from OSM servers
- ✅ **Animated jetpacks** moving in real-time at 20 FPS
- ✅ **Flight path lines** showing destination routes (dashed blue lines)
- ✅ **Destination markers** (red circles)
- ✅ **Pan and zoom** - Mouse drag to pan, scroll to zoom
- ✅ **Live updates** - Jetpacks continuously navigate to new destinations

**Geographic Coordinates:**
- New York: `40.7128° N, 74.0060° W` (Midtown Manhattan)
- Boston: `42.3601° N, 71.0589° W` (Downtown)
- Houston: `29.7604° N, 95.3698° W` (Downtown)
- Dallas: `32.7767° N, 96.7970° W` (Downtown)

### Satellite View (Toggle)
Click the **"🛰️ Satellite View"** button to switch to realistic satellite imagery:

- ✅ **High-resolution satellite images** from Wikipedia Commons
- ✅ **City-specific landmarks** with colorful pins:
  - **New York**: One WTC (Red), Empire State (Yellow), Times Square (Cyan), Central Park (Green)
  - **Boston**: Fenway Park (Green), Boston Common (Green), Harbor (Cyan)
  - **Houston**: NASA JSC (White), Downtown (Yellow), Medical Center (Red)
  - **Dallas**: Reunion Tower (Yellow), AT&T Stadium (Blue), Arts District (Magenta)
- ✅ **Jetpack position overlays** showing current locations with callsigns
- ✅ **Image attribution** displayed at bottom
- ✅ **Fallback rendering** - Stylized map if images unavailable

## 🎮 How to Use

### Step 1: Launch & Select City
```
1. Run the application
2. You'll see the city selection screen
3. Choose from: New York, Boston, Houston, or Dallas
4. Click "Monitor City"
```

### Step 2: View Interactive Map
```
- The map loads showing the city with OpenStreetMap tiles
- 10 jetpacks appear as orange aircraft icons with blue wings
- Each jetpack has a callsign label (e.g., "EAGLE-1")
- Dashed blue lines show where each jetpack is heading
- Red circles mark their destinations
- Jetpacks move smoothly toward destinations
```

### Step 3: Toggle Satellite View
```
- Click the "🛰️ Satellite View" button in the top-right
- The view switches to realistic satellite imagery
- Landmark pins appear showing famous locations
- Jetpack positions update as static markers
- Button changes to "🗺️ Interactive Map"
```

### Step 4: Switch Back
```
- Click "🗺️ Interactive Map" to return to animated view
- Animation resumes with jetpacks continuing their flights
```

### Step 5: Change Cities
```
- Click "Select Different City" at the bottom
- Choose a new city to monitor
- Repeat steps 2-4
```

## 📊 Jetpack Information Display

At the bottom of each city view, you'll see a table listing all 10 jetpacks:

| Serial # | Callsign | Owner | Year | Model | Manufacturer |
|----------|----------|-------|------|-------|--------------|
| NY-001 | EAGLE-1 | John Smith | 2023 | SkyRider X1 | AeroTech |
| ... | ... | ... | ... | ... | ... |

## 🎨 Visual Elements

### Interactive Map Elements
- **Jetpack Icon**: Orange circle body + blue wings + red nose
- **Callsign Label**: White text on semi-transparent black background
- **Flight Path**: Dashed blue line (50% opacity)
- **Destination**: Red circle with outline

### Satellite View Elements
- **Landmark Pins**: Colored circles (20px diameter) with white outline
- **Landmark Labels**: White text on semi-transparent black background
- **Jetpack Markers**: Orange circles (12px) with white outline
- **Callsign Labels**: White text below markers

## 🔧 Technical Implementation

### JXMapViewer Integration
```java
// Map viewer with OpenStreetMap tiles
JXMapViewer mapViewer = new JXMapViewer();
TileFactoryInfo info = new OSMTileFactoryInfo();
DefaultTileFactory tileFactory = new DefaultTileFactory(info);
mapViewer.setTileFactory(tileFactory);

// Set city center and zoom level
mapViewer.setZoom(5);
mapViewer.setAddressLocation(cityCenter);
```

### Satellite Image Loading
```java
// Async image loading with SwingWorker
SwingWorker<BufferedImage, Void> worker = new SwingWorker<>() {
    @Override
    protected BufferedImage doInBackground() throws Exception {
        URL imageUrl = new URL(CITY_IMAGES.get(cityName).url);
        return ImageIO.read(imageUrl);
    }
};
```

### Animation System
```java
// 20 FPS animation timer
animationTimer = new Timer(50, e -> {
    updateJetpackPositions();
    updateMapPainter();
    mapViewer.repaint();
});
```

## 📦 Dependencies

### JXMapViewer2 (version 2.6)
```xml
<dependency>
    <groupId>org.jxmapviewer</groupId>
    <artifactId>jxmapviewer2</artifactId>
    <version>2.6</version>
</dependency>
```

**Provides:**
- OpenStreetMap tile rendering
- Geographic coordinate system (GeoPosition)
- Waypoint and painter API
- Interactive map controls

### Image Sources
All satellite images sourced from **Wikipedia Commons** under Creative Commons licenses:
- Freely available for educational use
- High-resolution aerial photography
- Public domain NASA imagery for some cities

## 🎯 Key Features Comparison

| Feature | Interactive Map | Satellite View |
|---------|----------------|----------------|
| Real-time Animation | ✅ Yes | ❌ Static |
| Geographic Accuracy | ✅ OSM Tiles | ✅ Real Photos |
| Landmark Details | ⚠️ Via OSM | ✅ Pin Markers |
| Internet Required | ✅ For Tiles | ✅ For Images |
| Fallback Available | ⚠️ Blank | ✅ Generated Map |
| Jetpack Movement | ✅ Animated | ❌ Static Overlay |
| Pan & Zoom | ✅ Full Control | ❌ Fixed View |
| Visual Realism | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |

## 🚨 Troubleshooting

### Maps Not Loading
**Issue**: White/blank map area
**Solution**: 
- Check internet connection
- Verify firewall isn't blocking OSM servers
- Wait for tiles to download (may take 5-10 seconds)

### Satellite Images Not Loading
**Issue**: Fallback map appears instead of photos
**Solution**:
- Check internet connection
- Wikipedia Commons may be temporarily unavailable
- Fallback map still shows city structure and jetpacks

### Slow Performance
**Issue**: Choppy animation
**Solution**:
- Close other resource-intensive applications
- Reduce number of open browser tabs
- Satellite view uses less CPU than interactive map

## 💡 Pro Tips

1. **Use Satellite View for Screenshots** - Better visual quality for presentations
2. **Use Interactive Map for Monitoring** - Real-time tracking of jetpack movements
3. **Toggle During Flight** - Watch how positions translate between views
4. **Zoom on Interactive Map** - Get closer views of specific areas
5. **Check Landmark Pins** - Learn real geographic features of each city

## 🎓 Educational Value

This dual-view system demonstrates:
- **Map Projection**: Converting lat/lon to screen coordinates
- **Tile-based Rendering**: How web maps work (Google Maps, Bing Maps)
- **Asynchronous Loading**: SwingWorker for background tasks
- **Custom Painting**: Graphics2D overlays on map components
- **Animation Techniques**: Timer-based smooth movement
- **API Integration**: Using third-party mapping libraries

---

**Next Steps**: Explore each city, toggle between views, and watch the jetpacks navigate the skies! 🚁✈️
