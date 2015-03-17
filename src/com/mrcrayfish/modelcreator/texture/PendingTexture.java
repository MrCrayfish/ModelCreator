package com.mrcrayfish.modelcreator.texture;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class PendingTexture
{
	private String file;
	private TextureLoaderCallback callback;
	
	public PendingTexture(String file, TextureLoaderCallback callback)
	{
		this.file = file;
		this.callback = callback;
	}
	
	public void load()
	{
		try
		{
			Texture texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(file), true);
			callback.callback(texture);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
