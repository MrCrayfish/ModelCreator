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
		model.addElement("<html><div style='padding:5px;'>North</html>");
		model.addElement("<html><div style='padding:5px;'>East</html>");
		model.addElement("<html><div style='padding:5px;'>South</html>");
		model.addElement("<html><div style='padding:5px;'>West</html>");
		model.addElement("<html><div style='padding:5px;'>Down</html>");
		model.addElement("<html><div style='padding:5px;'>Up</html>");
	}

	public void initComponents()
	{
		menuPanel = new JPanel(new GridLayout(1, 1));
		menuPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Side"));
		menuList = new JComboBox<String>();
		menuList.setModel(model);
		menuList.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				creator.getSelectedCuboid().setSelectedFace(menuList.getSelectedIndex());
				updateValues(creator.getSelectedCuboid());
			}
		});
		menuPanel.setMaximumSize(new Dimension(186, 50));
		menuPanel.add(menuList);

		panelTexture = new TexturePanel(creator);
		panelUV = new UVPanel(creator);
		panelProperties = new FaceExtrasPanel(creator);
	}

	public void addComponents()
	{
		add(Box.createRigidArea(new Dimension(192, 5)));
		add(menuPanel);
		add(Box.createRigidArea(new Dimension(192, 5)));
		add(panelTexture);
		add(Box.createRigidArea(new Dimension(192, 5)));
		add(panelUV);
		add(Box.createRigidArea(new Dimension(192, 5)));
		add(panelProperties);
	}

	@Override
	public void updateValues(Cuboid cube)
	{
		if (cube != null)
		{
			menuList.setSelectedIndex(cube.getSelectedFaceIndex());
		}
		panelUV.updateValues(cube);
		panelProperties.updateValues(cube);
	}
}
