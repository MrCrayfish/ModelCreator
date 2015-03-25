package com.mrcrayfish.modelcreator.panels.tabs;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import com.mrcrayfish.modelcreator.Cuboid;
import com.mrcrayfish.modelcreator.CuboidManager;
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.panels.FaceExtrasPanel;
import com.mrcrayfish.modelcreator.panels.TexturePanel;
import com.mrcrayfish.modelcreator.panels.UVPanel;

public class FacePanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private CuboidManager manager;

	private JPanel menuPanel;
	private JComboBox<String> menuList;
	private UVPanel panelUV;
	private JPanel sliderPanel;
	private JSlider rotation;
	private TexturePanel panelTexture;
	private FaceExtrasPanel panelProperties;
	
	private final int ROTATION_MIN = 0;
	private final int ROTATION_MAX = 3;
	private final int ROTATION_INIT = 0;

	private DefaultComboBoxModel<String> model;

	public FacePanel(CuboidManager manager)
	{
		this.manager = manager;
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
			if (manager.getSelectedCuboid() != null)
			{
				manager.getSelectedCuboid().setSelectedFace(menuList.getSelectedIndex());
				updateValues(manager.getSelectedCuboid());
			}
		});
		menuPanel.setMaximumSize(new Dimension(186, 50));
		menuPanel.add(menuList);

		panelTexture = new TexturePanel(manager);
		panelUV = new UVPanel(manager);
		panelProperties = new FaceExtrasPanel(manager);
		
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(0), new JLabel("0\u00b0"));
		labelTable.put(new Integer(1), new JLabel("90\u00b0"));
		labelTable.put(new Integer(2), new JLabel("180\u00b0"));
		labelTable.put(new Integer(3), new JLabel("270\u00b0"));
		
		sliderPanel = new JPanel(new GridLayout(1, 1));
		sliderPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Rotation"));
		rotation = new JSlider(JSlider.HORIZONTAL, ROTATION_MIN, ROTATION_MAX, ROTATION_INIT);
		rotation.setMajorTickSpacing(4);
		rotation.setPaintTicks(true);
		rotation.setPaintLabels(true);
		rotation.setLabelTable(labelTable);
		rotation.addChangeListener(e ->
		{
			manager.getSelectedCuboid().getSelectedFace().setRotation(rotation.getValue() * 90D);
		});
		sliderPanel.setMaximumSize(new Dimension(190, 80));
		sliderPanel.add(rotation);
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
		add(sliderPanel);
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
