package com.mrcrayfish.modelcreator;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

import com.mrcrayfish.modelcreator.texture.TextureManager;

public class Face
{
	private String texture = null;
	private String textureModId = null;
	private double textureX = 0;
	private double textureY = 0;
	private boolean fitTexture = false;
	private boolean binded = false;
	private boolean cullface = false;
	private boolean enabled = true;

	private Cuboid cuboid;
	private int side;

	public Face(Cuboid cuboid, int side)
	{
		this.cuboid = cuboid;
		this.side = side;
	}

	public void render(double startX, double startY, double startZ, double endX, double endY, double endZ, double cubeW, double cubeH)
	{
		startZ = -startZ;
		endZ = -endZ;
		
		GL11.glPushMatrix();
		{
			bindTexture();
			GL11.glBegin(GL11.GL_QUADS);
			{
				// Top Right
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : 1 - (textureX / 16), fitTexture ? 1 : 1 - (textureY / 16));
				GL11.glVertex3d(startX, startY, startZ);
				// Bottom Right
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : 1 - (textureX / 16), fitTexture ? 0 : 1 - ((textureY / 16) + (cubeH / 16)));
				GL11.glVertex3d(startX, startY != endY ? endY : startY, startY != endY ? startZ : endZ);

				// Bottom Left
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : 1 - ((textureX / 16) + (cubeW / 16)), fitTexture ? 0 : 1 - ((textureY / 16) + (cubeH / 16)));
				GL11.glVertex3d(endX, endY, endZ);

				// Top Left
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : 1 - ((textureX / 16) + (cubeW / 16)), fitTexture ? 1 : 1 - (textureY / 16));
				GL11.glVertex3d(endX, startY != endY ? startY : endY, startY != endY ? endZ : startZ);
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();
	}

	public void setTexture(String texture)
	{
		this.texture = texture;
	}

	public void bindTexture()
	{
		TextureImpl.bindNone();
		if (texture != null)
		{
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			TextureManager.getTexture(texture).bind();
			binded = true;
		}
	}

	public void addTextureX(double amt)
	{
		this.textureX += amt;
	}

	public void addTextureY(double amt)
	{
		this.textureY += amt;
	}

	public double getStartU()
	{
		return textureX;
	}

	public double getStartV()
	{
		return textureY;
	}

	public double getEndU()
	{
		return textureX + cuboid.getFaceDimension(side).getWidth();
	}

	public double getEndV()
	{
		return textureY + cuboid.getFaceDimension(side).getHeight();
	}

	public String getTextureName()
	{
		return texture;
	}
	
	public Texture getTexture()
	{
		return TextureManager.getTexture(texture);
	}

	public String getTextureModId()
	{
		return textureModId;
	}

	public void setTextureModId(String textureModId)
	{
		this.textureModId = textureModId;
	}

	public void fitTexture(boolean fitTexture)
	{
		this.fitTexture = fitTexture;
	}

	public boolean shouldFitTexture()
	{
		return fitTexture;
	}

	public int getSide()
	{
		return side;
	}
	
	public boolean isCullfaced()
	{
		return cullface;
	}

	public void setCullface(boolean cullface)
	{
		this.cullface = cullface;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public static String getFaceName(int face)
	{
		switch(face)
		{
		case 0:
			return "north";
		case 1:
			return "east";
		case 2:
			return "south";
		case 3:
			return "west";
		case 4:
			return "down";
		case 5:
			return "up";
		}
		return null;
	}
}
