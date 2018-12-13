package com.mrcrayfish.modelcreator.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mrcrayfish.modelcreator.Exporter;
import com.mrcrayfish.modelcreator.Icons;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.util.Parser;

public class OriginPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

	private ElementManager manager;

	private JButton btnPlusX;
	private JButton btnPlusY;
	private JButton btnPlusZ;
	private JTextField xOriginField;
	private JTextField yOriginField;
	private JTextField zOriginField;
	private JButton btnNegX;
	private JButton btnNegY;
	private JButton btnNegZ;

	public OriginPanel(ElementManager manager)
	{
		this.manager = manager;
		this.setLayout(new GridLayout(3, 3, 4, 4));
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Origin</b></html>"));
		this.setMaximumSize(new Dimension(186, 124));
		this.initComponents();
		this.initProperties();
		this.addComponents();
	}

	public void initComponents()
	{
		btnPlusX = new JButton(Icons.arrow_up);
		btnPlusY = new JButton(Icons.arrow_up);
		btnPlusZ = new JButton(Icons.arrow_up);
		xOriginField = new JTextField();
		yOriginField = new JTextField();
		zOriginField = new JTextField();
		btnNegX = new JButton(Icons.arrow_down);
		btnNegY = new JButton(Icons.arrow_down);
		btnNegZ = new JButton(Icons.arrow_down);
	}

	public void initProperties()
	{
		Font defaultFont = new Font("SansSerif", Font.BOLD, 20);
		xOriginField.setSize(new Dimension(62, 30));
		xOriginField.setFont(defaultFont);
		xOriginField.setHorizontalAlignment(JTextField.CENTER);
		xOriginField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					Element element = manager.getSelectedElement();
					if (element != null)
					{
						element.setOriginX((Parser.parseDouble(xOriginField.getText(), element.getOriginX())));
						manager.updateValues();
					}
				}
			}
		});
		xOriginField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				Element element = manager.getSelectedElement();
				if (element != null)
				{
					element.setOriginX((Parser.parseDouble(xOriginField.getText(), element.getOriginX())));
					manager.updateValues();
				}
			}
		});

		yOriginField.setSize(new Dimension(62, 30));
		yOriginField.setFont(defaultFont);
		yOriginField.setHorizontalAlignment(JTextField.CENTER);
		yOriginField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					Element element = manager.getSelectedElement();
					if (element != null)
					{
						element.setOriginY((Parser.parseDouble(yOriginField.getText(), element.getOriginY())));
						manager.updateValues();
					}
				}
			}
		});
		yOriginField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				Element element = manager.getSelectedElement();
				if (element != null)
				{
					element.setOriginY((Parser.parseDouble(yOriginField.getText(), element.getOriginY())));
					manager.updateValues();
				}
			}
		});

		zOriginField.setSize(new Dimension(62, 30));
		zOriginField.setFont(defaultFont);
		zOriginField.setHorizontalAlignment(JTextField.CENTER);
		zOriginField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					Element element = manager.getSelectedElement();
					if (element != null)
					{
						element.setOriginZ((Parser.parseDouble(zOriginField.getText(), element.getOriginZ())));
						manager.updateValues();
					}
				}
			}
		});
		zOriginField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				Element element = manager.getSelectedElement();
				if (element != null)
				{
					element.setOriginZ((Parser.parseDouble(zOriginField.getText(), element.getOriginZ())));
					manager.updateValues();
				}
			}
		});

		btnPlusX.addActionListener(e ->
		{
			if (manager.getSelectedElement() != null)
			{
				Element cube = manager.getSelectedElement();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addOriginX(0.1F);
				}
				else
				{
					cube.addOriginX(1.0F);
				}
				xOriginField.setText(Exporter.FORMAT.format(cube.getOriginX()));
			}
		});
		btnPlusX.setPreferredSize(new Dimension(62, 30));
		btnPlusX.setFont(defaultFont);
		btnPlusX.setToolTipText("<html>Increases the X origin.<br><b>Hold shift for decimals</b></html>");

		btnPlusY.addActionListener(e ->
		{
			if (manager.getSelectedElement() != null)
			{
				Element cube = manager.getSelectedElement();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addOriginY(0.1F);
				}
				else
				{
					cube.addOriginY(1.0F);
				}
				yOriginField.setText(Exporter.FORMAT.format(cube.getOriginY()));
			}
		});
		btnPlusY.setPreferredSize(new Dimension(62, 30));
		btnPlusY.setFont(defaultFont);
		btnPlusY.setToolTipText("<html>Increases the Y origin.<br><b>Hold shift for decimals</b></html>");

		btnPlusZ.addActionListener(e ->
		{
			if (manager.getSelectedElement() != null)
			{
				Element cube = manager.getSelectedElement();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addOriginZ(0.1F);
				}
				else
				{
					cube.addOriginZ(1.0F);
				}
				zOriginField.setText(Exporter.FORMAT.format(cube.getOriginZ()));
			}
		});
		btnPlusZ.setPreferredSize(new Dimension(62, 30));
		btnPlusZ.setFont(defaultFont);
		btnPlusZ.setToolTipText("<html>Increases the Z origin.<br><b>Hold shift for decimals</b></html>");

		btnNegX.addActionListener(e ->
		{
			if (manager.getSelectedElement() != null)
			{
				Element cube = manager.getSelectedElement();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addOriginX(-0.1F);
				}
				else
				{
					cube.addOriginX(-1.0F);
				}
				xOriginField.setText(Exporter.FORMAT.format(cube.getOriginX()));
			}
		});
		btnNegX.setPreferredSize(new Dimension(62, 30));
		btnNegX.setFont(defaultFont);
		btnNegX.setToolTipText("<html>Decreases the X origin.<br><b>Hold shift for decimals</b></html>");

		btnNegY.addActionListener(e ->
		{
			if (manager.getSelectedElement() != null)
			{
				Element cube = manager.getSelectedElement();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addOriginY(-0.1F);
				}
				else
				{
					cube.addOriginY(-1.0F);
				}
				yOriginField.setText(Exporter.FORMAT.format(cube.getOriginY()));
			}
		});
		btnNegY.setPreferredSize(new Dimension(62, 30));
		btnNegY.setFont(defaultFont);
		btnNegY.setToolTipText("<html>Decreases the Y origin.<br><b>Hold shift for decimals</b></html>");

		btnNegZ.addActionListener(e ->
		{
			if (manager.getSelectedElement() != null)
			{
				Element cube = manager.getSelectedElement();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					cube.addOriginZ(-0.1F);
				}
				else
				{
					cube.addOriginZ(-1.0F);
				}
				zOriginField.setText(Exporter.FORMAT.format(cube.getOriginZ()));
			}
		});
		btnNegZ.setPreferredSize(new Dimension(62, 30));
		btnNegZ.setFont(defaultFont);
		btnNegZ.setToolTipText("<html>Decreases the Z origin.<br><b>Hold shift for decimals</b></html>");
	}

	public void addComponents()
	{
		this.add(btnPlusX);
		this.add(btnPlusY);
		this.add(btnPlusZ);
		this.add(xOriginField);
		this.add(yOriginField);
		this.add(zOriginField);
		this.add(btnNegX);
		this.add(btnNegY);
		this.add(btnNegZ);
	}

	@Override
	public void updateValues(Element cube)
	{
		if (cube != null)
		{
			xOriginField.setEnabled(true);
			yOriginField.setEnabled(true);
			zOriginField.setEnabled(true);
			xOriginField.setText(Exporter.FORMAT.format(cube.getOriginX()));
			yOriginField.setText(Exporter.FORMAT.format(cube.getOriginY()));
			zOriginField.setText(Exporter.FORMAT.format(cube.getOriginZ()));
		}
		else
		{
			xOriginField.setEnabled(false);
			yOriginField.setEnabled(false);
			zOriginField.setEnabled(false);
			xOriginField.setText("");
			yOriginField.setText("");
			zOriginField.setText("");
		}
	}
}
