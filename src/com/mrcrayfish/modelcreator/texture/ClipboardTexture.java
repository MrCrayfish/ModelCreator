package com.mrcrayfish.modelcreator.texture;

public class ClipboardTexture
{
	private String modid;
	private String texture;

	public ClipboardTexture(String texture, String modid)
	{
		this.texture = texture;
		this.modid = modid;
	}

	public String getModid()
	{
		return modid;
	}

	public String getTexture()
	{
		return texture;
	}
}
