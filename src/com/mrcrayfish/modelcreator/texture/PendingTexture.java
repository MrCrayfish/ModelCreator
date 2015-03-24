package com.mrcrayfish.modelcreator.texture;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class PendingTexture
{
	private String name;
	private TextureCallback callback;

	public PendingTexture(String name, TextureCallback callback)
	{
		this.name = name;
		this.callback = callback;
	}

	public void load()
	{
		try
		{
			String fileName = Paths.get(name).getFileName().toString();
			Texture texture = TextureManager.getTexture(fileName);
			if (texture == null)
			{
				FileInputStream is = new FileInputStream(new File(name));
				texture = TextureLoader.getTexture("PNG", is);
				TextureManager.loadTexture(Paths.get(name).getParent().toString(), fileName);
				is.close();
			}
			callback.callback(fileName.replace(".png", ""));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
