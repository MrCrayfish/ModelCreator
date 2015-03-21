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
import com.mrcrayfish.modelcreator.IValueUpdater;
import com.mrcrayfish.modelcreator.ModelCreator;

public class OriginPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

	private JButton btnPlusX;
	private JButton btnPlusY;
	private JButton btnPlusZ;
	private JTextField xOriginField;
	private JTextField yOriginField;
	private JTextField zOriginField;
	private JButton btnNegX;
	private JButton btnNegY;
	private JButton btnNegZ;

	private DecimalFormat df = new DecimalFormat("#.#");

	public OriginPanel(ModelCreator creator)
	{
		this.creator = creator;
		setLayout(new GridLayout(3, 3));
		setBorder(BorderFactory.createTitledBorder("Origin"));
		setMaximumSize(new Dimension(186,124));
		initComponents();
		initProperties();
		addComponents();
	}

	public void initComponents()
	{
		btnPlusX = new JButton("+");
		btnPlusY = new JButton("+");
		btnPlusZ = new JButton("+");
		xOriginField = new JTextField();
		yOriginField = new JTextField();
		zOriginField = new JTextField();
		btnNegX = new JButton("-");
		btnNegY = new JButton("-");
		btnNegZ = new JButton("-");
	}

	public void initProperties()
	{
		Font defaultFont = new Font("SansSerif", Font.BOLD, 20);
		xOriginField.setSize(new Dimension(62, 30));
		xOriginField.setFont(defaultFont);
		xOriginField.setEditable(false);
		xOriginField.setHorizontalAlignment(JTextField.CENTER);

		yOriginField.setSize(new Dimension(62, 30));
		yOriginField.setFont(defaultFont);
		yOriginField.setEditable(false);
		yOriginField.setHorizontalAlignment(JTextField.CENTER);

		zOriginField.setSize(new Dimension(62, 30));
		zOriginField.setFont(defaultFont);
		zOriginField.setEditable(false);
		zOriginField.setHorizontalAlignment(JTextField.CENTER);

		btnPlusX.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				Cuboid cube = creator.getSelectedCuboid();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addStartX(0.1F);
				}
				else
				{
					cube.addStartX(1.0F);
				}
				xOriginField.setText(cube.getStartX() + "");
			}
		});
		btnPlusX.setPreferredSize(new Dimension(62, 30));

		btnPlusY.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				Cuboid cube = creator.getSelectedCuboid();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addStartY(0.1F);
				}
				else
				{
					cube.addStartY(1.0F);
				}
				yOriginField.setText(cube.getStartY() + "");
			}
		});
		btnPlusY.setPreferredSize(new Dimension(62, 30));

		btnPlusZ.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				Cuboid cube = creator.getSelectedCuboid();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addStartZ(0.1F);
				}
				else
				{
					cube.addStartZ(1.0F);
				}
				zOriginField.setText(cube.getStartZ() + "");
			}
		});
		btnPlusZ.setPreferredSize(new Dimension(62, 30));

		btnNegX.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				Cuboid cube = creator.getSelectedCuboid();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addStartX(-0.1F);
				}
				else
				{
					cube.addStartX(-1.0F);
				}
				xOriginField.setText(cube.getStartX() + "");
			}
		});
		btnNegX.setPreferredSize(new Dimension(62, 30));

		btnNegY.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				Cuboid cube = creator.getSelectedCuboid();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addStartY(-0.1F);
				}
				else
				{
					cube.addStartY(-1.0F);
				}
				yOriginField.setText(cube.getStartY() + "");
			}
		});
		btnNegY.setPreferredSize(new Dimension(62, 30));

		btnNegZ.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				Cuboid cube = creator.getSelectedCuboid();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addStartZ(-0.1F);
				}
				else
				{
					cube.addStartZ(-1.0F);
				}
				zOriginField.setText(cube.getStartZ() + "");
			}
		});
		btnNegZ.setPreferredSize(new Dimension(62, 30));
	}

	public void addComponents()
	{
		add(btnPlusX);
		add(btnPlusY);
		add(btnPlusZ);
		add(xOriginField);
		add(yOriginField);
		add(zOriginField);
		add(btnNegX);
		add(btnNegY);
		add(btnNegZ);
	}

	@Override
	public void updateValues(Cuboid cube)
	{
		if (cube != null)
		{
			xOriginField.setText(df.format(cube.getOriginX()));
			yOriginField.setText(df.format(cube.getOriginY()));
			zOriginField.setText(df.format(cube.getOriginZ()));
		}
		else
		{
			xOriginField.setText("");
			yOriginField.setText("");
			zOriginField.setText("");
		}
	}
}
