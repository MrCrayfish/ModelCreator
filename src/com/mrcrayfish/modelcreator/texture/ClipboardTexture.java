package com.mrcrayfish.modelcreator.texture;

public class ClipboardTexture
{
	private String location;
	private String texture;
	private double startU, startV, endU, endV;
	private int rotation;

	public ClipboardTexture(String location, String texture, double startU, double startV, double endU, double endV, int rotation)
	{
		this.location = location;
		this.texture = texture;
		this.startU = startU;
		this.startV = startV;
		this.endU = endU;
		this.endV = endV;
		this.rotation = rotation;
	}

	public String getLocation()
	{
		return location;
	}

	public String getTexture()
	{
		return texture;
	}
	
	public double getStartU()
	{
		return startU;
	}
	
	public double getStartV()
	{
		return startV;
	}
	
	public double getEndU()
	{
		return endU;
	}
	
	public double getEndV()
	{
		return endV;
	}
	
	public int getRotation()
	{
		return rotation;
	}
}
