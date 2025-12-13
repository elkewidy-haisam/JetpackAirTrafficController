/**
 * Three-dimensional model representation of joglrenderer for JOGL rendering.
 * 
 * Purpose:
 * Models joglrenderer geometry with position, dimensions, and visual properties for
 * hardware-accelerated 3D rendering via JOGL. Supports collision detection, spatial queries,
 * and realistic visualization in the 3D city view.
 * 
 * Key Responsibilities:
 * - Store geometric data (position, dimensions) for rendering
 * - Provide visual attributes (color, texture, detail level) for realism
 * - Support collision and proximity queries for spatial operations
 * - Enable type classification and metadata access
 * 
 * Interactions:
 * - Rendered by JOGL3DPanel and JOGLRenderer3D
 * - Referenced by CityModel3D for scene composition
 * - Used in collision detection and spatial analysis
 * 
 * Patterns & Constraints:
 * - Immutable geometry for thread-safe access
 * - Compatible with OpenGL rendering pipelines
 * - Simple collision models for performance
 * 
 * @author Haisam Elkewidy
 */

package com.example.ui.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.accident.AccidentAlert.Accident;
import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.model.CityModel3D;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * JOGLRenderer3D - Hardware-accelerated 3D rendering using JOGL/OpenGL
 * Drop-in replacement for Renderer3D with the same interface but OpenGL backend
 */
public class JOGLRenderer3D implements GLEventListener {
                private List<Accident> accidents;
                private Map<JetPackFlight, JetPackFlightState> flightStates;
                private double cameraAzimuth = 45;
                private double cameraElevation = 30;
                private double cameraDistance = 400;
            private final GLUT glut = new GLUT();
        // Configurable offset for jetpack facing direction (degrees)
        private static final double JETPACK_DIRECTION_OFFSET_DEGREES = 90.0;
        // Mouse camera controls
        private double mouseAzimuth = 45;
        private double mouseElevation = 30;
        private double mouseDistance = 120;
        // Removed unused field mouseControlEnabled
        private int lastMouseX, lastMouseY;
        private boolean dragging = false;

        // Call this from your panel's mouse listeners
        public void mousePressed(int x, int y) {
            dragging = true;
            lastMouseX = x;
            lastMouseY = y;
        }
        public void mouseReleased(int x, int y) {
            dragging = false;
        }
        public void mouseDragged(int x, int y) {
            if (dragging) {
                int dx = x - lastMouseX;
                int dy = y - lastMouseY;
                mouseAzimuth += dx * 0.5;
                mouseElevation -= dy * 0.5;
                mouseElevation = Math.max(5, Math.min(89, mouseElevation)); // Prevent below ground
                lastMouseX = x;
                lastMouseY = y;
            }
        }
        public void mouseWheelMoved(int wheelRotation) {
            mouseDistance += wheelRotation * 10;
            mouseDistance = Math.max(50, Math.min(2000, mouseDistance));
        }
    @Override
    public void init(GLAutoDrawable drawable) {
        // Enable depth testing for correct 3D rendering
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glClearDepth(1.0f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // TODO: Implement cleanup logic if needed
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.7f, 0.85f, 1.0f, 1.0f); // sky blue background
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Enable lighting for polished look
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        float[] ambient = {0.3f, 0.3f, 0.3f, 1.0f};
        float[] diffuse = {0.7f, 0.7f, 0.7f, 1.0f};
        float[] position = {0.0f, 800.0f, 0.0f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);

        // Camera logic: follow jetpack if tracked, else center on city
        if (flight != null) {
            double jetX = flight.getX();
            double jetY = flight.getAltitude();
            double jetZ = flight.getY();
            // Use mouseAzimuth, mouseElevation, mouseDistance for camera position
            double azRad = Math.toRadians(mouseAzimuth);
            double elRad = Math.toRadians(mouseElevation);
            double camDist = mouseDistance;
            double eyeX = jetX + camDist * Math.cos(azRad) * Math.cos(elRad);
            double eyeY = Math.max(jetY + camDist * Math.sin(elRad), 5);
            double eyeZ = jetZ + camDist * Math.sin(azRad) * Math.cos(elRad);
            double lookX = jetX;
            double lookY = jetY;
            double lookZ = jetZ;
            glu.gluLookAt(eyeX, eyeY, eyeZ, lookX, lookY, lookZ, 0, 1, 0);
        } else {
            double centerX = cityModel != null ? cityModel.getMapWidth() / 2.0 : 0;
            double centerY = 0;
            double centerZ = cityModel != null ? cityModel.getMapHeight() / 2.0 : 0;
            double azRad = Math.toRadians(mouseAzimuth);
            double elRad = Math.toRadians(mouseElevation);
            double camDist = mouseDistance;
            double eyeX = centerX + camDist * Math.cos(azRad) * Math.cos(elRad);
            double eyeY = Math.max(centerY + camDist * Math.sin(elRad), 5);
            double eyeZ = centerZ + camDist * Math.sin(azRad) * Math.cos(elRad);
            glu.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, 0, 1, 0);
        }

