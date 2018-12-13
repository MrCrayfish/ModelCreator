package com.mrcrayfish.modelcreator.util;

import java.net.URL;
import java.io.InputStream;

public final class ResourceUtil {

  private ResourceUtil() { }
    
  public static URL getResource(String path) {
    return getClass().getClassLoader().getResource(path);
  }
  
  public static InputStream getResource(String path) {
    return getClass().getClassLoader().getResourceAsStream(path);
  }
}
