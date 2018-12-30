package com.mrcrayfish.modelcreator;

/**
 * Author: MrCrayfish
 */
public class Animation
{
    private static int counter;
    private static float partialTicks;

    public static void tick()
    {
        Animation.counter++;
    }

    public static void setPartialTicks(float partialTicks)
    {
        Animation.partialTicks = partialTicks;
    }

    public static int getCounter()
    {
        return Animation.counter;
    }

    public static float getPartialTicks()
    {
        return partialTicks;
    }
}
