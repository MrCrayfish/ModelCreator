package com.mrcrayfish.modelcreator.util;

import org.lwjgl.input.Keyboard;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Author: MrCrayfish
 */
public class KeyboardUtil
{
    public static String convertKeyStokeToString(KeyStroke keyStroke)
    {
        String shortcutText = "";
        int modifiers = keyStroke.getModifiers();
        if(modifiers > 0)
        {
            shortcutText = KeyEvent.getKeyModifiersText(modifiers);
            shortcutText += "+";
        }
        shortcutText += KeyEvent.getKeyText(keyStroke.getKeyCode());
        return shortcutText;
    }

    public static boolean isCtrlKeyDown()
    {
        if(OperatingSystem.get() == OperatingSystem.MAC)
        {
            return Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA);
        }
        else
        {
            return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        }
    }

    public static boolean isShiftKeyDown()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean isAltKeyDown()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }
}
