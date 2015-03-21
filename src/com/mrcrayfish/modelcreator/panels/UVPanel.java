package com.mrcrayfish.modelcreator.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mrcrayfish.modelcreator.Cuboid;
import com.mrcrayfish.modelcreator.Face;
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.ModelCreator;

public class UVPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;
	private JButton btnPlusX;
	private JButton btnPlusY;
	private JTextField xStartField;
	private JTextField yStartField;
	private JButton btnNegX;
	private JButton btnNegY;

	public UVPanel(ModelCreator creator)
	{
		this.creator = creator;
		setLayout(new GridLayout(3, 3));
		setBorder(BorderFactory.createTitledBorder("UV"));
		setMaximumSize(new Dimension(186,124));
		initComponents();
		initProperties();
		addComponents();
	}

	public void initComponents()
	{
		btnPlusX = new JButton("+");
		btnPlusY = new JButton("+");
		xStartField = new JTextField();
		yStartField = new JTextField();
		btnNegX = new JButton("-");
		btnNegY = new JButton("-");
	}

	public void initProperties()
	{
		Font defaultFont = new Font("SansSerif", Font.BOLD, 20);
		xStartField.setSize(new Dimension(62, 30));
		xStartField.setFont(defaultFont);
		xStartField.setEditable(false);
		xStartField.setHorizontalAlignment(JTextField.CENTER);

		yStartField.setSize(new Dimension(62, 30));
		yStartField.setFont(defaultFont);
		yStartField.setEditable(false);
		yStartField.setHorizontalAlignment(JTextField.CENTER);

		btnPlusX.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				Cuboid cube = creator.getSelectedCuboid();
				Face face = cube.getSelectedFace();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					face.addTextureX(0.1);
				}
				else
				{
					face.addTextureX(1.0);
				}
				xStartField.setText(face.getStartU() + "");
			}
		});
		btnPlusX.setSize(new Dimension(62, 30));

		btnPlusY.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				Cuboid cube = creator.getSelectedCuboid();
				Face face = cube.getSelectedFace();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					face.addTextureY(0.1);
				}
				else
				{
					face.addTextureY(1.0);
				}
				yStartField.setText(face.getStartV() + "");
			}
		});
		btnPlusY.setPreferredSize(new Dimension(62, 30));

		btnNegX.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				Cuboid cube = creator.getSelectedCuboid();
				Face face = cube.getSelectedFace();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					face.addTextureX(-0.1);
				}
				else
				{
					face.addTextureX(-1.0);
				}
				xStartField.setText(face.getStartU() + "");
			}
		});
		btnNegX.setSize(new Dimension(62, 30));

		btnNegY.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				Cuboid cube = creator.getSelectedCuboid();
				Face face = cube.getSelectedFace();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					face.addTextureY(-0.1);
				}
				else
				{
					face.addTextureY(-1.0);
				}
				yStartField.setText(face.getStartV() + "");
			}
		});
		btnNegY.setSize(new Dimension(62, 30));
	}

	public void addComponents()
	{
		add(btnPlusX);
		add(btnPlusY);
		add(xStartField);
		add(yStartField);
		add(btnNegX);
		add(btnNegY);
	}

	@Override
	public void updateValues(Cuboid cube)
	{
		xStartField.setText(cube.getSelectedFace().getStartU() + "");
		yStartField.setText(cube.getSelectedFace().getStartV() + "");
	}
}
