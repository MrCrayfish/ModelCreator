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

import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;

public class SizePanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ElementManager manager;

	private JButton btnPlusX;
	private JButton btnPlusY;
	private JButton btnPlusZ;
	private JTextField xSizeField;
	private JTextField ySizeField;
	private JTextField zSizeField;
	private JButton btnNegX;
	private JButton btnNegY;
	private JButton btnNegZ;

	private DecimalFormat df = new DecimalFormat("#.#");

	public SizePanel(ElementManager manager)
	{
		this.manager = manager;
		setLayout(new GridLayout(3, 3, 4, 4));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Size"));
		setMaximumSize(new Dimension(186, 124));
		initComponents();
		initProperties();
		addComponents();
	}

	public void initComponents()
	{
		btnPlusX = new JButton("+");
		btnPlusY = new JButton("+");
		btnPlusZ = new JButton("+");
		xSizeField = new JTextField();
		ySizeField = new JTextField();
		zSizeField = new JTextField();
		btnNegX = new JButton("-");
		btnNegY = new JButton("-");
		btnNegZ = new JButton("-");
	}

	public void initProperties()
	{
		Font defaultFont = new Font("SansSerif", Font.BOLD, 20);
		xSizeField.setSize(new Dimension(62, 30));
		xSizeField.setFont(defaultFont);
		xSizeField.setEditable(false);
		xSizeField.setHorizontalAlignment(JTextField.CENTER);

		ySizeField.setSize(new Dimension(62, 30));
		ySizeField.setFont(defaultFont);
		ySizeField.setEditable(false);
		ySizeField.setHorizontalAlignment(JTextField.CENTER);

		zSizeField.setSize(new Dimension(62, 30));
		zSizeField.setFont(defaultFont);
		zSizeField.setEditable(false);
		zSizeField.setHorizontalAlignment(JTextField.CENTER);

		btnPlusX.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				Element cube = manager.getSelectedCuboid();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addWidth(0.1F);
				}
				else
				{
					cube.addWidth(1.0F);
				}
				cube.updateUV();
				manager.updateValues();
			}
		});
		btnPlusX.setPreferredSize(new Dimension(62, 30));
		btnPlusX.setFont(defaultFont);
		btnPlusX.setToolTipText("<html>Increases the width.<br><b>Hold shift for decimals</b></html>");

		btnPlusY.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				Element cube = manager.getSelectedCuboid();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addHeight(0.1F);
				}
				else
				{
					cube.addHeight(1.0F);
				}
				cube.updateUV();
				manager.updateValues();
			}
		});
		btnPlusY.setPreferredSize(new Dimension(62, 30));
		btnPlusY.setFont(defaultFont);
		btnPlusY.setToolTipText("<html>Increases the height.<br><b>Hold shift for decimals</b></html>");

		btnPlusZ.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				Element cube = manager.getSelectedCuboid();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addDepth(0.1F);
				}
				else
				{
					cube.addDepth(1.0F);
				}
				cube.updateUV();
				manager.updateValues();
			}
		});
		btnPlusZ.setPreferredSize(new Dimension(62, 30));
		btnPlusZ.setFont(defaultFont);
		btnPlusZ.setToolTipText("<html>Increases the depth.<br><b>Hold shift for decimals</b></html>");

		btnNegX.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				Element cube = manager.getSelectedCuboid();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addWidth(-0.1F);
				}
				else
				{
					cube.addWidth(-1.0F);
				}
				cube.updateUV();
				manager.updateValues();
			}
		});
		btnNegX.setPreferredSize(new Dimension(62, 30));
		btnNegX.setFont(defaultFont);
		btnNegX.setToolTipText("<html>Decreases the width.<br><b>Hold shift for decimals</b></html>");

		btnNegY.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				Element cube = manager.getSelectedCuboid();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addHeight(-0.1F);
				}
				else
				{
					cube.addHeight(-1.0F);
				}
				cube.updateUV();
				manager.updateValues();
			}
		});
		btnNegY.setPreferredSize(new Dimension(62, 30));
		btnNegY.setFont(defaultFont);
		btnNegY.setToolTipText("<html>Decreases the height.<br><b>Hold shift for decimals</b></html>");

		btnNegZ.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				Element cube = manager.getSelectedCuboid();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addDepth(-0.1F);
				}
				else
				{
					cube.addDepth(-1.0F);
				}
				cube.updateUV();
				manager.updateValues();
			}
		});
		btnNegZ.setPreferredSize(new Dimension(62, 30));
		btnNegZ.setFont(defaultFont);
		btnNegZ.setToolTipText("<html>Decreases the depth.<br><b>Hold shift for decimals</b></html>");
	}

	public void addComponents()
	{
		add(btnPlusX);
		add(btnPlusY);
		add(btnPlusZ);
		add(xSizeField);
		add(ySizeField);
		add(zSizeField);
		add(btnNegX);
		add(btnNegY);
		add(btnNegZ);
	}

	@Override
	public void updateValues(Element cube)
	{
		if (cube != null)
		{
			xSizeField.setText(df.format(cube.getWidth()));
			ySizeField.setText(df.format(cube.getHeight()));
			zSizeField.setText(df.format(cube.getDepth()));
		}
		else
		{
			xSizeField.setText("");
			ySizeField.setText("");
			zSizeField.setText("");
		}
	}
}
