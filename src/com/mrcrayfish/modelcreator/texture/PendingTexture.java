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
	private File metaFile;

	public PendingTexture(String name, File metaFile, TextureCallback callback)
	{
		this.name = name;
		this.metaFile = metaFile;
		this.callback = callback;
	}

	public void load()
	{
		try
		{
			boolean result = false;
			String fileName = Paths.get(name).getFileName().toString();
			Texture texture = TextureManager.getTexture(fileName);
			if (texture == null)
			{
				FileInputStream is = new FileInputStream(new File(name));
				texture = TextureLoader.getTexture("PNG", is);
				result = TextureManager.loadExternalTexture(Paths.get(name).getParent().toString(), fileName, metaFile);
				is.close();
			}
			if (callback != null)
				callback.callback(result, fileName);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
