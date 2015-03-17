package com.mrcrayfish.modelcreator.util;

import org.newdawn.slick.opengl.Texture;

public class Clipboard
{
	private static Texture texture;
	
	public static void copyTexture(Texture texture)
	{
		Clipboard.texture = texture;
	}
	
	public static Texture getTexture()
	{
		return Clipboard.texture;
	}
}
