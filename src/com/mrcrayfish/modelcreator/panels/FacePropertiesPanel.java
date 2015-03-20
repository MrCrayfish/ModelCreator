package com.mrcrayfish.modelcreator.panels;

import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.mrcrayfish.modelcreator.Cube;
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.ModelCreator;

public class FacePropertiesPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;
	
	private ModelCreator creator;
	
	private JRadioButton boxCullFace;
	
	public FacePropertiesPanel(ModelCreator creator)
	{
		this.creator = creator;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createTitledBorder("Properties"));
		
		initComponents();
		addComponents();
		
		int i = new Random().nextInt(5);
	}
	
	public void initComponents()
	{
		boxCullFace = new JRadioButton("Cullface");
	}
	
	public void addComponents()
	{
		add(boxCullFace);
	}

	@Override
	public void updateValues(Cube cube)
	{
		
	}

}
