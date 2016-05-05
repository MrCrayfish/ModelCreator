package com.mrcrayfish.modelcreator;

import java.util.prefs.Preferences;

public class Settings
{
	private static final String IMAGE_IMPORT_DIR = "imageimportdir";

	public static String getImageImportDir()
	{
		Preferences prefs = getPreferences();
		return prefs.get(IMAGE_IMPORT_DIR, null);
	}
	
	public static void setImageImportDir(String dir)
	{		
		Preferences prefs = getPreferences();
		prefs.put(IMAGE_IMPORT_DIR, dir);
	}
}
