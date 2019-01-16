package com.mrcrayfish.modelcreator.display.render;

import com.mrcrayfish.modelcreator.Camera;
import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.component.Menu;
import com.mrcrayfish.modelcreator.display.CanvasRenderer;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.util.FontManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;

import static org.lwjgl.opengl.GL11.*;

/**
 * Author: MrCrayfish
 */
public class StandardRenderer extends CanvasRenderer
{
    @Override
    public void onInit(Camera camera)
    {
        camera.setX(0);
        camera.setY(-5);
        camera.setZ(-30);
        camera.setRX(20);
        camera.setRY(0);
        camera.setRZ(0);
    }

    @Override
    public void onRenderPerspective(ModelCreator creator, ElementManager manager, Camera camera)
    {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        camera.useView();

        this.drawGrid(camera, true); //TODO make this an option
        this.drawElements(manager);

        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
    }

    protected void drawElements(ElementManager manager)
    {
        glTranslatef(-8, 0, -8);
        for(int i = 0; i < manager.getElementCount(); i++)
        {
            Element cube = manager.getElement(i);
            if(cube.isVisible())
            {
                glLoadName(i + 1);
                cube.draw();
                glLoadName(0);
                cube.drawExtras(manager);
            }
        }

        Element selectedElement = manager.getSelectedElement();
        if(selectedElement != null && selectedElement.isVisible())
        {
            selectedElement.drawOutline();
        }
    }

    protected void drawGrid(Camera camera, boolean renderCardinalPoints)
    {
        if(Menu.isDisplayPropsShowing && !Menu.shouldRenderGrid)
        {
            return;
        }

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
                for(int i = 1; i <= 16; i++)
                {
                    glVertex3i(i, 0, 0);
                    glVertex3i(i, 0, 16);
                }

                for(int i = 1; i <= 16; i++)
                {
                    glVertex3i(0, 0, i);
                    glVertex3i(16, 0, i);
                }
            }
            glEnd();
        }
        glPopMatrix();

        glPushMatrix();
        {
            TextureImpl.bindNone();

            glTranslatef(-8, 0, -8);
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glShadeModel(GL_SMOOTH);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            glPushMatrix();
            glTranslated(0, 0, 16);
            glScaled(0.015, 0.015, 0.015);
            glRotated(90, 1, 0, 0);
            FontManager.BEBAS_NEUE_50.drawString(0, 0, "MrCrayfish's Model Creator", new Color(0.5F, 0.5F, 0.6F));
            glPopMatrix();

            if(renderCardinalPoints)
            {
                glPushMatrix();
                glTranslated(8, 0, 17);
                glScaled(0.025, 0.025, 0.025);
                glRotated(-camera.getRY(), 0, 1, 0);
                glTranslated(-(FontManager.BEBAS_NEUE_50.getWidth("S") / 2), 0, -(FontManager.BEBAS_NEUE_50.getHeight() / 2));
                glRotated(90, 1, 0, 0);
                FontManager.BEBAS_NEUE_50.drawString(0, 0, "S", new Color(0.5F, 0.5F, 0.6F));
                glPopMatrix();

                glPushMatrix();
                glTranslated(8, 0, -1);
                glScaled(0.025, 0.025, 0.025);
                glRotated(-camera.getRY(), 0, 1, 0);
                glTranslated(-(FontManager.BEBAS_NEUE_50.getWidth("N") / 2), 0, -(FontManager.BEBAS_NEUE_50.getHeight() / 2));
                glRotated(90, 1, 0, 0);
                FontManager.BEBAS_NEUE_50.drawString(0, 0, "N", new Color(0.5F, 0.5F, 0.6F));
                glPopMatrix();

                glPushMatrix();
                glTranslated(-1, 0, 8);
                glScaled(0.025, 0.025, 0.025);
                glRotated(-camera.getRY(), 0, 1, 0);
                glTranslated(-(FontManager.BEBAS_NEUE_50.getWidth("W") / 2), 0, -(FontManager.BEBAS_NEUE_50.getHeight() / 2));
                glRotated(90, 1, 0, 0);
                FontManager.BEBAS_NEUE_50.drawString(0, 0, "W", new Color(0.5F, 0.5F, 0.6F));
                glPopMatrix();

                glPushMatrix();
                glTranslated(17, 0, 8);
                glScaled(0.025, 0.025, 0.025);
                glRotated(-camera.getRY(), 0, 1, 0);
                glTranslated(-(FontManager.BEBAS_NEUE_50.getWidth("E") / 2), 0, -(FontManager.BEBAS_NEUE_50.getHeight() / 2));
                glRotated(90, 1, 0, 0);
                FontManager.BEBAS_NEUE_50.drawString(0, 0, "E", new Color(0.5F, 0.5F, 0.6F));
                glPopMatrix();
            }

            glDisable(GL_TEXTURE_2D);
            glDisable(GL_BLEND);
        }
        glPopMatrix();
    }
}
