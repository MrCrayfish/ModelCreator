package com.mrcrayfish.modelcreator;

import org.lwjgl.opengl.GL11;

public class Cube extends Shape
{
	private double minX, minY, minZ;
	private double maxX, maxY, maxZ;

	public Cube(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{
		super("Cube");
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	@Override
	public void draw()
	{
		GL11.glColor3f(1, 0, 0);
		// Front
		GL11.glVertex3d(minX, minY, minZ);
		GL11.glVertex3d(minX, maxY, minZ);
		GL11.glVertex3d(maxX, maxY, minZ);
		GL11.glVertex3d(maxX, minY, minZ);

		GL11.glColor3f(0, 1, 0);
		// Back
		GL11.glVertex3d(minX, minY, maxZ);
		GL11.glVertex3d(minX, maxY, maxZ);
		GL11.glVertex3d(maxX, maxY, maxZ);
		GL11.glVertex3d(maxX, minY, maxZ);

		GL11.glColor3f(0, 0, 1);
		// Left
		GL11.glVertex3d(minX, minY, minZ);
		GL11.glVertex3d(minX, maxY, minZ);
		GL11.glVertex3d(minX, maxY, maxZ);
		GL11.glVertex3d(minX, minY, maxZ);

		GL11.glColor3f(1, 1, 0);
		// Right
		GL11.glVertex3d(maxX, minY, minZ);
		GL11.glVertex3d(maxX, maxY, minZ);
		GL11.glVertex3d(maxX, maxY, maxZ);
		GL11.glVertex3d(maxX, minY, maxZ);

		GL11.glColor3f(1, 0, 1);
		// Bottom
		GL11.glVertex3d(minX, minY, minZ);
		GL11.glVertex3d(minX, minY, maxZ);
		GL11.glVertex3d(maxX, minY, maxZ);
		GL11.glVertex3d(maxX, minY, minZ);

		GL11.glColor3f(0, 1, 1);
		// Top
		GL11.glVertex3d(minX, maxY, minZ);
		GL11.glVertex3d(minX, maxY, maxZ);
		GL11.glVertex3d(maxX, maxY, maxZ);
		GL11.glVertex3d(maxX, maxY, minZ);
	}

	public double getMinX()
	{
		return minX;
	}

	public double getMinY()
	{
		return minY;
	}

	public double getMinZ()
	{
		return minZ;
	}

	public double getMaxX()
	{
		return maxX;
	}

	public double getMaxY()
	{
		return maxY;
	}

	public double getMaxZ()
	{
		return maxZ;
	}

	public void addX(double maxX)
	{
		this.maxX += maxX;
	}

	public void addY(double maxY)
	{
		this.maxY += maxY;
	}

	public void addZ(double maxZ)
	{
		this.maxZ += maxZ;
	}
}
