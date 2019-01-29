package com.mrcrayfish.modelcreator.component;

import com.mrcrayfish.modelcreator.Icons;
import com.mrcrayfish.modelcreator.Settings;
import com.mrcrayfish.modelcreator.TexturePath;
import com.mrcrayfish.modelcreator.texture.TextureEntry;
import com.mrcrayfish.modelcreator.util.ComponentUtil;
import com.mrcrayfish.modelcreator.util.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Author: MrCrayfish
 */
public class TextureEntryEditor extends JDialog
{
    private TextureEntry entry;

    private JLabel icon;
    private JTextField textFieldKey;
    private JTextField textFieldValue;
    private boolean triedEditingValue = false;

    private File texture = null;

    public TextureEntryEditor(Window owner, TextureEntry entry, ModalityType type)
    {
        super(owner, "Edit Texture Entry", type);
        this.entry = entry;
        this.setPreferredSize(new Dimension(300, 370));
        this.setResizable(false);
        this.initComponents();
        this.pack();
    }

    private void initComponents()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new SpringLayout());
        this.add(panel);

        JPanel image = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        image.setPreferredSize(new Dimension(150, 150));
        image.setBackground(Color.WHITE);
        icon = new JLabel(resize(entry.getSource(), 150));
        image.add(icon);
        panel.add(image);

        JPanel fileSelector = ComponentUtil.createFileSelector("File", this, entry.getTextureFile().getAbsolutePath(), new FileNameExtensionFilter("PNG Image", "png"), file ->
        {
            if(this.setTexture(file))
            {
                TexturePath texturePath = new TexturePath(file);
                textFieldValue.setText(texturePath.toString());
                return true;
            }
            return false;
        });
        panel.add(fileSelector);

        JSeparator separator1 = new JSeparator();
        panel.add(separator1);

        JLabel labelKey = new JLabel("Key");
        panel.add(labelKey);

        textFieldKey = new JTextField();
        textFieldKey.setPreferredSize(new Dimension(0, 24));
        textFieldKey.setText(entry.getKey());
        panel.add(textFieldKey);

        JLabel labelValue = new JLabel("Value");
        panel.add(labelValue);

        textFieldValue = new JTextField();
        textFieldValue.setPreferredSize(new Dimension(0, 24));
        textFieldValue.setText(entry.getTexturePath().toString());
        textFieldValue.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                if(!triedEditingValue)
                {
                    JOptionPane.showMessageDialog(TextureEntryEditor.this, "Only edit this value if you are an advanced user. Changing this may cause the texture to not load in Minecraft", "Important", JOptionPane.INFORMATION_MESSAGE);
                    triedEditingValue = true;
                }
            }
        });
        panel.add(textFieldValue);

        JSeparator separator2 = new JSeparator();
        panel.add(separator2);

        JButton btnRefresh = new JButton();
        btnRefresh.setIcon(Icons.refresh);
        btnRefresh.addActionListener(e -> this.setTexture(entry.getTextureFile()));
        panel.add(btnRefresh);

        JButton btnEdit = new JButton("Edit");
        btnEdit.setIcon(Icons.edit_image);
        btnEdit.addActionListener(a ->
        {
            String program = Settings.getImageEditor();
            if(!program.isEmpty())
            {
                try
                {
                    String command = program + " " + String.format(Settings.getImageEditorArgs(), entry.getTextureFile().getAbsolutePath());
                    Runtime.getRuntime().exec(command);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                int returnVal = JOptionPane.showConfirmDialog(this, "Image Editor has not been configured. Do you want to open with default editor?", "Message", JOptionPane.YES_NO_OPTION);
                if(returnVal == JOptionPane.YES_OPTION)
                {
                    try
                    {
                        Desktop.getDesktop().edit(entry.getTextureFile());
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        panel.add(btnEdit);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> this.dispose());
        panel.add(btnCancel);

        JButton btnSave = new JButton("Save");
        btnSave.setIcon(Icons.disk);
        btnSave.addActionListener(e ->
        {
            String key = textFieldKey.getText().trim().toLowerCase(Locale.ENGLISH);
            String value = textFieldValue.getText().trim().toLowerCase(Locale.ENGLISH);

            if(!TextureEntry.KEY_PATTERN.matcher(key).matches())
            {
                JOptionPane.showMessageDialog(this, "Invalid key format. It may only contain lowercase letters, numbers, and underscore.", "Key Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(TextureManager.getTexture(key) != null)
            {
                JOptionPane.showMessageDialog(this, "The key entered is already in use by another texture. Please choose a different key", "Key Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if("particle".equals(key))
            {
                JOptionPane.showMessageDialog(this, "The key 'particle' is reserved. Please choose a different key", "Key Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(!TexturePath.PATTERN.matcher(value).matches())
            {
                JOptionPane.showMessageDialog(this, "Invalid value format", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(texture != null)
            {
                try
                {
                    Dimension dimension = Util.getImageDimension(texture);
                    if(dimension.getWidth() % 16 != 0 || dimension.getHeight() % 16 != 0)
                    {
                        JOptionPane.showMessageDialog(this, "Image size must be multiple of 16", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                catch(IOException e1)
                {
                    JOptionPane.showMessageDialog(this, "Unable to determine image dimensions", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                entry.setTextureFile(texture);
            }
            entry.setTexturePath(new TexturePath(value));
            entry.setKey(key);
            this.dispose();
        });
        panel.add(btnSave);

        SpringLayout layout = (SpringLayout) panel.getLayout();
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, image, 0, SpringLayout.HORIZONTAL_CENTER, panel);
        layout.putConstraint(SpringLayout.NORTH, image, 10, SpringLayout.NORTH, panel);

        layout.putConstraint(SpringLayout.WEST, fileSelector, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, fileSelector, 20, SpringLayout.SOUTH, image);
        layout.putConstraint(SpringLayout.EAST, fileSelector, -10, SpringLayout.EAST, panel);

        layout.putConstraint(SpringLayout.WEST, separator1, 0, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, separator1, 10, SpringLayout.SOUTH, fileSelector);
        layout.putConstraint(SpringLayout.EAST, separator1, 0, SpringLayout.EAST, panel);

        layout.putConstraint(SpringLayout.WEST, labelKey, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, labelKey, 2, SpringLayout.NORTH, textFieldKey);

        layout.putConstraint(SpringLayout.WEST, textFieldKey, 40, SpringLayout.WEST, labelKey);
        layout.putConstraint(SpringLayout.NORTH, textFieldKey, 10, SpringLayout.SOUTH, separator1);
        layout.putConstraint(SpringLayout.EAST, textFieldKey, -10, SpringLayout.EAST, panel);

        layout.putConstraint(SpringLayout.WEST, labelValue, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, labelValue, 2, SpringLayout.NORTH, textFieldValue);

        layout.putConstraint(SpringLayout.WEST, textFieldValue, 40, SpringLayout.WEST, labelValue);
        layout.putConstraint(SpringLayout.NORTH, textFieldValue, 10, SpringLayout.SOUTH, textFieldKey);
        layout.putConstraint(SpringLayout.EAST, textFieldValue, -10, SpringLayout.EAST, panel);

        layout.putConstraint(SpringLayout.WEST, separator2, 0, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, separator2, 10, SpringLayout.SOUTH, textFieldValue);
        layout.putConstraint(SpringLayout.EAST, separator2, 0, SpringLayout.EAST, panel);

        layout.putConstraint(SpringLayout.WEST, btnRefresh, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.SOUTH, btnRefresh, -10, SpringLayout.SOUTH, panel);

        layout.putConstraint(SpringLayout.WEST, btnEdit, 10, SpringLayout.EAST, btnRefresh);
        layout.putConstraint(SpringLayout.SOUTH, btnEdit, -10, SpringLayout.SOUTH, panel);

        layout.putConstraint(SpringLayout.EAST, btnCancel, -10, SpringLayout.WEST, btnSave);
        layout.putConstraint(SpringLayout.SOUTH, btnCancel, -10, SpringLayout.SOUTH, panel);

        layout.putConstraint(SpringLayout.EAST, btnSave, -10, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.SOUTH, btnSave, -10, SpringLayout.SOUTH, panel);
    }

    private static ImageIcon resize(BufferedImage source, int size)
    {
        Image scaledImage = source.getScaledInstance(size, size, java.awt.Image.SCALE_FAST);
        return new ImageIcon(scaledImage);
    }

    private boolean setTexture(File file)
    {
        try
        {
            Dimension dimension = Util.getImageDimension(file);
            if(dimension.getWidth() % 16 != 0 || dimension.getHeight() % 16 != 0)
            {
                JOptionPane.showMessageDialog(this, "Image size must be multiple of 16", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        catch(IOException e1)
        {
            JOptionPane.showMessageDialog(this, "Unable to determine image dimensions", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try
        {
            icon.setIcon(resize(ImageIO.read(file), 150));
            texture = file;
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(this, "Unable to load image", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
