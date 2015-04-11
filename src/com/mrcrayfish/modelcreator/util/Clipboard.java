package com.mrcrayfish.modelcreator.util;

import com.mrcrayfish.modelcreator.texture.ClipboardTexture;

public class Clipboard
{
	private static ClipboardTexture texture;

	public static void copyTexture(String location, String texture, double startU, double startV, double endU, double endV, int rotation)
	{
		Clipboard.texture = new ClipboardTexture(location, texture, startU, startV, endU, endV, rotation);
	}

	public static ClipboardTexture getTexture()
	{
		return Clipboard.texture;
	}
}
