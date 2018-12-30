package com.mrcrayfish.modelcreator;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Author: MrCrayfish
 */
public class Textures
{
    public static Texture guiSlot;

    public static void load()
    {
        guiSlot = loadTexture("slot");
    }

    private static Texture loadTexture(String id)
    {
        try
        {
            URL url = Textures.class.getClassLoader().getResource("textures/" + id + ".png");
            BufferedImage bufferedImage = ImageIO.read(url);
            return BufferedImageUtil.getTexture("", bufferedImage);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
