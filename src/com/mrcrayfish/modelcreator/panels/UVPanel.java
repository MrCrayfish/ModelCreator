package com.mrcrayfish.modelcreator.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mrcrayfish.modelcreator.Cuboid;
import com.mrcrayfish.modelcreator.CuboidManager;
import com.mrcrayfish.modelcreator.Face;
import com.mrcrayfish.modelcreator.IValueUpdater;

public class UVPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private CuboidManager manager;
	private JButton btnPlusX;
	private JButton btnPlusY;
	private JTextField xStartField;
	private JTextField yStartField;
	private JButton btnNegX;
	private JButton btnNegY;

	private DecimalFormat df = new DecimalFormat("#.#");

	public UVPanel(CuboidManager manager)
	{
		this.manager = manager;
		setLayout(new GridLayout(3, 3, 4, 4));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "UV"));
		setMaximumSize(new Dimension(186, 124));
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
			if (manager.getSelectedCuboid() != null)
			{
				Cuboid cube = manager.getSelectedCuboid();
				Face face = cube.getSelectedFace();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					face.addTextureX(0.1);
				}
				else
				{
					face.addTextureX(1.0);
				}
				xStartField.setText(df.format(face.getStartU()));
			}
		});
		btnPlusX.setSize(new Dimension(62, 30));
		btnPlusX.setFont(defaultFont);

		btnPlusY.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				Cuboid cube = manager.getSelectedCuboid();
				Face face = cube.getSelectedFace();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					face.addTextureY(0.1);
				}
				else
				{
					face.addTextureY(1.0);
				}
				yStartField.setText(df.format(face.getStartV()));
			}
		});
		btnPlusY.setPreferredSize(new Dimension(62, 30));
		btnPlusY.setFont(defaultFont);

		btnNegX.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				Cuboid cube = manager.getSelectedCuboid();
				Face face = cube.getSelectedFace();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					face.addTextureX(-0.1);
				}
				else
				{
					face.addTextureX(-1.0);
				}
				xStartField.setText(df.format(face.getStartU()));
			}
		});
		btnNegX.setSize(new Dimension(62, 30));
		btnNegX.setFont(defaultFont);

		btnNegY.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				Cuboid cube = manager.getSelectedCuboid();
				Face face = cube.getSelectedFace();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					face.addTextureY(-0.1);
				}
				else
				{
					face.addTextureY(-1.0);
				}
				yStartField.setText(df.format(face.getStartV()));
			}
		});
		btnNegY.setSize(new Dimension(62, 30));
		btnNegY.setFont(defaultFont);
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
		if (cube != null)
		{
			xStartField.setText(df.format(cube.getSelectedFace().getStartU()));
			yStartField.setText(df.format(cube.getSelectedFace().getStartV()));
		}
		else
		{
			xStartField.setText("");
			yStartField.setText("");
		}
	}
}
