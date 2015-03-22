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
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import com.mrcrayfish.modelcreator.Cuboid;
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.panels.OriginPanel;

public class RotationPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

	private OriginPanel panelOrigin;
	private JPanel axisPanel;
	private JComboBox<String> axisList;
	private JPanel sliderPanel;
	private JSlider rotation;
	private JPanel extraPanel;
	private JRadioButton btnRescale;
	
	private DefaultComboBoxModel<String> model;

	private final int ROTATION_MIN = 0;
	private final int ROTATION_MAX = 15;
	private final int ROTATION_INIT = 0;

	public RotationPanel(ModelCreator creator)
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
		model.addElement("<html><div style='padding:5px;'>X</html>");
		model.addElement("<html><div style='padding:5px;'>Y</html>");
		model.addElement("<html><div style='padding:5px;'>Z</html>");
	}

	public void initComponents()
	{
		panelOrigin = new OriginPanel(creator);

		axisPanel = new JPanel(new GridLayout(1, 1));
		axisPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Axis"));
		axisList = new JComboBox<String>();
		axisList.setModel(model);
		axisList.setToolTipText("The axis the element will rotate around");
		axisList.addActionListener(e ->
		{
			creator.getSelectedCuboid().setPrevAxis(axisList.getSelectedIndex());
		});
		axisPanel.setMaximumSize(new Dimension(186, 50));
		axisPanel.add(axisList);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(0), new JLabel("0\u00b0"));
		labelTable.put(new Integer(4), new JLabel("90\u00b0"));
		labelTable.put(new Integer(8), new JLabel("180\u00b0"));
		labelTable.put(new Integer(12), new JLabel("270\u00b0"));

		sliderPanel = new JPanel(new GridLayout(1, 1));
		sliderPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Rotation"));
		rotation = new JSlider(JSlider.HORIZONTAL, ROTATION_MIN, ROTATION_MAX, ROTATION_INIT);
		rotation.setMajorTickSpacing(4);
		rotation.setMinorTickSpacing(1);
		rotation.setPaintTicks(true);
		rotation.setPaintLabels(true);
		rotation.setLabelTable(labelTable);
		rotation.addChangeListener(e ->
		{
			creator.getSelectedCuboid().setRotation(rotation.getValue() * 22.5D);
		});
		sliderPanel.setMaximumSize(new Dimension(190, 80));
		sliderPanel.add(rotation);

		extraPanel = new JPanel(new GridLayout(1, 2));
		extraPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Extras"));
		btnRescale = new JRadioButton("Rescale");
		btnRescale.setToolTipText("<html>Should scale faces across whole block<br>Default: Off<html>");
		btnRescale.addActionListener(e ->
		{
			creator.getSelectedCuboid().setRescale(btnRescale.isSelected());
		});
		extraPanel.setMaximumSize(new Dimension(186, 50));
		extraPanel.add(btnRescale);
	}

	public void addComponents()
	{
		add(Box.createRigidArea(new Dimension(188,5)));
		add(panelOrigin);
		add(Box.createRigidArea(new Dimension(188,5)));
		add(axisPanel);
		add(Box.createRigidArea(new Dimension(188,5)));
		add(sliderPanel);
		add(Box.createRigidArea(new Dimension(188,5)));
		add(extraPanel);
	}

	@Override
	public void updateValues(Cuboid cube)
	{
		panelOrigin.updateValues(cube);
		axisList.setSelectedIndex(cube.getPrevAxis());
		rotation.setValue((int) (cube.getRotation() / 22.5));
		btnRescale.setSelected(cube.shouldRescale());
	}
}
