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
		setLayout(new GridLayout(5, 2));
		setBorder(BorderFactory.createTitledBorder("Extras"));
		setMaximumSize(new Dimension(186, 500));
		initComponents();
		addComponents();
	}

	public void initComponents()
	{
		horizontalBox = new JPanel(new GridLayout(2, 1));
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
		boxCullFace.setSelected(cube.getSelectedFace().isCullfaced());
		boxEnabled.setSelected(cube.getSelectedFace().isEnabled());
	}

}
