package com.mrcrayfish.modelcreator.util;

import com.mrcrayfish.modelcreator.Start;

import java.io.InputStream;
import java.net.URL;

public final class ResourceUtil {

    private ResourceUtil() {
    }

    public static URL getResource(String path) {
        return Start.class.getClassLoader().getResource(path);
    }

    public static InputStream getResourceAsStream(String path) {
        return Start.class.getClassLoader().getResourceAsStream(path);
    }
}
