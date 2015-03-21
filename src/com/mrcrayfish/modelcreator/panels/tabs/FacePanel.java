package com.mrcrayfish.modelcreator.panels.tabs;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.mrcrayfish.modelcreator.Cuboid;
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.panels.FaceExtrasPanel;
import com.mrcrayfish.modelcreator.panels.TexturePanel;
import com.mrcrayfish.modelcreator.panels.UVPanel;

public class FacePanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

	private JPanel menuPanel;
	private JComboBox<String> menuList;
	private UVPanel panelUV;
	private TexturePanel panelTexture;
	private FaceExtrasPanel panelProperties;

	private DefaultComboBoxModel<String> model;

	public FacePanel(ModelCreator creator)
	{
		this.creator = creator;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initMenu();
		initComponents();
		addComponents();
	}

	public void initMenu()
	{
		model = new DefaultComboBoxModel<String>();
		model.addElement("North");
		model.addElement("East");
		model.addElement("South");
		model.addElement("West");
		model.addElement("Down");
		model.addElement("Up");
	}

	public void initComponents()
	{
		menuPanel = new JPanel(new GridLayout(1, 1));
		menuPanel.setBorder(BorderFactory.createTitledBorder("Side"));
		menuList = new JComboBox<String>();
		menuList.setModel(model);
		menuList.addActionListener(e ->
		{
			creator.getSelectedCuboid().setSelectedFace(menuList.getSelectedIndex());
		});
		menuPanel.setMaximumSize(new Dimension(186, 500));
		menuPanel.add(menuList);
		
		panelTexture = new TexturePanel(creator);
		panelUV = new UVPanel(creator);
		panelProperties = new FaceExtrasPanel(creator);
	}

	public void addComponents()
	{
		add(menuPanel);
		add(Box.createVerticalStrut(5));
		add(panelTexture);
		add(Box.createVerticalStrut(5));
		add(panelUV);
		add(Box.createVerticalStrut(5));
		add(panelProperties);
	}

	@Override
	public void updateValues(Cuboid cube)
	{
		menuList.setSelectedIndex(cube.getSelectedFaceIndex());
		panelUV.updateValues(cube);
	}
}
