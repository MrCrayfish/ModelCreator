package com.mrcrayfish.modelcreator.util;

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
}
