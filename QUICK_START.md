# 🚀 Quick Start Guide

## Running the Application

```bash
# From project directory
cd c:\Users\Elkewidy\Desktop\e10b\e10btermproject

# Build and run
setup-maven.bat && mvn clean compile
mvn exec:java -Dexec.mainClass="com.example.App"
```

## Using the Dual Map System

### Step 1: Select a City
- Choose: **New York** | **Boston** | **Houston** | **Dallas**
- Click: **"Monitor City"**

### Step 2: Interactive Map (Default)
- 🗺️ **OpenStreetMap tiles** load automatically
- 🚁 **10 jetpacks** appear with callsigns (EAGLE-1, HAWK-2, etc.)
- ✈️ **Animated movement** toward destinations (20 FPS)
- 🔵 **Dashed blue lines** show flight paths
- 🔴 **Red circles** mark destinations

### Step 3: Toggle to Satellite View
- Click: **"🛰️ Satellite View"** button (top-right)
- 🛰️ **Satellite imagery** loads from Wikipedia Commons
- 📍 **Landmark pins** appear (Empire State, Central Park, etc.)
- 🟠 **Jetpack positions** shown as orange markers
- ⏸️ **Animation pauses** (static view)

### Step 4: Toggle Back
- Click: **"🗺️ Interactive Map"** button
- ▶️ **Animation resumes**
- 🚁 **Jetpacks continue** moving

### Step 5: Change Cities
- Click: **"Select Different City"** (bottom)
- Repeat steps 1-4 for new city

## 🎯 Quick Reference

### Jetpack Info by City

**New York** (10 jetpacks)
- Callsigns: EAGLE-1 → PHOENIX-10
- Center: 40.7128° N, 74.0060° W

**Boston** (10 jetpacks)
- Callsigns: PATRIOT-1 → LIBERTY-10
- Center: 42.3601° N, 71.0589° W

**Houston** (10 jetpacks)
- Callsigns: ROCKET-1 → GALVEZ-10
- Center: 29.7604° N, 95.3698° W

**Dallas** (10 jetpacks)
- Callsigns: COWBOY-1 → ARMADILLO-10
- Center: 32.7767° N, 96.7970° W

### Map Controls

**Interactive Map:**
- 🖱️ **Drag** to pan
- 🔍 **Scroll** to zoom in/out
- 👁️ **Watch** jetpacks move automatically

**Satellite View:**
- 📸 **Fixed** view (no pan/zoom)
- 📍 **Landmark pins** clickable
- 🟠 **Static** jetpack positions

## 🎨 Visual Legend

### Interactive Map Icons
- 🟠 **Orange circle** = Jetpack body
- 🔵 **Blue triangles** = Wings
- 🔴 **Red dot** = Nose (direction indicator)
- 🏷️ **White label** = Callsign

### Satellite View Markers
- 🔴 **Red pin** = Major building/monument
- 🟡 **Yellow pin** = Iconic structure
- 🔵 **Blue pin** = Stadium/water
- 🟢 **Green pin** = Park/green space
- 🟠 **Orange circle** = Jetpack position

## 📊 Feature Comparison

| Feature | Interactive 🗺️ | Satellite 🛰️ |
|---------|---------------|--------------|
| Real-time Animation | ✅ | ❌ |
| Realistic Imagery | ⚠️ OSM | ✅ Photo |
| Landmark Details | ⚠️ | ✅ Pins |
| Pan & Zoom | ✅ | ❌ |
| Jetpack Tracking | ✅ Live | ⚠️ Static |
| Visual Appeal | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |

## 🔧 Troubleshooting

### Issue: Blank Map
**Solution:** Wait 5-10 seconds for tiles to download

### Issue: No Satellite Image
**Solution:** Fallback map will appear automatically

### Issue: Slow Animation
**Solution:** Close other apps, or use satellite view

## 📂 Project Files

**Core Classes:**
- `App.java` - Entry point
- `AirTrafficControllerFrame.java` - Main GUI
- `RealisticCityMap.java` - Satellite imagery
- `JetPack.java` - Jetpack entity
- `FlightPath.java` - Path management

**Documentation:**
- `README.md` - Setup and build instructions
- `FEATURES.md` - Complete feature guide
- `INTEGRATION_SUMMARY.md` - Technical details
- `QUICK_START.md` - This file

**Test:**
- `MapTest.java` - Component testing

## 🎓 Learning Points

This project demonstrates:
- ✅ **JXMapViewer2** - Third-party map library integration
- ✅ **OpenStreetMap** - Free tile-based mapping
- ✅ **SwingWorker** - Asynchronous image loading
- ✅ **Graphics2D** - Custom painting and overlays
- ✅ **Timer Animation** - Smooth 20 FPS updates
- ✅ **GeoPosition** - Latitude/longitude coordinates
- ✅ **Toggle UI** - Dynamic component switching

## 🏆 What Makes This Special

1. **Dual visualization** - Best of both worlds
2. **Real maps** - Actual OSM tiles and satellite photos
3. **40 jetpacks** - Pre-populated data for 4 cities
4. **Smooth animation** - Professional 20 FPS rendering
5. **One-click toggle** - Instant view switching
6. **Fallback graceful** - Works even without images

---

**Pro Tip:** Start with Interactive Map to see the technology, then switch to Satellite View to impress with visuals! 🚀

**Need Help?** See `FEATURES.md` for detailed documentation.
