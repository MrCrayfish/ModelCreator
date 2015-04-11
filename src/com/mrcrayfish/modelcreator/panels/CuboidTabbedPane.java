package com.mrcrayfish.modelcreator.panels;

import java.awt.Component;

import javax.swing.JTabbedPane;

import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;

public class CuboidTabbedPane extends JTabbedPane
{
	private static final long serialVersionUID = 1L;

	private ElementManager manager;

	public CuboidTabbedPane(ElementManager manager)
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
					Element cube = manager.getSelectedElement();
					updater.updateValues(cube);
				}
			}
		}
	}
}
