package com.mrcrayfish.modelcreator.element;

import java.util.List;

import com.mrcrayfish.modelcreator.texture.PendingTexture;

public interface ElementManager
{
	 Element getSelectedElement();

	 void setSelectedElement(int pos);

	 List<Element> getAllElements();

	 Element getElement(int index);

	 int getElementCount();

	 void clearElements();

	 void updateName();

	 void updateValues();

	 void addPendingTexture(PendingTexture texture);

	 boolean getAmbientOcc();

	 void setAmbientOcc(boolean occ);

	 void addElement(Element e);
	
	 void setParticle(String texture);
	
	 String getParticle();
	
	 void reset();

	 default ElementManagerState createState()
	 {
	 	return new ElementManagerState(this);
	 }

	 void restoreState(ElementManagerState state);
}
