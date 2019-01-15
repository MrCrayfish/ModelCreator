package com.mrcrayfish.modelcreator;

import com.mrcrayfish.modelcreator.util.AssetsUtil;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Author: MrCrayfish
 */
public class TexturePath
{
    public static final Pattern PATTERN = Pattern.compile("([a-z_0-9]+:)?([a-z_0-9]+/)*[a-z_0-9]+");

    private String modId = "minecraft";
    private String directory;
    private String name;

    public TexturePath(String s)
    {
        String[] split = s.split(":");
        if(split.length == 2)
        {
            this.modId = split[0];
        }
        String assetPath = split[split.length - 1];
        this.directory = assetPath.substring(0, Math.max(0, assetPath.lastIndexOf("/")));
        this.name = assetPath.replace(this.directory, "").substring(1);
    }

    public TexturePath(File file)
    {
        this.modId = AssetsUtil.getModId(file);
        this.directory = AssetsUtil.getTextureDirectory(file);
        this.name = file.getName().substring(0, file.getName().indexOf("."));
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

    @Override
    public String toString()
    {
        return modId + ":" + directory + "/" + name;
    }

    public String toRelativePath()
    {
        return modId + File.separator + "textures" + File.separator + directory + File.separator + name + ".png";
    }
}
