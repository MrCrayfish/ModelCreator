package com.mrcrayfish.modelcreator.texture;

import javax.swing.ImageIcon;

import org.newdawn.slick.opengl.Texture;

public class TextureEntry
{
	private String name;
	private Texture texture;
	private ImageIcon image;

	public TextureEntry(String name, Texture texture, ImageIcon image)
	{
		this.name = name;
		this.texture = texture;
		this.image = image;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Texture getTexture()
	{
		return texture;
	}

	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}

	public ImageIcon getImage()
	{
		return image;
	}

	public void setImage(ImageIcon image)
	{
		this.image = image;
	}
}
