package com.mrcrayfish.modelcreator.util;

import javax.swing.JRadioButton;

import com.mrcrayfish.modelcreator.Icons;

import java.awt.*;

public class ComponentUtil
{
	public static JRadioButton createRadioButton(String name, String toolTip)
	{
		JRadioButton button = new JRadioButton(name);
		button.setToolTipText(toolTip);
		button.setIcon(Icons.light_off);
		button.setRolloverIcon(Icons.light_off);
		button.setSelectedIcon(Icons.light_on);
		button.setRolloverSelectedIcon(Icons.light_on);
		return button;
	}

	public static Rectangle expandRectangle(Rectangle r, int amount)
	{
		return new Rectangle(r.x - amount, r.y - amount, r.width + amount * 2, r.height + amount * 2);
	}
}
