package com.mrcrayfish.modelcreator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PositionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

	private JButton btnPlusX;
	private JButton btnPlusY;
	private JButton btnPlusZ;
	private JTextField xPositionField;
	private JTextField yPositionField;
	private JTextField zPositionField;
	private JButton btnNegX;
	private JButton btnNegY;
	private JButton btnNegZ;

	private DecimalFormat df = new DecimalFormat("#.#");

	public PositionPanel(ModelCreator creator) {
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
		xPositionField = new JTextField();
		yPositionField = new JTextField();
		zPositionField = new JTextField();
		btnNegX = new JButton("-");
		btnNegY = new JButton("-");
		btnNegZ = new JButton("-");
	}

	public void initProperties() {
		Font defaultFont = new Font("SansSerif", Font.BOLD, 20);
		xPositionField.setSize(new Dimension(62, 30));
		xPositionField.setFont(defaultFont);
		xPositionField.setEditable(false);
		xPositionField.setHorizontalAlignment(JTextField.CENTER);

		yPositionField.setSize(new Dimension(62, 30));
		yPositionField.setFont(defaultFont);
		yPositionField.setEditable(false);
		yPositionField.setHorizontalAlignment(JTextField.CENTER);

		zPositionField.setSize(new Dimension(62, 30));
		zPositionField.setFont(defaultFont);
		zPositionField.setEditable(false);
		zPositionField.setHorizontalAlignment(JTextField.CENTER);

		btnPlusX.addActionListener(e -> {
			System.out.println("Hey");
			if (creator.getSelected() != null) {
				Cube cube = creator.getSelected();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1) {
					cube.addStartX(0.1F);
				} else {
					cube.addStartX(1.0F);
				}
				xPositionField.setText(cube.getStartX() + "");
			}
		});
		btnPlusX.setPreferredSize(new Dimension(62, 30));

		btnPlusY.addActionListener(e -> {
			if (creator.getSelected() != null) {
				Cube cube = creator.getSelected();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1) {
					cube.addStartY(0.1F);
				} else {
					cube.addStartY(1.0F);
				}
				yPositionField.setText(cube.getStartY() + "");
			}
		});
		btnPlusY.setPreferredSize(new Dimension(62, 30));

		btnPlusZ.addActionListener(e -> {
			if (creator.getSelected() != null) {
				Cube cube = creator.getSelected();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1) {
					cube.addStartZ(0.1F);
				} else {
					cube.addStartZ(1.0F);
				}
				zPositionField.setText(cube.getStartZ() + "");
			}
		});
		btnPlusZ.setPreferredSize(new Dimension(62, 30));

		btnNegX.addActionListener(e -> {
			if (creator.getSelected() != null) {
				Cube cube = creator.getSelected();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1) {
					cube.addStartX(-0.1F);
				} else {
					cube.addStartX(-1.0F);
				}
				xPositionField.setText(cube.getStartX() + "");
			}
		});
		btnNegX.setPreferredSize(new Dimension(62, 30));

		btnNegY.addActionListener(e -> {
			if (creator.getSelected() != null) {
				Cube cube = creator.getSelected();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1) {
					cube.addStartY(-0.1F);
				} else {
					cube.addStartY(-1.0F);
				}
				yPositionField.setText(cube.getStartY() + "");
			}
		});
		btnNegY.setPreferredSize(new Dimension(62, 30));

		btnNegZ.addActionListener(e -> {
			if (creator.getSelected() != null) {
				Cube cube = creator.getSelected();
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1) {
					cube.addStartZ(-0.1F);
				} else {
					cube.addStartZ(-1.0F);
				}
				zPositionField.setText(cube.getStartZ() + "");
			}
		});
		btnNegZ.setPreferredSize(new Dimension(62, 30));
	}

	public void addComponents() {
		add(btnPlusX);
		add(btnPlusY);
		add(btnPlusZ);
		add(xPositionField);
		add(yPositionField);
		add(zPositionField);
		add(btnNegX);
		add(btnNegY);
		add(btnNegZ);
	}

	public void updateValues(Cube cube) {
		if (cube != null) {
			xPositionField.setText(df.format(cube.getStartX()));
			yPositionField.setText(df.format(cube.getStartY()));
			zPositionField.setText(df.format(cube.getStartZ()));
		} else {
			xPositionField.setText("");
			yPositionField.setText("");
			zPositionField.setText("");
		}
	}
}
