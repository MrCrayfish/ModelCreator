package com.mrcrayfish.modelcreator.panels;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.mrcrayfish.modelcreator.Cube;
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.ModelCreator;

public class FacePanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

	private JComboBox<String> menu;
	private UVPanel panelUV;
	private TexturePanel panelTexture;

	private DefaultComboBoxModel<String> model;

	public FacePanel(ModelCreator creator)
	{
		this.creator = creator;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(LEFT_ALIGNMENT);
		initMenu();
		initComponents();
		addComponents();
	}

	public void initMenu()
	{
		model = new DefaultComboBoxModel<String>();
		model.addElement("Front");
		model.addElement("Back");
		model.addElement("Left");
		model.addElement("Right");
		model.addElement("Bottom");
		model.addElement("Top");
	}

	public void initComponents()
	{
		menu = new JComboBox<String>();
		menu.setModel(model);
		menu.setPreferredSize(new Dimension(190, 30));
		menu.addActionListener(e ->
		{
			creator.getSelectedCube().setSelectedFace(menu.getSelectedIndex());
		});
		
		panelTexture = new TexturePanel(creator);
		panelUV = new UVPanel(creator);
	}

	public void addComponents()
	{
		add(menu);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(panelTexture);
		add(panelUV);
	}

	@Override
	public void updateValues(Cube cube)
	{
		menu.setSelectedIndex(cube.getSelectedFaceIndex());
		panelUV.updateValues(cube);
	}
}
