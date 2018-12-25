package com.mrcrayfish.modelcreator;

import java.util.prefs.Preferences;

public class Settings
{
	private static final String IMAGE_IMPORT_DIR = "image_import_dir";
	private static final String SCREENSHOT_DIR = "screenshot_dir";
	private static final String MODEL_DIR = "model_dir";
	private static final String JSON_DIR = "json_dir";
	private static final String EXPORT_JSON_DIR = "export_json_dir";
	private static final String TRANSPARENCY_MODE = "transparency_mode";
	private static final String UNDO_LIMIT = "undo_limit";

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

	public static String getScreenshotDir()
	{
		Preferences prefs = getPreferences();
		return prefs.get(SCREENSHOT_DIR, null);
	}

	public static void setScreenshotDir(String dir)
	{
		Preferences prefs = getPreferences();
		prefs.put(SCREENSHOT_DIR, dir);
	}

	public static String getModelDir()
	{
		Preferences prefs = getPreferences();
		return prefs.get(MODEL_DIR, null);
	}

	public static void setModelDir(String dir)
	{
		Preferences prefs = getPreferences();
		prefs.put(MODEL_DIR, dir);
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

	public static boolean getTransparencyMode()
	{
		Preferences prefs = getPreferences();
		return prefs.get(TRANSPARENCY_MODE, "0").equals("1");
	}

	public static void setTransparencyMode(boolean value)
	{
		Preferences prefs = getPreferences();
		prefs.put(TRANSPARENCY_MODE, value ? "1" : "0");
	}

	public static String getExportJSONDir()
	{
		Preferences prefs = getPreferences();
		return prefs.get(EXPORT_JSON_DIR, null);
	}

	public static void setExportJSONDir(String dir)
	{
		Preferences prefs = getPreferences();
		prefs.put(EXPORT_JSON_DIR, dir);
	}

	public static int getUndoLimit()
	{
		Preferences prefs = getPreferences();
		String s = prefs.get(UNDO_LIMIT, null);
		try
		{
			return Math.max(1, Integer.parseInt(s));
		}
		catch(NumberFormatException e)
		{
			return 50;
		}
	}

	public static void setUndoLimit(int limit)
	{
		Preferences prefs = getPreferences();
		prefs.put(UNDO_LIMIT, Integer.toString(Math.max(1, limit)));
	}

	private static Preferences getPreferences()
	{
		return Preferences.userNodeForPackage(Settings.class);
	}

	public static int parseInt(String number, int def)
	{
		try
		{
			return Integer.parseInt(number);
		}
		catch(NumberFormatException e)
		{
			return def;
		}
	}
}
