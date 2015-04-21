package com.mrcrayfish.modelcreator.util;

import javax.swing.JRadioButton;

import com.mrcrayfish.modelcreator.Icons;

public class ComponentUtil
{
	public static JRadioButton createRadioButton(String name, String toolTip)
	{
		JRadioButton button = new JRadioButton(name);
		button.setToolTipText(toolTip);
		button.setIcon(Icons.light_on);
		button.setRolloverIcon(Icons.light_on);
		button.setSelectedIcon(Icons.light_off);
		button.setRolloverSelectedIcon(Icons.light_off);
		return button;
	}
}
