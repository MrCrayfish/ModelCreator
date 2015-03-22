package com.mrcrayfish.modelcreator.panels;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.mrcrayfish.modelcreator.Cuboid;
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.ModelCreator;

public class ElementExtraPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

	private JRadioButton btnShade;

	public ElementExtraPanel(ModelCreator creator)
	{
		this.creator = creator;
		setLayout(new GridLayout(1, 2));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Extras"));
		setMaximumSize(new Dimension(186, 50));
		initComponents();
		addComponents();
	}

	public void initComponents()
	{
		btnShade = new JRadioButton("Shade");
		btnShade.addActionListener(e ->
		{
			creator.getSelectedCuboid().setShade(btnShade.isSelected());
		});
	}

	public void addComponents()
	{
		add(btnShade);
	}

	@Override
	public void updateValues(Cuboid cube)
	{
		if (cube != null)
		{
			btnShade.setEnabled(true);
			btnShade.setSelected(cube.isShaded());
		}
		else
		{
			btnShade.setEnabled(false);
			btnShade.setSelected(false);
		}
	}
}
