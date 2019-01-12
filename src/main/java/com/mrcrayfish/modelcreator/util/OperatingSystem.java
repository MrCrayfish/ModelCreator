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

    private static final OperatingSystem OS;

    static
    {
        String name = System.getProperty("os.name").toLowerCase();
        if(name.contains("win"))
        {
            OS = OperatingSystem.WINDOWS;
        }
        else if(name.contains("mac"))
        {
            OS = OperatingSystem.MAC;
        }
        else if(name.contains("solaris"))
        {
            OS = OperatingSystem.SOLARIS;
        }
        else if(name.contains("sunos"))
        {
            OS = OperatingSystem.SOLARIS;
        }
        else if(name.contains("linux"))
        {
            OS = OperatingSystem.LINUX;
        }
        else if(name.contains("unix"))
        {
            OS = OperatingSystem.LINUX;
        }
        else
        {
            OS = OperatingSystem.UNKNOWN;
        }
    }

    public static OperatingSystem get()
    {
        return OS;
    }
}
