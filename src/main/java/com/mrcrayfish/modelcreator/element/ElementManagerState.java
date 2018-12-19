package com.mrcrayfish.modelcreator.element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: MrCrayfish
 */
public class ElementManagerState
{
    private final List<Element> elements;
    private final int selectedIndex;
    private final boolean ambientOcclusion;
    private final String particleTexture;

    public ElementManagerState(ElementManager manager)
    {
        this.elements = manager.getAllElements().stream().map(Element::new).collect(Collectors.toList());
        this.selectedIndex = manager.getAllElements().indexOf(manager.getSelectedElement());
        this.ambientOcclusion = manager.getAmbientOcc();
        this.particleTexture = manager.getParticle();
    }

    public List<Element> getElements()
    {
        return elements;
    }

    public int getSelectedIndex()
    {
        return selectedIndex;
    }

    public boolean isAmbientOcclusion()
    {
        return ambientOcclusion;
    }

    public String getParticleTexture()
    {
        return particleTexture;
    }
}
