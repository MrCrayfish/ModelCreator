package com.mrcrayfish.modelcreator;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class ModelCreator extends JFrame
{
	private static final long serialVersionUID = 1L;

	private final Canvas canvas;
	private SpringLayout layout;
	private Camera camera;

	public boolean closeRequested = false;
	private boolean isShifting = false;

	private JButton btnAdd = new JButton("Add");
	private JButton btnRemove = new JButton("Remove");
	private JButton btnPlusX = new JButton("+");
	private JButton btnPlusY = new JButton("+");
	private JButton btnPlusZ = new JButton("+");
	private JButton btnNegX = new JButton("-");
	private JButton btnNegY = new JButton("-");
	private JButton btnNegZ = new JButton("-");
	private JTextField xSizeField = new JTextField();
	private JTextField ySizeField = new JTextField();
	private JTextField zSizeField = new JTextField();

	private JList<Shape> list = new JList<Shape>();
	private JScrollPane scrollPane;

	private DefaultListModel<Shape> model = new DefaultListModel<Shape>();

	public ModelCreator(String title)
	{
		super(title);

		layout = new SpringLayout();
		canvas = new Canvas();

		setResizable(false);
		setPreferredSize(new Dimension(1200, 700));
		setLayout(layout);

		initComponents();
		setLayoutConstaints();

		addWindowFocusListener(new WindowAdapter()
		{
			@Override
			public void windowGainedFocus(WindowEvent e)
			{
				canvas.requestFocusInWindow();
			}
		});

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				closeRequested = true;
			}
		});

		initDisplay();
		pack();
		setVisible(true);
		try
		{
			Display.create();
		}
		catch (LWJGLException e1)
		{
			e1.printStackTrace();
		}

		// Program loop
		loop();

		Display.destroy();
		dispose();
		System.exit(0);
	}

	public void initComponents()
	{
		canvas.setSize(new Dimension(1000, 700));
		add(canvas);

		btnAdd.addActionListener(e ->
		{
			model.addElement(new Cube(0, 0, 0, 1, 1, 1));
		});
		btnAdd.setPreferredSize(new Dimension(95, 30));
		add(btnAdd);

		btnRemove.addActionListener(e ->
		{
			int selected = list.getSelectedIndex();
			if (selected != -1)
				model.remove(selected);
		});
		btnRemove.setPreferredSize(new Dimension(95, 30));
		add(btnRemove);

		Font defaultFont = new Font("SansSerif", Font.BOLD, 20);

		xSizeField.setPreferredSize(new Dimension(60, 30));
		xSizeField.setFont(defaultFont);
		xSizeField.setEditable(false);
		xSizeField.setHorizontalAlignment(JTextField.CENTER);
		add(xSizeField);

		ySizeField.setPreferredSize(new Dimension(60, 30));
		ySizeField.setFont(defaultFont);
		ySizeField.setEditable(false);
		ySizeField.setHorizontalAlignment(JTextField.CENTER);
		add(ySizeField);

		zSizeField.setPreferredSize(new Dimension(60, 30));
		zSizeField.setFont(defaultFont);
		zSizeField.setEditable(false);
		zSizeField.setHorizontalAlignment(JTextField.CENTER);
		add(zSizeField);

		btnPlusX.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int selected = list.getSelectedIndex();
				if (selected != -1)
				{
					Cube cube = (Cube) model.getElementAt(selected);
					if (e.isShiftDown())
					{
						cube.addX(0.1F);
					}
					else
					{
						cube.addX(1.0F);
					}
					xSizeField.setText(cube.getMaxX() + "");
					updateValues(selected);
				}
			}
		});
		btnPlusX.setPreferredSize(new Dimension(62, 30));
		add(btnPlusX);

		btnPlusY.addActionListener(e ->
		{
			int selected = list.getSelectedIndex();
			if (selected != -1)
			{
				Cube cube = (Cube) model.getElementAt(selected);
				if (true)
				{
					cube.addY(0.1F);
				}
				else
				{
					cube.addY(1.0F);
				}
			}
		});
		btnPlusY.setPreferredSize(new Dimension(62, 30));
		add(btnPlusY);
		
		btnPlusZ.addActionListener(e ->
		{
			int selected = list.getSelectedIndex();
			if (selected != -1)
			{
				Cube cube = (Cube) model.getElementAt(selected);
				if (true)
				{
					cube.addZ(0.1F);
				}
				else
				{
					cube.addZ(1.0F);
				}
			}
		});
		btnPlusZ.setPreferredSize(new Dimension(62, 30));
		add(btnPlusZ);

		list.setModel(model);
		list.addListSelectionListener(e ->
		{
			int selected = list.getSelectedIndex();
			updateValues(selected);
		});

		scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(190, 300));
		add(scrollPane);
	}

	public void setLayoutConstaints()
	{
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 1005, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, btnAdd, 310, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, btnAdd, 1003, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, btnRemove, 310, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, btnRemove, 1102, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, xSizeField, 375, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, xSizeField, 1003, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, ySizeField, 375, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, ySizeField, 1070, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, zSizeField, 375, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, zSizeField, 1137, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, btnPlusX, 344, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, btnPlusX, 1003, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, btnPlusY, 344, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, btnPlusY, 1069, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, btnPlusZ, 344, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, btnPlusZ, 1135, SpringLayout.WEST, this);
	}

	public void initDisplay()
	{
		try
		{
			Display.setParent(canvas);
			Display.setVSyncEnabled(true);
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	}

	private void loop()
	{
		camera = new Camera(60, (float) Display.getWidth() / (float) Display.getHeight(), 0.3F, 1000);

		while (!Display.isCloseRequested() && !closeRequested)
		{
			handleInput();

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			camera.useView();

			GL11.glPushMatrix();
			{
				GL11.glScalef(0.3F, 0.3F, 0.3F);
				for (int i = 0; i < model.size(); i++)
				{
					Shape shape = model.getElementAt(i);
					GL11.glBegin(GL11.GL_QUADS);
					{
						shape.draw();
					}
					GL11.glEnd();
				}
			}
			GL11.glPopMatrix();
			Display.update();
		}
		System.out.println("Hey");
	}

	public void handleInput()
	{
		if (Mouse.isButtonDown(0))
		{
			camera.addX((float) Mouse.getDX() * 0.01F);
			camera.addY((float) Mouse.getDY() * 0.01F);
		}
		else if (Mouse.isButtonDown(1))
		{
			camera.rotateY((float) Mouse.getDX() * 0.5F);
			camera.rotateX(-(float) Mouse.getDY() * 0.5F);
		}

		camera.addZ((float) Mouse.getDWheel() / 100F);
	}

	public void updateValues(int selected)
	{
		if (selected != -1)
		{
			Cube cube = (Cube) model.getElementAt(selected);
			DecimalFormat df = new DecimalFormat("#.#");
			xSizeField.setText(df.format(cube.getMaxX()));
			ySizeField.setText(df.format(cube.getMaxY()));
			zSizeField.setText(df.format(cube.getMaxZ()));
		}
		else
		{
			xSizeField.setText("");
			ySizeField.setText("");
			zSizeField.setText("");
		}
	}
}
