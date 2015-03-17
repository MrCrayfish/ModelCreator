package com.mrcrayfish.modelcreator;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

public class Face
{
	private Texture texture = null;
	private double textureX = 0;
	private double textureY = 0;
	private boolean fitTexture = false;
	private boolean binded = false;

	public void render(double startX, double startY, double startZ, double endX, double endY, double endZ, double cubeW, double cubeH)
	{
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

	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}

	public void bindTexture()
	{
		TextureImpl.bindNone();
		if (texture != null)
		{
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			texture.bind();
			binded = true;
		}
	}

	public void releaseTexture()
	{
		texture.release();
	}

	public void addTextureX(double amt)
	{
		this.textureX += amt;
	}

	public void addTextureY(double amt)
	{
		this.textureY += amt;
	}

	public double getTextureX()
	{
		return textureX;
	}

	public double getTextureY()
	{
		return textureY;
	}

	public Texture getTexture()
	{
		return texture;
	}

	public void fitTexture(boolean fitTexture)
	{
		this.fitTexture = fitTexture;
	}

	public boolean shouldFitTexture()
	{
		return fitTexture;
	}
}
