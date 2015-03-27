package com.mrcrayfish.modelcreator;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

import com.mrcrayfish.modelcreator.texture.TextureManager;

public class Face
{
	private String texture = "brick";
	private String textureLocation = "blocks/";
	private double textureX = 0;
	private double textureY = 0;
	private boolean fitTexture = false;
	private boolean binded = false;
	private boolean cullface = false;
	private boolean enabled = true;
	private double rotation;

	private Cuboid cuboid;
	private int side;

	public Face(Cuboid cuboid, int side)
	{
		this.cuboid = cuboid;
		this.side = side;
	}

	public void renderNorth()
	{
		GL11.glPushMatrix();
		{
			startRender();

			GL11.glBegin(GL11.GL_QUADS);
			{
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (-textureX / 16) - (cuboid.getWidth() / 16), fitTexture ? 0 : (-textureY / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), -(cuboid.getStartZ() + cuboid.getDepth()));

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : (-textureX / 16), fitTexture ? 0 : (-textureY / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), -(cuboid.getStartZ() + cuboid.getDepth()));

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (-textureX / 16), fitTexture ? 1 : (-textureY / 16) - (cuboid.getHeight() / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), -(cuboid.getStartZ() + cuboid.getDepth()));

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (-textureX / 16) - (cuboid.getWidth() / 16), fitTexture ? 1 : (-textureY / 16) - (cuboid.getHeight() / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), -(cuboid.getStartZ() + cuboid.getDepth()));
			}
			GL11.glEnd();

			finishRender();
		}
		GL11.glPopMatrix();
	}

	public void renderEast()
	{
		GL11.glPushMatrix();
		{
			startRender();

			GL11.glBegin(GL11.GL_QUADS);
			{
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16) - (cuboid.getDepth() / 16), fitTexture ? 0 : (-textureY / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), -cuboid.getStartZ());
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : (textureX / 16), fitTexture ? 0 : (-textureY / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), -(cuboid.getStartZ() + cuboid.getDepth()));
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16), fitTexture ? 1 : (-textureY / 16) - (cuboid.getHeight() / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), -(cuboid.getStartZ() + cuboid.getDepth()));
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16) - (cuboid.getDepth() / 16), fitTexture ? 1 : (-textureY / 16) - (cuboid.getHeight() / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), -cuboid.getStartZ());
			}
			GL11.glEnd();

			finishRender();
		}
		GL11.glPopMatrix();
	}

	public void renderSouth()
	{
		GL11.glPushMatrix();
		{
			startRender();

			GL11.glBegin(GL11.GL_QUADS);
			{
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : (textureX / 16) - (cuboid.getWidth() / 16), fitTexture ? 0 : (-textureY / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), -cuboid.getStartZ());
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16), fitTexture ? 0 : (-textureY / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), -cuboid.getStartZ());
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16), fitTexture ? 1 : (-textureY / 16) - (cuboid.getHeight() / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), -cuboid.getStartZ());
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16) - (cuboid.getWidth() / 16), fitTexture ? 1 : (-textureY / 16) - (cuboid.getHeight() / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), -cuboid.getStartZ());
			}
			GL11.glEnd();

			finishRender();
		}
		GL11.glPopMatrix();
	}

	public void renderWest()
	{
		GL11.glPushMatrix();
		{
			startRender();

			GL11.glBegin(GL11.GL_QUADS);
			{
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16) - (cuboid.getDepth() / 16), fitTexture ? 0 : (-textureY / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), -(cuboid.getStartZ() + cuboid.getDepth()));

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : (textureX / 16), fitTexture ? 0 : (-textureY / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), -cuboid.getStartZ());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16), fitTexture ? 1 : (-textureY / 16) - (cuboid.getHeight() / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), -cuboid.getStartZ());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16) - (cuboid.getDepth() / 16), fitTexture ? 1 : (-textureY / 16) - (cuboid.getHeight() / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), -(cuboid.getStartZ() + cuboid.getDepth()));
			}
			GL11.glEnd();

			finishRender();
		}
		GL11.glPopMatrix();
	}

	public void renderUp()
	{
		GL11.glPushMatrix();
		{
			startRender();

			GL11.glBegin(GL11.GL_QUADS);
			{
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16) - (cuboid.getWidth() / 16), fitTexture ? 0 : (-textureY / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), -cuboid.getStartZ());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : (textureX / 16), fitTexture ? 0 : (-textureY / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), -cuboid.getStartZ());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16), fitTexture ? 1 : (-textureY / 16) - (cuboid.getDepth() / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), -(cuboid.getStartZ() + cuboid.getDepth()));

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16) - (cuboid.getWidth() / 16), fitTexture ? 1 : (-textureY / 16) - (cuboid.getDepth() / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), -(cuboid.getStartZ() + cuboid.getDepth()));
			}
			GL11.glEnd();

			finishRender();
		}
		GL11.glPopMatrix();
	}

	public void renderDown()
	{
		GL11.glPushMatrix();
		{
			startRender();

			GL11.glBegin(GL11.GL_QUADS);
			{
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16) - (cuboid.getWidth() / 16), fitTexture ? 0 : (-textureY / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), -(cuboid.getStartZ() + cuboid.getDepth()));
				
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : (textureX / 16), fitTexture ? 0 : (-textureY / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), -(cuboid.getStartZ() + cuboid.getDepth()));
				
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16), fitTexture ? 1 : (-textureY / 16) - (cuboid.getDepth() / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), -cuboid.getStartZ());
				
				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureX / 16) - (cuboid.getWidth() / 16), fitTexture ? 1 : (-textureY / 16) - (cuboid.getDepth() / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), -cuboid.getStartZ());
			}
			GL11.glEnd();

			finishRender();
		}
		GL11.glPopMatrix();
	}

	public void startRender()
	{
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		bindTexture();
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		GL11.glRotated(getRotation(), 0, 0, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public void finishRender()
	{
		GL11.glDisable(GL_TEXTURE_2D);
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

	public String getTextureLocation()
	{
		return textureLocation;
	}

	public void setTextureLocation(String textureLocation)
	{
		this.textureLocation = textureLocation;
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
		switch (face)
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
			return "up";
		case 5:
			return "down";
		}
		return null;
	}

	public double getRotation()
	{
		return rotation;
	}

	public void setRotation(double rotation)
	{
		this.rotation = rotation;
	}
}
