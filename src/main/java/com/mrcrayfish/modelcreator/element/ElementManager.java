package com.mrcrayfish.modelcreator.element;

import com.mrcrayfish.modelcreator.display.DisplayProperties;
import com.mrcrayfish.modelcreator.texture.TextureEntry;

import java.util.List;

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

    boolean getAmbientOcc();

    void setAmbientOcc(boolean occ);

    void addElement(Element e);

    void setParticle(TextureEntry entry);

    TextureEntry getParticle();

    void reset();

    default ElementManagerState createState()
    {
        return new ElementManagerState(this);
    }

    void restoreState(ElementManagerState state);

    void setDisplayProperties(DisplayProperties properties);

    DisplayProperties getDisplayProperties();
}
