package com.mrcrayfish.modelcreator;

import java.util.prefs.Preferences;

public class Settings
{
    private static final String IMAGE_IMPORT_DIR = "image_import_dir";
    private static final String SCREENSHOT_DIR = "screenshot_dir";
    private static final String MODEL_DIR = "model_dir";
    private static final String JSON_DIR = "json_dir";
    private static final String EXPORT_JSON_DIR = "export_json_dir";
    private static final String UNDO_LIMIT = "undo_limit";
    private static final String ASSESTS_DIR = "assets_dir";
    private static final String FACE_COLORS = "face_colors";
    private static final String IMAGE_EDITOR = "image_editor";
    private static final String IMAGE_EDITOR_ARGS = "image_editor_args";

    public static final int[] DEFAULT_FACE_COLORS = {16711680, 65280, 255, 16776960, 16711935, 65535};

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

    public static String getAssetsDir()
    {
        Preferences prefs = getPreferences();
        return prefs.get(ASSESTS_DIR, null);
    }

    public static void setAssetsDir(String dir)
    {
        Preferences prefs = getPreferences();
        prefs.put(ASSESTS_DIR, dir);
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

    public static int[] getFaceColors()
    {
        Preferences prefs = getPreferences();
        String s = prefs.get(FACE_COLORS, "");
        String[] values = s.split(",");
        if(values.length == 6)
        {
            int[] colors = new int[6];
            for(int i = 0; i < values.length; i++)
            {
                int color = Integer.parseInt(values[i]);
                colors[i] = color;
            }
            return colors;
        }
        return DEFAULT_FACE_COLORS;
    }

    public static void setFaceColors(int[] colors)
    {
        StringBuilder builder = new StringBuilder();
        for(int value : colors)
        {
            builder.append(value);
            builder.append(",");
        }
        builder.setLength(builder.length() - 1);
        Preferences prefs = getPreferences();
        prefs.put(FACE_COLORS, builder.toString());
    }

    public static String getImageEditor()
    {
        Preferences prefs = getPreferences();
        return prefs.get(IMAGE_EDITOR, null);
    }

    public static void setImageEditor(String file)
    {
        Preferences prefs = getPreferences();
        prefs.put(IMAGE_EDITOR, file);
    }

    public static String getImageEditorArgs()
    {
        Preferences prefs = getPreferences();
        return prefs.get(IMAGE_EDITOR_ARGS, "\"%s\"");
    }

    public static void setImageEditorArgs(String args)
    {
        Preferences prefs = getPreferences();
        prefs.put(IMAGE_EDITOR_ARGS, args);
    }

    private static Preferences getPreferences()
    {
        return Preferences.userNodeForPackage(Settings.class);
    }
}
