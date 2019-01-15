package com.mrcrayfish.modelcreator.texture;

import com.mrcrayfish.modelcreator.TexturePath;
import com.mrcrayfish.modelcreator.component.TextureManager;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class TextureEntry
{
    public static final Pattern KEY_PATTERN = Pattern.compile("[a-z_0-9]+");

    private String key;
    private TexturePath path;

    private BufferedImage source;
    private ImageIcon icon;
    private List<Integer> textures;

    private File textureFile;
    private File metaFile;

    private TextureAnimation anim;

    public TextureEntry(File texture) throws IOException
    {
        this.path = new TexturePath(texture);
        this.key = path.getName();
        this.textureFile = texture;
        this.source = ImageIO.read(texture);
        this.anim = TextureAnimation.getAnimationForTexture(texture, this.source.getWidth(), this.source.getHeight());
        this.icon = createIcon(this.source);
        File metaFile = new File(texture.getAbsolutePath() + ".mcmeta");
        if(metaFile.exists())
        {
            this.metaFile = metaFile;
        }
    }

    public TextureEntry(String key, File texture) throws IOException
    {
        this.key = key;
        this.path = new TexturePath(texture);
        this.textureFile = texture;
        this.source = ImageIO.read(texture);
        this.anim = TextureAnimation.getAnimationForTexture(texture, this.source.getWidth(), this.source.getHeight());
        this.icon = createIcon(this.source);
        File metaFile = new File(texture.getAbsolutePath() + ".mcmeta");
        if(metaFile.exists())
        {
            this.metaFile = metaFile;
        }
    }

    public TextureEntry(String key, TexturePath path, File texture) throws IOException
    {
        this.key = key;
        this.path = path;
        this.textureFile = texture;
        this.source = ImageIO.read(texture);
        this.anim = TextureAnimation.getAnimationForTexture(texture, this.source.getWidth(), this.source.getHeight());
        this.icon = createIcon(this.source);
        File metaFile = new File(texture.getAbsolutePath() + ".mcmeta");
        if(metaFile.exists())
        {
            this.metaFile = metaFile;
        }
    }

    public void setTexturePath(TexturePath path)
    {
        this.path = path;
    }

    public TexturePath getTexturePath()
    {
        return path;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getModId()
    {
        return path.getModId();
    }

    public String getDirectory()
    {
        return path.getDirectory();
    }

    public String getName()
    {
        return path.getName();
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

    public TextureAnimation getAnimation()
    {
        return anim;
    }

    public boolean isAnimated()
    {
        return anim != null;
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

    public void setTextureFile(File texture)
    {
        try
        {
            this.textureFile = texture;
            this.source = ImageIO.read(texture);
            this.icon = createIcon(this.source);
            this.anim = TextureAnimation.getAnimationForTexture(texture, source.getWidth(), source.getHeight());
            TextureManager.loadTexture(this);
        }
        catch(IOException e)
        {
            e.printStackTrace();
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
            textures = null;
        }
    }

    private static ImageIcon createIcon(BufferedImage source)
    {
        source = source.getSubimage(0, 0, source.getWidth(), source.getWidth());
        Image scaledImage = source.getScaledInstance(64, 64, java.awt.Image.SCALE_FAST);
        return new ImageIcon(scaledImage);
    }

    public void loadTexture()
    {
        if(textures == null)
        {
            if(anim == null)
            {
                this.textures = Collections.singletonList(loadTexture(source));
            }
            else
            {
                List<Integer> textures = new ArrayList<>();
                int width = anim.getWidth();
                int height = anim.getHeight();
                int x = 0;
                while(x + width <= source.getWidth())
                {
                    int y = 0;
                    while(y + height <= source.getHeight())
                    {
                        BufferedImage subImage = source.getSubimage(x, y, width, height);
                        textures.add(loadTexture(subImage));
                        y += height;
                    }
                    x += width;
                }
                this.textures = textures;
            }
        }
    }

    private int loadTexture(BufferedImage image)
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
