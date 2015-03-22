package com.mrcrayfish.modelcreator.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.mrcrayfish.modelcreator.Cuboid;
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.ModelCreator;

public class FaceExtrasPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

	private JPanel horizontalBox;
	private JRadioButton boxCullFace;
	private JRadioButton boxEnabled;

	private JPanel panelModId;
	private JLabel modidLabel;
	private JTextField modidField;

	public FaceExtrasPanel(ModelCreator creator)
	{
		this.creator = creator;
		setLayout(new GridLayout(2, 1));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Extras"));
		setMaximumSize(new Dimension(186, 50));
		initComponents();
		addComponents();
	}

	public void initComponents()
	{
		horizontalBox = new JPanel(new GridLayout(1, 1));
		boxCullFace = new JRadioButton("Cullface");
		boxCullFace.setToolTipText("<html>Should render face is another block is adjacent<br>Default: Off</html>");
		boxCullFace.addActionListener(e ->
		{
			creator.getSelectedCuboid().getSelectedFace().setCullface(boxCullFace.isSelected());
		});
		boxEnabled = new JRadioButton("Enable");
		boxEnabled.setToolTipText("<html>Determines if face should be included<br>Default: On</html>");
		boxEnabled.addActionListener(e ->
		{
			creator.getSelectedCuboid().getSelectedFace().setEnabled(boxEnabled.isSelected());
		});
		horizontalBox.add(boxCullFace);
		horizontalBox.add(boxEnabled);

		panelModId = new JPanel(new BorderLayout());
		modidLabel = new JLabel("Mod ID: ");
		modidField = new JTextField();
		modidField.setPreferredSize(new Dimension(190, 50));
		modidField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if(creator.getSelectedCuboid() != null)
					{
						creator.getSelectedCuboid().getSelectedFace().setTextureModId(modidField.getText());
					}
				}
			}
		});
		modidField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				if(creator.getSelectedCuboid() != null)
				{
					creator.getSelectedCuboid().getSelectedFace().setTextureModId(modidField.getText());
				}
			}
		});
		panelModId.add(modidLabel, BorderLayout.WEST);
		panelModId.add(modidField, BorderLayout.CENTER);
	}

	public void addComponents()
	{
		add(horizontalBox);
		add(panelModId);
	}

	@Override
	public void updateValues(Cuboid cube)
	{
		if (cube != null)
		{
			boxCullFace.setEnabled(true);
			boxCullFace.setSelected(cube.getSelectedFace().isCullfaced());
			boxEnabled.setEnabled(true);
			boxEnabled.setSelected(cube.getSelectedFace().isEnabled());
			modidField.setEnabled(true);
			modidField.setText(cube.getSelectedFace().getTextureModId());
		}
		else
		{
			boxCullFace.setEnabled(false);
			boxCullFace.setSelected(false);
			boxEnabled.setEnabled(false);
			boxEnabled.setSelected(false);
			modidField.setEnabled(false);
			modidField.setText("");
		}
	}
}
