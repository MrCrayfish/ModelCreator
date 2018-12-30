package com.mrcrayfish.modelcreator.display.render;

import com.mrcrayfish.modelcreator.Camera;
import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.display.DisplayProperties;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;

import static org.lwjgl.opengl.GL11.*;

/**
 * Author: MrCrayfish
 */
public class HeadPropertyRenderer extends DisplayPropertyRenderer
{
    public HeadPropertyRenderer()
    {
        this.addElements();
    }

    private void addElements()
    {
        Element rightArm = new Element(4, 12, 4);
        rightArm.setStartX(-8);
        rightArm.setStartY(-12);
        rightArm.setStartZ(-2);
        rightArm.setOriginX(-6);
        rightArm.setOriginY(-2);
        rightArm.setOriginZ(0);
        rightArm.setRotation(15F); //TODO maybe add option to change
        elements.add(rightArm);

        Element leftArm = new Element(4, 12, 4);
        leftArm.setStartX(4);
        leftArm.setStartY(-12);
        leftArm.setStartZ(-2);
        leftArm.setOriginX(6);
        leftArm.setOriginY(-2);
        leftArm.setOriginZ(0);
        leftArm.setRotation(-15F); //TODO maybe add option to change
        elements.add(leftArm);

        Element body = new Element(8, 12, 4);
        body.setStartX(-4);
        body.setStartY(-12);
        body.setStartZ(-2);
        body.setOriginX(0);
        body.setOriginY(0);
        body.setOriginZ(0);
        elements.add(body);

        Element head = new Element(8, 8, 8);
        head.setStartX(-4);
        head.setStartY(0);
        head.setStartZ(-4);
        head.setOriginX(0);
        head.setOriginY(0);
        head.setOriginZ(0);
        elements.add(head);

        Element rightLeg = new Element(4, 12, 4);
        rightLeg.setStartX(-4);
        rightLeg.setStartY(-24);
        rightLeg.setStartZ(-2);
        rightLeg.setOriginX(4);
        rightLeg.setOriginY(-12);
        rightLeg.setOriginZ(0);
        rightLeg.setRotation(-15F);
        elements.add(rightLeg);

        Element leftLeg = new Element(4, 12, 4);
        leftLeg.setStartX(0);
        leftLeg.setStartY(-24);
        leftLeg.setStartZ(-2);
        leftLeg.setOriginX(8);
        leftLeg.setOriginX(2);
        leftLeg.setOriginY(-12);
        leftLeg.setOriginZ(0);
        leftLeg.setRotation(15F);
        elements.add(leftLeg);
    }

    @Override
    public void onRenderPerspective(ModelCreator creator, ElementManager manager, Camera camera)
    {
        DisplayProperties.Entry entry = creator.getElementManager().getDisplayProperties().getEntry("head");
        if(entry != null)
        {
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glEnable(GL_DEPTH_TEST);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            camera.useView();

            glScaled(2.0, 2.0, 2.0);

            for(Element element : elements)
            {
                element.drawExtras(manager);
                element.draw();
            }

            glTranslated(0, 4, 0);
            glTranslated(-entry.getTranslationX(), entry.getTranslationY(), -entry.getTranslationZ());
            glScaled(entry.getScaleX(), entry.getScaleY(), entry.getScaleZ());
            glRotatef(180F, 0, 1, 0);
            glRotatef((float) entry.getRotationX(), 1, 0, 0);
            glRotatef((float) entry.getRotationY(), 0, 1, 0);
            glRotatef((float) entry.getRotationZ(), 0, 0, 1);
            glTranslated(0, -4, 0);
            glScalef(0.625F, 0.625F, 0.625F);

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
