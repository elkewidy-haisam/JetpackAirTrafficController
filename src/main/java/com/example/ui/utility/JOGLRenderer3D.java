package com.example.ui.utility;

    import com.example.accident.AccidentAlert.Accident;
    import com.example.flight.JetPackFlight;
    import com.example.flight.JetPackFlightState;
    import com.example.model.CityModel3D;
    import com.jogamp.opengl.GL2;
    import com.jogamp.opengl.GL;
    import com.jogamp.opengl.GLAutoDrawable;
    import com.jogamp.opengl.GLEventListener;
    import com.jogamp.opengl.glu.GLU;
    import com.jogamp.opengl.util.gl2.GLUT;
    import java.util.List;
    import java.util.ArrayList;
    import java.util.Map;
    import java.awt.Point;

/**
 * JOGLRenderer3D - Hardware-accelerated 3D rendering using JOGL/OpenGL
 * Drop-in replacement for Renderer3D with the same interface but OpenGL backend
 */
public class JOGLRenderer3D implements GLEventListener {
            private final GLUT glut = new GLUT();
        // Mouse camera controls
        private double mouseAzimuth = 45;
        private double mouseElevation = 30;
        private double mouseDistance = 120;
        private boolean mouseControlEnabled = true;
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
        // TODO: Initialize OpenGL state if needed
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
            double dx = 0, dz = 0;
            Point dest = flight.getDestination();
            if (dest != null) {
                dx = dest.x - jetX;
                dz = dest.y - jetZ;
                double len = Math.sqrt(dx*dx + dz*dz);
                if (len > 0) { dx /= len; dz /= len; }
            }
            double camDist = 80;
            double camHeight = 40;
            double eyeX = jetX - dx * camDist;
            double eyeY = Math.max(jetY + camHeight, 5); // Prevent below ground
            double eyeZ = jetZ - dz * camDist;
            double lookX = jetX + dx * 30;
            double lookY = jetY;
            double lookZ = jetZ + dz * 30;
            glu.gluLookAt(eyeX, eyeY, eyeZ, lookX, lookY, lookZ, 0, 1, 0);
        } else {
            double centerX = cityModel != null ? cityModel.getMapWidth() / 2.0 : 0;
            double centerY = 0;
            double centerZ = cityModel != null ? cityModel.getMapHeight() / 2.0 : 0;
            double eyeX = centerX + cameraDistance * Math.cos(Math.toRadians(cameraAzimuth)) * Math.cos(Math.toRadians(cameraElevation));
            double eyeY = Math.max(centerY + cameraDistance * Math.sin(Math.toRadians(cameraElevation)), 5);
            double eyeZ = centerZ + cameraDistance * Math.sin(Math.toRadians(cameraAzimuth)) * Math.cos(Math.toRadians(cameraElevation));
            glu.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, 0, 1, 0);
        }

        // Draw ground plane (grass/land)
        if (cityModel != null) {
            gl.glColor3f(0.4f, 0.7f, 0.3f); // grass green
            gl.glPushMatrix();
            gl.glTranslated(cityModel.getMapWidth()/2, 0, cityModel.getMapHeight()/2);
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
                float baseR = 0.6f, baseG = 0.6f, baseB = 0.7f;
                gl.glPushMatrix();
                gl.glTranslated(building.getX(), building.getHeight() / 2, building.getY());
                // Skyscraper: tall, with spire and windows
                if ("skyscraper".equals(building.getType())) {
                    baseR = 0.7f + (float)Math.random()*0.1f;
                    baseG = 0.7f + (float)Math.random()*0.1f;
                    baseB = 0.8f + (float)Math.random()*0.1f;
                    gl.glColor3f(baseR, baseG, baseB);
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.9f,0.9f,1.0f,1.0f}, 0);
                    gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 80f);
                    gl.glScaled(building.getWidth(), building.getHeight(), building.getLength());
                    glut.glutSolidCube(1.0f);
                    // Spire
                    gl.glPushMatrix();
                    gl.glTranslated(0, 0.6, 0);
                    gl.glScaled(0.2, 0.5, 0.2);
                    gl.glColor3f(0.9f, 0.9f, 0.9f);
                    glut.glutSolidCone(1, 2, 8, 2);
                    gl.glPopMatrix();
                } else if ("office".equals(building.getType())) {
                    baseR = 0.5f + (float)Math.random()*0.2f;
                    baseG = 0.6f + (float)Math.random()*0.2f;
                    baseB = 0.7f + (float)Math.random()*0.1f;
                    gl.glColor3f(baseR, baseG, baseB);
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.7f,0.7f,0.7f,1.0f}, 0);
                    gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 40f);
                    gl.glScaled(building.getWidth(), building.getHeight(), building.getLength());
                    glut.glutSolidCube(1.0f);
                } else if ("residential".equals(building.getType())) {
                    baseR = 0.8f + (float)Math.random()*0.1f;
                    baseG = 0.7f + (float)Math.random()*0.1f;
                    baseB = 0.6f + (float)Math.random()*0.1f;
                    gl.glColor3f(baseR, baseG, baseB);
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.8f,0.7f,0.6f,1.0f}, 0);
                    gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 20f);
                    gl.glScaled(building.getWidth(), building.getHeight(), building.getLength());
                    glut.glutSolidCube(1.0f);
                    // Roof
                    gl.glPushMatrix();
                    gl.glTranslated(0, 0.5, 0);
                    gl.glScaled(0.8, 0.4, 0.8);
                    gl.glColor3f(baseR*0.8f, baseG*0.8f, baseB*0.8f);
                    glut.glutSolidCone(0.7, 1.2, 8, 2);
                    gl.glPopMatrix();
                } else if ("commercial".equals(building.getType())) {
                    baseR = 0.7f + (float)Math.random()*0.1f;
                    baseG = 0.6f + (float)Math.random()*0.1f;
                    baseB = 0.3f + (float)Math.random()*0.1f;
                    gl.glColor3f(baseR, baseG, baseB);
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.9f,0.8f,0.5f,1.0f}, 0);
                    gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 30f);
                    gl.glScaled(building.getWidth(), building.getHeight(), building.getLength());
                    glut.glutSolidCube(1.0f);
                } else {
                    baseR = 0.5f + (float)Math.random()*0.1f;
                    baseG = 0.5f + (float)Math.random()*0.1f;
                    baseB = 0.5f + (float)Math.random()*0.1f;
                    gl.glColor3f(baseR, baseG, baseB);
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.5f,0.5f,0.5f,1.0f}, 0);
                    gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 10f);
                    gl.glScaled(building.getWidth(), building.getHeight(), building.getLength());
                    glut.glutSolidCube(1.0f);
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
                    // Jetpack colors
                    float[] bodyColor = jetpack == flight ? new float[]{0, 1, 0} : new float[]{1, 1, 0};
                    float[] tankColor = {0.7f, 0.7f, 0.7f};
                    float[] topColor = {0.2f, 0.2f, 0.2f};
                    // Draw left tank
                    gl.glPushMatrix();
                    gl.glTranslated(-2.5, 0, 0);
                    gl.glColor3fv(tankColor, 0);
                    glut.glutSolidCylinder(1.2, 10, 12, 2);
                    gl.glTranslated(0, 10, 0);
                    gl.glColor3fv(topColor, 0);
                    glut.glutSolidSphere(1.3, 10, 8);
                    gl.glPopMatrix();
                    // Draw right tank
                    gl.glPushMatrix();
                    gl.glTranslated(2.5, 0, 0);
                    gl.glColor3fv(tankColor, 0);
                    glut.glutSolidCylinder(1.2, 10, 12, 2);
                    gl.glTranslated(0, 10, 0);
                    gl.glColor3fv(topColor, 0);
                    glut.glutSolidSphere(1.3, 10, 8);
                    gl.glPopMatrix();
                    // Draw central body
                    gl.glPushMatrix();
                    gl.glColor3fv(bodyColor, 0);
                    glut.glutSolidCylinder(2, 12, 16, 2);
                    gl.glTranslated(0, 12, 0);
                    gl.glColor3fv(topColor, 0);
                    glut.glutSolidSphere(2.2, 12, 8);
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
    }
// ...existing code...
    private GLU glu;
    private JetPackFlight flight;
    private CityModel3D cityModel;
    private List<JetPackFlight> nearbyJetpacks;
    private List<Accident> accidents;
    private Map<JetPackFlight, JetPackFlightState> flightStates;

    // Camera parameters (user-controlled)
    private double cameraAzimuth = 45;
    private double cameraElevation = 30;
    private double cameraDistance = 400;

    private static final double FOV = 60.0;
    private static final double NEAR_PLANE = 1.0;
    private static final double FAR_PLANE = 2000.0;

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
