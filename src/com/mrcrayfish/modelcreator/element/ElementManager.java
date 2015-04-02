package com.mrcrayfish.modelcreator.element;

import java.util.List;

import com.mrcrayfish.modelcreator.texture.PendingTexture;

public interface ElementManager
{
	public Element getSelectedCuboid();

	public List<Element> getAllCuboids();

	public Element getCuboid(int index);

	public int getCuboidCount();
	
	public void clearElements();

	public void updateName();

	public void updateValues();

	public void addPendingTexture(PendingTexture texture);
	
	public boolean getAmbientOcc();
}
