package com.mrcrayfish.modelcreator.display;

import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public abstract class DisplayPropertyRender
{


    protected List<Element> elements = new ArrayList<>();

    public void onRender(DisplayProperties.Entry entry, ElementManager manager)
    {
        for(Element element : elements)
        {
            element.drawExtras(manager);
            element.draw();
        }
    }

    public void onPreRenderModel(DisplayProperties.Entry entry) {}

    public void onPostRenderModel(DisplayProperties.Entry entry) {}

    public void onPreRenderElements() {}
}
