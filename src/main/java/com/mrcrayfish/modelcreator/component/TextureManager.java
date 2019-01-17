package com.mrcrayfish.modelcreator.component;

import com.mrcrayfish.modelcreator.Icons;
import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.Settings;
import com.mrcrayfish.modelcreator.TexturePath;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.texture.TextureEntry;
import com.mrcrayfish.modelcreator.util.Util;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class TextureManager extends JDialog
{
    public static final Object LOCK = new Object();

    public static final int CANCELLED = 1;
    public static final int APPLIED = 1;

    private static final List<TextureEntry> pendingLoad = new ArrayList<>();
    private static final List<TextureEntry> pendingRemove = new ArrayList<>();
    private static final List<TextureEntry> textureEntries = new ArrayList<>();
    private static File lastLocation = null;

    private ElementManager manager;
    private JList<TextureEntry> textureEntryList;
    private JButton btnApply;
    private JButton btnCancel;
    private JButton btnNew;
    private JButton btnEdit;
    private JButton btnRemove;
    private int result;
    private boolean canApply = true;

    public TextureManager(Frame owner, ElementManager manager, ModalityType type, boolean canApply)
    {
        super(owner, "Texture Manager", type);
        this.canApply = canApply;
        this.manager = manager;
        this.setPreferredSize(new Dimension(500, 392));
        this.setResizable(false);
        this.initComponents();
        this.pack();
    }

    private void initComponents()
    {
        JPanel content = new JPanel();
        content.setLayout(new SpringLayout());
        this.add(content);

        textureEntryList = new JList<>();
        textureEntryList.setModel(new DefaultListModel<>());
        textureEntryList.setCellRenderer(new TextureCellRenderer());
        textureEntryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        textureEntryList.addListSelectionListener(e ->
        {
            if(btnApply != null)
            {
                btnApply.setEnabled(true);
            }
            btnEdit.setEnabled(true);
            btnRemove.setEnabled(true);
        });

        JScrollPane scrollPane = new JScrollPane(textureEntryList);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        content.add(scrollPane);

        if(canApply)
        {
            btnApply = new JButton("Apply");
            btnApply.setPreferredSize(new Dimension(110, 26));
            btnApply.setEnabled(false);
            btnApply.addActionListener(e ->
            {
                result = APPLIED;
                this.dispose();
            });
            content.add(btnApply);
        }

        btnCancel = new JButton(canApply ? "Cancel" : "Close");
        btnCancel.setPreferredSize(new Dimension(110, 26));
        btnCancel.addActionListener(e -> this.dispose());
        content.add(btnCancel);

        btnNew = new JButton("New Texture");
        btnNew.addActionListener(e -> showFileChooser());
        btnNew.setPreferredSize(new Dimension(110, 26));
        btnNew.setIcon(Icons.texture);
        content.add(btnNew);

        btnEdit = new JButton("Edit");
        btnEdit.setPreferredSize(new Dimension(110, 26));
        btnEdit.setIcon(Icons.edit);
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(e ->
        {
            TextureEntry entry = textureEntryList.getSelectedValue();
            if(entry != null)
            {
                TextureEntryEditor editor = new TextureEntryEditor(this.getOwner(), entry, ModalityType.APPLICATION_MODAL);
                editor.setLocationRelativeTo(null);
                editor.setVisible(true);
            }
        });
        content.add(btnEdit);

        btnRemove = new JButton("Remove");
        btnRemove.setPreferredSize(new Dimension(110, 26));
        btnRemove.setIcon(Icons.bin2);
        btnRemove.setEnabled(false);
        btnRemove.addActionListener(e ->
        {
            if(textureEntryList.getSelectedIndex() != -1)
            {
                TextureEntry entry = textureEntryList.getSelectedValue();
                manager.getAllElements().forEach(element ->
                {
                    for(Face face : element.getAllFaces())
                    {
                        if(face.getTexture() == entry)
                        {
                            face.setTexture(null);
                        }
                    }
                });
                if(manager.getParticle() == entry)
                {
                    manager.setParticle(null);
                }
                textureEntries.remove(entry);
                DefaultListModel<TextureEntry> listModel = (DefaultListModel<TextureEntry>) textureEntryList.getModel();
                listModel.removeElement(entry);
                TextureManager.removeTexture(entry);
                manager.updateValues();
            }
            if(btnApply != null)
            {
                btnApply.setEnabled(false);
            }
            btnEdit.setEnabled(false);
            btnRemove.setEnabled(false);
        });
        content.add(btnRemove);

        SpringLayout layout = (SpringLayout) content.getLayout();
        layout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, content);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, content);
        layout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.WEST, btnNew);
        layout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, content);

        if(canApply)
        {
            layout.putConstraint(SpringLayout.SOUTH, btnApply, -10, SpringLayout.NORTH, btnCancel);
            layout.putConstraint(SpringLayout.EAST, btnApply, -10, SpringLayout.EAST, content);
        }

        layout.putConstraint(SpringLayout.SOUTH, btnCancel, -10, SpringLayout.SOUTH, content);
        layout.putConstraint(SpringLayout.EAST, btnCancel, -10, SpringLayout.EAST, content);

        layout.putConstraint(SpringLayout.NORTH, btnNew, 10, SpringLayout.NORTH, content);
        layout.putConstraint(SpringLayout.EAST, btnNew, -10, SpringLayout.EAST, content);
        layout.putConstraint(SpringLayout.NORTH, btnEdit, 10, SpringLayout.SOUTH, btnNew);
        layout.putConstraint(SpringLayout.EAST, btnEdit, -10, SpringLayout.EAST, content);
        layout.putConstraint(SpringLayout.NORTH, btnRemove, 10, SpringLayout.SOUTH, btnEdit);
        layout.putConstraint(SpringLayout.EAST, btnRemove, -10, SpringLayout.EAST, content);

        DefaultListModel<TextureEntry> listModel = (DefaultListModel<TextureEntry>) textureEntryList.getModel();
        textureEntries.forEach(listModel::addElement);
    }

    public TextureEntry getSelectedTexture()
    {
        if(textureEntryList != null)
        {
            return textureEntryList.getSelectedValue();
        }
        return null;
    }

    public int getResult()
    {
        return result;
    }

    public void showFileChooser()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a Texture");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setApproveButtonText("Select");

        if(lastLocation == null)
        {
            String dir = Settings.getImageImportDir();
            if(dir != null)
            {
                lastLocation = new File(dir);
            }
        }

        if(lastLocation != null)
        {
            chooser.setCurrentDirectory(lastLocation);
        }
        else
        {
            try
            {
                if(Settings.getAssetsDir() != null)
                {
                    chooser.setCurrentDirectory(new File(Settings.getAssetsDir()));
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            this.addImage(chooser.getSelectedFile());
        }
    }

    private void addImage(File image)
    {
        try
        {
            String type = Files.probeContentType(image.toPath());
            if(type.equals("image/png"))
            {
                Dimension dimension = Util.getImageDimension(image);
                if(dimension.getWidth() % 16 != 0 || dimension.getHeight() % 16 != 0)
                {
                    JOptionPane.showMessageDialog(this, "Image size must be multiple of 16", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                lastLocation = image.getParentFile();
                Settings.setImageImportDir(lastLocation.toString());

                DefaultListModel<TextureEntry> listModel = (DefaultListModel<TextureEntry>) textureEntryList.getModel();
                TextureEntry entry = new TextureEntry(image);
                listModel.addElement(entry);
                textureEntries.add(entry);
                TextureManager.loadTexture(entry);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static TextureEntry addImage(String id, TexturePath path, File image)
    {
        for(TextureEntry entry : textureEntries)
        {
            if(entry.getKey().equals(id))
            {
                return entry;
            }
        }
        try
        {
            if(image.exists())
            {
                String type = Files.probeContentType(image.toPath());
                if(type.equals("image/png"))
                {
                    Dimension dimension = Util.getImageDimension(image);
                    if(dimension.getWidth() % 16 != 0 || dimension.getHeight() % 16 != 0)
                    {
                        JOptionPane.showMessageDialog(null, "Image size must be multiple of 16", "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                    TextureEntry entry = new TextureEntry(id, path, image);
                    textureEntries.add(entry);
                    TextureManager.loadTexture(entry);
                    return entry;
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static TextureEntry getTexture(String id)
    {
        for(TextureEntry entry : textureEntries)
        {
            if(entry.getKey().equals(id))
            {
                return entry;
            }
        }
        return null;
    }

    public static class TextureCellRenderer implements ListCellRenderer<TextureEntry>
    {
        @Override
        public Component getListCellRendererComponent(JList<? extends TextureEntry> list, TextureEntry entry, int index, boolean isSelected, boolean cellHasFocus)
        {
            JPanel panel = new JPanel();
            panel.setBackground(isSelected ? new Color(186, 193, 211) : ModelCreator.BACKGROUND);
            panel.setPreferredSize(new Dimension(200, 85));
            if(isSelected)
            {
                panel.setBorder(BorderFactory.createLineBorder(new Color(131, 138, 156), 1));
            }

            SpringLayout layout = new SpringLayout();
            panel.setLayout(layout);

            JLabel icon = new JLabel(entry.getIcon());
            panel.add(icon);

            JLabel id = new JLabel("<html><b>" + entry.getKey() + "</b></html>");
            panel.add(id);

            JLabel name = new JLabel("<html><span style=\"color:#555555\">" + entry.getTexturePath().toString() + "</span></html>");
            panel.add(name);

            layout.putConstraint(SpringLayout.WEST, icon, 10, SpringLayout.WEST, panel);
            layout.putConstraint(SpringLayout.NORTH, icon, 10, SpringLayout.NORTH, panel);

            layout.putConstraint(SpringLayout.WEST, id, 10, SpringLayout.EAST, icon);
            layout.putConstraint(SpringLayout.NORTH, id, 10, SpringLayout.NORTH, panel);
            layout.putConstraint(SpringLayout.EAST, id, -10, SpringLayout.EAST, panel);

            layout.putConstraint(SpringLayout.WEST, name, 10, SpringLayout.EAST, icon);
            layout.putConstraint(SpringLayout.NORTH, name, 5, SpringLayout.SOUTH, id);
            layout.putConstraint(SpringLayout.EAST, name, -10, SpringLayout.EAST, panel);

            return panel;
        }
    }

    public static TextureEntry display(Frame owner, ElementManager manager, ModalityType modalityType)
    {
        TextureManager textureManager = new TextureManager(owner, manager, modalityType, true);
        textureManager.setLocationRelativeTo(null);
        textureManager.setVisible(true);
        if(textureManager.getResult() == APPLIED)
        {
            return textureManager.getSelectedTexture();
        }
        return null;
    }

    public static void loadTexture(TextureEntry entry)
    {
        synchronized(LOCK)
        {
            pendingLoad.add(entry);
        }
    }

    public static void removeTexture(TextureEntry entry)
    {
        synchronized(LOCK)
        {
            pendingRemove.add(entry);
        }
    }

    public static void processPendingTextures()
    {
        if(pendingRemove.size() > 0)
        {
            synchronized(LOCK)
            {
                for(TextureEntry entry : pendingRemove)
                {
                    entry.deleteTexture();
                }
                pendingRemove.clear();
            }
        }

        if(pendingLoad.size() > 0)
        {
            synchronized(LOCK)
            {
                for(TextureEntry entry : pendingLoad)
                {
                    entry.deleteTexture();
                    entry.loadTexture();
                }
                pendingLoad.clear();
            }
        }
    }

    public static void clear()
    {
        synchronized(LOCK)
        {
            pendingRemove.addAll(textureEntries);
            textureEntries.clear();
        }
    }
}
