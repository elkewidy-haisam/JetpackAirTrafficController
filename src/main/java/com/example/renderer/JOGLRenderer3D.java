/**
 * JOGLRenderer3D.java
 * by Haisam Elkewidy
 *
 * 3D renderer for city models using JOGL. Renders water, roads, bridges, buildings, and houses in OpenGL.
 */

package com.example.renderer;

import com.example.model.Bridge3D;
import com.example.model.Building3D;
import com.example.model.CityModel3D;
import com.example.model.House3D;
import com.example.model.Road3D;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.gl2.GLUT;
public class JOGLRenderer3D {
    private final CityModel3D cityModel;
    private final GLUT glut;

    public JOGLRenderer3D(CityModel3D cityModel) {
        this.cityModel = cityModel;
        this.glut = new GLUT();
    }

    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        // ...existing code...
        // Draw city features in detail
        renderCityFeatures(gl);
        // ...existing code...
    }

    private void renderCityFeatures(GL2 gl) {
        // Draw water areas
        for (int x = 0; x < cityModel.getMapWidth(); x += 8) {
            for (int y = 0; y < cityModel.getMapHeight(); y += 8) {
                int rgb = cityModel.getMapImage().getRGB(x, y);
                if ((rgb & 0xFFFFFF) == 0x2040E0 || (rgb & 0xFFFFFF) == 0x2080E0 || (rgb & 0xFFFFFF) == 0x2090E0 || (rgb & 0xFFFFFF) == 0x2070E0) {
                    gl.glColor3f(0.13f, 0.25f, 0.88f);
                    gl.glBegin(GL2.GL_QUADS);
                    gl.glVertex3f(x, 0, y);
                    gl.glVertex3f(x + 8, 0, y);
                    gl.glVertex3f(x + 8, 0, y + 8);
                    gl.glVertex3f(x, 0, y + 8);
                    gl.glEnd();
                }
            }
        }
        // Draw roads
        for (Road3D road : cityModel.getRoads()) {
            gl.glColor3f(0.3f, 0.3f, 0.3f);
            gl.glPushMatrix();
            gl.glTranslatef((float) road.getX(), 0.1f, (float) road.getY());
            gl.glScalef((float) road.getLength(), 1.0f, (float) road.getWidth());
            glut.glutSolidCube(1.0f);
            gl.glPopMatrix();
        }
        // Draw bridges
        for (Bridge3D bridge : cityModel.getBridges()) {
            gl.glColor3f(0.7f, 0.7f, 0.5f);
            gl.glPushMatrix();
            gl.glTranslatef((float) bridge.getX(), 0.2f, (float) bridge.getY());
            gl.glScalef((float) bridge.getLength(), 1.0f, (float) bridge.getWidth());
            glut.glutSolidCube(1.0f);
            gl.glPopMatrix();
        }
        // Draw buildings
        for (Building3D b : cityModel.getBuildings()) {
            if (b.getType().equals("skyscraper")) gl.glColor3f(0.7f, 0.7f, 0.8f);
            else if (b.getType().equals("office")) gl.glColor3f(0.5f, 0.6f, 0.7f);
            else if (b.getType().equals("residential")) gl.glColor3f(0.8f, 0.7f, 0.6f);
            else if (b.getType().equals("commercial")) gl.glColor3f(0.6f, 0.7f, 0.5f);
            else gl.glColor3f(0.7f, 0.7f, 0.7f);
            gl.glPushMatrix();
            gl.glTranslatef((float) b.getX(), (float) b.getHeight() / 2, (float) b.getY());
            gl.glScalef((float) b.getWidth(), (float) b.getHeight(), (float) b.getLength());
            glut.glutSolidCube(1.0f);
            gl.glPopMatrix();
        }
        // Draw houses
        for (House3D h : cityModel.getHouses()) {
            gl.glColor3f(0.9f, 0.8f, 0.7f);
            gl.glPushMatrix();
            gl.glTranslatef((float) h.getX(), (float) h.getHeight() / 2, (float) h.getY());
            gl.glScalef((float) h.getWidth(), (float) h.getHeight(), (float) h.getLength());
            glut.glutSolidCube(1.0f);
            gl.glPopMatrix();
        }
    }
}