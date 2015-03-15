package com.mrcrayfish.modelcreator;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureManager
{
	public static Texture cobblestone;
	public static Texture dirt;

	public static void init()
	{
		try
		{
			cobblestone = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/brick.png"));
			dirt = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/dirt.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
