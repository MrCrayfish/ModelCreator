package com.mrcrayfish.modelcreator.element;

import com.mrcrayfish.modelcreator.Icons;

import javax.swing.*;
import java.awt.*;

/**
 * Author: MrCrayfish
 */
public class ElementCellEntry
{
    private Element element;
    private JPanel panel;
    private JLabel visibility;
    private JLabel name;

    public ElementCellEntry(Element element)
    {
        this.element = element;
        this.createPanel();
    }

    private void createPanel()
    {
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        visibility = new JLabel();
        visibility.setIcon(element.isVisible() ? Icons.light_on : Icons.light_off);
        panel.add(visibility);

        name = new JLabel(element.getName());
        panel.add(name);
    }

    public Element getElement()
    {
        return element;
    }

    public JPanel getPanel()
    {
        return panel;
    }

    public JLabel getVisibility()
    {
        return visibility;
    }

    public JLabel getName()
    {
        return name;
    }

    public void toggleVisibility()
    {
        element.setVisible(!element.isVisible());
        visibility.setIcon(element.isVisible() ? Icons.light_on : Icons.light_off);
    }
}
