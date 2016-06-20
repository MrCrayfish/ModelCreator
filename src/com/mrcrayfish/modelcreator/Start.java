package com.mrcrayfish.modelcreator;

import com.jtattoo.plaf.fast.FastLookAndFeel;

import javax.swing.*;
import java.io.File;
import java.util.Properties;

public class Start {

    public static void main(String[] args) {
        try {
            if (Double.parseDouble(System.getProperty("java.specification.version")) < 1.8) {
                JOptionPane.showMessageDialog(null, "You need Java 1.8 or higher to run this program.");
                return;
            }

            String os = System.getProperty("os.name").toLowerCase();

            if (os.indexOf("win") > 0) {
                System.setProperty("org.lwjgl.librarypath", new File("natives/windows").getAbsolutePath());
                System.setProperty("java.library.path", new File("natives/windows").getAbsolutePath());
            } else if (os.indexOf("mac") > 0) {
                System.setProperty("org.lwjgl.librarypath", new File("natives/macosx").getAbsolutePath());
            } else if (os.indexOf("sunos") > 0 || os.indexOf("solaris") > 0) {
                System.setProperty("org.lwjgl.librarypath", new File("natives/solaris").getAbsolutePath());
            } else if (os.indexOf("aix") > 0 || os.indexOf("nix") > 0 || os.indexOf("nux") > 0) {
                System.setProperty("org.lwjgl.librarypath", new File("natives/linux").getAbsolutePath());
            }

            System.setProperty("org.lwjgl.util.Debug", "true");


            Properties props = new Properties();
            props.put("logoString", "");
            props.put("centerWindowTitle", "on");
            props.put("buttonBackgroundColor", "127 132 145");
            props.put("buttonForegroundColor", "255 255 255");
            props.put("windowTitleBackgroundColor", "97 102 115");
            props.put("windowTitleForegroundColor", "255 255 255");
            props.put("backgroundColor", "221 221 228");
            props.put("menuBackgroundColor", "221 221 228");
            props.put("controlForegroundColor", "120 120 120");
            props.put("windowBorderColor", "97 102 110");

            FastLookAndFeel.setTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");

            new ModelCreator("Model Creator - pre4");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
