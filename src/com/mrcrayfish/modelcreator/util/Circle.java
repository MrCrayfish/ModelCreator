package com.mrcrayfish.modelcreator.util;

import org.lwjgl.opengl.GL11;

public class Circle
{
	public static void draw(float cx, float cy, float r, int num_segments, int axis)
	{
		double theta = 2 * 3.1415926 / num_segments;
		double c = Math.cos(theta);// precalculate the sine and cosine
		double s = Math.sin(theta);

		double t;
		double x = r;// we start at angle = 0
		double y = 0;

		GL11.glBegin(GL11.GL_LINE_LOOP);
		for (int ii = 0; ii < num_segments; ii++)
		{
			switch (axis)
			{
			case 0:
				GL11.glVertex3d(x + cx, y + cy, 0);
				break;
			case 1:
				GL11.glVertex3d(x + cx, 0, y + cy);
				break;
			case 2:
				GL11.glVertex3d(0, x + cx, y + cy);
				break;
			}
			// output vertex

			// apply the rotation matrix
			t = x;
			x = c * x - s * y;
			y = s * t + c * y;
		}
		GL11.glEnd();
	}
}
