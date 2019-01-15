package com.mrcrayfish.modelcreator.display.render;

import com.mrcrayfish.modelcreator.Camera;
import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.display.DisplayProperties;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.texture.TextureAtlas;
import com.mrcrayfish.modelcreator.util.AtlasRenderUtil;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * Author: MrCrayfish
 */
public class GuiPropertyRenderer extends DisplayPropertyRenderer
{
    @Override
    public void onRenderPerspective(ModelCreator creator, ElementManager manager, Camera camera) {}

    @Override
    public void onRenderOverlay(ElementManager manager, Camera camera, ModelCreator creator)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        DisplayProperties.Entry entry = creator.getElementManager().getDisplayProperties().getEntry("gui");
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
            {
                TextureAtlas.bind();
                glEnable(GL_TEXTURE_2D);
                glEnable(GL_BLEND);
                glDisable(GL_CULL_FACE);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                glColor3f(1.0F, 1.0F, 1.0F);

                int scale = 30;
                int slotSize = 16 * scale;
                int startX = (canvasWidth - slotSize) / 2;
                int startY = (canvasHeight - slotSize) / 2;

                glTranslatef(startX, startY, 0);
                glScalef(scale, scale, 0);

                AtlasRenderUtil.bindTexture(TextureAtlas.GUI_SLOT);
                AtlasRenderUtil.drawQuad(0, 0, 16, 16);
            }
            glPopMatrix();

            glPushMatrix();
            {
                int scale = 24;
                int startX = canvasWidth / 2;
                int startY = canvasHeight / 2;
                glTranslatef(startX, startY, 0);
                glScaled(scale, scale, scale);
                glTranslated(entry.getTranslationX(), -entry.getTranslationY(), entry.getTranslationZ());
                glScaled(entry.getScaleX(), entry.getScaleY(), entry.getScaleZ());
                glRotatef(180F, 1, 0, 0);
                glRotatef((float) entry.getRotationX(), 1, 0, 0);
                glRotatef((float) entry.getRotationY(), 0, 1, 0);
                glRotatef((float) entry.getRotationZ(), 0, 0, 1);
                glTranslatef(0, -8, 0);

                glEnable(GL_TEXTURE_2D);
                glEnable(GL_DEPTH_TEST);
                glEnable(GL_CULL_FACE);

                this.drawElements(manager);

                glDisable(GL_DEPTH_TEST);
                glDisable(GL_CULL_FACE);
                glDisable(GL_TEXTURE_2D);
            }
            glPopMatrix();


        }
    }
}
