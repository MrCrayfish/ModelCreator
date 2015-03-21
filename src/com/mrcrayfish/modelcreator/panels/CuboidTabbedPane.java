package com.mrcrayfish.modelcreator.panels;

import java.awt.Component;

import javax.swing.JTabbedPane;

import com.mrcrayfish.modelcreator.Cuboid;
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.ModelCreator;

public class CuboidTabbedPane extends JTabbedPane
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

	public CuboidTabbedPane(ModelCreator creator)
	{
		this.creator = creator;
	}

	public void updateValues()
	{
		for (int i = 0; i < getTabCount(); i++)
		{
			Component component = getComponentAt(i);
			if (component != null)
			{
				if (component instanceof IValueUpdater)
				{
					IValueUpdater updater = (IValueUpdater) component;
					Cuboid cube = creator.getSelectedCuboid();
					if (cube != null)
					{
						updater.updateValues(cube);
					}
				}
			}
		}
	}
}
