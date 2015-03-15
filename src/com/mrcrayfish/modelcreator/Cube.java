package com.mrcrayfish.modelcreator;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;

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

	public void draw()
	{
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL_TEXTURE_2D);
	
		// Front
		if (faces[0] != null)
		{
			faces[0].bindTexture();
		}
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(faces[0].getTextureX(), faces[0].getTextureY());
			GL11.glVertex3d(startX, startY, -startZ);
			GL11.glTexCoord2d(faces[0].getTextureX(), faces[0].getTextureY() + (height / 16));
			GL11.glVertex3d(startX, startY + height, -startZ);
			GL11.glTexCoord2d(faces[0].getTextureX() + (width / 16), faces[0].getTextureY() + (height / 16));
			GL11.glVertex3d(startX + width, startY + height, -startZ);
			GL11.glTexCoord2d(faces[0].getTextureX() + (width / 16), faces[0].getTextureY());
			GL11.glVertex3d(startX + width, startY, -startZ);
		}
		GL11.glEnd();
		
		GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		// Back
		if (faces[1] != null)
		{
			faces[1].bindTexture();
		}
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(faces[0].getTextureX(), faces[0].getTextureY());
			GL11.glVertex3d(startX, startY, -(startZ + depth));
			GL11.glTexCoord2d(faces[0].getTextureX(), faces[0].getTextureY() + (height / 16));
			GL11.glVertex3d(startX, startY + height, -(startZ + depth));
			GL11.glTexCoord2d(faces[0].getTextureX() + (width / 16), faces[0].getTextureY() + (height / 16));
			GL11.glVertex3d(startX + width, startY + height, -(startZ + depth));
			GL11.glTexCoord2d(faces[0].getTextureX() + (width / 16), faces[0].getTextureY());
			GL11.glVertex3d(startX + width, startY, -(startZ + depth));
		}
		GL11.glEnd();

		if (faces[2] != null)
		{
			faces[2].bindTexture();
		}
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(faces[0].getTextureX(), faces[0].getTextureY());
			GL11.glVertex3d(startX, startY, -startZ);
			GL11.glTexCoord2d(faces[0].getTextureX(), faces[0].getTextureY() + (height / 16));
			GL11.glVertex3d(startX, startY + height, -startZ);
			GL11.glTexCoord2d(faces[0].getTextureX() + (depth / 16), faces[0].getTextureY() + (height / 16));
			GL11.glVertex3d(startX, startY + height, -(startZ + depth));
			GL11.glTexCoord2d(faces[0].getTextureX() + (depth / 16), faces[0].getTextureY());
			GL11.glVertex3d(startX, startY, -(startZ + depth));
		}
		GL11.glEnd();

		if (faces[3] != null)
		{
			faces[3].bindTexture();
		}
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(faces[0].getTextureX(), faces[0].getTextureY());
			GL11.glVertex3d(startX + width, startY, -startZ);
			GL11.glTexCoord2d(faces[0].getTextureX(), faces[0].getTextureY() + (height / 16));
			GL11.glVertex3d(startX + width, startY + height, -startZ);
			GL11.glTexCoord2d(faces[0].getTextureX() + (depth / 16), faces[0].getTextureY() + (height / 16));
			GL11.glVertex3d(startX + width, startY + height, -(startZ + depth));
			GL11.glTexCoord2d(faces[0].getTextureX() + (depth / 16), faces[0].getTextureY());
			GL11.glVertex3d(startX + width, startY, -(startZ + depth));
		}
		GL11.glEnd();

		if (faces[4] != null)
		{
			faces[4].bindTexture();
		}
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(faces[0].getTextureX(), faces[0].getTextureY());
			GL11.glVertex3d(startX, startY, -startZ);
			GL11.glTexCoord2d(faces[0].getTextureX(), faces[0].getTextureY() + (depth / 16));
			GL11.glVertex3d(startX, startY, -(startZ + depth));
			GL11.glTexCoord2d(faces[0].getTextureX() + (width / 16), faces[0].getTextureY() + (depth / 16));
			GL11.glVertex3d(startX + width, startY, -(startZ + depth));
			GL11.glTexCoord2d(faces[0].getTextureX() + (width / 16), faces[0].getTextureY());
			GL11.glVertex3d(startX + width, startY, -startZ);
		}
		GL11.glEnd();

		if (faces[5] != null)
		{
			faces[5].bindTexture();
		}
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(faces[0].getTextureX(), faces[0].getTextureY());
			GL11.glVertex3d(startX, startY + height, -startZ);
			GL11.glTexCoord2d(faces[0].getTextureX(), faces[0].getTextureY() + (depth / 16));
			GL11.glVertex3d(startX, startY + height, -(startZ + depth));
			GL11.glTexCoord2d(faces[0].getTextureX() + (width / 16), faces[0].getTextureY() + (depth / 16));
			GL11.glVertex3d(startX + width, startY + height, -(startZ + depth));
			GL11.glTexCoord2d(faces[0].getTextureX() + (width / 16), faces[0].getTextureY());
			GL11.glVertex3d(startX + width, startY + height, -startZ);
		}
		GL11.glEnd();
		
		GL11.glDisable(GL_TEXTURE_2D);
	}

	public void drawExtras()
	{
		GL11.glTranslated(startX, startY, -startZ);
		sphere.draw(0.2F, 16, 16);
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

	@Override
	public String toString()
	{
		return name;
	}
}
