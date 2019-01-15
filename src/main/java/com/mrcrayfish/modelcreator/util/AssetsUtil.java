package com.mrcrayfish.modelcreator.util;

import java.io.File;

/**
 * Author: MrCrayfish
 */
public class AssetsUtil
{
    public static String getTextureDirectory(File file)
    {
        StringBuilder builder = new StringBuilder();
        File parent = file;
        while((parent = parent.getParentFile()) != null)
        {
            if(!parent.getName().equals("textures"))
            {
                builder.insert(0, parent.getName()).insert(0, "/");
                continue;
            }
            return builder.length() > 0 ? builder.substring(1, builder.length()) : "";
        }
        return "blocks";
    }

    public static String getModId(File file)
    {
        File previous = null;
        File parent = file;
        while((parent = parent.getParentFile()) != null)
        {
            if(parent.getName().equals("assets"))
            {
                break;
            }
            previous = parent;
        }
        return previous != null && Util.hasFolder(previous, "textures") ? previous.getName() : "minecraft";
    }
}
