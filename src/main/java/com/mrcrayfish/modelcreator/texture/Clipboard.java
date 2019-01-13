package com.mrcrayfish.modelcreator.texture;

import com.mrcrayfish.modelcreator.element.Face;

public class Clipboard
{
    private static TextureEntry entry;

    public static void copyTexture(Face face)
    {
        entry = face.getTexture();
    }

    public static TextureEntry getTexture()
    {
        return entry;
    }
}
