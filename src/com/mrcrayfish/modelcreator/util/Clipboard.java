package com.mrcrayfish.modelcreator.util;

import com.mrcrayfish.modelcreator.texture.ClipboardTexture;

public class Clipboard
{
	private static ClipboardTexture texture;

	public static void copyTexture(String texture)
	{
		copyTexture(texture, null);
	}

	public static void copyTexture(String texture, String modid)
	{
		Clipboard.texture = new ClipboardTexture(texture, modid);
	}

	public static ClipboardTexture getTexture()
	{
		return Clipboard.texture;
	}
}
