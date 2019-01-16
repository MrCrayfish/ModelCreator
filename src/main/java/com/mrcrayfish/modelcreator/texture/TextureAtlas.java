package com.mrcrayfish.modelcreator.texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * Author: MrCrayfish
 */
public class TextureAtlas
{
    private static final int ATLAS_WIDTH = 1024;
    private static final int ATLAS_HEIGHT = 1024;
    private static int atlasTextureId;

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
                atlasTextureId = loadTexture(bufferedImage);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void bind()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, atlasTextureId);
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

    private static int loadTexture(BufferedImage image)
    {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);
        for(int y = 0; y < image.getHeight(); y++)
        {
            for(int x = 0; x < image.getWidth(); x++)
            {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        return textureId;
    }
}
