package com.mrcrayfish.modelcreator.panels.tabs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.panels.FaceExtrasPanel;
import com.mrcrayfish.modelcreator.panels.IValueUpdater;
import com.mrcrayfish.modelcreator.panels.TexturePanel;
import com.mrcrayfish.modelcreator.panels.UVPanel;

public class FacePanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ElementManager manager;

	private JPanel sidesPanel;
	private JComboBox<String> facesBox;

	private TexturePanel texturePanel;
	private UVPanel uvPanel;

	private JPanel rotationPanel;
	private JSlider rotation;

	private JPanel modIdPanel;
	private JTextField modidField;

	private FaceExtrasPanel propertiesPanel;


	public FacePanel(ElementManager manager)
	{
		this.manager = manager;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initComponents();
		addComponents();
	}


	public void initComponents()
	{
		makeSidesPanel();
		texturePanel = new TexturePanel(manager);
		uvPanel = new UVPanel(manager);
		makeRotationPanel();
		makeModIdPanel();
		propertiesPanel = new FaceExtrasPanel(manager);
	}

	public void addComponents()
	{
		add(Box.createRigidArea(new Dimension(192, 5)));
		add(sidesPanel);
		add(texturePanel);
		add(uvPanel);
		add(rotationPanel);
		add(modIdPanel);
		add(propertiesPanel);
	}

	private void makeSidesPanel()
	{
		sidesPanel = new JPanel(new GridLayout(1, 1));
		sidesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Side</b></html>"));
		sidesPanel.setPreferredSize(new Dimension(186, 50));
		initFacesBox();
		sidesPanel.add(facesBox);
	}

	private void initFacesBox()
	{
		facesBox = new JComboBox<String>();
		DefaultComboBoxModel<String> facesModel = initFacesModel();
		facesBox.setModel(facesModel);
		facesBox.setToolTipText("The face to edit.");
		facesBox.addActionListener(e ->
		{
			if (manager.getSelectedElement() != null)
			{
				manager.getSelectedElement().setSelectedFace(facesBox.getSelectedIndex());
				updateValues(manager.getSelectedElement());
			}
		});
	}

	public DefaultComboBoxModel<String> initFacesModel()
	{
		DefaultComboBoxModel<String> facesModel;
		facesModel = new DefaultComboBoxModel<String>();
		facesModel.addElement("<html><div style='padding:5px;color:rgb(255,0,0);'><b>North</b></html>");
		facesModel.addElement("<html><div style='padding:5px;color:rgb(0,255,0);'><b>East</b></html>");
		facesModel.addElement("<html><div style='padding:5px;color:rgb(0,0,255);'><b>South</b></html>");
		facesModel.addElement("<html><div style='padding:5px;color:rgb(255,187,0);'><b>West</b></html>");
		facesModel.addElement("<html><div style='padding:5px;color:rgb(0,255,255);'><b>Up</b></html>");
		facesModel.addElement("<html><div style='padding:5px;color:rgb(255,0,255);'><b>Down</b></html>");
		return facesModel;
	}

	private void makeRotationPanel()
	{
		rotationPanel = new JPanel(new GridLayout(1, 1));
		rotationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Rotation</b></html>"));
		rotationPanel.setMaximumSize(new Dimension(190, 80));
		makeRotationSlider();
		rotationPanel.add(rotation);
	}

	private void makeRotationSlider()
	{
		int ROTATION_MIN = 0;
		int ROTATION_MAX = 3;
		int ROTATION_INIT = 0;
		String tipText = "<html>The rotation of the texture<br>Default: 0\u00b0</html>";

		rotation = new JSlider(JSlider.HORIZONTAL, ROTATION_MIN, ROTATION_MAX, ROTATION_INIT);
		rotation.setMajorTickSpacing(4);
		rotation.setPaintTicks(true);
		rotation.setPaintLabels(true);
		Hashtable<Integer, JLabel> labelTable = makeLabelTable();
		rotation.setLabelTable(labelTable);
		rotation.addChangeListener(e ->
				manager.getSelectedElement().getSelectedFace().setRotation(rotation.getValue()));
		rotation.setToolTipText(tipText);
	}

	private Hashtable<Integer, JLabel> makeLabelTable()
	{
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(0, new JLabel("0\u00b0"));
		labelTable.put(1, new JLabel("90\u00b0"));
		labelTable.put(2, new JLabel("180\u00b0"));
		labelTable.put(3, new JLabel("270\u00b0"));
		return labelTable;
	}

	private void makeModIdPanel()
	{
		modIdPanel = new JPanel(new GridLayout(1, 1));
		modIdPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Location</b></html>"));
		makeModIdField();
		modIdPanel.add(modidField);
	}

	private void makeModIdField()
	{
		modidField = new JTextField();
		modidField.setSize(new Dimension(190, 30));
		modidField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if (manager.getSelectedElement() != null)
					{
						manager.getSelectedElement().getSelectedFace().setTextureLocation(modidField.getText());
					}
				}
			}
		});
		modidField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				if (manager.getSelectedElement() != null)
				{
					manager.getSelectedElement().getSelectedFace().setTextureLocation(modidField.getText());
				}
			}
		});
		modidField.setToolTipText("<html>The specific location of the texture. If you have the<br>" + "texture in a sub folder, write the custom directory<br>" + "here. Can include Mod ID prefix.<br>Default: 'blocks/'</html>");
	}

	@Override
	public void updateValues(Element cube)
	{
		if (cube != null)
		{
			facesBox.setSelectedIndex(cube.getSelectedFaceIndex());
			modidField.setEnabled(true);
			modidField.setText(cube.getSelectedFace().getTextureLocation());
			rotation.setEnabled(true);
			rotation.setValue(cube.getSelectedFace().getRotation());
		}
		else
		{
			modidField.setEnabled(false);
			modidField.setText("");
			rotation.setEnabled(false);
			rotation.setValue(0);
		}
		uvPanel.updateValues(cube);
		propertiesPanel.updateValues(cube);
	}
}
