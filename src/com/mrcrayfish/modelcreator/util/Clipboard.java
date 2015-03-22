package com.mrcrayfish.modelcreator.util;

public class Clipboard
{
	private static String texture;
	
	public static void copyTexture(String texture)
	{
		Clipboard.texture = texture;
	}
	
	public static String getTexture()
	{
		return Clipboard.texture;
	}
}
