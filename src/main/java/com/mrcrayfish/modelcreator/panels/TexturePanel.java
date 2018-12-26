package com.mrcrayfish.modelcreator.panels;

import com.mrcrayfish.modelcreator.Icons;
import com.mrcrayfish.modelcreator.StateManager;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.texture.ClipboardTexture;
import com.mrcrayfish.modelcreator.texture.TextureCallback;
import com.mrcrayfish.modelcreator.texture.TextureManager;
import com.mrcrayfish.modelcreator.util.Clipboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;

public class TexturePanel extends JPanel implements TextureCallback
{
    private ElementManager manager;

    private JButton btnSelect;
    private JButton btnClear;
    private JButton btnCopy;
    private JButton btnPaste;

    public TexturePanel(ElementManager manager)
    {
        this.manager = manager;
        this.setLayout(new GridLayout(2, 2, 4, 4));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Texture</b></html>"));
        this.setMaximumSize(new Dimension(186, 90));
        this.initComponents();
        this.addComponents();
    }

    private void initComponents()
    {
        Font defaultFont = new Font("SansSerif", Font.BOLD, 14);

        btnSelect = new JButton("Image");
        btnSelect.setIcon(Icons.texture);
        btnSelect.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                String texture = TextureManager.display(manager);
                if(texture != null)
                {
                    selectedElement.getSelectedFace().setTexture(texture);
                    StateManager.pushState(manager);
                }
            }
        });
        btnSelect.setFont(defaultFont);
        btnSelect.setToolTipText("Opens the Texture Manager");

        btnClear = new JButton("Clear");
        btnClear.setIcon(Icons.clear);
        btnClear.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    selectedElement.setAllTextures(null, null);
                }
                else
                {
                    selectedElement.getSelectedFace().setTexture(null);
                }
                StateManager.pushState(manager);
            }
        });
        btnClear.setFont(defaultFont);
        btnClear.setToolTipText("<html>Clears the texture from this face.<br><b>Hold shift to clear all faces</b></html>");

        btnCopy = new JButton("Copy");
        btnCopy.setIcon(Icons.copy);
        btnCopy.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                Clipboard.copyTexture(face.getTextureLocation(), face.getTextureName());
            }
        });
        btnCopy.setFont(defaultFont);
        btnCopy.setToolTipText("Copies the texture on this face to clipboard");

        btnPaste = new JButton("Paste");
        btnPaste.setIcon(Icons.clipboard);
        btnPaste.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                ClipboardTexture texture = Clipboard.getTexture();
                if(texture != null)
                {
                    if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                    {
                        selectedElement.setAllTextures(texture);
                    }
                    else
                    {
                        Face face = selectedElement.getSelectedFace();
                        face.setTexture(texture.getTexture());
                        face.setTextureLocation(texture.getLocation());
                    }
                    StateManager.pushState(manager);
                }
            }
        });
        btnPaste.setFont(defaultFont);
        btnPaste.setToolTipText("<html>Pastes the clipboard texture to this face.<br><b>Hold shift to paste to all faces</b></html>");
    }

    private void addComponents()
    {
        this.add(btnSelect);
        this.add(btnClear);
        this.add(btnCopy);
        this.add(btnPaste);
    }

    @Override
    public void callback(boolean success, String texture)
    {
        if(success)
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                selectedElement.getSelectedFace().setTexture(texture);
                StateManager.pushState(manager);
            }
        }
    }
}
