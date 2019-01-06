package com.mrcrayfish.modelcreator.util;

import com.mrcrayfish.modelcreator.Icons;

import javax.swing.*;
import java.awt.*;

public class ComponentUtil
{
    public static JRadioButton createRadioButton(String label, String toolTip)
    {
        JRadioButton radioButton = new JRadioButton(label);
        radioButton.setToolTipText(toolTip);
        radioButton.setIcon(Icons.light_off);
        radioButton.setRolloverIcon(Icons.light_off);
        radioButton.setSelectedIcon(Icons.light_on);
        radioButton.setRolloverSelectedIcon(Icons.light_on);
        return radioButton;
    }

    public static JCheckBox createCheckBox(String text, String tooltip, boolean selected)
    {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setToolTipText(tooltip);
        checkBox.setSelected(selected);
        checkBox.setIcon(Icons.light_off);
        checkBox.setRolloverIcon(Icons.light_off);
        checkBox.setSelectedIcon(Icons.light_on);
        checkBox.setRolloverSelectedIcon(Icons.light_on);
        return checkBox;
    }

    public static Rectangle expandRectangle(Rectangle r, int amount)
    {
        return new Rectangle(r.x - amount, r.y - amount, r.width + amount * 2, r.height + amount * 2);
    }
}
