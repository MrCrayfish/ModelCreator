package com.mrcrayfish.modelcreator.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;

public class FaceExtrasPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ElementManager manager;

	private JPanel horizontalBox;
	private JRadioButton boxCullFace;
	private JRadioButton boxFill;
	private JRadioButton boxEnabled;

	public FaceExtrasPanel(ElementManager manager)
	{
		this.manager = manager;
		setLayout(new BorderLayout(0, 5));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Extras"));
		setMaximumSize(new Dimension(186, 100));
		initComponents();
		addComponents();
	}

	public void initComponents()
	{
		horizontalBox = new JPanel(new GridLayout(2, 2));
		boxCullFace = new JRadioButton("Cullface");
		boxCullFace.setToolTipText("<html>Should render face is another block is adjacent<br>Default: Off</html>");
		boxCullFace.addActionListener(e ->
		{
			manager.getSelectedCuboid().getSelectedFace().setCullface(boxCullFace.isSelected());
		});
		boxFill = new JRadioButton("Fill");
		boxFill.setToolTipText("<html>Makes the texture fill the face<br>Default: Off</html>");
		boxFill.addActionListener(e ->
		{
			manager.getSelectedCuboid().getSelectedFace().fitTexture(boxFill.isSelected());
		});
		boxEnabled = new JRadioButton("Enable");
		boxEnabled.setToolTipText("<html>Determines if face should be included<br>Default: On</html>");
		boxEnabled.addActionListener(e ->
		{
			manager.getSelectedCuboid().getSelectedFace().setEnabled(boxEnabled.isSelected());
		});
		horizontalBox.add(boxCullFace);
		horizontalBox.add(boxFill);
		horizontalBox.add(boxEnabled);

		
	}

	public void addComponents()
	{
		add(horizontalBox, BorderLayout.NORTH);
	}

	@Override
	public void updateValues(Element cube)
	{
		if (cube != null)
		{
			boxCullFace.setEnabled(true);
			boxCullFace.setSelected(cube.getSelectedFace().isCullfaced());
			boxFill.setEnabled(true);
			boxFill.setSelected(cube.getSelectedFace().shouldFitTexture());
			boxEnabled.setEnabled(true);
			boxEnabled.setSelected(cube.getSelectedFace().isEnabled());
		}
		else
		{
			boxCullFace.setEnabled(false);
			boxCullFace.setSelected(false);
			boxFill.setEnabled(false);
			boxFill.setSelected(false);
			boxEnabled.setEnabled(false);
			boxEnabled.setSelected(false);
		}
	}
}
