package com.mrcrayfish.modelcreator;

import java.io.File;

/**
 * Author: MrCrayfish
 */
public class TexturePath
{
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

    public String toRelativePath()
    {
        return modId + File.separator + "textures" + File.separator + directory + File.separator + name + ".png";
    }
}
