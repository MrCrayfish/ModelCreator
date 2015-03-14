package com.mrcrayfish.modelcreator;

public abstract class Shape
{
	public String name;

	public Shape(String name)
	{
		this.name = name;
	}

	public void draw() {}
	
	@Override
	public String toString()
	{
		return name;
	}
}
