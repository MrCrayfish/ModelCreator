package com.mrcrayfish.modelcreator;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glVertex3i;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

public class ModelCreator extends JFrame
{
	private static final long serialVersionUID = 1L;

	private final Canvas canvas;
	private SpringLayout layout;
	private Camera camera;
	public Texture texture;

	public boolean closeRequested = false;

	private JTabbedPane tabbedPane = new JTabbedPane();
	private SizePanel panelSize = new SizePanel(this);
	private PositionPanel panelPosition = new PositionPanel(this);
	private JButton btnAdd = new JButton("Add");
	private JButton btnRemove = new JButton("Remove");
	private JList<Cube> list = new JList<Cube>();
	private JScrollPane scrollPane;

	private DefaultListModel<Cube> model = new DefaultListModel<Cube>();

	public ModelCreator(String title)
	{
		super(title);

		layout = new SpringLayout();
		canvas = new Canvas();

		setResizable(false);
		setPreferredSize(new Dimension(1400, 700));
		setLayout(layout);

		initDisplay();

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
		TextureManager.init();

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
			model.addElement(new Cube(1, 1, 1));
			list.setSelectedIndex(model.size() - 1);
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

		list.setModel(model);
		list.addListSelectionListener(e ->
		{
			Cube cube = getSelected();
			if (cube != null)
				panelSize.updateValues(cube);
				panelPosition.updateValues(cube);
		});

		scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(190, 300));
		add(scrollPane);

		tabbedPane.add("Size", panelSize);
		tabbedPane.add("Position", panelPosition);
		add(tabbedPane);
	}

	public void setLayoutConstaints()
	{
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 1005, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, btnAdd, 310, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, btnAdd, 1003, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, btnRemove, 310, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, btnRemove, 1102, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, tabbedPane, 341, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, tabbedPane, 1005, SpringLayout.WEST, this);
	}

	public void initDisplay()
	{
		try
		{
			Display.setParent(canvas);
			Display.setVSyncEnabled(true);
			Display.setInitialBackground(0.75F, 0.75F, 0.75F);
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	}

	private void loop()
	{
		camera = new Camera(60, (float) Display.getWidth() / (float) Display.getHeight(), 0.3F, 1000);

		Font awtFont = new Font("Times New Roman", Font.PLAIN, 24);
		TrueTypeFont font = new TrueTypeFont(awtFont, true);

		while (!Display.isCloseRequested() && !closeRequested)
		{
			handleInput();

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glLoadIdentity();
			camera.useView();
			glScalef(0.25F, 0.25F, 0.25F);
			drawGrid();
			drawAxis();
			glTranslatef(-8, 0, 8);
			for (int i = 0; i < model.size(); i++)
			{
				Cube cube = (Cube) model.getElementAt(i);
				cube.draw();
				cube.drawExtras();
			}

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
		if (getSelected() != null)
		{
			Cube cube = getSelected();
			// DecimalFormat df = new DecimalFormat("#.#");
			// xSizeField.setText(df.format(cube.getMaxX()));
			// ySizeField.setText(df.format(cube.getMaxY()));
			// zSizeField.setText(df.format(cube.getMaxZ()));
		}
		else
		{
			// xSizeField.setText("");
			// ySizeField.setText("");
			// zSizeField.setText("");
		}
	}

	public void drawGrid()
	{
		glPushMatrix();
		{
			glColor3f(0, 0, 0);

			// Bold outside lines
			glLineWidth(2F);
			glBegin(GL_LINES);
			{
				glVertex3i(-8, 0, -8);
				glVertex3i(-8, 0, 8);
				glVertex3i(8, 0, -8);
				glVertex3i(8, 0, 8);
				glVertex3i(-8, 0, 8);
				glVertex3i(8, 0, 8);
				glVertex3i(-8, 0, -8);
				glVertex3i(8, 0, -8);
			}
			glEnd();

			// Thin inside lines
			glLineWidth(1F);
			glBegin(GL_LINES);
			{
				for (int i = -7; i <= 7; i++)
				{
					glVertex3i(i, 0, -8);
					glVertex3i(i, 0, 8);
				}

				for (int i = -7; i <= 7; i++)
				{
					glVertex3i(-8, 0, i);
					glVertex3i(8, 0, i);
				}
			}
			glEnd();
		}
		glPopMatrix();
	}

	public void drawAxis()
	{
		glPushMatrix();
		{
			GL11.glLineWidth(5F);
			glTranslatef(-9, 0, -9);
			glBegin(GL_LINES);
			{
				glColor4f(0, 1, 0, 0.5F);
				glVertex3f(40F, 0.01F, 0);
				glVertex3f(0, 0.01F, 0);

				glColor4f(1, 0, 0, 0.5F);
				glVertex3f(0, 0.01F, 0);
				glVertex3f(0, 40F, 0);

				glColor4f(0, 0, 1, 0.5F);
				glVertex3f(0, 0.01F, 40F);
				glVertex3f(0, 0.01F, 0);
			}
			glEnd();
		}
		glPopMatrix();
	}

	public Cube getSelected()
	{
		int i = list.getSelectedIndex();
		if (i != -1)
			return (Cube) model.getElementAt(i);
		return null;
	}
}
