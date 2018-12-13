package com.mrcrayfish.modelcreator.element;

import java.util.List;

import com.mrcrayfish.modelcreator.texture.PendingTexture;

public interface ElementManager
{
	public Element getSelectedElement();

	public void setSelectedElement(int pos);

	public List<Element> getAllElements();

	public Element getElement(int index);

	public int getElementCount();

	public void clearElements();

	public void updateName();

	public void updateValues();

	public void addPendingTexture(PendingTexture texture);

	public boolean getAmbientOcc();

	public void setAmbientOcc(boolean occ);

	public void addElement(Element e);
	
	public void setParticle(String texture);
	
	public String getParticle();
	
	public void reset();
}
