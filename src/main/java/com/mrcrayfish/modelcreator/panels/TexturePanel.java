package com.mrcrayfish.modelcreator.panels;

import com.mrcrayfish.modelcreator.Icons;
import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.StateManager;
import com.mrcrayfish.modelcreator.component.TextureManager;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.texture.Clipboard;
import com.mrcrayfish.modelcreator.texture.TextureEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;

public class TexturePanel extends JPanel
{
    private ElementManager manager;

    private JButton btnSelect;
    private JButton btnClear;
    private JButton btnCopy;
    private JButton btnPaste;

    public TexturePanel(ElementManager manager)
    {
        this.manager = manager;
        this.setBackground(ModelCreator.BACKGROUND);
        this.setLayout(new GridLayout(2, 2, 4, 4));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ModelCreator.BACKGROUND, 5), "<html><b>Texture</b></html>"));
        this.setMaximumSize(new Dimension(186, 80));
        this.initComponents();
        this.addComponents();
    }

    private void initComponents()
    {
        btnSelect = new JButton("Select...");
        btnSelect.setIcon(Icons.texture);
        btnSelect.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                TextureEntry entry = TextureManager.display(((SidebarPanel) manager).getCreator(), manager, Dialog.ModalityType.APPLICATION_MODAL);
                if(entry != null)
                {
                    selectedElement.getSelectedFace().setTexture(entry);
                    StateManager.pushState(manager);
                }
            }
        });
        btnSelect.setToolTipText("Opens the Texture Manager");

        btnClear = new JButton("Clear");
        btnClear.setIcon(Icons.clear_texture);
        btnClear.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & InputEvent.SHIFT_MASK) == 1)
                {
                    selectedElement.setAllTextures(null);
                }
                else
                {
                    selectedElement.getSelectedFace().setTexture(null);
                }
                StateManager.pushState(manager);
            }
        });
        btnClear.setToolTipText("<html>Clears the texture from this face.<br><b>Hold shift to clear all faces</b></html>");

        btnCopy = new JButton("Copy");
        btnCopy.setIcon(Icons.copy_small);
        btnCopy.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                Clipboard.copyTexture(face);
            }
        });
        btnCopy.setToolTipText("Copies the texture on this face to clipboard");

        btnPaste = new JButton("Paste");
        btnPaste.setIcon(Icons.clipboard_texture);
        btnPaste.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                TextureEntry entry = Clipboard.getTexture();
                if(entry != null)
                {
                    if((e.getModifiers() & InputEvent.SHIFT_MASK) == 1)
                    {
                        selectedElement.setAllTextures(entry);
                    }
                    else
                    {
                        Face face = selectedElement.getSelectedFace();
                        face.setTexture(entry);
                    }
                    StateManager.pushState(manager);
                }
            }
        });
        btnPaste.setToolTipText("<html>Pastes the clipboard texture to this face.<br><b>Hold shift to paste to all faces</b></html>");
    }

    private void addComponents()
    {
        this.add(btnSelect);
        this.add(btnClear);
        this.add(btnCopy);
        this.add(btnPaste);
    }
}
