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
    private TextureProperties props;

    public TextureEntry(File texture) throws IOException
    {
        this.path = new TexturePath(texture);
        this.key = path.getName();
        this.textureFile = texture;
        this.source = ImageIO.read(texture);
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

    public void setTextureFile(File texture)
    {
        try
        {
            this.textureFile = texture;
            this.source = ImageIO.read(texture);
            this.icon = createIcon(this.source);
            TextureManager.loadTexture(this);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
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
            textures = null;
        }
    }

    private static ImageIcon createIcon(BufferedImage source)
    {
        source = source.getSubimage(0, 0, source.getWidth(), source.getWidth());
        Image scaledImage = source.getScaledInstance(64, 64, java.awt.Image.SCALE_FAST);
        return new ImageIcon(scaledImage);
    }

    /*public static boolean loadExternalTexture(File file, File meta) throws IOException
    {
        TextureMeta textureMeta = TextureMeta.parse(meta);

        if(textureMeta != null)
        {
            if(textureMeta.getAnimation() != null)
            {
                BufferedImage image = ImageIO.read(file);

                int width = textureMeta.getAnimation().getWidth();
                int height = textureMeta.getAnimation().getHeight();

                ImageIcon icon = null;

                List<Texture> textures = new ArrayList<>();

                int x = 0;
                while(x + width <= image.getWidth())
                {
                    int y = 0;
                    while(y + height <= image.getHeight())
                    {
                        BufferedImage subImage = image.getSubimage(x, y, width, height);
                        if(icon == null)
                        {
                            icon = TextureManager.upscale(new ImageIcon(subImage), 256);
                        }
                        Texture texture = BufferedImageUtil.getTexture("", subImage);
                        textures.add(texture);
                        y += height;
                    }
                    x += width;
                }
                String imageName = file.getName();
                //textureCache.add(new TextureEntry(file.getName().substring(0, imageName.indexOf(".png")), textures, icon, file.getAbsolutePath(), textureMeta, meta.getAbsolutePath()));
                return true;
            }
            return loadTexture(file, textureMeta, meta.getAbsolutePath());
        }
        return loadTexture(file, null, null);
    }

    private static boolean loadTexture(File image, TextureMeta meta, String location) throws IOException
    {
        FileInputStream is = new FileInputStream(image);
        Texture texture = TextureLoader.getTexture("PNG", is);
        is.close();

        if(texture.getImageHeight() % 16 != 0 || texture.getImageWidth() % 16 != 0)
        {
            texture.release();
            return false;
        }
        ImageIcon icon = upscale(new ImageIcon(image.getAbsolutePath()), 256);
        //textureCache.add(new TextureEntry(image.getName().replace(".png", "").replaceAll("\\d*$", ""), texture, icon, image.getAbsolutePath(), meta, location));
        return true;
    }
     */
}
