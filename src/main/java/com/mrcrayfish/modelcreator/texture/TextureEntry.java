package com.mrcrayfish.modelcreator.texture;

import com.mrcrayfish.modelcreator.util.AssetsUtil;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TextureEntry
{
    private String id;
    private String modId = "minecraft";
    private String directory = "blocks";
    private String name;

    private ImageIcon icon;
    private List<Texture> textures;

    private File textureFile;
    private File metaFile;

    private TextureAnimation anim;
    private TextureProperties props;

    public TextureEntry(File texture)
    {
        this.id = texture.getName().substring(0, texture.getName().indexOf("."));
        this.modId = AssetsUtil.getModId(texture);
        this.directory = AssetsUtil.getTexturePath(texture);
        this.name = this.id;
        this.textureFile = texture;
        this.icon = upscale(new ImageIcon(texture.getAbsolutePath()), 64);
        File meta = new File(texture.getAbsolutePath() + ".mcmeta");
        if(meta.exists())
        {
            metaFile = meta;
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

    public Texture getTexture()
    {
        this.loadTexture();
        if(this.isAnimated())
        {
            return textures.get(anim.getCurrentAnimationFrame());
        }
        return textures.get(0);
    }

    public Texture getNextTexture()
    {
        this.loadTexture();
        if(this.isAnimated())
        {
            return textures.get(anim.getNextAnimationFrame());
        }
        return textures.get(0);
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

    private void loadTexture()
    {
        try
        {
            try(FileInputStream is = new FileInputStream(textureFile))
            {
                Texture texture = TextureLoader.getTexture("PNG", is);
                this.textures = Collections.singletonList(texture);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static ImageIcon upscale(ImageIcon source, int length)
    {
        Image scaledImage = source.getImage().getScaledInstance(length, length, java.awt.Image.SCALE_FAST);
        return new ImageIcon(scaledImage);
    }
}
