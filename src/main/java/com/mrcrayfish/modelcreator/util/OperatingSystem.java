package com.mrcrayfish.modelcreator.util;

/**
 * Author: MrCrayfish
 */
public enum OperatingSystem
{
    WINDOWS,
    MAC,
    LINUX,
    SOLARIS,
    UNKNOWN;

    public static OperatingSystem get()
    {
        String name = System.getProperty("os.name").toLowerCase();
        if(name.contains("win"))
        {
            return OperatingSystem.WINDOWS;
        }
        if(name.contains("mac"))
        {
            return OperatingSystem.MAC;
        }
        if(name.contains("solaris"))
        {
            return OperatingSystem.SOLARIS;
        }
        if(name.contains("sunos"))
        {
            return OperatingSystem.SOLARIS;
        }
        if(name.contains("linux"))
        {
            return OperatingSystem.LINUX;
        }
        if(name.contains("unix"))
        {
            return OperatingSystem.LINUX;
        }
        return OperatingSystem.UNKNOWN;
    }
}
