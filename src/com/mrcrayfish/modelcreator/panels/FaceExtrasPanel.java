package com.mrcrayfish.modelcreator.panels;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.mrcrayfish.modelcreator.Cuboid;
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.ModelCreator;

public class FaceExtrasPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

	private JRadioButton boxCullFace;
	private JRadioButton boxEnabled;
	private JPanel horizontalBox;

	public FaceExtrasPanel(ModelCreator creator)
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
	}

	public void addComponents()
	{
		add(horizontalBox);
	}

	@Override
	public void updateValues(Cuboid cube)
	{
		if (cube != null)
		{
			boxCullFace.setEnabled(true);
			boxCullFace.setSelected(cube.getSelectedFace().isCullfaced());
			boxCullFace.setEnabled(true);
			boxEnabled.setSelected(cube.getSelectedFace().isEnabled());
		}
		else
		{
			boxCullFace.setEnabled(false);
			boxCullFace.setSelected(false);
			boxEnabled.setEnabled(false);
			boxEnabled.setSelected(false);
		}
	}
}
