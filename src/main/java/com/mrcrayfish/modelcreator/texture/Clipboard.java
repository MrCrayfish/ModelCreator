package com.mrcrayfish.modelcreator.texture;

import com.mrcrayfish.modelcreator.element.Face;

public class Clipboard
{
    // TODO make it so you can copy and paste certain properties. Eg Copy only texture and tint index, not UV.
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
