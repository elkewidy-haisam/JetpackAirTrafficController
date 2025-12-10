# JOGL Architecture Diagram

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    JETPACK TRACKING WINDOW                  │
│                  JetpackTrackingWindow.java                 │
└────────────────────────────┬────────────────────────────────┘
                             │
                    ┌────────▼────────┐
                    │  Try JOGL First │
                    └────────┬────────┘
                             │
              ┌──────────────┴──────────────┐
              │                             │
         ✅ SUCCESS                    ❌ FAILURE
              │                             │
              ▼                             ▼
    ┌─────────────────────┐      ┌─────────────────────┐
    │   JOGL MODE         │      │  FALLBACK MODE      │
    │  (Hardware Accel)   │      │  (Software)         │
    └──────────┬──────────┘      └──────────┬──────────┘
               │                            │
               ▼                            ▼
    ┌─────────────────────┐      ┌─────────────────────┐
    │  JOGL3DPanel        │      │  MapTrackingPanel   │
    │  (GLJPanel)         │      │  (JPanel)           │
    │  • Swing compatible │      │  • Graphics2D       │
    │  • HUD overlay      │      │  • Manual 3D        │
    │  • Auto updates     │      │  • 20 FPS           │
    └──────────┬──────────┘      └──────────┬──────────┘
               │                            │
               ▼                            ▼
    ┌─────────────────────┐      ┌─────────────────────┐
    │ JOGLRenderer3D      │      │  Renderer3D         │
    │ (GLEventListener)   │      │  (Static Methods)   │
    │  • OpenGL calls     │      │  • Graphics2D       │
    │  • GPU rendering    │      │  • CPU rendering    │
    │  • 60+ FPS          │      │  • 20 FPS           │
    │  • Lighting         │      │  • Basic shading    │
    │  • Z-buffer         │      │  • Manual sort      │
    └──────────┬──────────┘      └──────────┬──────────┘
               │                            │
               └────────────┬───────────────┘
                            │
                            ▼
                 ┌──────────────────────┐
                 │    SHARED DATA       │
                 │                      │
                 │  • CityModel3D       │
                 │  • Building3D        │
                 │  • JetPackFlight     │
                 │  • CityMapLoader     │
                 └──────────────────────┘
```

## Component Flow

### JOGL Mode (Hardware-Accelerated)

```
User Clicks "Track"
       ↓
JetpackTrackingWindow.createRenderPanel()
       ↓
Try: new JOGL3DPanel(...)
       ↓
Load CityModel3D from city map
       ↓
Create JOGLRenderer3D
       ↓
Initialize OpenGL (init())
  • Enable depth testing
  • Set up lighting
  • Configure viewport
       ↓
Start Update Timer (50ms)
       ↓
┌──────────────────────────┐
│   RENDER LOOP (60 FPS)   │
│                          │
│  1. Update flight data   │
│  2. Update HUD labels    │
│  3. Call renderer.update │
│  4. Trigger OpenGL draw  │
│  5. display() called     │
│     • Clear buffers      │
│     • Set camera         │
│     • Draw ground        │
│     • Draw buildings     │
│     • Draw jetpacks      │
│     • Draw markers       │
│  6. Swap buffers         │
└───────────┬──────────────┘
            ↓
      GPU renders frame
            ↓
       60+ FPS output
```

### Fallback Mode (Software)

```
User Clicks "Track"
       ↓
JetpackTrackingWindow.createRenderPanel()
       ↓
JOGL failed OR disabled
       ↓
Create MapTrackingPanel
       ↓
Load CityModel3D
       ↓
Start Update Timer (50ms)
       ↓
┌──────────────────────────┐
│   RENDER LOOP (20 FPS)   │
│                          │
│  1. Update timer tick    │
│  2. Trigger repaint()    │
│  3. paintComponent()     │
│  4. Renderer3D.render    │
│     • Manual projection  │
│     • Sort buildings     │
│     • Draw back-to-front │
│     • Manual shading     │
│  5. Draw HUD             │
└───────────┬──────────────┘
            ↓
      CPU renders frame
            ↓
       20 FPS output
```

## Data Flow

```
┌─────────────────┐
│  City Map Image │ (PNG file)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ CityMapLoader   │ Load image
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  CityModel3D    │ Generate buildings
│  • Scan pixels  │
│  • Detect water │
│  • Place bldgs  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ List<Building3D>│ Building data
│  • Position     │
│  • Dimensions   │
│  • Type/Color   │
└────────┬────────┘
         │
         ├─────────────────┬─────────────────┐
         │                 │                 │
         ▼                 ▼                 ▼
