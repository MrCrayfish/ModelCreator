package com.mrcrayfish.modelcreator.texture;

public class ClipboardTexture
{
	private String location;
	private String texture;

	public ClipboardTexture(String texture, String location)
	{
		this.texture = texture;
		this.location = location;
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
