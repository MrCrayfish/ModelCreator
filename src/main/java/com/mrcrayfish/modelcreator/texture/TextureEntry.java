package com.mrcrayfish.modelcreator.texture;

import com.mrcrayfish.modelcreator.util.AssetsUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

public class TextureEntry
{
    private String id;
    private String modId = "minecraft";
    private String directory = "blocks";
    private String name;

    private BufferedImage source;
    private ImageIcon icon;
    private List<Integer> textures;

    private File textureFile;
    private File metaFile;

    private TextureAnimation anim;
    private TextureProperties props;

    public TextureEntry(File texture) throws IOException
    {
        this.id = texture.getName().substring(0, texture.getName().indexOf("."));
        this.modId = AssetsUtil.getModId(texture);
        this.directory = AssetsUtil.getTexturePath(texture);
        this.name = this.id;
        this.textureFile = texture;
        this.source = ImageIO.read(texture);
        this.icon = resize(this.source, 64);
        File metaFile = new File(texture.getAbsolutePath() + ".mcmeta");
        if(metaFile.exists())
        {
            this.metaFile = metaFile;
        }
    }

    public TextureEntry(String id, File texture) throws IOException
    {
        this.id = id;
        this.modId = AssetsUtil.getModId(texture);
        this.directory = AssetsUtil.getTexturePath(texture);
        this.name = this.id;
        this.textureFile = texture;
        this.source = ImageIO.read(texture);
        this.icon = resize(this.source, 64);
        File metaFile = new File(texture.getAbsolutePath() + ".mcmeta");
        if(metaFile.exists())
        {
            this.metaFile = metaFile;
        }
    }

    public String getId()
    {
        return id;
    }

    public String getModId()
    {
        return modId;
    }

    public String getDirectory()
    {
        return directory;
    }

    public String getName()
    {
        return name;
    }

    public void bindTexture()
    {
        if(textures != null)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.get(this.isAnimated() ? anim.getCurrentAnimationFrame() : 0));
        }
    }

    public void bindNextTexture()
    {
        if(textures != null)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.get(this.isAnimated() ? anim.getNextAnimationFrame() : 0));
        }
    }

    public BufferedImage getSource()
    {
        return source;
    }

    public ImageIcon getIcon()
    {
        return icon;
    }

    public String getTextureLocation()
    {
        return textureFile.getAbsolutePath();
    }

    public TextureAnimation getAnimation()
    {
        return anim;
    }

    public boolean isAnimated()
    {
        return anim != null;
    }

    public TextureProperties getProperties()
    {
        return props;
    }

    public boolean hasProperties()
    {
        return props != null;
    }

    public String getMetaLocation()
    {
        return metaFile.getAbsolutePath();
    }

    public int getPasses()
    {
        if(anim != null)
        {
            return anim.getPasses();
        }
        return 1;
    }

    public File getTextureFile()
    {
        return textureFile;
    }

    public boolean isLoaded()
    {
        return textures != null;
    }

    public void loadTexture()
    {
        if(textures == null)
        {
            int[] pixels = new int[source.getWidth() * source.getHeight()];
            source.getRGB(0, 0, source.getWidth(), source.getHeight(), pixels, 0, source.getWidth());
            ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);
            for(int y = 0; y < source.getHeight(); y++)
            {
                for(int x = 0; x < source.getWidth(); x++)
                {
                    int pixel = pixels[y * source.getWidth() + x];
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
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, source.getWidth(), source.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            this.textures = Collections.singletonList(textureId);
        }
    }

    public void deleteTexture()
    {
        if(textures != null)
        {
            for(int i : textures)
            {
                GL11.glDeleteTextures(i);
            }
        }
    }

    private static ImageIcon resize(BufferedImage source, int size)
    {
        Image scaledImage = source.getScaledInstance(size, size, java.awt.Image.SCALE_FAST);
        return new ImageIcon(scaledImage);
    }
}
