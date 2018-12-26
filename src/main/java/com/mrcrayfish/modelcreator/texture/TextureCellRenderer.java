package com.mrcrayfish.modelcreator.texture;

import javax.swing.*;
import java.awt.*;

public class TextureCellRenderer extends DefaultListCellRenderer
{

    private JLabel lbl = new JLabel();

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        lbl.setIcon(TextureManager.getIcon((String) list.getModel().getElementAt(index)));
        lbl.revalidate();
        if(isSelected)
        {
            lbl.setOpaque(true);
            lbl.setEnabled(false);
        }
        else
        {
            lbl.setOpaque(false);
            lbl.setEnabled(true);
        }
        return lbl;
    }
}
