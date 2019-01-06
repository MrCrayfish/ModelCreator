package com.mrcrayfish.modelcreator.texture;

public class Clipboard
{
    private static ClipboardTexture texture;

    public static void copyTexture(String location, String texture)
    {
        Clipboard.texture = new ClipboardTexture(location, texture);
    }

    public static ClipboardTexture getTexture()
    {
        return Clipboard.texture;
    }
}
