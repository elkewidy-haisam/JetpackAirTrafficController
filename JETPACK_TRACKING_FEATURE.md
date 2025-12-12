# Jetpack Individual Tracking Feature

## Overview
Added functionality to allow users to select individual jetpacks from the list of 100 jetpacks per city and track them in a separate window showing their flight path in real-time.

## New Files Created

### 1. JetpackTrackingWindow.java
Location: `src\main\java\com\example\ui\frames\JetpackTrackingWindow.java`

**Purpose**: Creates a separate window that displays a bird's eye view of a single selected jetpack flying over the city map.

**Key Features**:
- Displays jetpack information in header (callsign, serial number, owner, model, manufacturer, year)
- Shows city map as background
- Renders selected jetpack with:
  - Glow effect (larger, more visible than main view)
  - Callsign label
  - Destination marker (red cross)
- Real-time position information overlay showing:
  - Current position (X, Y coordinates)
  - Current status (ACTIVE, DETOUR, EMERGENCY, etc.)
  - Destination coordinates
- Updates at 50ms intervals (20 FPS) for smooth animation
- Independent window that can be closed without affecting main view

## Modified Files

### 2. CityMapJetpackListFactory.java
Location: `src\main\java\com\example\ui\citymap\CityMapJetpackListFactory.java`

**Changes**:
- Added `JetpackTrackingCallback` interface for handling track button clicks
- Enhanced method signature to accept:
  - `List<JetPackFlight>` to link jetpacks with their flights
  - `JetpackTrackingCallback` for tracking functionality
- Replaced simple text list with interactive panel list featuring:
  - Individual row for each jetpack
  - "🔍 Track" button for each jetpack
  - Alternating row colors for better readability
  - Scrollable view for all 100 jetpacks
  - Information banner showing total jetpack count
- Maintained backward compatibility with overloaded method

**Visual Improvements**:
- More modern UI with styled buttons
- Better color scheme (cornflower blue track buttons)
- Improved spacing and padding
- Fixed-height rows for consistent appearance

### 3. CityMapPanel.java
Location: `src\main\java\com\example\ui\citymap\CityMapPanel.java`

**Changes**:
- Added import for `JetpackTrackingWindow`
- Modified `setShowCitySelectionCallback()` to:
  - Pass `jetpackFlights` list to factory
  - Include tracking callback: `(jetpack, flight) -> openJetpackTrackingWindow(jetpack, flight)`
- Added new method `openJetpackTrackingWindow()`:
  - Creates new `JetpackTrackingWindow` instance
  - Passes selected jetpack, flight, and city name
  - Makes window visible

### 4. CityMapLoader.java
Location: `src\main\java\com\example\ui\citymap\CityMapLoader.java`

**Changes**:
- Added overloaded `loadCityMap(String cityName)` method
- Simplified map loading for tracking window
- Returns `BufferedImage` directly
- Uses same map file resolution logic as main method
- Includes additional path checks for `src/main/resources/`

## User Interaction Flow

1. User selects a city (New York, Boston, Houston, or Dallas)
2. Main view displays all ~100 jetpacks flying
3. User scrolls through jetpack list at bottom of screen
4. User clicks "🔍 Track" button next to desired jetpack
5. New window opens showing:
   - Selected jetpack only
   - Full city map background
   - Real-time position updates
   - Flight information overlay
6. User can:
   - Open multiple tracking windows simultaneously
   - Close tracking window anytime
   - Continue monitoring main view while tracking individual jetpacks

## Technical Details

### Data Flow
```
CityMapPanel
  ↓ (creates)
CityMapJetpackListFactory
  ↓ (user clicks Track button)
JetpackTrackingCallback
  ↓ (calls)
CityMapPanel.openJetpackTrackingWindow()
  ↓ (creates)
JetpackTrackingWindow
  ↓ (displays)
MapTrackingPanel (inner class)
  ↓ (renders)
Single Jetpack + City Map
```

### Rendering Details
- **Main View**: Shows all jetpacks as small dots
- **Tracking View**: Shows single jetpack as larger glowing dot with:
  - Outer glow (24px diameter, 80 alpha)
  - Middle glow (16px diameter, 150 alpha)
  - Inner dot (10px diameter, full opacity)
  - Callsign label with background
  - Larger font size for better visibility

### Update Mechanism
- Tracking window uses Timer updating at 50ms intervals
- Directly accesses JetPackFlight object's current position
- No separate animation logic needed - reads from existing flight simulation
- Shares same flight state as main view (synchronized automatically)

## Benefits

1. **Enhanced Monitoring**: Users can focus on specific jetpacks of interest
2. **Multiple Windows**: Track multiple jetpacks simultaneously
3. **Independent Views**: Main view continues while tracking individual jetpacks
4. **Detailed Information**: Larger view with enhanced visual feedback
5. **Non-Intrusive**: Tracking windows are optional and don't affect main simulation
6. **Real-Time Updates**: Position updates reflect actual flight simulation state

## Future Enhancement Possibilities

- Add trail visualization in tracking window
- Add zoom controls for tracking window
- Add "Follow" mode that auto-centers on jetpack
- Add flight path prediction overlay
- Add collision warnings in tracking view
- Export tracking data to file
- Record/replay flight paths
- Compare multiple jetpacks side-by-side
