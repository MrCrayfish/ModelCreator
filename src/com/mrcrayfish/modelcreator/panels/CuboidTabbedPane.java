package com.mrcrayfish.modelcreator.panels;

import java.awt.Component;

import javax.swing.JTabbedPane;

import com.mrcrayfish.modelcreator.Cuboid;
import com.mrcrayfish.modelcreator.CuboidManager;
import com.mrcrayfish.modelcreator.IValueUpdater;

public class CuboidTabbedPane extends JTabbedPane
{
	private static final long serialVersionUID = 1L;

	private CuboidManager manager;

	public CuboidTabbedPane(CuboidManager manager)
	{
		this.manager = manager;
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
					Cuboid cube = manager.getSelectedCuboid();
					updater.updateValues(cube);
				}
			}
		}
	}
}
