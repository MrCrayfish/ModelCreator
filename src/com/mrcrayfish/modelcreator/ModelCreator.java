package com.mrcrayfish.modelcreator;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glVertex3i;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;

import com.mrcrayfish.modelcreator.panels.SidebarPanel;
import com.mrcrayfish.modelcreator.texture.PendingTexture;
import com.mrcrayfish.modelcreator.texture.TextureManager;

public class ModelCreator extends JFrame
{
	private static final long serialVersionUID = 1L;

	// Canvas Variables
	private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
	private final Canvas canvas;
	private int width = 990, height = 800;

	private Camera camera;
	public Texture texture;

	public boolean closeRequested = false;

	// Swing Components
	private JMenuBar menuBar = new JMenuBar();
	private ElementManager manager;

	// Texture Loading Cache
	public List<PendingTexture> pendingTextures = new ArrayList<PendingTexture>();

	private TrueTypeFont font;
	private TrueTypeFont fontBebasNeue;

	public ModelCreator(String title)
	{
		super(title);

		setPreferredSize(new Dimension(1200, 840));
		setMinimumSize(new Dimension(1200, 840));
		setLayout(new BorderLayout(10, 0));

		canvas = new Canvas();

		initComponents();

		canvas.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				newCanvasSize.set(canvas.getSize());
			}
		});

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

		manager.updateValues();

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

					initFonts();
					TextureManager.init();

					loop();

					Display.destroy();
					dispose();
					System.exit(0);
				}
				catch (LWJGLException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		gameThread.start();

		WelcomeDialog.show(this);
	}

	public void initComponents()
	{
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		JMenuItem menuItemNew = new JMenuItem("New");
		menuItemNew.setMnemonic(KeyEvent.VK_N);
		menuItemNew.setToolTipText("New Model");
		menuItemNew.addActionListener(a ->
		{
			int returnVal = JOptionPane.showConfirmDialog(this, "You current work will be cleared, are you sure?", "Note", JOptionPane.YES_NO_OPTION);
			if(returnVal == JOptionPane.YES_OPTION)
			{
				manager.clearElements();
				manager.updateValues();
			}
		});

		JMenuItem menuItemExport = new JMenuItem("Export");
		menuItemExport.setMnemonic(KeyEvent.VK_E);
		menuItemExport.setToolTipText("Export model to JSON");
		menuItemExport.addActionListener(e ->
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Output Directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				Exporter exporter = new Exporter(manager, chooser.getSelectedFile().getAbsolutePath(), "test");
				exporter.export();
			}
		});

		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.setMnemonic(KeyEvent.VK_E);
		menuItemExit.setToolTipText("Exit application");
		menuItemExit.addActionListener(e ->
		{
			System.exit(0);
		});

		file.add(menuItemNew);
		file.add(menuItemExport);
		file.add(menuItemExit);
		menuBar.add(file);
		setJMenuBar(menuBar);

		canvas.setSize(new Dimension(990, 790));
		add(canvas, BorderLayout.CENTER);

		canvas.setFocusable(true);
		canvas.setVisible(true);
		canvas.requestFocus();

		manager = new SidebarPanel(this);
		add((JPanel) manager, BorderLayout.EAST);
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

	public void initFonts()
	{
		Font awtFont = new Font("Times New Roman", Font.BOLD, 20);
		font = new TrueTypeFont(awtFont, false);

		try
		{
			InputStream inputStream = ResourceLoader.getResourceAsStream("res/bebas_neue.otf");
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(24f);
			fontBebasNeue = new TrueTypeFont(customFont, false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void loop() throws LWJGLException
	{
		camera = new Camera(60F, (float) Display.getWidth() / (float) Display.getHeight(), 0.3F, 1000F);

		Dimension newDim;

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

			newDim = newCanvasSize.getAndSet(null);

			if (newDim != null)
			{
				width = newDim.width;
				height = newDim.height;
			}

			handleInput();

			glViewport(0, 0, width, height);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			GLU.gluPerspective(60F, (float) width / (float) height, 0.3F, 1000F);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glEnable(GL_DEPTH_TEST);

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glLoadIdentity();
			camera.useView();

			drawPerspective();

			glDisable(GL_DEPTH_TEST);
			glDisable(GL_CULL_FACE);
			glDisable(GL_TEXTURE_2D);
			glDisable(GL_LIGHTING);

			glViewport(0, 0, width, height);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			GLU.gluOrtho2D(0, width, height, 0);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();

			drawOverlay();

			Display.update();
		}
	}

	public void drawPerspective()
	{
		glClearColor(0.92F, 0.92F, 0.93F, 1.0F);
		drawGrid();

		glTranslatef(-8, 0, 8);
		for (int i = 0; i < manager.getCuboidCount(); i++)
		{
			Element cube = manager.getCuboid(i);
			cube.draw();
			cube.drawExtras(manager);
		}

		GL11.glPushMatrix();
		{
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GL11.glScaled(0.02, 0.02, 0.02);
			GL11.glRotated(90, 1, 0, 0);
			fontBebasNeue.drawString(8, 0, "Model Creator by MrCrayfish", new Color(0.5F, 0.5F, 0.55F));

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glDisable(GL11.GL_BLEND);
		}
		GL11.glPopMatrix();
	}

	public void drawOverlay()
	{
		glTranslatef(width - 80, height - 80, 0);
		glLineWidth(2F);
		glRotated(-camera.getRY(), 0, 0, 1);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		font.drawString(-7, -75, "N", new Color(1, 1, 1));
		GL11.glDisable(GL11.GL_BLEND);

		glColor3d(0.6, 0.6, 0.6);
		glBegin(GL_LINES);
		{
			glVertex2i(0, -50);
			glVertex2i(0, 50);
			glVertex2i(-50, 0);
			glVertex2i(50, 0);
		}
		glEnd();

		glColor3d(0.3, 0.3, 0.6);
		glBegin(GL_TRIANGLES);
		{
			glVertex2i(-5, -45);
			glVertex2i(0, -50);
			glVertex2i(5, -45);

			glVertex2i(-5, 45);
			glVertex2i(0, 50);
			glVertex2i(5, 45);

			glVertex2i(-45, -5);
			glVertex2i(-50, 0);
			glVertex2i(-45, 5);

			glVertex2i(45, -5);
			glVertex2i(50, 0);
			glVertex2i(45, 5);
		}
		glEnd();

	}

	public void handleInput()
	{
		final float cameraMod = Math.abs(camera.getZ());

		if (Mouse.isButtonDown(0))
		{
			final float modifier = (cameraMod * 0.05f);
			camera.addX((float) (Mouse.getDX() * 0.01F) * modifier);
			camera.addY((float) (Mouse.getDY() * 0.01F) * modifier);
		}
		else if (Mouse.isButtonDown(1))
		{
			final float modifier = applyLimit(cameraMod * 0.1f);
			camera.rotateX(-(float) (Mouse.getDY() * 0.5F) * modifier);
			final float rxAbs = Math.abs(camera.getRX());
			camera.rotateY((rxAbs >= 90 && rxAbs < 270 ? -1 : 1) * (float) (Mouse.getDX() * 0.5F) * modifier);
		}

		final float wheel = Mouse.getDWheel();
		if (wheel != 0)
		{
			camera.addZ(wheel * (cameraMod / 5000F));
		}
	}

	public float applyLimit(float value)
	{
		if (value > 0.4F)
		{
			value = 0.4F;
		}
		else if (value < 0.15F)
		{
			value = 0.15F;
		}
		return value;
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
}
