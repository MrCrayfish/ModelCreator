package com.mrcrayfish.modelcreator.panels;

import com.mrcrayfish.modelcreator.element.ElementManager;

import javax.swing.*;
import java.awt.*;

public class CuboidTabbedPane extends JTabbedPane
{
    private ElementManager manager;

    public CuboidTabbedPane(ElementManager manager)
    {
        this.manager = manager;
    }

    public void updateValues()
    {
        for(int i = 0; i < getTabCount(); i++)
        {
            Component component = getComponentAt(i);
            if(component != null)
            {
                if(component instanceof IElementUpdater)
                {
                    IElementUpdater updater = (IElementUpdater) component;
                    updater.updateValues(manager.getSelectedElement());
                }
            }
        }
    }
}
