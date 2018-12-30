package com.mrcrayfish.modelcreator.display.render;

import com.mrcrayfish.modelcreator.Animation;
import com.mrcrayfish.modelcreator.Camera;
import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.display.DisplayProperties;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;

import static org.lwjgl.opengl.GL11.*;

/**
 * Author: MrCrayfish
 */
public class GroundPropertyRenderer extends DisplayPropertyRenderer
{
    public GroundPropertyRenderer()
    {
        this.addElements();
    }

    private void addElements()
    {
        Element block = new Element(16, 16, 16);
        block.setStartX(-8);
        block.setStartY(-16);
        block.setStartZ(-8);
        elements.add(block);
    }

    @Override
    public void onInit(Camera camera)
    {
        camera.setX(0);
        camera.setY(0);
        camera.setZ(-25);
        camera.setRX(20);
        camera.setRY(0);
        camera.setRZ(0);
    }

    @Override
    public void onRenderPerspective(ModelCreator creator, ElementManager manager, Camera camera)
    {
        DisplayProperties.Entry entry = creator.getElementManager().getDisplayProperties().getEntry("ground");
        if(entry != null)
        {
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glEnable(GL_DEPTH_TEST);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            camera.useView();

            for(Element element : elements)
            {
                element.drawExtras(manager);
                element.draw();
            }

            glTranslated(-entry.getTranslationX(), entry.getTranslationY(), -entry.getTranslationZ());
            glScaled(entry.getScaleX(), entry.getScaleY(), entry.getScaleZ());

            glTranslatef(0, 7, 0);

            double yOffset = (Animation.getCounter() + Animation.getCounter()) / 60F;
            yOffset = Math.sin(yOffset);
            glTranslated(0, yOffset * 3, 0);
            glRotated((Animation.getCounter() + Animation.getPartialTicks()), 0, 1, 0);

            glRotatef(180F, 0, 1, 0);
            glRotatef((float) entry.getRotationX(), 1, 0, 0);
            glRotatef((float) entry.getRotationY(), 0, 1, 0);
            glRotatef((float) entry.getRotationZ(), 0, 0, 1);
            glTranslated(0, -8, 0);

            glPushMatrix();
            {
                this.drawGrid();
                this.drawElements(manager);
            }
            glPopMatrix();

            glDisable(GL_DEPTH_TEST);
            glDisable(GL_CULL_FACE);
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_LIGHTING);
        }
    }
}
