package com.mrcrayfish.modelcreator.display.render;

import com.mrcrayfish.modelcreator.Animation;
import com.mrcrayfish.modelcreator.Camera;
import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.display.DisplayProperties;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.glDisable;

/**
 * Author: MrCrayfish
 */
public class FixedPropertyRenderer extends DisplayPropertyRenderer
{
    public FixedPropertyRenderer()
    {
        this.addElements();
    }

    private void addElements()
    {
        Element frameOne = new Element(12, 1, 1);
        frameOne.setStartX(-6);
        frameOne.setStartY(2);
        frameOne.setStartZ(-1);
        elements.add(frameOne);

        Element frameTwo = new Element(12, 1, 1);
        frameTwo.setStartX(-6);
        frameTwo.setStartY(13);
        frameTwo.setStartZ(-1);
        elements.add(frameTwo);

        Element frameThree = new Element(10, 10, 0.5);
        frameThree.setStartX(-5);
        frameThree.setStartY(3);
        frameThree.setStartZ(-1);
        elements.add(frameThree);

        Element frameFour = new Element(1, 10, 1);
        frameFour.setStartX(-6);
        frameFour.setStartY(3);
        frameFour.setStartZ(-1);
        elements.add(frameFour);

        Element frameFive = new Element(1, 10, 1);
        frameFive.setStartX(5);
        frameFive.setStartY(3);
        frameFive.setStartZ(-1);
        elements.add(frameFive);
    }

    @Override
    public void onInit(Camera camera)
    {
        camera.setX(0);
        camera.setY(-9);
        camera.setZ(-30);
        camera.setRX(0);
        camera.setRY(0);
        camera.setRZ(0);
    }

    @Override
    public void onRenderPerspective(ModelCreator creator, ElementManager manager, Camera camera)
    {
        DisplayProperties.Entry entry = creator.getElementManager().getDisplayProperties().getEntry("fixed");
        if(entry != null)
        {
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glEnable(GL_DEPTH_TEST);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            camera.useView();

            glTranslated(0, -5, 0);
            glScalef(1.75F, 1.75F, 1.75F);

            glPushMatrix();
            for(Element element : elements)
            {
                element.drawExtras(manager);
                element.draw();
            }
            glPopMatrix();

            glTranslated(0, 8, 0);
            glScaled(entry.getScaleX(), entry.getScaleY(), entry.getScaleZ());
            glRotatef(180F, 0, 1, 0);
            glScalef(0.5F, 0.5F, 0.5F);
            glTranslated(entry.getTranslationX(), entry.getTranslationY(), entry.getTranslationZ());
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