        // Draw ground plane (grass/land)
        if (cityModel != null) {
            gl.glColor3f(0.4f, 0.7f, 0.3f); // grass green
            gl.glPushMatrix();
            gl.glTranslated(cityModel.getMapWidth()/2, -0.1, cityModel.getMapHeight()/2); // Slightly below Y=0
            gl.glScaled(cityModel.getMapWidth(), 1, cityModel.getMapHeight());
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex3f(-0.5f, 0, -0.5f);
            gl.glVertex3f(0.5f, 0, -0.5f);
            gl.glVertex3f(0.5f, 0, 0.5f);
            gl.glVertex3f(-0.5f, 0, 0.5f);
            gl.glEnd();
            gl.glPopMatrix();
        }

        // Draw water
        if (cityModel != null) {
            int step = 16;
            for (int x = 0; x < cityModel.getMapWidth(); x += step) {
                for (int y = 0; y < cityModel.getMapHeight(); y += step) {
                    if (cityModel.isWater(x, y)) {
                        gl.glColor3f(0.2f, 0.4f, 0.9f); // water blue
                        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.8f,0.9f,1.0f,1.0f}, 0);
                        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 80f);
                        gl.glPushMatrix();
                        gl.glTranslated(x, 0.5, y);
                        gl.glScaled(step, 1, step);
                        gl.glBegin(GL2.GL_QUADS);
                        gl.glVertex3f(-0.5f, 0, -0.5f);
                        gl.glVertex3f(0.5f, 0, -0.5f);
                        gl.glVertex3f(0.5f, 0, 0.5f);
                        gl.glVertex3f(-0.5f, 0, 0.5f);
                        gl.glEnd();
                        gl.glPopMatrix();
                    }
                }
            }
        }

        // Draw roads (thinner, more realistic)
        if (cityModel != null) {
            for (var road : cityModel.getRoads()) {
                gl.glColor3f(0.35f, 0.35f, 0.35f); // dark gray
                gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.2f,0.2f,0.2f,1.0f}, 0);
                gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 8f);
                gl.glPushMatrix();
                gl.glTranslated(road.x, 0.5, road.y);
                gl.glRotated(road.angle + Math.random()*2-1, 0, 1, 0); // slight random rotation
                gl.glScaled(road.length, 0.5, Math.max(road.width*0.6, 2)); // thinner roads
                gl.glBegin(GL2.GL_QUADS);
                gl.glVertex3f(-0.5f, 0, -0.5f);
                gl.glVertex3f(0.5f, 0, -0.5f);
                gl.glVertex3f(0.5f, 0, 0.5f);
                gl.glVertex3f(-0.5f, 0, 0.5f);
                gl.glEnd();
                gl.glPopMatrix();
            }
        }

        // Draw bridges (arched, more distinct)
        if (cityModel != null) {
            for (var bridge : cityModel.getBridges()) {
                gl.glColor3f(0.85f, 0.7f, 0.2f); // muted yellow
                gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f,0.9f,0.5f,1.0f}, 0);
                gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 40f);
                gl.glPushMatrix();
                gl.glTranslated(bridge.x, 2, bridge.y);
                gl.glRotated(bridge.angle, 0, 1, 0);
                gl.glScaled(bridge.length, 2, bridge.width);
                // Draw base
                gl.glBegin(GL2.GL_QUADS);
                gl.glVertex3f(-0.5f, 0, -0.5f);
                gl.glVertex3f(0.5f, 0, -0.5f);
                gl.glVertex3f(0.5f, 0, 0.5f);
                gl.glVertex3f(-0.5f, 0, 0.5f);
                gl.glEnd();
                // Draw simple arch
                gl.glBegin(GL2.GL_LINE_STRIP);
                for (int i = 0; i <= 10; i++) {
                    double t = i / 10.0;
                    double archY = Math.sin(Math.PI * t) * 0.5;
                    gl.glVertex3f((float)(-0.5 + t), (float)archY, 0);
                }
                gl.glEnd();
                gl.glPopMatrix();
            }
        }

        // Draw houses (with roof, color variation)
        if (cityModel != null) {
            for (var house : cityModel.getHouses()) {
                float r = 0.6f + (float)Math.random()*0.2f;
                float g = 0.4f + (float)Math.random()*0.2f;
                float b = 0.2f + (float)Math.random()*0.1f;
                gl.glColor3f(r, g, b); // varied brown
                gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.8f,0.6f,0.3f,1.0f}, 0);
                gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 30f);
                gl.glPushMatrix();
                gl.glTranslated(house.x, house.height / 2, house.y);
                gl.glScaled(house.width, house.height, house.depth);
                // Draw base
                gl.glBegin(GL2.GL_QUADS);
                gl.glVertex3f(-0.5f, -0.5f, -0.5f);
                gl.glVertex3f(0.5f, -0.5f, -0.5f);
                gl.glVertex3f(0.5f, 0.5f, -0.5f);
                gl.glVertex3f(-0.5f, 0.5f, -0.5f);
                gl.glVertex3f(-0.5f, -0.5f, 0.5f);
                gl.glVertex3f(0.5f, -0.5f, 0.5f);
                gl.glVertex3f(0.5f, 0.5f, 0.5f);
                gl.glVertex3f(-0.5f, 0.5f, 0.5f);
                gl.glEnd();
                // Draw simple roof (triangle)
                gl.glBegin(GL2.GL_TRIANGLES);
                gl.glColor3f(r*0.8f, g*0.8f, b*0.8f);
                gl.glVertex3f(-0.5f, 0.5f, -0.5f);
                gl.glVertex3f(0.5f, 0.5f, -0.5f);
                gl.glVertex3f(0.0f, 0.8f, -0.5f);
                gl.glVertex3f(-0.5f, 0.5f, 0.5f);
                gl.glVertex3f(0.5f, 0.5f, 0.5f);
                gl.glVertex3f(0.0f, 0.8f, 0.5f);
                gl.glEnd();
                gl.glPopMatrix();
            }
        }

        // Draw buildings (better proportions, color/material variation, roof detail)
        if (cityModel != null) {
            for (var building : cityModel.getBuildings()) {
                float baseR, baseG, baseB;
                gl.glPushMatrix();
                // Place building base at Y=0
                gl.glTranslated(building.getX(), building.getHeight() / 2.0, building.getY());
                double w = building.getWidth();
                double h = building.getHeight();
                double l = building.getLength();
                switch (building.getType()) {
                    case "skyscraper":
                        baseR = 0.7f; baseG = 0.7f; baseB = 0.8f;
                        gl.glColor3f(baseR, baseG, baseB);
                        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.9f,0.9f,1.0f,1.0f}, 0);
                        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 80f);
                        gl.glScaled(w, h, l);
                        glut.glutSolidCube(1.0f);
                        // Windows: draw blue rectangles on all 4 sides
                        gl.glLineWidth(1.2f);
                        gl.glColor3f(0.2f, 0.4f, 0.9f);
                        int floors = Math.max(3, (int)h/8);
                        int winCols = Math.max(2, (int)w/6);
                        for (int side = 0; side < 4; side++) {
                            gl.glPushMatrix();
                            if (side == 1) gl.glRotated(90, 0, 1, 0);
                            if (side == 2) gl.glRotated(180, 0, 1, 0);
                            if (side == 3) gl.glRotated(270, 0, 1, 0);
                            for (int i = 0; i < floors; i++) {
                                double y = -0.5 + (i+0.5)/floors;
                                for (int j = 0; j < winCols; j++) {
                                    double x = -0.4 + (j+0.5)/winCols*0.8;
                                    gl.glBegin(GL2.GL_QUADS);
                                    gl.glVertex3d(x-0.04, y-0.06, 0.51);
                                    gl.glVertex3d(x+0.04, y-0.06, 0.51);
                                    gl.glVertex3d(x+0.04, y+0.06, 0.51);
                                    gl.glVertex3d(x-0.04, y+0.06, 0.51);
                                    gl.glEnd();
                                }
                            }
                            gl.glPopMatrix();
                        }
                        // Spire
                        gl.glPushMatrix();
                        gl.glTranslated(0, 0.6, 0);
                        gl.glScaled(0.2, 0.5, 0.2);
                        gl.glColor3f(0.9f, 0.9f, 0.9f);
                        glut.glutSolidCone(1, 2, 8, 2);
                        gl.glPopMatrix();
                        break;
                    case "office":
                        baseR = 0.5f; baseG = 0.6f; baseB = 0.7f;
                        gl.glColor3f(baseR, baseG, baseB);
                        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.7f,0.7f,0.7f,1.0f}, 0);
                        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 40f);
                        gl.glScaled(w, h, l);
                        glut.glutSolidCube(1.0f);
                        // Windows: draw white rectangles
                        gl.glLineWidth(1.1f);
                        gl.glColor3f(0.85f, 0.85f, 1.0f);
                        floors = Math.max(2, (int)h/10);
                        winCols = Math.max(2, (int)w/8);
                        for (int side = 0; side < 4; side++) {
                            gl.glPushMatrix();
                            if (side == 1) gl.glRotated(90, 0, 1, 0);
                            if (side == 2) gl.glRotated(180, 0, 1, 0);
                            if (side == 3) gl.glRotated(270, 0, 1, 0);
                            for (int i = 0; i < floors; i++) {
                                double y = -0.5 + (i+0.5)/floors;
                                for (int j = 0; j < winCols; j++) {
                                    double x = -0.4 + (j+0.5)/winCols*0.8;
                                    gl.glBegin(GL2.GL_QUADS);
                                    gl.glVertex3d(x-0.045, y-0.07, 0.51);
                                    gl.glVertex3d(x+0.045, y-0.07, 0.51);
                                    gl.glVertex3d(x+0.045, y+0.07, 0.51);
                                    gl.glVertex3d(x-0.045, y+0.07, 0.51);
                                    gl.glEnd();
                                }
                            }
                            gl.glPopMatrix();
                        }
                        break;
                    case "residential":
                        baseR = 0.8f; baseG = 0.7f; baseB = 0.6f;
                        gl.glColor3f(baseR, baseG, baseB);
                        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.8f,0.7f,0.6f,1.0f}, 0);
                        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 20f);
                        gl.glScaled(w, h, l);
                        glut.glutSolidCube(1.0f);
                        // Bricks: draw horizontal and vertical lines
                        gl.glColor3f(0.6f, 0.3f, 0.2f);
                        int brickRows = Math.max(3, (int)h/4);
                        int brickCols = Math.max(2, (int)w/3);
                        for (int side = 0; side < 4; side++) {
                            gl.glPushMatrix();
                            if (side == 1) gl.glRotated(90, 0, 1, 0);
                            if (side == 2) gl.glRotated(180, 0, 1, 0);
                            if (side == 3) gl.glRotated(270, 0, 1, 0);
                            // Horizontal lines
                            for (int i = 1; i < brickRows; i++) {
                                double y = -0.5 + i/(double)brickRows;
                                gl.glBegin(GL2.GL_LINES);
                                gl.glVertex3d(-0.5, y, 0.51);
                                gl.glVertex3d(0.5, y, 0.51);
                                gl.glEnd();
                            }
                            // Vertical lines
                            for (int j = 1; j < brickCols; j++) {
                                double x = -0.5 + j/(double)brickCols;
                                gl.glBegin(GL2.GL_LINES);
                                gl.glVertex3d(x, -0.5, 0.51);
                                gl.glVertex3d(x, 0.5, 0.51);
                                gl.glEnd();
                            }
                            gl.glPopMatrix();
                        }
                        // Roof
                        gl.glPushMatrix();
                        gl.glTranslated(0, 0.5, 0);
                        gl.glScaled(0.8, 0.4, 0.8);
                        gl.glColor3f(baseR*0.8f, baseG*0.8f, baseB*0.8f);
                        glut.glutSolidCone(0.7, 1.2, 8, 2);
                        gl.glPopMatrix();
                        break;
                    case "commercial":
                        baseR = 0.7f; baseG = 0.6f; baseB = 0.3f;
                        gl.glColor3f(baseR, baseG, baseB);
                        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.9f,0.8f,0.5f,1.0f}, 0);
                        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 30f);
                        gl.glScaled(w, h, l);
                        glut.glutSolidCube(1.0f);
                        // Tiles: draw grid pattern
                        gl.glColor3f(0.85f, 0.85f, 0.7f);
                        int tileRows = Math.max(2, (int)h/8);
                        int tileCols = Math.max(2, (int)w/5);
                        for (int side = 0; side < 4; side++) {
                            gl.glPushMatrix();
                            if (side == 1) gl.glRotated(90, 0, 1, 0);
                            if (side == 2) gl.glRotated(180, 0, 1, 0);
                            if (side == 3) gl.glRotated(270, 0, 1, 0);
                            // Horizontal lines
                            for (int i = 1; i < tileRows; i++) {
                                double y = -0.5 + i/(double)tileRows;
                                gl.glBegin(GL2.GL_LINES);
                                gl.glVertex3d(-0.5, y, 0.51);
                                gl.glVertex3d(0.5, y, 0.51);
                                gl.glEnd();
                            }
                            // Vertical lines
                            for (int j = 1; j < tileCols; j++) {
                                double x = -0.5 + j/(double)tileCols;
                                gl.glBegin(GL2.GL_LINES);
                                gl.glVertex3d(x, -0.5, 0.51);
                                gl.glVertex3d(x, 0.5, 0.51);
                                gl.glEnd();
                            }
                            gl.glPopMatrix();
                        }
                        break;
                    default:
                        baseR = 0.5f; baseG = 0.5f; baseB = 0.5f;
                        gl.glColor3f(baseR, baseG, baseB);
                        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.5f,0.5f,0.5f,1.0f}, 0);
                        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 10f);
                        gl.glScaled(w, h, l);
                        glut.glutSolidCube(1.0f);
                        break;
                }
                gl.glPopMatrix();
            }
        }

        // Draw jetpacks in vicinity (actual jetpack shape)
        if (flight != null && nearbyJetpacks != null) {
            double trackedX = flight.getX();
            double trackedY = flight.getY();
            for (var jetpack : nearbyJetpacks) {
                double x = jetpack.getX();
                double y = jetpack.getAltitude();
                double z = jetpack.getY();
                double dist = Math.hypot(x - trackedX, z - trackedY);
                if (dist < 500) {
                    gl.glPushMatrix();
                    gl.glTranslated(x, y, z);
                    // Calculate direction angle using FlightMovementController
                    double angleRad = 0;
                    try {
                        angleRad = jetpack.getDirectionAngle();
                    } catch (Exception e) {
                        angleRad = 0;
                    }
                    // Apply offset so jetpack model faces correct direction
                    gl.glRotated(Math.toDegrees(angleRad) + JETPACK_DIRECTION_OFFSET_DEGREES, 0, 1, 0);
                    // Jetpack colors
                    float[] bodyColor = jetpack == flight ? new float[]{0, 1, 0} : new float[]{1, 1, 0};
                    float[] tankColor = {0.75f, 0.75f, 0.8f}; // Metallic silver
                    float[] topColor = {0.15f, 0.15f, 0.15f}; // Dark cap
                    float[] nozzleColor = {0.2f, 0.2f, 0.25f}; // Dark nozzle
                    float[] thrustColor = {1.0f, 0.5f, 0.0f}; // Orange thrust glow
                    float[] strapColor = {0.2f, 0.2f, 0.2f}; // Black straps
                    float[] backplateColor = {0.3f, 0.3f, 0.35f}; // Gray backplate
                    
                    // Draw backplate/harness (behind the tanks)
                    gl.glPushMatrix();
                    gl.glTranslated(0, 5, -1.5);
                    gl.glColor3fv(backplateColor, 0);
                    gl.glScaled(6, 11, 0.8);
                    glut.glutSolidCube(1.0f);
                    gl.glPopMatrix();
                    
                    // Draw shoulder straps
                    gl.glPushMatrix();
                    gl.glTranslated(-2, 9, -1);
                    gl.glRotated(-15, 0, 0, 1);
                    gl.glColor3fv(strapColor, 0);
                    glut.glutSolidCylinder(0.4, 4, 8, 2);
                    gl.glPopMatrix();
                    
                    gl.glPushMatrix();
                    gl.glTranslated(2, 9, -1);
                    gl.glRotated(15, 0, 0, 1);
                    gl.glColor3fv(strapColor, 0);
                    glut.glutSolidCylinder(0.4, 4, 8, 2);
                    gl.glPopMatrix();
                    
                    // Draw left fuel tank with details
                    gl.glPushMatrix();
                    gl.glTranslated(-2.2, 0, 0);
                    // Main tank body
                    gl.glColor3fv(tankColor, 0);
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.9f, 0.9f, 1.0f, 1.0f}, 0);
                    gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 60f);
                    glut.glutSolidCylinder(1.3, 10, 16, 4);
                    // Tank top cap
                    gl.glTranslated(0, 10, 0);
                    gl.glColor3fv(topColor, 0);
                    glut.glutSolidSphere(1.4, 12, 10);
                    // Exhaust nozzle at bottom
                    gl.glTranslated(0, -10, 0);
                    gl.glPushMatrix();
                    gl.glRotated(180, 1, 0, 0);
                    gl.glColor3fv(nozzleColor, 0);
                    glut.glutSolidCone(1.5, 2.5, 12, 4);
                    // Thrust glow effect
                    gl.glTranslated(0, 0, 2.5);
                    gl.glColor4f(thrustColor[0], thrustColor[1], thrustColor[2], 0.6f);
                    glut.glutSolidCone(1.2, 1.5, 8, 2);
                    gl.glPopMatrix();
                    // Tank straps
                    for (int i = 0; i < 3; i++) {
                        gl.glPushMatrix();
                        gl.glTranslated(0, 2 + i * 3, 0);
                        gl.glColor3fv(strapColor, 0);
                        glut.glutSolidTorus(0.15, 1.4, 8, 12);
                        gl.glPopMatrix();
                    }
                    gl.glPopMatrix();
                    
                    // Draw right fuel tank with details (mirror of left)
                    gl.glPushMatrix();
                    gl.glTranslated(2.2, 0, 0);
                    // Main tank body
                    gl.glColor3fv(tankColor, 0);
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.9f, 0.9f, 1.0f, 1.0f}, 0);
                    gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 60f);
                    glut.glutSolidCylinder(1.3, 10, 16, 4);
                    // Tank top cap
                    gl.glTranslated(0, 10, 0);
                    gl.glColor3fv(topColor, 0);
                    glut.glutSolidSphere(1.4, 12, 10);
                    // Exhaust nozzle at bottom
                    gl.glTranslated(0, -10, 0);
                    gl.glPushMatrix();
                    gl.glRotated(180, 1, 0, 0);
                    gl.glColor3fv(nozzleColor, 0);
                    glut.glutSolidCone(1.5, 2.5, 12, 4);
                    // Thrust glow effect
                    gl.glTranslated(0, 0, 2.5);
                    gl.glColor4f(thrustColor[0], thrustColor[1], thrustColor[2], 0.6f);
                    glut.glutSolidCone(1.2, 1.5, 8, 2);
                    gl.glPopMatrix();
                    // Tank straps
                    for (int i = 0; i < 3; i++) {
                        gl.glPushMatrix();
                        gl.glTranslated(0, 2 + i * 3, 0);
                        gl.glColor3fv(strapColor, 0);
                        glut.glutSolidTorus(0.15, 1.4, 8, 12);
                        gl.glPopMatrix();
                    }
                    gl.glPopMatrix();
                    
                    // Draw central control unit
                    gl.glPushMatrix();
                    gl.glTranslated(0, 5, 0);
                    gl.glColor3fv(bodyColor, 0);
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.6f, 0.8f, 0.4f, 1.0f}, 0);
                    gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 40f);
                    glut.glutSolidCube(2.5f);
                    // Control panel indicator lights
                    gl.glTranslated(0, 0, 1.3);
                    gl.glColor3f(0.0f, 1.0f, 0.0f);
                    glut.glutSolidSphere(0.2, 8, 8);
                    gl.glTranslated(0.5, 0, 0);
                    gl.glColor3f(1.0f, 0.0f, 0.0f);
                    glut.glutSolidSphere(0.2, 8, 8);
                    gl.glTranslated(-1.0, 0, 0);
                    gl.glColor3f(0.0f, 0.0f, 1.0f);
                    glut.glutSolidSphere(0.2, 8, 8);
                    gl.glPopMatrix();
                    
                    // Draw stabilizer fins
                    gl.glPushMatrix();
                    gl.glTranslated(0, 8, 0);
                    gl.glColor3fv(topColor, 0);
                    // Left fin
                    gl.glPushMatrix();
                    gl.glTranslated(-3.5, 0, 0);
                    gl.glRotated(90, 0, 1, 0);
                    gl.glScaled(1.5, 2.5, 0.2);
                    glut.glutSolidCube(1.0f);
                    gl.glPopMatrix();
                    // Right fin
                    gl.glPushMatrix();
                    gl.glTranslated(3.5, 0, 0);
                    gl.glRotated(90, 0, 1, 0);
                    gl.glScaled(1.5, 2.5, 0.2);
                    glut.glutSolidCube(1.0f);
                    gl.glPopMatrix();
                    gl.glPopMatrix();
                    
                    // Draw connecting crossbar between tanks
                    gl.glPushMatrix();
                    gl.glTranslated(0, 6, 0);
                    gl.glRotated(90, 0, 0, 1);
                    gl.glColor3fv(strapColor, 0);
                    glut.glutSolidCylinder(0.3, 4.4, 8, 2);
                    gl.glPopMatrix();
                    // Draw callsign label above each jetpack
                    gl.glColor3f(1, 1, 1);
                    gl.glRasterPos3f(0, 15, 0);
                    String callsign = jetpack.getJetpack() != null ? jetpack.getJetpack().getCallsign() : "JP";
                    for (char c : callsign.toCharArray()) {
                        glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_12, c);
                    }
                    gl.glPopMatrix();
                }
            }
        }
        // --- Time zone-based shading overlay (like 2D map) ---
        if (cityName != null && !cityName.isEmpty()) {
            java.time.ZoneId zoneId = com.example.utility.timezone.TimezoneHelper.getTimezoneForCity(cityName);
            java.time.LocalDateTime now = java.time.LocalDateTime.now(zoneId);
            int hour = now.getHour();
            float[] shadingColor;
            float alpha;
            if (hour >= 6 && hour < 7) {
                // Dawn
                shadingColor = new float[]{1.0f, 0.55f, 0.0f}; alpha = 0.12f;
            } else if (hour >= 7 && hour < 17) {
                // Daytime
                shadingColor = new float[]{1.0f, 1.0f, 0.78f}; alpha = 0.04f;
            } else if (hour >= 17 && hour < 18) {
                // Dusk
                shadingColor = new float[]{1.0f, 0.27f, 0.0f}; alpha = 0.16f;
            } else if (hour >= 18 && hour < 20) {
                // Evening
                shadingColor = new float[]{0.1f, 0.1f, 0.44f}; alpha = 0.24f;
            } else if (hour >= 20 || hour < 6) {
                // Night
                shadingColor = new float[]{0.0f, 0.0f, 0.2f}; alpha = 0.32f;
            } else {
                shadingColor = new float[]{1.0f, 1.0f, 1.0f}; alpha = 0.0f;
            }
            if (alpha > 0.01f) {
                gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_COLOR_BUFFER_BIT);
                gl.glDisable(GL2.GL_LIGHTING);
                gl.glEnable(GL2.GL_BLEND);
                gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
                gl.glMatrixMode(GL2.GL_PROJECTION);
                gl.glPushMatrix();
                gl.glLoadIdentity();
                gl.glOrtho(0, 1, 0, 1, -1, 1);
                gl.glMatrixMode(GL2.GL_MODELVIEW);
                gl.glPushMatrix();
                gl.glLoadIdentity();
                gl.glColor4f(shadingColor[0], shadingColor[1], shadingColor[2], alpha);
                gl.glBegin(GL2.GL_QUADS);
                gl.glVertex2f(0, 0);
                gl.glVertex2f(1, 0);
                gl.glVertex2f(1, 1);
                gl.glVertex2f(0, 1);
                gl.glEnd();
                gl.glPopMatrix();
                gl.glMatrixMode(GL2.GL_PROJECTION);
                gl.glPopMatrix();
                gl.glMatrixMode(GL2.GL_MODELVIEW);
                gl.glPopAttrib();
            }
        }
    }
    private GLU glu;
    private JetPackFlight flight;
    private CityModel3D cityModel;
    // Only render visible jetpacks (set by panel)
    private List<JetPackFlight> nearbyJetpacks;
    // Removed unused fields: accidents, flightStates, cameraAzimuth, cameraElevation, cameraDistance

    private static final double FOV = 60.0;
    private static final double NEAR_PLANE = 1.0;
    private static final double FAR_PLANE = 2000.0;

    private String cityName = "";

    public JOGLRenderer3D() {
        this.glu = new GLU();
        this.nearbyJetpacks = new ArrayList<>();
        this.accidents = new ArrayList<>();
    }

    /**
     * Set camera parameters from panel
     */
    public void setCamera(double azimuth, double elevation, double distance) {
        this.cameraAzimuth = azimuth;
        this.cameraElevation = elevation;
        this.cameraDistance = distance;
    }

    /**
     * Update rendering data
     */
    public void updateData(JetPackFlight flight, CityModel3D cityModel,
                          List<JetPackFlight> nearbyJetpacks,
                          List<Accident> accidents,
                          Map<JetPackFlight, JetPackFlightState> flightStates) {
        this.flight = flight;
        this.cityModel = cityModel;
        this.nearbyJetpacks = nearbyJetpacks != null ? nearbyJetpacks : new ArrayList<>();
        this.accidents = accidents != null ? accidents : new ArrayList<>();
        this.flightStates = flightStates;
        // ...existing code...
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if (height == 0) height = 1;
        float aspect = (float) width / height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(FOV, aspect, NEAR_PLANE, FAR_PLANE);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    // ...existing code...

}
