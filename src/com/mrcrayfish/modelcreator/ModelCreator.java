package com.mrcrayfish.modelcreator;

import static org.lwjgl.opengl.GL11.*;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;

import com.mrcrayfish.modelcreator.dialog.WelcomeDialog;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.panels.SidebarPanel;
import com.mrcrayfish.modelcreator.sidebar.Sidebar;
import com.mrcrayfish.modelcreator.sidebar.UVSidebar;
import com.mrcrayfish.modelcreator.texture.PendingTexture;
import com.mrcrayfish.modelcreator.texture.TextureManager;
import com.mrcrayfish.modelcreator.util.FontManager;

public class ModelCreator extends JFrame
{
	private static final long serialVersionUID = 1L;

	// TODO remove static instance
	public static String texturePath = ".";

	// Canvas Variables
	private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
	private final Canvas canvas;
	private int width = 990, height = 800;

	// Swing Components
	private JScrollPane scroll;
	private JMenuBar menuBar = new JMenuBar();

	private Camera camera;
	private ElementManager manager;
	private Element grabbed = null;

	// Texture Loading Cache
	public List<PendingTexture> pendingTextures = new ArrayList<PendingTexture>();

	private int lastMouseX, lastMouseY;
	private boolean grabbing = false;
	private boolean closeRequested = false;

	private final int SIDEBAR_WIDTH = 130;
	public Sidebar activeSidebar = null;
	public static Sidebar SIDEBAR_UV;

	public ModelCreator(String title)
	{
		super(title);

		setPreferredSize(new Dimension(1200, 835));
		setMinimumSize(new Dimension(1200, 500));
		setLayout(new BorderLayout(10, 0));

		canvas = new Canvas();

		initComponents();

		SIDEBAR_UV = new UVSidebar("UV Editor", manager);

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

		Thread loopThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				initDisplay();

				try
				{
					Display.create();

					WelcomeDialog.show(ModelCreator.this);

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
		loopThread.start();
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
			if (returnVal == JOptionPane.YES_OPTION)
			{
				manager.clearElements();
				manager.updateValues();
			}
		});

