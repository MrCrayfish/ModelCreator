package com.mrcrayfish.modelcreator.panels.tabs;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.mrcrayfish.modelcreator.Cuboid;
import com.mrcrayfish.modelcreator.CuboidManager;
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.panels.ElementExtraPanel;
import com.mrcrayfish.modelcreator.panels.PositionPanel;
import com.mrcrayfish.modelcreator.panels.SizePanel;

public class ElementPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private CuboidManager manager;

	private SizePanel panelSize;
	private PositionPanel panelPosition;
	private ElementExtraPanel panelExtras;

	public ElementPanel(CuboidManager manager)
	{
		this.manager = manager;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initComponents();
		addComponents();
	}

	public void initComponents()
	{
		panelSize = new SizePanel(manager);
		panelPosition = new PositionPanel(manager);
		panelExtras = new ElementExtraPanel(manager);
	}

	public void addComponents()
	{
		add(Box.createRigidArea(new Dimension(188, 5)));
		add(panelSize);
		add(Box.createRigidArea(new Dimension(188, 5)));
		add(panelPosition);
		add(Box.createRigidArea(new Dimension(188, 5)));
		add(panelExtras);
	}

	@Override
	public void updateValues(Cuboid cube)
	{
		panelSize.updateValues(cube);
		panelPosition.updateValues(cube);
		panelExtras.updateValues(cube);
	}
}
