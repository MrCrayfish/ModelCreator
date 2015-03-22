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

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;

import com.mrcrayfish.modelcreator.panels.SidebarPanel;
import com.mrcrayfish.modelcreator.texture.PendingTexture;
import com.mrcrayfish.modelcreator.texture.TextureManager;

public class ModelCreator extends JFrame
{
	private static final long serialVersionUID = 1L;

	// Canvas Variables
	private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
	private final Canvas canvas;

	private Camera camera;
	public Texture texture;

	public boolean closeRequested = false;

	// Swing Components
	private JMenuBar menuBar = new JMenuBar();
	private CuboidManager manager;

	// Texture Loading Cache
	public List<PendingTexture> pendingTextures = new ArrayList<PendingTexture>();

	public ModelCreator(String title)
	{
		super(title);

		setPreferredSize(new Dimension(1200, 800));
		setLayout(new BorderLayout());

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
			// Exporter.export(this, "test");
			});

		file.add(menuItemExport);
		file.add(menuItemExit);
		menuBar.add(file);
		setJMenuBar(menuBar);

		canvas.setSize(new Dimension(990, 800));
		add(canvas, BorderLayout.CENTER);

		canvas.setFocusable(true);
		canvas.setVisible(true);
		canvas.requestFocus();

		manager = new SidebarPanel();
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

	private void loop()
	{
		camera = new Camera(60F, (float) Display.getWidth() / (float) Display.getHeight(), 0.3F, 1000F);
		
		Dimension newDim;
        float counter = 0;
        
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
                GL11.glViewport(0, 0, newDim.width, newDim.height);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GLU.gluPerspective(60F, (float) newDim.width / (float) newDim.height, 0.3F, 1000F);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
            }
            

			handleInput();

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glLoadIdentity();
			camera.useView();

			glScalef(0.25F, 0.25F, 0.25F);
			drawGrid();
			drawAxis();

			glTranslatef(-8, 0, 8);
			for (int i = 0; i < manager.getCuboidCount(); i++)
			{
				Cuboid cube = manager.getCuboid(i);
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
}
