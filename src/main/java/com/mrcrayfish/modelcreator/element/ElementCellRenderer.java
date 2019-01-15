package com.mrcrayfish.modelcreator.element;

import com.mrcrayfish.modelcreator.ModelCreator;

import javax.swing.*;
import java.awt.*;

/**
 * Author: MrCrayfish
 */
public class ElementCellRenderer extends DefaultListCellRenderer
{
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        ElementCellEntry entry = (ElementCellEntry) value;
        JPanel panel = entry.getPanel();
        panel.setBackground(isSelected ? new Color(186, 193, 211) : new Color(234, 234, 242));
        entry.getName().setText(entry.getElement().getName());
        return panel;
    }
}
