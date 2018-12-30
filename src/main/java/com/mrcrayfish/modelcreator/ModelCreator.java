package com.mrcrayfish.modelcreator;

import com.mrcrayfish.modelcreator.dialog.WelcomeDialog;
import com.mrcrayfish.modelcreator.display.CanvasRenderer;
import com.mrcrayfish.modelcreator.display.render.FixedPropertyRenderer;
import com.mrcrayfish.modelcreator.display.render.GroundPropertyRenderer;
import com.mrcrayfish.modelcreator.display.render.StandardRenderer;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.ElementManagerState;
import com.mrcrayfish.modelcreator.panels.SidebarPanel;
import com.mrcrayfish.modelcreator.screenshot.PendingScreenshot;
import com.mrcrayfish.modelcreator.screenshot.Screenshot;
import com.mrcrayfish.modelcreator.sidebar.Sidebar;
import com.mrcrayfish.modelcreator.sidebar.UVSidebar;
import com.mrcrayfish.modelcreator.texture.PendingTexture;
import com.mrcrayfish.modelcreator.texture.TextureAtlas;
import com.mrcrayfish.modelcreator.util.FontManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.opengl.GL11.*;

public class ModelCreator extends JFrame
{
    // Canvas Variables
    private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<>();
    private Canvas canvas;
    private int width = 990, height = 800;

    // Swing Components
    private JScrollPane scroll;
    private Camera camera;
    private SidebarPanel manager;
    private Element grabbed = null;

    // Texture Loading Cache
    public List<PendingTexture> pendingTextures = new ArrayList<>();
    private PendingScreenshot screenshot = null;

    private int lastMouseX, lastMouseY;
    private boolean grabbing = false;
    private boolean closeRequested = false;
    private boolean performedChange = false;

    /* Sidebar Variables */
    private final int SIDEBAR_WIDTH = 130;
    public Sidebar activeSidebar = null;
    public static Sidebar uvSidebar;

    /* Key Events */
    private Set<Integer> keyDown = new HashSet<>();
    private List<KeyAction> keyActions = new ArrayList<>();

    private static boolean changedCanvas = false;
    private static CanvasRenderer standardRenderer = new StandardRenderer();
    private static CanvasRenderer canvasRenderer = standardRenderer;

    private boolean debugMode = false;

    public ModelCreator(String title)
    {
        super(title);

        setPreferredSize(new Dimension(1200, 815));
        setMinimumSize(new Dimension(1200, 500));
        setLayout(new BorderLayout(10, 0));
        setIconImages(getIcons());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e ->
        {
            if(e.getID() == KeyEvent.KEY_PRESSED && !keyDown.contains(e.getKeyCode()))
            {
                keyDown.add(e.getKeyCode());
                ModelCreator.this.handleKeyAction(e.getKeyCode(), e.getModifiers(), true, true);
            }
            else if(e.getID() == KeyEvent.KEY_RELEASED)
            {
                keyDown.remove(e.getKeyCode());
                ModelCreator.this.handleKeyAction(e.getKeyCode(), e.getModifiers(), true, false);
            }
            return false;
        });

        try
        {
            canvas = new AWTGLCanvas();
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
        }
        catch(LWJGLException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        initComponents();
        registerShortcuts();

        uvSidebar = new UVSidebar("UV Editor", manager);

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

        SwingUtilities.invokeLater(() -> WelcomeDialog.show(ModelCreator.this));

        createDisplay();

        try
        {
            loop();

            Display.destroy();
            dispose();
            System.exit(0);
        }
        catch(LWJGLException e1)
        {
            e1.printStackTrace();
        }
    }

