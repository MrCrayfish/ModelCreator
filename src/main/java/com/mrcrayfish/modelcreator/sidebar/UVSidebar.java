package com.mrcrayfish.modelcreator.sidebar;

import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.util.FontManager;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;

import static org.lwjgl.opengl.GL11.*;

public class UVSidebar extends Sidebar
{
    private ElementManager manager;

    private final int LENGTH = 110;

    private final Color BLACK_ALPHA = new Color(0, 0, 0, 0.75F);
    private final Color BLACK_ALPHA_HOVER = new Color(0, 0, 0, 0.25F);
    public static final java.awt.Color BACKGROUND = new java.awt.Color(230, 230, 240);

    private int[] startX = {0, 0, 0, 0, 0, 0};
    private int[] startY = {0, 0, 0, 0, 0, 0};

    private int hoveredFace = -1;
    private int canvasHeight;

    public UVSidebar(String title, ElementManager manager)
    {
        super(title);
        this.manager = manager;
    }

    @Override
    public void draw(int sidebarWidth, int canvasWidth, int canvasHeight, int frameHeight)
    {
        super.draw(sidebarWidth, canvasWidth, canvasHeight, frameHeight);

        this.canvasHeight = frameHeight;
        if(!grabbing)
        {
            hoveredFace = getFace(frameHeight, Mouse.getX(), Mouse.getY());
        }

        glPushMatrix();
        {
            glTranslatef(10, 30, 0);

            int count = 0;

            for(int i = 0; i < 6; i++)
            {
                glPushMatrix();
                {
                    if(30 + i * (LENGTH + 10) + (LENGTH + 10) > canvasHeight)
                    {
                        glTranslatef(10 + LENGTH, count * (LENGTH + 10), 0);
                        startX[i] = 20 + LENGTH;
                        startY[i] = count * (LENGTH + 10) + 40;
                        count++;
                    }
                    else
                    {
                        glTranslatef(0, i * (LENGTH + 10), 0);
                        startX[i] = 10;
                        startY[i] = i * (LENGTH + 10) + 40;
                    }

                    Face[] faces = null;
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        faces = selectedElement.getAllFaces();
                    }

                    if(faces != null)
                    {
                        glDisable(GL_TEXTURE_2D);

                        int color = ModelCreator.BACKGROUND.getRGB();
                        float b = (float) (color & 0xFF) / 0xFF;
                        float g = (float) ((color >>> 8) & 0xFF) / 0xFF;
                        float r = (float) ((color >>> 16) & 0xFF) / 0xFF;
                        glColor3f(r * 0.85F, g * 0.85F, b * 0.85F);

                        glBegin(GL_QUADS);
                        {
                            glVertex2i(-1, LENGTH + 1);
                            glVertex2i(LENGTH + 1, LENGTH + 1);
                            glVertex2i(LENGTH + 1, -1);
                            glVertex2i(-1, -1);
                        }
                        glEnd();

                        b = (float) (color & 0xFF) / 0xFF;
                        g = (float) ((color >>> 8) & 0xFF) / 0xFF;
                        r = (float) ((color >>> 16) & 0xFF) / 0xFF;
                        glColor3f(r, g, b);

                        glBegin(GL_QUADS);
                        {
                            glVertex2i(0, LENGTH);
                            glVertex2i(LENGTH, LENGTH);
                            glVertex2i(LENGTH, 0);
                            glVertex2i(0, 0);
                        }
                        glEnd();

                        faces[i].bindTexture(0);

                        glBegin(GL_QUADS);
                        {
                            if(faces[i].isBinded())
                            {
                                glTexCoord2f(0, 1);
                            }
                            glVertex2i(0, LENGTH);

                            if(faces[i].isBinded())
                            {
                                glTexCoord2f(1, 1);
                            }
                            glVertex2i(LENGTH, LENGTH);

                            if(faces[i].isBinded())
                            {
                                glTexCoord2f(1, 0);
                            }
                            glVertex2i(LENGTH, 0);

                            if(faces[i].isBinded())
                            {
                                glTexCoord2f(0, 0);
                            }
                            glVertex2i(0, 0);
                        }
                        glEnd();

                        TextureImpl.bindNone();

                        glColor3f(1, 1, 1);
                        glLineWidth(1.25F);

                        glBegin(GL_LINES);
                        {
                            glVertex2d(faces[i].getStartU() * (LENGTH / 16D), faces[i].getStartV() * (LENGTH / 16D));
                            glVertex2d(faces[i].getStartU() * (LENGTH / 16D), faces[i].getEndV() * (LENGTH / 16D));

                            glVertex2d(faces[i].getStartU() * (LENGTH / 16D), faces[i].getEndV() * (LENGTH / 16D));
                            glVertex2d(faces[i].getEndU() * (LENGTH / 16D), faces[i].getEndV() * (LENGTH / 16D));

                            glVertex2d(faces[i].getEndU() * (LENGTH / 16D), faces[i].getEndV() * (LENGTH / 16D));
                            glVertex2d(faces[i].getEndU() * (LENGTH / 16D), faces[i].getStartV() * (LENGTH / 16D));

                            glVertex2d(faces[i].getEndU() * (LENGTH / 16D), faces[i].getStartV() * (LENGTH / 16D));
                            glVertex2d(faces[i].getStartU() * (LENGTH / 16D), faces[i].getStartV() * (LENGTH / 16D));

                        }
                        glEnd();

                        Color colorText = BLACK_ALPHA;
                        if(hoveredFace == faces[i].getSide())
                        {
                            colorText = BLACK_ALPHA_HOVER;
                        }
                        glEnable(GL_BLEND);
                        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                        FontManager.BEBAS_NEUE_20.drawString(5, 5, Face.getFaceName(i), colorText);
                        glDisable(GL_BLEND);
                    }
                }
                glPopMatrix();
            }
        }
        glPopMatrix();
    }

    private int lastMouseX, lastMouseY;
    private int selected = -1;
    private boolean grabbing = false;

    @Override
    public void handleMouseInput(int button, int mouseX, int mouseY, boolean pressed)
    {
        if(pressed && (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)))
        {
            if(!grabbing && hoveredFace != -1)
            {
                this.lastMouseX = mouseX;
                this.lastMouseY = mouseY;
                this.grabbing = true;
                this.hoveredFace = getFace(canvasHeight, Mouse.getX(), Mouse.getY());

                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    selectedElement.setSelectedFace(this.hoveredFace);
                    manager.updateValues();
                }
            }
        }
        else
        {
            this.grabbing = false;
        }
    }

    @Override
    public void handleInput(int canvasHeight)
    {
        int newMouseX = Mouse.getX();
        int newMouseY = Mouse.getY();

        if(grabbing)
        {
            if(hoveredFace != -1 || selected != -1)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    Face face = selectedElement.getAllFaces()[(selected != -1 ? selected : hoveredFace)];

                    int xMovement = (newMouseX - this.lastMouseX) / 6;
                    int yMovement = (newMouseY - this.lastMouseY) / 6;

                    if(xMovement != 0 || yMovement != 0)
                    {
                        if(Mouse.isButtonDown(0))
                        {
                            if((face.getStartU() + xMovement) >= 0.0 && (face.getEndU() + xMovement) <= 16.0)
                            {
                                face.moveTextureU(xMovement);
                            }
                            if((face.getStartV() - yMovement) >= 0.0 && (face.getEndV() - yMovement) <= 16.0)
                            {
                                face.moveTextureV(-yMovement);
                            }
                        }
                        else
                        {
                            face.setAutoUVEnabled(false);

                            double uMovement = (face.getEndU() + xMovement);
                            if(uMovement >= 0 && uMovement <= 16.0)
                            {
                                face.addTextureXEnd(xMovement);
                            }
                            double vMovement = (face.getEndV() - yMovement);
                            if(vMovement >= 0 && vMovement <= 16.0)
                            {
                                face.addTextureYEnd(-yMovement);
                            }

                            face.setAutoUVEnabled(false);
                        }
                        face.updateEndUV();

                        if(xMovement != 0)
                        {
                            this.lastMouseX = newMouseX;
                        }
                        if(yMovement != 0)
                        {
                            this.lastMouseY = newMouseY;
                        }
                    }
                    manager.updateValues();
                }
            }
        }
        else
        {
            selected = -1;
        }
    }

    private int getFace(int canvasHeight, int mouseX, int mouseY)
    {
        for(int i = 0; i < 6; i++)
        {
            if(mouseX >= startX[i] && mouseX <= startX[i] + LENGTH)
            {
                if((canvasHeight - mouseY - 45) >= startY[i] && (canvasHeight - mouseY - 45) <= startY[i] + LENGTH)
                {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getHoveredFace()
    {
        return hoveredFace;
    }

    public boolean isGrabbing()
    {
        return grabbing;
    }
}
