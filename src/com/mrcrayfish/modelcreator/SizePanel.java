package com.mrcrayfish.modelcreator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SizePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

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

	public SizePanel(ModelCreator creator) {
		this.creator = creator;
		setLayout(new GridLayout(3, 3));
		initComponents();
		initProperties();
		addComponents();
	}

	public void initComponents() {
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

	public void initProperties() {
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

		btnPlusX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != 1)
					return;

				if (creator.getSelected() != null) {
					Cube cube = creator.getSelected();
					if (e.isShiftDown()) {
						cube.addWidth(0.1F);
					} else {
						cube.addWidth(1.0F);
					}
					xSizeField.setText(cube.getWidth() + "");
				}
			}
		});
		btnPlusX.setPreferredSize(new Dimension(62, 30));

		btnPlusY.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != 1)
					return;

				if (creator.getSelected() != null) {
					Cube cube = creator.getSelected();
					if (e.isShiftDown()) {
						cube.addHeight(0.1F);
					} else {
						cube.addHeight(1.0F);
					}
					ySizeField.setText(cube.getHeight() + "");
				}
			}
		});
		btnPlusY.setPreferredSize(new Dimension(62, 30));

		btnPlusZ.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != 1)
					return;

				if (creator.getSelected() != null) {
					Cube cube = creator.getSelected();
					if (e.isShiftDown()) {
						cube.addDepth(0.1F);
					} else {
						cube.addDepth(1.0F);
					}
					zSizeField.setText(cube.getDepth() + "");
				}
			}
		});
		btnPlusZ.setPreferredSize(new Dimension(62, 30));

		btnNegX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != 1)
					return;

				if (creator.getSelected() != null) {
					Cube cube = creator.getSelected();
					if (e.isShiftDown()) {
						cube.addWidth(-0.1F);
					} else {
						cube.addWidth(-1.0F);
					}
					xSizeField.setText(cube.getWidth() + "");
				}
			}
		});
		btnNegX.setPreferredSize(new Dimension(62, 30));

		btnNegY.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != 1)
					return;

				if (creator.getSelected() != null) {
					Cube cube = creator.getSelected();
					if (e.isShiftDown()) {
						cube.addHeight(-0.1F);
					} else {
						cube.addHeight(-1.0F);
					}
					ySizeField.setText(cube.getHeight() + "");
				}
			}
		});
		btnNegY.setPreferredSize(new Dimension(62, 30));

		btnNegZ.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != 1)
					return;

				if (creator.getSelected() != null) {
					Cube cube = creator.getSelected();
					if (e.isShiftDown()) {
						cube.addDepth(-0.1F);
					} else {
						cube.addDepth(-1.0F);
					}
					zSizeField.setText(cube.getDepth() + "");
				}
			}
		});
		btnNegZ.setPreferredSize(new Dimension(62, 30));
	}

	public void addComponents() {
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
	
	public void updateValues(Cube cube)
	{
		if (cube != null) {
			xSizeField.setText(df.format(cube.getWidth()));
			ySizeField.setText(df.format(cube.getHeight()));
			zSizeField.setText(df.format(cube.getDepth()));
		} else {
			xSizeField.setText("");
			ySizeField.setText("");
			zSizeField.setText("");
		}
	}
}
