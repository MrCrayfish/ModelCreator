package com.mrcrayfish.modelcreator.texture;

public class ClipboardTexture
{
	private String location;
	private String texture;
	public ClipboardTexture(String location, String texture)
	{
		this.location = location;
		this.texture = texture;
	}

	public String getLocation()
	{
		return location;
	}

	public String getTexture()
	{
		return texture;
	}
}
