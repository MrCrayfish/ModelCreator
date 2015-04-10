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

import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.panels.IValueUpdater;
import com.mrcrayfish.modelcreator.panels.OriginPanel;

public class RotationPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ElementManager manager;

	private OriginPanel panelOrigin;
	private JPanel axisPanel;
	private JComboBox<String> axisList;
	private JPanel sliderPanel;
	private JSlider rotation;
	private JPanel extraPanel;
	private JRadioButton btnRescale;

	private DefaultComboBoxModel<String> model;

	private final int ROTATION_MIN = -2;
	private final int ROTATION_MAX = 2;
	private final int ROTATION_INIT = 0;

	public RotationPanel(ElementManager manager)
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
		model.addElement("<html><div style='padding:5px;color:red;'><b>X</b></html>");
		model.addElement("<html><div style='padding:5px;color:green;'><b>Y</b></html>");
		model.addElement("<html><div style='padding:5px;color:blue;'><b>Z</b></html>");
	}

	public void initComponents()
	{
		panelOrigin = new OriginPanel(manager);

		axisPanel = new JPanel(new GridLayout(1, 1));
		axisPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Axis"));
		axisList = new JComboBox<String>();
		axisList.setModel(model);
		axisList.setToolTipText("The axis the element will rotate around");
		axisList.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
				manager.getSelectedCuboid().setPrevAxis(axisList.getSelectedIndex());
		});
		axisPanel.setMaximumSize(new Dimension(186, 50));
		axisPanel.add(axisList);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(-2), new JLabel("-45\u00b0"));
		labelTable.put(new Integer(-1), new JLabel("-22.5\u00b0"));
		labelTable.put(new Integer(0), new JLabel("0\u00b0"));
		labelTable.put(new Integer(1), new JLabel("22.5\u00b0"));
		labelTable.put(new Integer(2), new JLabel("45\u00b0"));

		sliderPanel = new JPanel(new GridLayout(1, 1));
		sliderPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Rotation"));
		rotation = new JSlider(JSlider.HORIZONTAL, ROTATION_MIN, ROTATION_MAX, ROTATION_INIT);
		rotation.setMajorTickSpacing(1);
		rotation.setPaintTicks(true);
		rotation.setPaintLabels(true);
		rotation.setLabelTable(labelTable);
		rotation.addChangeListener(e ->
		{
			manager.getSelectedCuboid().setRotation(rotation.getValue() * 22.5D);
		});
		rotation.setToolTipText("<html>The rotation of the element<br>Default: 0</html>");
		sliderPanel.setMaximumSize(new Dimension(190, 80));
		sliderPanel.add(rotation);

		extraPanel = new JPanel(new GridLayout(1, 2));
		extraPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Extras"));
		btnRescale = new JRadioButton("Rescale");
		btnRescale.setToolTipText("<html>Should scale faces across whole block<br>Default: Off<html>");
		btnRescale.addActionListener(e ->
		{
			manager.getSelectedCuboid().setRescale(btnRescale.isSelected());
		});
		extraPanel.setMaximumSize(new Dimension(186, 50));
		extraPanel.add(btnRescale);
	}

	public void addComponents()
	{
		add(Box.createRigidArea(new Dimension(188, 5)));
		add(panelOrigin);
		add(Box.createRigidArea(new Dimension(188, 5)));
		add(axisPanel);
		add(Box.createRigidArea(new Dimension(188, 5)));
		add(sliderPanel);
		add(Box.createRigidArea(new Dimension(188, 5)));
		add(extraPanel);
	}

	@Override
	public void updateValues(Element cube)
	{
		panelOrigin.updateValues(cube);
		if (cube != null)
		{
			axisList.setSelectedIndex(cube.getPrevAxis());
			rotation.setEnabled(true);
			rotation.setValue((int) (cube.getRotation() / 22.5));
			btnRescale.setEnabled(true);
			btnRescale.setSelected(cube.shouldRescale());
		}
		else
		{
			rotation.setValue(0);
			rotation.setEnabled(false);
			btnRescale.setSelected(false);
			btnRescale.setEnabled(false);
		}
	}
}
