package com.mrcrayfish.modelcreator.panels.tabs;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.panels.ElementExtraPanel;
import com.mrcrayfish.modelcreator.panels.GlobalPanel;
import com.mrcrayfish.modelcreator.panels.IValueUpdater;
import com.mrcrayfish.modelcreator.panels.PositionPanel;
import com.mrcrayfish.modelcreator.panels.SizePanel;

public class ElementPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ElementManager manager;

	private SizePanel panelSize;
	private PositionPanel panelPosition;
	private ElementExtraPanel panelExtras;
	private GlobalPanel panelGlobal;

	public ElementPanel(ElementManager manager)
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
		panelGlobal = new GlobalPanel(manager);
	}

	public void addComponents()
	{
		add(Box.createRigidArea(new Dimension(188, 5)));
		add(panelSize);
		add(Box.createRigidArea(new Dimension(188, 5)));
		add(panelPosition);
		add(Box.createRigidArea(new Dimension(188, 5)));
		add(panelExtras);
		add(Box.createRigidArea(new Dimension(188, 70)));
		add(new JSeparator(JSeparator.HORIZONTAL));
		add(panelGlobal);
	}

	@Override
	public void updateValues(Element cube)
	{
		panelSize.updateValues(cube);
		panelPosition.updateValues(cube);
		panelExtras.updateValues(cube);
		panelGlobal.updateValues(cube);
	}
}
