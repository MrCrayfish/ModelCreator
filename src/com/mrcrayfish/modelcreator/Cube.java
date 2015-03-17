package com.mrcrayfish.modelcreator;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class Cube
{
	private double startX, startY, startZ;
	private double width, height, depth;

	private String name = "Cube";
	private Face[] faces = new Face[6];
	private Sphere sphere = new Sphere();

	public Cube(double width, double height, double depth)
	{
		this.width = width;
		this.height = height;
		this.depth = depth;
		initFaces();
	}

	public void initFaces()
	{
		for (int i = 0; i < faces.length; i++)
			faces[i] = new Face();
		faces[1].setTexture(TextureManager.dirt);
	}
	
	public Face getSelectedFace()
	{
		return null;
	}

	public void draw()
	{
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		GL11.glColor3f(1,0,0);
		faces[0].render(startX, startY, -startZ, startX + width, startY + height, -startZ, width, height);
		GL11.glColor3f(0,1,0);
		faces[1].render(startX, startY, -(startZ + depth), startX + width, startY + height, -(startZ + depth), width, height);
		GL11.glColor3f(0,0,1);
		faces[2].render(startX, startY, -startZ, startX, startY + height, -(startZ + depth), depth, height);
		GL11.glColor3f(1,1,0);
		faces[3].render(startX + width, startY, -startZ, startX + width, startY + height, -(startZ + depth), depth, height);
		GL11.glColor3f(1,0,1);
		faces[4].render(startX, startY, -startZ, startX + width, startY, -(startZ + depth), width, depth);
		GL11.glColor3f(0,1,1);
		faces[5].render(startX, startY + height, -startZ, startX + width, startY + height, -(startZ + depth), width, depth);
		GL11.glDisable(GL_TEXTURE_2D);
	}

	public void drawExtras()
	{
		GL11.glPushMatrix();
		{
			GL11.glTranslated(startX, startY, -startZ);
			sphere.draw(0.2F, 16, 16);
		}
		GL11.glPopMatrix();
	}

	public void addStartX(double amt)
	{
		this.startX += amt;
	}

	public void addStartY(double amt)
	{
		this.startY += amt;
	}

	public void addStartZ(double amt)
	{
		this.startZ += amt;
	}

	public double getStartX()
	{
		return startX;
	}

	public double getStartY()
	{
		return startY;
	}

	public double getStartZ()
	{
		return startZ;
	}

	public double getWidth()
	{
		return width;
	}

	public double getHeight()
	{
		return height;
	}

	public double getDepth()
	{
		return depth;
	}

	public void addWidth(double amt)
	{
		this.width += amt;
	}

	public void addHeight(double amt)
	{
		this.height += amt;
	}

	public void addDepth(double amt)
	{
		this.depth += amt;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
