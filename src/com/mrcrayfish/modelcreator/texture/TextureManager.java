package com.mrcrayfish.modelcreator.texture;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextureManager
{
	private static Map<String, Texture> textureCache = new HashMap<String, Texture>();

	public static Texture cobblestone;
	public static Texture dirt;

	public static void init()
	{
		try
		{
			loadTexture("brick");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static Texture loadTexture(String name) throws IOException
	{
		FileInputStream is = new FileInputStream(new File("res/" + name + ".png"));
		Texture texture = TextureLoader.getTexture("PNG", is);
		textureCache.put(name, texture);
		return texture;
	}
	
	public static synchronized void putTexture(String name, Texture texture)
	{
		textureCache.put(name, texture);
	}
	
	public static synchronized Texture getTexture(String name)
	{
		return textureCache.get(name);
	}
}
