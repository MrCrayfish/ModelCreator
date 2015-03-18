package com.mrcrayfish.modelcreator.panels;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.mrcrayfish.modelcreator.Cube;
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.ModelCreator;

public class PropertiesPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;
	
	private ModelCreator creator;
	
	private SizePanel panelSize;
	private PositionPanel panelPosition;

	public PropertiesPanel(ModelCreator creator)
	{
		this.creator = creator;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initComponents();
		addComponents();
	}
	
	public void initComponents()
	{
		panelSize = new SizePanel(creator);
		panelPosition = new PositionPanel(creator);
	}
	
	public void addComponents()
	{
		add(panelSize);
		add(panelPosition);
	}

	@Override
	public void updateValues(Cube cube)
	{
		panelSize.updateValues(cube);
		panelPosition.updateValues(cube);
	}
}
