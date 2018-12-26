package com.mrcrayfish.modelcreator.texture;

import org.newdawn.slick.opengl.Texture;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TextureEntry
{
    private String name;
    private ImageIcon image;
    private List<Texture> textures;
    private String textureLocation;
    private String metaLocation;

    private TextureAnimation anim;
    private TextureProperties props;

    public TextureEntry(String name, Texture texture, ImageIcon image, String textureLocation, TextureMeta meta, String metaLocation)
    {
        this.name = name;
        this.textures = Collections.singletonList(texture);
        this.image = image;
        this.textureLocation = textureLocation;
        if(meta != null)
        {
            this.anim = meta.getAnimation();
        }
        if(meta != null)
        {
            this.props = meta.getProperties();
        }
        this.metaLocation = metaLocation;
    }

    public TextureEntry(String name, List<Texture> textures, ImageIcon image, String textureLocation, TextureMeta meta, String metaLocation)
    {
        this.name = name;
        this.textures = textures;
        this.image = image;
        this.textureLocation = textureLocation;
        if(meta != null)
        {
            this.anim = meta.getAnimation();
        }
        if(meta != null)
        {
            this.props = meta.getProperties();
        }
        this.metaLocation = metaLocation;
    }

    public String getName()
    {
        return name;
    }

    public Texture getTexture()
    {
        if(isAnimated())
        {
            return textures.get(anim.getCurrentAnimationFrame());
        }
        return textures.get(0);
    }

    public Texture getNextTexture()
    {
        if(isAnimated())
        {
            return textures.get(anim.getNextAnimationFrame());
        }
        return textures.get(0);
    }

    public ImageIcon getImage()
    {
        return image;
    }

    public String getTextureLocation()
    {
        return textureLocation;
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
        return metaLocation;
    }

    public int getPasses()
    {
        if(anim != null)
        {
            return anim.getPasses();
        }
        return 1;
    }
}
