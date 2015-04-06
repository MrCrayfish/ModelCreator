package com.mrcrayfish.modelcreator.element;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

import com.mrcrayfish.modelcreator.texture.TextureManager;

public class Face
{
	private String texture = null;
	private String textureLocation = "blocks/";
	private double textureU = 0;
	private double textureV = 0;
	private double textureUEnd = 16;
	private double textureVEnd = 16;
	private boolean fitTexture = false;
	private boolean binded = false;
	private boolean cullface = false;
	private boolean enabled = true;
	private boolean autoUV = true;
	private double rotation;

	private Element cuboid;
	private int side;

	public Face(Element cuboid, int side)
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
					GL11.glTexCoord2d(fitTexture ? 0 : (textureU / 16), fitTexture ? 1 : (textureVEnd / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), cuboid.getStartZ());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureUEnd / 16), fitTexture ? 1 : (textureVEnd / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), cuboid.getStartZ());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureUEnd / 16), fitTexture ? 0 : (textureV / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : (textureU / 16), fitTexture ? 0 : (textureV / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ());
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
					GL11.glTexCoord2d(fitTexture ? 0 : (textureU / 16), fitTexture ? 1 : (textureVEnd / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), cuboid.getStartZ() + cuboid.getDepth());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureUEnd / 16), fitTexture ? 1 : (textureVEnd / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), cuboid.getStartZ());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureUEnd / 16), fitTexture ? 0 : (textureV / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : (textureU / 16), fitTexture ? 0 : (textureV / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ() + cuboid.getDepth());
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
					GL11.glTexCoord2d(fitTexture ? 0 : (textureU / 16), fitTexture ? 1 : (textureVEnd / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), cuboid.getStartZ() + cuboid.getDepth());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureUEnd / 16), fitTexture ? 1 : (textureVEnd / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), cuboid.getStartZ() + cuboid.getDepth());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureUEnd / 16), fitTexture ? 0 : (textureV / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ() + cuboid.getDepth());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : (textureU / 16), fitTexture ? 0 : (textureV / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ() + cuboid.getDepth());
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
					GL11.glTexCoord2d(fitTexture ? 0 : (textureU / 16), fitTexture ? 1 : (textureVEnd / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), cuboid.getStartZ());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureUEnd / 16), fitTexture ? 1 : (textureVEnd / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), cuboid.getStartZ() + cuboid.getDepth());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureUEnd / 16), fitTexture ? 0 : (textureV / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ() + cuboid.getDepth());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : (textureU / 16), fitTexture ? 0 : (textureV / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ());
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
					GL11.glTexCoord2d(fitTexture ? 0 : (textureU / 16), fitTexture ? 1 : (textureVEnd / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ() + cuboid.getDepth());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureUEnd / 16), fitTexture ? 1 : (textureVEnd / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ() + cuboid.getDepth());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureUEnd / 16), fitTexture ? 0 : (textureV / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : (textureU / 16), fitTexture ? 0 : (textureV / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ());
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
					GL11.glTexCoord2d(fitTexture ? 0 : (textureU / 16), fitTexture ? 1 : (textureVEnd / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), cuboid.getStartZ());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureUEnd / 16), fitTexture ? 1 : (textureVEnd / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), cuboid.getStartZ());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 1 : (textureUEnd / 16), fitTexture ? 0 : (textureV / 16));
				GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), cuboid.getStartZ() + cuboid.getDepth());

				if (binded)
					GL11.glTexCoord2d(fitTexture ? 0 : (textureU / 16), fitTexture ? 0 : (textureV / 16));
				GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), cuboid.getStartZ() + cuboid.getDepth());
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
			if (TextureManager.getTexture(texture) != null)
			{
				GL11.glColor3f(1.0F, 1.0F, 1.0F);
				TextureManager.getTexture(texture).bind();
				binded = true;
			}
		}
	}

	public void addTextureX(double amt)
	{
		this.textureU += amt;
	}

	public void addTextureY(double amt)
	{
		this.textureV += amt;
	}

	public void addTextureXEnd(double amt)
	{
		this.textureUEnd += amt;
	}

	public void addTextureYEnd(double amt)
	{
		this.textureVEnd += amt;
	}

	public double getStartU()
	{
		return textureU;
	}

	public double getStartV()
	{
		return textureV;
	}

	public double getEndU()
	{
		return textureUEnd;
	}

	public double getEndV()
	{
		return textureVEnd;
	}

	public void setStartU(double u)
	{
		textureU = u;
	}

	public void setStartV(double v)
	{
		textureV = v;
	}

	public void setEndU(double ue)
	{
		textureUEnd = ue;
	}

	public void setEndV(double ve)
	{
		textureVEnd = ve;
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

	public boolean isAutoUVEnabled()
	{
		return autoUV;
	}

	public void setAutoUVEnabled(boolean enabled)
	{
		this.autoUV = enabled;
	}

	public boolean isBinded()
	{
		return binded;
	}

	public void updateUV()
	{
		if (autoUV)
		{
			textureUEnd = textureU + cuboid.getFaceDimension(side).getWidth();
			textureVEnd = textureV + cuboid.getFaceDimension(side).getHeight();
		}
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

	public static int getFaceSide(String name)
	{
		switch (name)
		{
		case "north":
			return 0;
		case "east":
			return 1;
		case "south":
			return 2;
		case "west":
			return 3;
		case "up":
			return 4;
		case "down":
			return 5;
		}
		return -1;
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
