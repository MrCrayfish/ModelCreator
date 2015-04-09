package com.mrcrayfish.modelcreator.sidebar;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;

import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.util.FontManager;

public class UVSidebar extends Sidebar
{
	private ElementManager manager;

	private final int LENGTH = 110;

	private final Color WHITE_ALPHA = new Color(0, 0, 0, 0.5F);

	public UVSidebar(String title, ElementManager manager)
	{
		super(title);
		this.manager = manager;
	}

	@Override
	public void draw(int sidebarWidth, int canvasWidth, int canvasHeight, int frameHeight)
	{
		super.draw(sidebarWidth, canvasWidth, canvasHeight, frameHeight);

		glPushMatrix();
		{
			glTranslatef(10, 30, 0);

			int longerCount = 0;

			for (int i = 0; i < 6; i++)
			{
				glPushMatrix();
				{
					if (30 + i * (LENGTH + 10) + (LENGTH + 10) > canvasHeight)
					{
						glTranslatef(20 + LENGTH, (5 - i) * (LENGTH + 10), 0);
					}
					else
					{
						glTranslatef(0, i * (LENGTH + 10), 0);
					}

					Color color = Face.getFaceColour(i);
					glColor3f(color.getRed(), color.getGreen(), color.getBlue());

					Face[] faces = null;
					if (manager.getSelectedCuboid() != null)
						faces = manager.getSelectedCuboid().getAllFaces();

					if (faces != null)
					{
						faces[i].bindTexture();

						glBegin(GL_QUADS);
						{
							if (faces[i].isBinded())
								faces[i].setTexCoord(0);
							glVertex2i(0, LENGTH);

							if (faces[i].isBinded())
								faces[i].setTexCoord(1);
							glVertex2i(LENGTH, LENGTH);

							if (faces[i].isBinded())
								faces[i].setTexCoord(2);
							glVertex2i(LENGTH, 0);

							if (faces[i].isBinded())
								faces[i].setTexCoord(3);
							glVertex2i(0, 0);
						}
						glEnd();

						glColor3f(1, 1, 1);

						glBegin(GL_LINES);
						{
							glVertex2d(faces[i].getStartU(), faces[i].getStartV());
							glVertex2d(faces[i].getStartU(), faces[i].getEndV() - faces[i].getStartV());

							glVertex2d(faces[i].getStartU(), faces[i].getEndV() - faces[i].getStartV());
							glVertex2d(faces[i].getEndU() - faces[i].getStartU(), faces[i].getEndV() - faces[i].getStartV());

							glVertex2d(faces[i].getEndU() - faces[i].getStartU(), faces[i].getEndV() - faces[i].getStartV());
							glVertex2d(faces[i].getEndU() - faces[i].getStartU(), faces[i].getStartV());

							glVertex2d(faces[i].getEndU() - faces[i].getStartU(), faces[i].getStartV());
							glVertex2d(faces[i].getStartU(), faces[i].getStartV());
						}
						glEnd();

						glEnable(GL_BLEND);
						glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
						FontManager.BEBAS_NEUE_20.drawString(5, 5, Face.getFaceName(i), WHITE_ALPHA);
						glDisable(GL_BLEND);
					}
				}
				glPopMatrix();
			}
		}
		glPopMatrix();
	}
}