		JMenuItem menuItemImport = new JMenuItem("Import");
		menuItemImport.setMnemonic(KeyEvent.VK_I);
		menuItemImport.setToolTipText("Import model from JSON");
		menuItemImport.addActionListener(e ->
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Input File");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setApproveButtonText("Import");

			FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON (.json)", "json");
			chooser.setFileFilter(filter);

			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				Importer importer = new Importer(manager, chooser.getSelectedFile().getAbsolutePath());
				importer.importFromJSON();
			}
		});

		JMenuItem menuItemExport = new JMenuItem("Export");
		menuItemExport.setMnemonic(KeyEvent.VK_E);
		menuItemExport.setToolTipText("Export model to JSON");
		menuItemExport.addActionListener(e ->
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Output Directory");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setApproveButtonText("Export");

			FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON (.json)", "json");
			chooser.setFileFilter(filter);

			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				String filePath = chooser.getSelectedFile().getAbsolutePath();
				if (!filePath.endsWith(".json"))
					chooser.setSelectedFile(new File(filePath + ".json"));
				Exporter exporter = new Exporter(manager);
				exporter.export(chooser.getSelectedFile());
			}
		});

		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.setMnemonic(KeyEvent.VK_E);
		menuItemExit.setToolTipText("Exit application");
		menuItemExit.addActionListener(e ->
		{
			System.exit(0);
		});

		// Going to change this to be integrated into the import option
		JMenuItem menuItemTexturePath = new JMenuItem("Set Texture path");
		menuItemTexturePath.setMnemonic(KeyEvent.VK_S);
		menuItemTexturePath.setToolTipText("Set the base path from where to look for textures");
		menuItemTexturePath.addActionListener(e ->
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Texture path");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				texturePath = chooser.getSelectedFile().getAbsolutePath();
			}
		});

		file.add(menuItemNew);
		file.add(menuItemImport);
		file.add(menuItemExport);
		file.add(menuItemTexturePath);
		file.add(menuItemExit);
		menuBar.add(file);
		setJMenuBar(menuBar);

		canvas.setPreferredSize(new Dimension(1000, 790));
		add(canvas, BorderLayout.CENTER);

		canvas.setFocusable(true);
		canvas.setVisible(true);
		canvas.requestFocus();

		manager = new SidebarPanel(this);
		scroll = new JScrollPane((JPanel) manager);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scroll, BorderLayout.EAST);
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

	private void loop() throws LWJGLException
	{
		camera = new Camera(60F, (float) Display.getWidth() / (float) Display.getHeight(), 0.3F, 1000F);

		Dimension newDim;

		while (!Display.isCloseRequested() && !getCloseRequested())
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

			int offset = activeSidebar == null ? 0 : getHeight() < 805 ? SIDEBAR_WIDTH * 2 : SIDEBAR_WIDTH;

			glViewport(offset, 0, width - offset, height);

			handleInput(offset);

			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			GLU.gluPerspective(60F, (float) (width - offset) / (float) height, 0.3F, 1000F);

			draw();

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

			drawOverlay(offset);

			Display.update();
		}
	}

	public void draw()
	{
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		camera.useView();

		drawPerspective();
	}

	public void drawPerspective()
	{
		glClearColor(0.92F, 0.92F, 0.93F, 1.0F);
		drawGrid();

		glTranslatef(-8, 0, -8);
		for (int i = 0; i < manager.getCuboidCount(); i++)
		{
			GL11.glLoadName(i + 1);
			Element cube = manager.getCuboid(i);
			cube.draw();
			GL11.glLoadName(0);
			cube.drawExtras(manager);
		}

		GL11.glPushMatrix();
		{
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GL11.glTranslated(0, 0, 16);
			GL11.glScaled(0.018, 0.018, 0.018);
			GL11.glRotated(90, 1, 0, 0);
			FontManager.BEBAS_NEUE_50.drawString(8, 0, "Model Creator by MrCrayfish", new Color(0.5F, 0.5F, 0.6F));

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glDisable(GL11.GL_BLEND);
		}
		GL11.glPopMatrix();
	}

	public void drawOverlay(int offset)
	{
		glPushMatrix();
		{
			glColor3f(0.58F, 0.58F, 0.58F);
			glLineWidth(2F);
			glBegin(GL_LINES);
			{
				glVertex2i(offset, 0);
				glVertex2i(width, 0);
				glVertex2i(width, 0);
				glVertex2i(width, height);
				glVertex2i(offset, height);
				glVertex2i(offset, 0);
			}
			glEnd();
		}
		glPopMatrix();

		if (activeSidebar != null)
			activeSidebar.draw(offset, width, height, getHeight());

		glPushMatrix();
		{
			glTranslatef(width - 80, height - 80, 0);
			glLineWidth(2F);
			glRotated(-camera.getRY(), 0, 0, 1);

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			FontManager.BEBAS_NEUE_20.drawString(-5, -75, "N", new Color(1, 1, 1));
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
		glPopMatrix();
	}

	public void handleInput(int offset)
	{
		final float cameraMod = Math.abs(camera.getZ());

		if (Mouse.isButtonDown(0) | Mouse.isButtonDown(1))
		{
			if (!grabbing)
			{
				lastMouseX = Mouse.getX();
				lastMouseY = Mouse.getY();
				grabbing = true;
			}
		}
		else
		{
			grabbing = false;
			grabbed = null;
		}

		if (Mouse.getX() < offset)
		{
			SIDEBAR_UV.handleInput(getHeight());
		}
		else
		{

			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			{
				if (grabbed == null)
				{
					if (Mouse.isButtonDown(0) | Mouse.isButtonDown(1))
					{
						int sel = select(Mouse.getX(), Mouse.getY());
						if (sel >= 0)
						{
							grabbed = manager.getAllCuboids().get(sel);
							manager.setSelectedCuboid(sel);
						}
					}
				}
				else
				{
					Element element = grabbed;
					int state = getCameraState(camera);

					int newMouseX = Mouse.getX();
					int newMouseY = Mouse.getY();

					int xMovement = (int) ((newMouseX - lastMouseX) / 20);
					int yMovement = (int) ((newMouseY - lastMouseY) / 20);

					if (xMovement != 0 | yMovement != 0)
					{
						if (Mouse.isButtonDown(0))
						{
							switch (state)
							{
							case 0:
								element.addStartX(xMovement);
								element.addStartY(yMovement);
								break;
							case 1:
								element.addStartZ(xMovement);
								element.addStartY(yMovement);
								break;
							case 2:
								element.addStartX(-xMovement);
								element.addStartY(yMovement);
								break;
							case 3:
								element.addStartZ(-xMovement);
								element.addStartY(yMovement);
								break;
							case 4:
								element.addStartX(xMovement);
								element.addStartZ(-yMovement);
								break;
							case 5:
								element.addStartX(yMovement);
								element.addStartZ(xMovement);
								break;
							case 6:
								element.addStartX(-xMovement);
								element.addStartZ(yMovement);
								break;
							case 7:
								element.addStartX(-yMovement);
								element.addStartZ(-xMovement);
								break;
							}
						}
						else if (Mouse.isButtonDown(1))
						{
							switch (state)
							{
							case 0:
								element.addHeight(yMovement);
								element.addWidth(xMovement);
								break;
							case 1:
								element.addHeight(yMovement);
								element.addDepth(xMovement);
								break;
							case 2:
								element.addHeight(yMovement);
								element.addWidth(-xMovement);
								break;
							case 3:
								element.addHeight(yMovement);
								element.addDepth(-xMovement);
								break;
							case 4:
								element.addDepth(-yMovement);
								element.addWidth(xMovement);
								break;
							case 5:
								element.addDepth(xMovement);
								element.addWidth(yMovement);
								break;
							case 6:
								element.addDepth(yMovement);
								element.addWidth(-xMovement);
								break;
							case 7:
								element.addDepth(-xMovement);
								element.addWidth(-yMovement);
								break;
							case 8:
								element.addDepth(-yMovement);
								element.addWidth(xMovement);
								break;
							}
						}

						if (xMovement != 0)
							lastMouseX = newMouseX;
						if (yMovement != 0)
							lastMouseY = newMouseY;

						manager.updateValues();
						element.updateUV();
					}
				}
			}
			else
			{
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
		}
	}

	public int select(int x, int y)
	{
		IntBuffer selBuffer = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder()).asIntBuffer();
		int[] buffer = new int[256];

		IntBuffer viewBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		int[] viewport = new int[4];

		int hits;
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewBuffer);
		viewBuffer.get(viewport);

		GL11.glSelectBuffer(selBuffer);
		GL11.glRenderMode(GL11.GL_SELECT);
		GL11.glInitNames();
		GL11.glPushName(0);
		GL11.glPushMatrix();
		{
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GLU.gluPickMatrix(x, y, 1, 1, IntBuffer.wrap(viewport));
			GLU.gluPerspective(60F, (float) (width) / (float) height, 0.3F, 1000F);

			draw();
		}
		GL11.glPopMatrix();
		hits = GL11.glRenderMode(GL11.GL_RENDER);

		selBuffer.get(buffer);
		if (hits > 0)
		{
			int choose = buffer[3];
			int depth = buffer[1];

			for (int i = 1; i < hits; i++)
			{
				if ((buffer[i * 4 + 1] < depth || choose == 0) && buffer[i * 4 + 3] != 0)
				{
					choose = buffer[i * 4 + 3];
					depth = buffer[i * 4 + 1];
				}
			}

			if (choose > 0)
			{
				return choose - 1;
			}
		}

		return -1;
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

	public int getCameraState(Camera camera)
	{
		int cameraRotY = (int) (camera.getRY() >= 0 ? camera.getRY() : 360 + camera.getRY());
		int state = (int) ((cameraRotY * 4.0F / 360.0F) + 0.5D) & 3;

		if (camera.getRX() > 45)
		{
			state += 4;
		}
		if (camera.getRX() < -45)
		{
			state += 8;
		}
		return state;
	}

	public void drawGrid()
	{
		glPushMatrix();
		{
			glColor3f(0.55F, 0.55F, 0.60F);
			glTranslatef(-8, 0, -8);

			// Bold outside lines
			glLineWidth(2F);
			glBegin(GL_LINES);
			{
				glVertex3i(0, 0, 0);
				glVertex3i(0, 0, 16);
				glVertex3i(16, 0, 0);
				glVertex3i(16, 0, 16);
				glVertex3i(0, 0, 16);
				glVertex3i(16, 0, 16);
				glVertex3i(0, 0, 0);
				glVertex3i(16, 0, 0);
			}
			glEnd();

			// Thin inside lines
			glLineWidth(1F);
			glBegin(GL_LINES);
			{
				for (int i = 1; i <= 16; i++)
				{
					glVertex3i(i, 0, 0);
					glVertex3i(i, 0, 16);
				}

				for (int i = 1; i <= 16; i++)
				{
					glVertex3i(0, 0, i);
					glVertex3i(16, 0, i);
				}
			}
			glEnd();
		}
		glPopMatrix();
	}

	public void setSidebar(Sidebar s)
	{
		activeSidebar = s;
	}

	public synchronized boolean getCloseRequested()
	{
		return closeRequested;
	}
}
