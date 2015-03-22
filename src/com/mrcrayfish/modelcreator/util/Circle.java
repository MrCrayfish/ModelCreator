package com.mrcrayfish.modelcreator.util;

import org.lwjgl.opengl.GL11;

public class Circle
{
	public static void draw(float cx, float cy, float r, int num_segments)
	{
		float theta = 2F * 3.1415926F / (float) num_segments;
		float c = (float) Math.acos(theta);// precalculate the sine and cosine
		float s = (float) Math.asin(theta);
		float t;

		float x = r;// we start at angle = 0
		float y = 0;

		GL11.glBegin(GL11.GL_LINE_LOOP);
		{
			for (int ii = 0; ii < num_segments; ii++)
			{
				GL11.glVertex2f(x + cx, y + cy);// output vertex

				// apply the rotation matrix
				t = x;
				x = c * x - s * y;
				y = s * t + c * y;
			}
		}
		GL11.glEnd();
	}
}
