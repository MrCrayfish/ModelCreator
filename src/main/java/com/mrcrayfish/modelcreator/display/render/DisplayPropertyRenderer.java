package com.mrcrayfish.modelcreator.display.render;

import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;

import static org.lwjgl.opengl.GL11.glTranslatef;

/**
 * Author: MrCrayfish
 */
public abstract class DisplayPropertyRenderer extends StandardRenderer
{
    @Override
    protected void drawElements(ElementManager manager)
    {
        glTranslatef(-8, 0, -8);
        for(int i = 0; i < manager.getElementCount(); i++)
        {
            Element cube = manager.getElement(i);
            if(cube.isVisible())
            {
                cube.draw();
                cube.drawExtras(manager);
            }
        }
    }
}
