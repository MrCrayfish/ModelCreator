package com.mrcrayfish.modelcreator;

import java.util.prefs.Preferences;

public class Settings
{
	private static final String IMAGE_IMPORT_DIR = "imageimportdir";
	private static final String JSON_DIR = "jsondir";

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

	public static String getJSONDir()
	{
		Preferences prefs = getPreferences();
		return prefs.get(JSON_DIR, null);
	}
	
	public static void setJSONDir(String dir)
	{		
		Preferences prefs = getPreferences();
		prefs.put(JSON_DIR, dir);
	}		
	
	private static Preferences getPreferences()
	{
		return Preferences.userNodeForPackage(Settings.class);
	}
}