┌────────────────┐ ┌──────────────┐ ┌──────────────┐
│ JOGLRenderer3D │ │ Renderer3D   │ │ Other Uses   │
│ (OpenGL draw)  │ │ (2D project) │ │ (collision)  │
└────────────────┘ └──────────────┘ └──────────────┘
```

## Rendering Pipeline

### JOGL OpenGL Pipeline

```
Application Thread          OpenGL Thread
─────────────────          ──────────────
      │
      │ Update flight data
      ├──────────────────────►  renderer.updateData()
      │                                  │
      │                         Store in member vars
      │                                  │
      │ Trigger repaint        ◄─────────┘
      ├──────────────────────►  
      │                         display() called
      │                                  │
      │                         Clear buffers (GPU)
      │                                  │
      │                         Set camera position
      │                                  │
      │                         For each building:
      │                           • Transform vertices (GPU)
      │                           • Apply lighting (GPU)
      │                           • Depth test (GPU)
      │                           • Rasterize (GPU)
      │                                  │
      │                         Swap buffers
      │                                  │
      │                         Frame complete
      ◄──────────────────────────────────┘
      │
```

### Graphics2D Pipeline

```
Event Thread
────────────
      │
      │ Timer tick (50ms)
      ├──────────────► repaint()
      │                    │
      │             paintComponent()
      │                    │
      │             Renderer3D.renderScene()
      │                    │
      │             For each building:
      │               • Manual 3D projection (CPU)
      │               • Calculate screen pos (CPU)
      │               • Sort by distance (CPU)
      │                    │
      │             Draw back-to-front:
      │               • drawPolygon (CPU)
      │               • fillPolygon (CPU)
      │               • drawLine (CPU)
      │                    │
      │             Draw HUD
      │                    │
      │             Frame complete
      ◄────────────────────┘
      │
```

## Class Relationships

```
┌──────────────────────┐
│ JetpackTrackingWindow│
│  extends JFrame      │
└──────────┬───────────┘
           │
           │ contains
           ▼
    ┌──────────────┐
    │ JComponent   │ (interface)
    └──────┬───────┘
           │
    ┏━━━━━━┻━━━━━━┓
    ▼              ▼
┌─────────────┐ ┌──────────────┐
│JOGL3DPanel  │ │MapTracking   │
│extends      │ │Panel         │
│GLJPanel     │ │extends JPanel│
└──────┬──────┘ └──────┬───────┘
       │               │
       │uses           │uses
       ▼               ▼
┌──────────────┐ ┌──────────────┐
│JOGLRenderer3D│ │Renderer3D    │
│implements    │ │(static)      │
│GLEventListen │ │              │
└──────┬───────┘ └──────┬───────┘
       │                │
       └────────┬───────┘
                │ both use
                ▼
         ┌──────────────┐
         │ CityModel3D  │
         │  • buildings │
         │  • water map │
         └──────┬───────┘
                │ contains
                ▼
         ┌──────────────┐
         │ Building3D   │
         │  • position  │
         │  • size      │
         │  • color     │
         └──────────────┘
```

## Key Differences

### JOGL vs Graphics2D

```
Feature              │ JOGL OpenGL        │ Graphics2D
─────────────────────┼────────────────────┼──────────────────
Rendering            │ GPU                │ CPU
Frame Rate           │ 60+ FPS            │ 20 FPS
Depth Testing        │ Z-buffer (auto)    │ Manual sorting
Lighting             │ Real 3D lighting   │ Fake shading
Transformations      │ Matrix (GPU)       │ Manual calc (CPU)
Anti-aliasing        │ Hardware MSAA      │ Limited
Perspective          │ gluPerspective     │ Manual projection
Camera               │ gluLookAt          │ Manual transform
Drawing              │ gl.glVertex3d      │ g2d.drawLine
Performance          │ Scales with GPU    │ Scales with CPU
Dependencies         │ JOGL + natives     │ None (Java std)
Compatibility        │ Needs OpenGL       │ Always works
```

---

## Summary

The architecture allows **two completely separate rendering paths** that share the same data models, providing:

1. **Hardware acceleration** when available (JOGL)
2. **Graceful fallback** when not (Graphics2D)
3. **Zero impact** on existing code
4. **Same visual results** (but faster with JOGL)
5. **Easy maintenance** (separate renderers)

Both paths produce identical functionality, just with different performance characteristics!
