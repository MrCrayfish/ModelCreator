package com.mrcrayfish.modelcreator;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glTexParameterf;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.opengl.Texture;

public class Cube {
	public String name;

	private double startX, startY, startZ;
	private double width, height, depth;

	private double textureX;
	private double textureY;
	private double textureZ;

	private Sphere sphere = new Sphere();

	public Cube(double width, double height, double depth) {
		this.name = "Cube";
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	public void initTexture() {

	}

	public void draw(Texture texture) {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		// Front
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(0, 0);
			GL11.glVertex3d(startX, startY, -startZ);
			GL11.glTexCoord2d(0, height / 16);
			GL11.glVertex3d(startX, startY + height, -startZ);
			GL11.glTexCoord2d(width / 16, height / 16);
			GL11.glVertex3d(startX + width, startY + height, -startZ);
			GL11.glTexCoord2d(width / 16, 0);
			GL11.glVertex3d(startX + width, startY, -startZ);
		}
		GL11.glEnd();

		// Back
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex3d(startX, startY, -(startZ + depth));
			GL11.glVertex3d(startX, startY + height, -(startZ + depth));
			GL11.glVertex3d(startX + width, startY + height, -(startZ + depth));
			GL11.glVertex3d(startX + width, startY, -(startZ + depth));
		}
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(0, 0, 1);
			GL11.glVertex3d(startX, startY, -startZ);
			GL11.glVertex3d(startX, startY + height, -startZ);
			GL11.glVertex3d(startX, startY + height, -(startZ + depth));
			GL11.glVertex3d(startX, startY, -(startZ + depth));
		}
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(1, 1, 0);
			GL11.glVertex3d(startX + width, startY, -startZ);
			GL11.glVertex3d(startX + width, startY + height, -startZ);
			GL11.glVertex3d(startX + width, startY + height, -(startZ + depth));
			GL11.glVertex3d(startX + width, startY, -(startZ + depth));
		}
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(1, 0, 1);
			GL11.glVertex3d(startX, startY, -startZ);
			GL11.glVertex3d(startX, startY, -(startZ + depth));
			GL11.glVertex3d(startX + width, startY, -(startZ + depth));
			GL11.glVertex3d(startX + width, startY, -startZ);
		}
		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(0, 1, 1);
			GL11.glVertex3d(startX, startY + height, -startZ);
			GL11.glVertex3d(startX, startY + height, -(startZ + depth));
			GL11.glVertex3d(startX + width, startY + height, -(startZ + depth));
			GL11.glVertex3d(startX + width, startY + height, -startZ);
		}
		GL11.glEnd();
	}

	public void drawExtras() {
		GL11.glTranslated(startX, startY, -startZ);
		sphere.draw(0.2F, 16, 16);
	}

	public void addStartX(double amt) {
		this.startX += amt;
	}

	public void addStartY(double amt) {
		this.startY += amt;
	}

	public void addStartZ(double amt) {
		this.startZ += amt;
	}

	public double getStartX() {
		return startX;
	}

	public double getStartY() {
		return startY;
	}

	public double getStartZ() {
		return startZ;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getDepth() {
		return depth;
	}

	public void addWidth(double amt) {
		this.width += amt;
	}

	public void addHeight(double amt) {
		this.height += amt;
	}

	public void addDepth(double amt) {
		this.depth += amt;
	}

	@Override
	public String toString() {
		return name;
	}
}
