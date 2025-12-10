# 3D Jetpack Tracking View Implementation

## Overview
The jetpack tracking window has been converted from a 2D bird's-eye view to a 3D behind-the-jetpack perspective view.

## Key Changes

### Visual Transformation
1. **3D Perspective Rendering**: The tracking window now shows a first-person view from behind the jetpack
2. **Sky Gradient**: Dynamic sky rendering with gradient from horizon to top
3. **Ground Plane**: Perspective grid showing depth and distance
4. **3D Buildings**: City buildings rendered with proper perspective scaling based on distance
5. **Jetpack Model**: Visible jetpack with thrusters and flame effects in the foreground
6. **Destination Marker**: 3D projection of destination coordinates in the view

### Coordinate Synchronization
The tracking window coordinates are **always synchronized** with the main panel:
- Uses `flight.getX()` and `flight.getY()` directly from the shared JetPackFlight object
- Real-time coordinate updates displayed in HUD
- Same coordinate system as main panel at all times

### HUD Display
The heads-up display shows:
- **X coordinate**: Real-time X position matching main panel
- **Y coordinate**: Real-time Y position matching main panel  
- **Status**: Current flight status
- **Callsign**: Jetpack identification
- **Crosshair**: Center targeting reticle

### 3D Elements

#### Sky
- Gradient from cornflower blue to sky blue
- Creates sense of atmosphere and depth

#### Ground Plane
- Perspective grid with receding lines
- Horizontal and vertical grid lines converge at horizon
- Green gradient simulating terrain

#### Buildings
- 20 procedurally generated buildings based on jetpack position
- Perspective scaling: closer buildings appear larger
- Windows with random illumination
- Distance-based brightness (darker when further away)

#### Jetpack Model
- Central jetpack visible in lower portion of screen
- Body with metallic grey coloring
- Two thrusters with animated flames
- Control straps for realism

#### Destination Marker
- Red crosshair projected into 3D space
- Shows direction and distance to target
- Scales based on distance from jetpack

## Technical Implementation

### Coordinate Synchronization
```java
// Get jetpack position (these are the actual coordinates from main panel)
double jetpackX = flight.getX();
double jetpackY = flight.getY();
```

These coordinates come from the same `JetPackFlight` object used by the main CityMapPanel, ensuring perfect synchronization.

### Perspective Projection
Buildings use simplified perspective projection:
```java
double scale = 1.0 / (offsetY / 200.0);
int screenX = width / 2 + (int)(offsetX * scale);
```

### Update Rate
- 50ms refresh timer (20 FPS) for smooth animation
- Real-time coordinate updates from flight object

## Usage
1. Select a jetpack from the main panel
2. Click "Track" to open the 3D tracking window
3. The window shows a behind-the-jetpack view
4. HUD displays real-time coordinates matching the main panel
5. Watch the destination marker to see where the jetpack is heading

## Benefits
1. **Immersive View**: First-person perspective provides better situational awareness
2. **Coordinate Accuracy**: Direct access to flight object ensures coordinates always match
3. **Real-time Updates**: 50ms refresh keeps view synchronized with flight movements
4. **Visual Feedback**: 3D rendering makes it easier to understand jetpack movement and orientation
5. **Enhanced Tracking**: Destination markers and HUD provide complete flight information

## Future Enhancements
- Add altitude visualization in HUD
- Implement banking/rolling animation based on turn direction
- Add weather effects (clouds, rain, fog)
- Include other nearby jetpacks in 3D view
- Add terrain details from city map
