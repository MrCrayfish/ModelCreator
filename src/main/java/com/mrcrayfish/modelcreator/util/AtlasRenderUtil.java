package com.mrcrayfish.modelcreator.util;

import com.mrcrayfish.modelcreator.texture.TextureAtlas;

import static org.lwjgl.opengl.GL11.*;

/**
 * Author: MrCrayfish
 */
public class AtlasRenderUtil
{
    private static TextureAtlas.Entry entry;

    public static void bindTexture(TextureAtlas.Entry entry)
    {
        AtlasRenderUtil.entry = entry;
    }

    public static void drawQuad(int startX, int startY, int endX, int endY)
    {
        TextureAtlas.bind();
        glBegin(GL_QUADS);
        {
            if(entry != null)
            {
                glTexCoord2d(entry.getU(), entry.getV());
            }
            glVertex2f(startX, startY);
            if(entry != null)
            {
                glTexCoord2d(entry.getU() + entry.getWidth(), entry.getV());
            }
            glVertex2f(endX, startY);
            if(entry != null)
            {
                glTexCoord2d(entry.getU() + entry.getWidth(), entry.getV() + entry.getHeight());
            }
            glVertex2f(endX, endY);
            if(entry != null)
            {
                glTexCoord2d(entry.getU(), entry.getV() + entry.getHeight());
            }
            glVertex2f(startX, endY);
        }
        glEnd();
    }
}