    private void initComponents()
    {
        Icons.init(getClass());
        setupMenuBar();

        canvas.setFocusable(true);
        add(canvas, BorderLayout.CENTER);

        manager = new SidebarPanel(this);
        scroll = new JScrollPane(manager);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroll, BorderLayout.EAST);
        StateManager.pushState(manager);
    }

    private void registerShortcuts()
    {
        this.keyActions.add(new KeyAction(KeyEvent.VK_Z, Keyboard.KEY_Z, (modifiers, pressed) ->
        {
            if(pressed && modifiers == InputEvent.CTRL_MASK)
            {
                StateManager.restorePreviousState(manager);
            }
        }));
        this.keyActions.add(new KeyAction(KeyEvent.VK_Y, Keyboard.KEY_Y, (modifiers, pressed) ->
        {
            if(pressed && modifiers == InputEvent.CTRL_MASK)
            {
                StateManager.restoreNextState(manager);
            }
        }));
        this.keyActions.add(new KeyAction(KeyEvent.VK_E, Keyboard.KEY_E, (modifiers, pressed) ->
        {
            if(pressed && modifiers == InputEvent.CTRL_MASK)
            {
                manager.newElement();
            }
        }));
        this.keyActions.add(new KeyAction(KeyEvent.VK_D, Keyboard.KEY_D, (modifiers, pressed) ->
        {
            if(pressed && (modifiers & InputEvent.CTRL_MASK) != 0 && (modifiers & InputEvent.SHIFT_MASK) != 0 && (modifiers & InputEvent.ALT_MASK) != 0)
            {
                debugMode = !debugMode;
            }
        }));
        this.keyActions.add(new KeyAction(KeyEvent.VK_N, Keyboard.KEY_N, (modifiers, pressed) ->
        {
            if(pressed && modifiers == InputEvent.CTRL_MASK)
            {
                Menu.newProject(this);
            }
        }));
        this.keyActions.add(new KeyAction(KeyEvent.VK_S, Keyboard.KEY_S, (modifiers, pressed) ->
        {
            if(pressed && modifiers == InputEvent.CTRL_MASK)
            {
                Menu.saveProject(this);
            }
        }));
        this.keyActions.add(new KeyAction(KeyEvent.VK_O, Keyboard.KEY_O, (modifiers, pressed) ->
        {
            if(pressed && modifiers == InputEvent.CTRL_MASK)
            {
                Menu.loadProject(this);
            }
        }));
        this.keyActions.add(new KeyAction(KeyEvent.VK_O, Keyboard.KEY_O, (modifiers, pressed) ->
        {
            if(pressed && (modifiers & InputEvent.CTRL_MASK) != 0 && (modifiers & InputEvent.SHIFT_MASK) != 0)
            {
                Menu.optimizeModel(this);
            }
        }));
        this.keyActions.add(new KeyAction(KeyEvent.VK_F, Keyboard.KEY_F, (modifiers, pressed) ->
        {
            if(pressed && modifiers == InputEvent.CTRL_MASK)
            {
                SidebarPanel.ElementEntry entry = manager.getSelectedElementEntry();
                if(entry != null)
                {
                    entry.toggleVisibility();
                    manager.getList().repaint();
                }
            }
        }));
        this.keyActions.add(new KeyAction(KeyEvent.VK_D, Keyboard.KEY_D, (modifiers, pressed) ->
        {
            if(pressed && modifiers == InputEvent.CTRL_MASK)
            {
                manager.deleteElement();
            }
        }));
    }

    private List<Image> getIcons()
    {
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage("res/icons/set/icon_16x.png"));
        icons.add(Toolkit.getDefaultToolkit().getImage("res/icons/set/icon_32x.png"));
        icons.add(Toolkit.getDefaultToolkit().getImage("res/icons/set/icon_64x.png"));
        icons.add(Toolkit.getDefaultToolkit().getImage("res/icons/set/icon_128x.png"));
        return icons;
    }

    public int getCanvasWidth()
    {
        return width;
    }

    public int getCanvasHeight()
    {
        return height;
    }

    public int getCanvasOffset()
    {
        return activeSidebar == null ? 0 : getHeight() < 805 ? SIDEBAR_WIDTH * 2 : SIDEBAR_WIDTH;
    }

    private void setupMenuBar()
    {
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        setJMenuBar(new Menu(this));
    }

    private void createDisplay()
    {
        try
        {
            Display.setVSyncEnabled(true);
            Display.setInitialBackground(0.92F, 0.92F, 0.93F);
            Display.setParent(canvas);

        }
        catch(LWJGLException e)
        {
            e.printStackTrace();
        }

        try
        {
            Display.create((new PixelFormat()).withDepthBits(24));
            return;
        }
        catch(LWJGLException e)
        {
            e.printStackTrace();
        }

        try
        {
            Thread.sleep(1000L);
        }
        catch(InterruptedException ignored)
        {
        }

        try
        {
            Display.create();
        }
        catch(LWJGLException e)
        {
            e.printStackTrace();
        }
    }

    private void loop() throws LWJGLException
    {
        TextureAtlas.load();

        camera = new Camera(60F, (float) Display.getWidth() / (float) Display.getHeight(), 0.3F, 1000F);

        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1000000000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while(!Display.isCloseRequested() && !getCloseRequested())
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1.0)
            {
                tick();
                updates++;
                delta--;
            }
            render(frames / 60.0F);
            frames++;
            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                updates = 0;
                frames = 0;
            }
        }
    }

    private void tick()
    {
        Animation.tick();
    }

    private void render(float partialTicks)
    {
        Animation.setPartialTicks(partialTicks);

        Dimension newDim = newCanvasSize.getAndSet(null);
        if (newDim != null)
        {
            width = newDim.width;
            height = newDim.height;
        }

        this.handleKeyboardInput();

        for(PendingTexture texture : pendingTextures)
        {
            texture.load();
        }
        pendingTextures.clear();

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        this.draw();

        Display.update();

        if(screenshot != null)
        {
            if(screenshot.getFile() != null)
            {
                Screenshot.getScreenshot(width, height, screenshot.getCallback(), screenshot.getFile());
            }
            else
            {
                Screenshot.getScreenshot(width, height, screenshot.getCallback());
            }
            screenshot = null;
        }
    }

    private void handleKeyboardInput()
    {
        while(Keyboard.next())
        {
            int modifiers = 0;
            if(isCtrlKeyDown())
            {
                modifiers += InputEvent.CTRL_MASK;
            }
            if(isShiftKeyDown())
            {
                modifiers += InputEvent.SHIFT_MASK;
            }
            if(isAltKeyDown())
            {
                modifiers += InputEvent.ALT_MASK;
            }

            int code = Keyboard.getEventKey();
            int finalModifiers = modifiers;
            if(Keyboard.getEventKeyState())
            {
                SwingUtilities.invokeLater(() -> this.handleKeyAction(code, finalModifiers, false, true));
            }
            else
            {
                SwingUtilities.invokeLater(() -> this.handleKeyAction(code, finalModifiers, false, false));
            }
        }
    }

    private void draw()
    {
        if(changedCanvas)
        {
            canvasRenderer.onInit(camera);
            changedCanvas = false;
        }

        int offset = activeSidebar == null ? 0 : getHeight() < 805 ? SIDEBAR_WIDTH * 2 : SIDEBAR_WIDTH;

        glViewport(offset, 0, width - offset, height);

        this.handleInput(offset);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(60F, (float) (width - offset) / (float) height, 0.3F, 1000F);

        canvasRenderer.onRenderPerspective(this, manager, camera);

        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluOrtho2D(0, width, height, 0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        canvasRenderer.onRenderOverlay(manager, camera, this);

        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluOrtho2D(0, width, height, 0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        drawOverlay(offset);
    }

    private void drawOverlay(int offset)
    {
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
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
                glVertex2i(offset, height);
                glVertex2i(width, height);
            }
            glEnd();
        }
        glPopMatrix();

        if(debugMode)
        {
            glPushMatrix();
            {
                List<ElementManagerState> states = StateManager.getStates();
                for(int i = 0; i < states.size(); i++)
                {
                    ElementManagerState managerState = states.get(i);
                    String text = "No Elements";
                    if(managerState.getElements().size() > 0)
                    {
                        text = managerState.getElements().toString();
                    }

                    if(StateManager.getTailIndex() == i)
                    {
                        text = text + " <<<";
                    }

                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    FontManager.BEBAS_NEUE_20.drawString(10, 10 + i * 20, text, new Color(1, 1, 1));
                    GL11.glDisable(GL11.GL_BLEND);
                }
            }
            glPopMatrix();
        }

        if(activeSidebar != null)
        {
            activeSidebar.draw(offset, width, height, getHeight());
        }

        if(canvasRenderer == standardRenderer)
        {
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
    }

    private void handleInput(int offset)
    {
        final float cameraMod = Math.abs(camera.getZ());

        if(Mouse.isButtonDown(0) || Mouse.isButtonDown(1))
        {
            if(!grabbing)
            {
                lastMouseX = Mouse.getX();
                lastMouseY = Mouse.getY();
                grabbing = true;
            }
        }
        else if(grabbing)
        {
            if(grabbed != null && performedChange)
            {
                StateManager.pushState(manager);
                performedChange = false;
            }
            grabbing = false;
            grabbed = null;
        }

        if(Mouse.getX() < offset)
        {
            activeSidebar.handleInput(getHeight());
        }
        else
        {
            if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && canvasRenderer == standardRenderer)
            {
                if(grabbed == null)
                {
                    if(Mouse.isButtonDown(0) || Mouse.isButtonDown(1))
                    {
                        int sel = select(Mouse.getX(), Mouse.getY());
                        if(sel >= 0)
                        {
                            grabbed = manager.getAllElements().get(sel);
                            manager.setSelectedElement(sel);
                        }
                        else
                        {
                            grabbed = null;
                            manager.setSelectedElement(-1);
                        }
                    }
                }
                else
                {
                    Element element = grabbed;
                    int state = getCameraState(camera);

                    int newMouseX = Mouse.getX();
                    int newMouseY = Mouse.getY();

                    int xMovement = (newMouseX - lastMouseX) / 20;
                    int yMovement = (newMouseY - lastMouseY) / 20;

                    if(xMovement != 0 | yMovement != 0)
                    {
                        if(Mouse.isButtonDown(0))
                        {
                            switch(state)
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
                        else if(Mouse.isButtonDown(1))
                        {
                            switch(state)
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

                        if(xMovement != 0)
                        {
                            lastMouseX = newMouseX;
                        }
                        if(yMovement != 0)
                        {
                            lastMouseY = newMouseY;
                        }

                        manager.updateValues();
                        element.updateEndUVs();

                        performedChange = true;
                    }
                }
            }
            else
            {
                if(Mouse.isButtonDown(0))
                {
                    final float modifier = (cameraMod * 0.05f);
                    camera.addX(Mouse.getDX() * 0.01F * modifier);
                    camera.addY(Mouse.getDY() * 0.01F * modifier);
                }
                else if(Mouse.isButtonDown(1))
                {
                    final float modifier = applyLimit(cameraMod * 0.1f);
                    camera.rotateX(-(Mouse.getDY() * 0.5F) * modifier);
                    final float rxAbs = Math.abs(camera.getRX());
                    camera.rotateY((rxAbs >= 90 && rxAbs < 270 ? -1 : 1) * Mouse.getDX() * 0.5F * modifier);
                }

                final float wheel = Mouse.getDWheel();
                if(wheel != 0)
                {
                    camera.addZ(wheel * (cameraMod / 5000F));
                }
            }
        }
    }

    private int select(int x, int y)
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

            int offset = activeSidebar == null ? 0 : getHeight() < 805 ? SIDEBAR_WIDTH * 2 : SIDEBAR_WIDTH;
            GLU.gluPerspective(60F, (float) (width - offset) / (float) height, 0.3F, 1000F);
            canvasRenderer.onRenderPerspective(this, manager, camera);
        }
        GL11.glPopMatrix();
        hits = GL11.glRenderMode(GL11.GL_RENDER);

        selBuffer.get(buffer);
        if(hits > 0)
        {
            int choose = buffer[3];
            int depth = buffer[1];

            for(int i = 1; i < hits; i++)
            {
                if((buffer[i * 4 + 1] < depth || choose == 0) && buffer[i * 4 + 3] != 0)
                {
                    choose = buffer[i * 4 + 3];
                    depth = buffer[i * 4 + 1];
                }
            }

            if(choose > 0)
            {
                return choose - 1;
            }
        }

        return -1;
    }

    private float applyLimit(float value)
    {
        if(value > 0.4F)
        {
            value = 0.4F;
        }
        else if(value < 0.15F)
        {
            value = 0.15F;
        }
        return value;
    }

    private int getCameraState(Camera camera)
    {
        int cameraRotY = (int) (camera.getRY() >= 0 ? camera.getRY() : 360 + camera.getRY());
        int state = (int) ((cameraRotY * 4.0F / 360.0F) + 0.5D) & 3;

        if(camera.getRX() > 45)
        {
            state += 4;
        }
        if(camera.getRX() < -45)
        {
            state += 8;
        }
        return state;
    }

    public void startScreenshot(PendingScreenshot screenshot)
    {
        this.screenshot = screenshot;
    }

    public void setSidebar(Sidebar s)
    {
        activeSidebar = s;
    }

    public ElementManager getElementManager()
    {
        return manager;
    }

    public void close()
    {
        this.closeRequested = true;
    }

    private boolean getCloseRequested()
    {
        return closeRequested;
    }

    private static boolean isCtrlKeyDown()
    {
       /* if(Minecraft.IS_RUNNING_ON_MAC)
        {
            return Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220);
        }
        else*/
        {
            return Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
        }
    }

    private static boolean isShiftKeyDown()
    {
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
    }

    private static boolean isAltKeyDown()
    {
        return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184);
    }

    private void handleKeyAction(int code, int modifiers, boolean awt, boolean pressed)
    {
        keyActions.forEach(keyAction ->
        {
            if(awt)
            {
                if(keyAction.awtCode == code)
                {
                    keyAction.handler.process(modifiers, pressed);
                }
            }
            else
            {
                if(keyAction.keyboardCode == code)
                {
                    keyAction.handler.process(modifiers, pressed);
                }
            }
        });
    }

    public static void setCanvasRenderer(CanvasRenderer displayRenderer)
    {
        canvasRenderer = displayRenderer;
        changedCanvas = true;
    }

    public static void restoreStandardRenderer()
    {
        setCanvasRenderer(standardRenderer);
    }

    private static class KeyAction
    {
        private final int awtCode;
        private final int keyboardCode;
        private final Handler handler;

        public KeyAction(int awtCode, int keyboardCode, Handler handler)
        {
            this.awtCode = awtCode;
            this.keyboardCode = keyboardCode;
            this.handler = handler;
        }

        public interface Handler
        {
            void process(int modifiers, boolean pressed);
        }
    }
}
