package com.mrcrayfish.modelcreator.sidebar;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2i;

import org.newdawn.slick.Color;

import com.mrcrayfish.modelcreator.util.FontManager;

public class Sidebar
{
	private String title;

	public Sidebar(String title)
	{
		this.title = title;
	}

	public void draw(int sidebarWidth, int canvasWidth, int canvasHeight, int frameHeight)
	{
		glColor3f(0.866F, 0.866F, 0.894F);
		glBegin(GL_QUADS);
		{
			glVertex2i(0, 0);
			glVertex2i(sidebarWidth, 0);
			glVertex2i(sidebarWidth, canvasHeight);
			glVertex2i(0, canvasHeight);
		}
		glEnd();

		drawTitle();
	}

	private void drawTitle()
	{
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		FontManager.BEBAS_NEUE_20.drawString(5, 5, title, new Color(0.5F, 0.5F, 0.6F));
		glDisable(GL_BLEND);
	}

	public void handleInput(int canvasHeight)
	{

	}
}
