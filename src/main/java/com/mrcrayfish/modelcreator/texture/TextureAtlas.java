package com.mrcrayfish.modelcreator.texture;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Author: MrCrayfish
 */
public class TextureAtlas
{
    private static final int ATLAS_WIDTH = 1024;
    private static final int ATLAS_HEIGHT = 1024;
    private static Texture ATLAS;

    public static final Entry GUI_SLOT;
    public static final Entry FIRST_PERSON_PREVIEW;

    static
    {
        GUI_SLOT = new Entry(0, 0, 20, 20);
        FIRST_PERSON_PREVIEW = new Entry(0, 20, 512, 288);
    }

    public static void load()
    {
        try
        {
            URL url = TextureAtlas.class.getClassLoader().getResource("atlas.png");
            if(url != null)
            {
                BufferedImage bufferedImage = ImageIO.read(url);
                ATLAS = BufferedImageUtil.getTexture("", bufferedImage);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void bind()
    {
        ATLAS.bind();
    }

    public static class Entry
    {
        int u, v;
        int width, height;

        private Entry(int u, int v, int width, int height)
        {
            this.u = u;
            this.v = v;
            this.width = width;
            this.height = height;
        }

        public double getU()
        {
            return u / (double) ATLAS_WIDTH;
        }

        public double getV()
        {
            return v / (double) ATLAS_HEIGHT;
        }

        public double getWidth()
        {
            return width / (double) ATLAS_WIDTH;
        }

        public double getHeight()
        {
            return height / (double) ATLAS_HEIGHT;
        }
    }
}
