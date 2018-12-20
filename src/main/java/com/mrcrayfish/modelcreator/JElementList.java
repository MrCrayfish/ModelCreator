package com.mrcrayfish.modelcreator;

import com.mrcrayfish.modelcreator.panels.SidebarPanel;
import com.mrcrayfish.modelcreator.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Author: MrCrayfish
 */
public class JElementList extends JList<SidebarPanel.ElementEntry>
{
    private boolean processInput;

    @Override
    protected void processMouseEvent(MouseEvent e)
    {
        if(e.getID() == MouseEvent.MOUSE_FIRST)
            return;

        if(e.getID() == MouseEvent.MOUSE_RELEASED)
        {
            processInput = true;
            return;
        }

        if(!processInput)
            return;

        if(e.getButton() == MouseEvent.BUTTON1)
        {
            int index = this.locationToIndex(e.getPoint());
            if(index != -1)
            {
                Rectangle rectangle = this.getCellBounds(index, index);
                if(rectangle.contains(e.getPoint()))
                {
                    Point relativePoint = new Point((int) e.getPoint().getX(), (int) (e.getPoint().getY() - rectangle.getY()));
                    SidebarPanel.ElementEntry entry = this.getModel().getElementAt(index);
                    Rectangle buttonBounds = ComponentUtil.expandRectangle(entry.getVisibility().getBounds(), 4);
                    if(buttonBounds.contains(relativePoint))
                    {
                        entry.toggleVisibility();
                        this.repaint();
                        e.consume();
                        processInput = false;
                        return;
                    }
                }
            }
        }

        super.processMouseEvent(e);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e)
    {
        if(processInput)
        {
            super.processMouseMotionEvent(e);
        }
    }
}
