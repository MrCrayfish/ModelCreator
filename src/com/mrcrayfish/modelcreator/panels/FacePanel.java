package com.mrcrayfish.modelcreator.panels;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.mrcrayfish.modelcreator.Cube;
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.ModelCreator;

public class FacePanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;
	
	private JButton btnTexture;
	private TexturePosPanel panel;
	
	public FacePanel(ModelCreator creator)
	{
		this.creator = creator;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createTitledBorder("Texture"));
		initComponents();
		addComponents();
	}
	
	public void initComponents() {
		btnTexture = new JButton("Select Texture");
		panel = new TexturePosPanel(creator);
	}
	
	public void addComponents() {
		add(btnTexture);
		add(panel);
	}

	@Override
	public void updateValues(Cube cube)
	{

	}
}
