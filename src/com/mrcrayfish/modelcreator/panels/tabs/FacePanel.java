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

	private JPanel menuPanel;
	private JComboBox<String> menuList;
	private UVPanel panelUV;
	private JPanel sliderPanel;
	private JSlider rotation;
	private TexturePanel panelTexture;
	private FaceExtrasPanel panelProperties;

	private JPanel panelModId;
	private JTextField modidField;

	private final int ROTATION_MIN = 0;
	private final int ROTATION_MAX = 3;
	private final int ROTATION_INIT = 0;

	private DefaultComboBoxModel<String> model;

	public FacePanel(ElementManager manager)
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
		model.addElement("<html><div style='padding:5px;color:rgb(255,0,0);'><b>North</b></html>");
		model.addElement("<html><div style='padding:5px;color:rgb(0,255,0);'><b>East</b></html>");
		model.addElement("<html><div style='padding:5px;color:rgb(0,0,255);'><b>South</b></html>");
		model.addElement("<html><div style='padding:5px;color:rgb(255,187,0);'><b>West</b></html>");
		model.addElement("<html><div style='padding:5px;color:rgb(0,255,255);'><b>Up</b></html>");
		model.addElement("<html><div style='padding:5px;color:rgb(255,0,255);'><b>Down</b></html>");
	}

	public void initComponents()
	{
		menuPanel = new JPanel(new GridLayout(1, 1));
		menuPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Side</b></html>"));
		menuList = new JComboBox<String>();
		menuList.setModel(model);
		menuList.setToolTipText("The face to edit.");
		menuList.addActionListener(e ->
		{
			if (manager.getSelectedElement() != null)
			{
				manager.getSelectedElement().setSelectedFace(menuList.getSelectedIndex());
				updateValues(manager.getSelectedElement());
			}
		});
		menuPanel.setPreferredSize(new Dimension(186, 50));
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
		sliderPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Rotation</b></html>"));
		rotation = new JSlider(JSlider.HORIZONTAL, ROTATION_MIN, ROTATION_MAX, ROTATION_INIT);
		rotation.setMajorTickSpacing(4);
		rotation.setPaintTicks(true);
		rotation.setPaintLabels(true);
		rotation.setLabelTable(labelTable);
		rotation.addChangeListener(e ->
		{
			manager.getSelectedElement().getSelectedFace().setRotation(rotation.getValue());
		});
		rotation.setToolTipText("<html>The rotation of the texture<br>Default: 0\u00b0</html>");
		sliderPanel.setMaximumSize(new Dimension(190, 80));
		sliderPanel.add(rotation);

		panelModId = new JPanel(new GridLayout(1, 1));
		panelModId.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Location</b></html>"));
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
		panelModId.add(modidField);
	}

	public void addComponents()
	{
		add(Box.createRigidArea(new Dimension(192, 5)));
		add(menuPanel);
		add(panelTexture);
		add(panelUV);
		add(sliderPanel);
		add(panelModId);
		add(panelProperties);
	}

	@Override
	public void updateValues(Element cube)
	{
		if (cube != null)
		{
			menuList.setSelectedIndex(cube.getSelectedFaceIndex());
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
		panelUV.updateValues(cube);
		panelProperties.updateValues(cube);
	}
}
