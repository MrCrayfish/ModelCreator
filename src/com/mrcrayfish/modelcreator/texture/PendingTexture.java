package com.mrcrayfish.modelcreator.texture;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PendingTexture
{
	private File texture;
	private File meta;
	private ITextureCallback callback;

	public PendingTexture(File texture)
	{
		this(texture, (ITextureCallback) null);
	}

	public PendingTexture(File texture, File meta)
	{
		this(texture, meta, null);
	}

	public PendingTexture(File texture, ITextureCallback callback)
	{
		this.texture = texture;
		this.callback = callback;
	}

	public PendingTexture(File texture, File meta, ITextureCallback callback)
	{
		this.texture = texture;
		this.meta = meta;
		this.callback = callback;
	}

	public void load()
	{
		try
		{
			boolean result = false;
			String fileName = this.texture.getName().replace(".png", "").replaceAll("\\d*$", "");
			Texture texture = TextureManager.getTexture(fileName);
			if (texture == null)
			{
				FileInputStream is = new FileInputStream(this.texture);
				texture = TextureLoader.getTexture("PNG", is);
				result = TextureManager.loadExternalTexture(this.texture, this.meta);
				is.close();
			}
			else
			{
				result = true;
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
