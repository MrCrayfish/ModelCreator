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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import com.mrcrayfish.modelcreator.panels.CuboidTabbedPane;
import com.mrcrayfish.modelcreator.panels.TabPanel;
import com.mrcrayfish.modelcreator.texture.PendingTexture;
import com.mrcrayfish.modelcreator.texture.TextureManager;

public class ModelCreator extends JFrame
{
	private static final long serialVersionUID = 1L;

	private final Canvas canvas;
	private SpringLayout layout;
	private Camera camera;
	public Texture texture;

	public boolean closeRequested = false;

	private JMenuBar menuBar = new JMenuBar();
	private JList<Cube> list = new JList<Cube>();
	private JScrollPane scrollPane;
	private JButton btnAdd = new JButton("Add");
	private JButton btnRemove = new JButton("Remove");
	private JTextField name = new JTextField();
	private CuboidTabbedPane tabbedPane = new CuboidTabbedPane(this);

	private DefaultListModel<Cube> model = new DefaultListModel<Cube>();

	public List<PendingTexture> pendingTextures = new ArrayList<PendingTexture>();

	public ModelCreator(String title)
	{
		super(title);

		layout = new SpringLayout();
		canvas = new Canvas();

		setResizable(false);
		setPreferredSize(new Dimension(1200, 800));
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

		TextureManager.init();

		loop();

		Display.destroy();
		dispose();
		System.exit(0);
	}

	public void initComponents()
	{
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		JMenuItem eMenuItem = new JMenuItem("Exit");
		eMenuItem.setMnemonic(KeyEvent.VK_E);
		eMenuItem.setToolTipText("Exit application");
		eMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});

		file.add(eMenuItem);
		menuBar.add(file);
		setJMenuBar(menuBar);

		canvas.setSize(new Dimension(1000, 800));
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
			{
				model.remove(selected);
				name.setText("");
				name.setEnabled(false);
			}
		});
		btnRemove.setPreferredSize(new Dimension(95, 30));
		add(btnRemove);

		name.setPreferredSize(new Dimension(190, 30));
		name.setToolTipText("Placeholder");
		name.setEnabled(false);
		name.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					updateName();
				}
			}
		});
		name.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				updateName();
			}
		});
		add(name);

		list.setModel(model);
		list.addListSelectionListener(e ->
		{
			Cube cube = getSelectedCube();
			if (cube != null)
			{
				tabbedPane.updateValues();
				name.setEnabled(true);
				name.setText(cube.toString());
			}
		});

		scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(190, 200));
		add(scrollPane);

		tabbedPane.add("Element", new TabPanel(this, TabPanel.Type.ELEMENT));
		tabbedPane.add("Rotation", new TabPanel(this, TabPanel.Type.ROTATION));
		tabbedPane.add("Faces", new TabPanel(this, TabPanel.Type.TEXTURE));
		tabbedPane.setPreferredSize(new Dimension(190, 350));
		add(tabbedPane);
	}

	public void setLayoutConstaints()
	{
		layout.putConstraint(SpringLayout.NORTH, name, 245, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, name, 1005, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 1005, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, btnAdd, 210, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, btnAdd, 1003, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, btnRemove, 210, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, btnRemove, 1102, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, tabbedPane, 281, SpringLayout.NORTH, this);
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

		while (!Display.isCloseRequested() && !closeRequested)
		{
			synchronized (this)
			{
				for (PendingTexture texture : pendingTextures)
				{
					texture.load();
				}
				pendingTextures.clear();
			}

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

	public Cube getSelectedCube()
	{
		int i = list.getSelectedIndex();
		if (i != -1)
			return (Cube) model.getElementAt(i);
		return null;
	}

	public void updateName()
	{
		String newName = name.getText();
		if (newName.isEmpty())
			newName = "Cuboid";
		Cube cube = getSelectedCube();
		if (cube != null)
		{
			cube.setName(newName);
			name.setText(newName);
			list.updateUI();
		}
	}
}
