package com.mrcrayfish.modelcreator.display.render;

import com.mrcrayfish.modelcreator.Camera;
import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.display.DisplayProperties;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.texture.TextureAtlas;
import com.mrcrayfish.modelcreator.util.AtlasRenderUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.TextureImpl;

import static org.lwjgl.opengl.GL11.*;

/**
 * Author: MrCrayfish
 */
public class FirstPersonPropertyRenderer extends DisplayPropertyRenderer
{
    private boolean leftHanded;

    public FirstPersonPropertyRenderer(boolean leftHanded)
    {
        this.leftHanded = leftHanded;
    }

    @Override
    public void onRenderPerspective(ModelCreator creator, ElementManager manager, Camera camera) {}

    @Override
    public void onRenderOverlay(ElementManager manager, Camera camera, ModelCreator creator)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        DisplayProperties.Entry entry = creator.getElementManager().getDisplayProperties().getEntry(!leftHanded ? "firstperson_righthand" : "firstperson_lefthand");
        if(entry != null)
        {
            int canvasOffset = creator.getCanvasOffset();
            int canvasWidth = creator.getCanvasWidth() - creator.getCanvasOffset();
            int canvasHeight = creator.getCanvasHeight();

            glViewport(canvasOffset, 0, canvasWidth, canvasHeight);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            GL11.glOrtho(canvasOffset, canvasWidth, canvasHeight, 0, 500.0D, -500.0D);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();

            glPushMatrix();

            TextureAtlas.bind();
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glDisable(GL_CULL_FACE);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glColor3f(1.0F, 1.0F, 1.0F);

            double startX = (canvasWidth - 960) / 2;
            double startY = (canvasHeight - 540) / 2;

            glTranslated((int) startX, (int) startY, 0);
            AtlasRenderUtil.bindTexture(TextureAtlas.FIRST_PERSON_PREVIEW);
            AtlasRenderUtil.drawQuad(0, 0, 960, 540);

            glEnable(GL_SCISSOR_TEST);
            glScissor((int) startX, (int) startY, 960, 540);

            glPopMatrix();

            glPushMatrix();
            {
                glViewport((int) startX, (int) startY, 960, 540);
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                GLU.gluPerspective(-70F, (float) 960 / (float) 540, 0.3F, 1000F);
                glMatrixMode(GL_MODELVIEW);
                glLoadIdentity();
                glEnable(GL_DEPTH_TEST);

                if(leftHanded)
                {
                    glScaled(-1, 1, 1);
                }

                glTranslated(-9, 8.35, -11.5);
                glTranslated(-entry.getTranslationX(), -entry.getTranslationY(), entry.getTranslationZ());
                glScaled(entry.getScaleX(), entry.getScaleY(), entry.getScaleZ());
                glRotatef(180F, 1, 0, 0);
                glRotatef((float) entry.getRotationX(), 1, 0, 0);
                glRotatef((float) entry.getRotationY(), 0, 1, 0);
                glRotatef((float) entry.getRotationZ(), 0, 0, 1);
                glTranslated(0, -8, 0);
                glRotated(180F, 0, 1, 0);

                glEnable(GL_TEXTURE_2D);
                glEnable(GL_DEPTH_TEST);
                glEnable(GL_CULL_FACE);

                if(leftHanded)
                {
                    glScaled(-1, 1, 1);
                    glRotatef(180F, 0, 1, 0);
                }
                this.drawElements(manager);

                glDisable(GL_DEPTH_TEST);
                glDisable(GL_CULL_FACE);
                glDisable(GL_TEXTURE_2D);
                glDisable(GL_SCISSOR_TEST);
            }
            glPopMatrix();
        }
    }
}
