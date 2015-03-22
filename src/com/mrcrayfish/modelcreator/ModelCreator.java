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
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import com.mrcrayfish.modelcreator.panels.CuboidTabbedPane;
import com.mrcrayfish.modelcreator.panels.tabs.ElementPanel;
import com.mrcrayfish.modelcreator.panels.tabs.FacePanel;
import com.mrcrayfish.modelcreator.panels.tabs.RotationPanel;
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

	// Swing Components
	private JMenuBar menuBar = new JMenuBar();
	private JList<Cuboid> list = new JList<Cuboid>();
	private JScrollPane scrollPane;
	private JButton btnAdd = new JButton("Add");
	private JButton btnRemove = new JButton("Remove");
	private JTextField name = new JTextField();
	private CuboidTabbedPane tabbedPane = new CuboidTabbedPane(this);

	private DefaultListModel<Cuboid> model = new DefaultListModel<Cuboid>();

	// Texture Loading Cache
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
		
		tabbedPane.updateValues();
		pack();
		setVisible(true);
		setLocationRelativeTo(null);

		Thread gameThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				initDisplay();

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
		});
		gameThread.start();
	}

	public void initComponents()
	{
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.setMnemonic(KeyEvent.VK_E);
		menuItemExit.setToolTipText("Exit application");
		menuItemExit.addActionListener(e ->
		{
			System.exit(0);
		});

		JMenuItem menuItemExport = new JMenuItem("Export");
		menuItemExport.setMnemonic(KeyEvent.VK_E);
		menuItemExport.setToolTipText("Export model to JSON");
		menuItemExport.addActionListener(e ->
		{
			//Exporter.export(this, "test");
		});

		file.add(menuItemExport);
		file.add(menuItemExit);
		menuBar.add(file);
		setJMenuBar(menuBar);

		canvas.setSize(new Dimension(990, 800));
		add(canvas);

		canvas.setFocusable(true);
		canvas.setVisible(true);
		canvas.requestFocus();
		
		Font defaultFont = new Font("SansSerif", Font.BOLD, 14);
		btnAdd.addActionListener(e ->
		{
			model.addElement(new Cuboid(1, 1, 1));
			list.setSelectedIndex(model.size() - 1);
		});
		btnAdd.setPreferredSize(new Dimension(90, 30));
		btnAdd.setFont(defaultFont);
		add(btnAdd);

		btnRemove.addActionListener(e ->
		{
			int selected = list.getSelectedIndex();
			if (selected != -1)
			{
				model.remove(selected);
				name.setText("");
				name.setEnabled(false);
				tabbedPane.updateValues();
			}
		});
		btnRemove.setFont(defaultFont);
		btnRemove.setPreferredSize(new Dimension(90, 30));
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
			Cuboid cube = getSelectedCuboid();
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

		tabbedPane.add("Element", new ElementPanel(this));
		tabbedPane.add("Rotation", new RotationPanel(this));
		tabbedPane.add("Faces", new FacePanel(this));
		tabbedPane.setPreferredSize(new Dimension(190, 385));
		tabbedPane.setTabPlacement(JTabbedPane.TOP);
		add(tabbedPane);
	}

	public void setLayoutConstaints()
	{
		layout.putConstraint(SpringLayout.NORTH, name, 245, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, name, 998, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 998, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, btnAdd, 210, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, btnAdd, 998, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, btnRemove, 210, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, btnRemove, 1097, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, tabbedPane, 281, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, tabbedPane, 998, SpringLayout.WEST, this);
	}

	public void initDisplay()
	{
		try
		{
			Display.setParent(canvas);
			Display.setVSyncEnabled(true);
			Display.setInitialBackground(0.92F, 0.92F, 0.93F);
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
				Cuboid cube = (Cuboid) model.getElementAt(i);
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
			glColor3f(0.2F, 0.2F, 0.27F);

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

	public Cuboid getSelectedCuboid()
	{
		int i = list.getSelectedIndex();
		if (i != -1)
			return (Cuboid) model.getElementAt(i);
		return null;
	}

	public List<Cuboid> getAllCuboids()
	{
		List<Cuboid> list = new ArrayList<Cuboid>();
		for (int i = 0; i < model.size(); i++)
		{
			list.add(model.getElementAt(i));
		}
		return list;
	}

	public Cuboid getCuboid(int index)
	{
		return model.getElementAt(index);
	}

	public int getCuboidCount()
	{
		return model.size();
	}

	public void updateName()
	{
		String newName = name.getText();
		if (newName.isEmpty())
			newName = "Cuboid";
		Cuboid cube = getSelectedCuboid();
		if (cube != null)
		{
			cube.setName(newName);
			name.setText(newName);
			list.updateUI();
		}
	}
}
