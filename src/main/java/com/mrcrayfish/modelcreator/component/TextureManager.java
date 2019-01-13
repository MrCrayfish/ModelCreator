package com.mrcrayfish.modelcreator.component;

import com.mrcrayfish.modelcreator.Icons;
import com.mrcrayfish.modelcreator.Settings;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.texture.TextureEntry;
import com.mrcrayfish.modelcreator.texture.TextureMeta;
import com.mrcrayfish.modelcreator.util.AssetsUtil;
import com.mrcrayfish.modelcreator.util.Util;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class TextureManager extends JDialog
{
    private static List<TextureEntry> loadedTextures = new ArrayList<>();
    private static File lastLocation = null;

    private ElementManager manager;
    private JList<TextureEntry> textureEntries;
    private JButton btnApply;
    private JButton btnCancel;
    private JButton btnNew;
    private JButton btnEdit;
    private JButton btnRemove;

    public TextureManager(Frame owner, ElementManager manager, ModalityType type)
    {
        super(owner, "Texture Manager", type);
        this.manager = manager;
        this.setPreferredSize(new Dimension(500, 400));
        this.initComponents();
    }

    private void initComponents()
    {
        JPanel content = new JPanel();
        content.setLayout(new SpringLayout());
        this.add(content);

        textureEntries = new JList<>();
        textureEntries.setModel(new DefaultListModel<>());
        textureEntries.setCellRenderer(new TextureCellRenderer());

        JScrollPane scrollPane = new JScrollPane(textureEntries);
        content.add(scrollPane);

        if(this.getModalityType() != ModalityType.MODELESS)
        {
            btnApply = new JButton("Apply");
            btnApply.setPreferredSize(new Dimension(110, 26));
            btnApply.setEnabled(false);
            content.add(btnApply);

            btnCancel = new JButton("Cancel");
            btnCancel.setPreferredSize(new Dimension(110, 26));
            btnCancel.addActionListener(e -> this.dispose());
            content.add(btnCancel);
        }

        btnNew = new JButton("New Texture");
        btnNew.addActionListener(e -> showFileChooser());
        btnNew.setPreferredSize(new Dimension(110, 26));
        btnNew.setIcon(Icons.texture);
        content.add(btnNew);

        btnEdit = new JButton("Edit");
        btnEdit.setPreferredSize(new Dimension(110, 26));
        btnEdit.setIcon(Icons.new_);
        btnEdit.setEnabled(false);
        content.add(btnEdit);

        btnRemove = new JButton("Remove");
        btnRemove.setPreferredSize(new Dimension(110, 26));
        btnRemove.setIcon(Icons.bin);
        btnRemove.setEnabled(false);
        content.add(btnRemove);

        SpringLayout layout = (SpringLayout) content.getLayout();
        layout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, content);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, content);
        layout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.WEST, btnNew);
        layout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, content);

        if(this.getModalityType() != ModalityType.MODELESS)
        {
            layout.putConstraint(SpringLayout.SOUTH, btnApply, -10, SpringLayout.NORTH, btnCancel);
            layout.putConstraint(SpringLayout.EAST, btnApply, -10, SpringLayout.EAST, content);
            layout.putConstraint(SpringLayout.SOUTH, btnCancel, -10, SpringLayout.SOUTH, content);
            layout.putConstraint(SpringLayout.EAST, btnCancel, -10, SpringLayout.EAST, content);
        }

        layout.putConstraint(SpringLayout.NORTH, btnNew, 10, SpringLayout.NORTH, content);
        layout.putConstraint(SpringLayout.EAST, btnNew, -10, SpringLayout.EAST, content);
        layout.putConstraint(SpringLayout.NORTH, btnEdit, 10, SpringLayout.SOUTH, btnNew);
        layout.putConstraint(SpringLayout.EAST, btnEdit, -10, SpringLayout.EAST, content);
        layout.putConstraint(SpringLayout.NORTH, btnRemove, 10, SpringLayout.SOUTH, btnEdit);
        layout.putConstraint(SpringLayout.EAST, btnRemove, -10, SpringLayout.EAST, content);
    }

    public TextureEntry getSelectedTexture()
    {
        if(textureEntries != null)
        {
            return textureEntries.getSelectedValue();
        }
        return null;
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

                System.out.println("Mod ID: " + AssetsUtil.getModId(image));
                System.out.println("Path: " + AssetsUtil.getTexturePath(image));

                lastLocation = image.getParentFile();
                Settings.setImageImportDir(lastLocation.toString());

                DefaultListModel<TextureEntry> listModel = (DefaultListModel<TextureEntry>) textureEntries.getModel();
                listModel.addElement(new TextureEntry(image));
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void showEditTextureDialog()
    {

    }

    public static class TextureCellRenderer implements ListCellRenderer<TextureEntry>
    {
        @Override
        public Component getListCellRendererComponent(JList<? extends TextureEntry> list, TextureEntry value, int index, boolean isSelected, boolean cellHasFocus)
        {
            JPanel panel = new JPanel();
            panel.setBackground(isSelected ? new Color(186, 193, 211) : new Color(234, 234, 242));
            panel.setPreferredSize(new Dimension(200, 85));
            
            SpringLayout layout = new SpringLayout();
            panel.setLayout(layout);

            JLabel icon = new JLabel(value.getIcon());
            panel.add(icon);

            JLabel id = new JLabel("<html><b>" + value.getId() + "</b></html>");
            panel.add(id);

            JLabel name = new JLabel("<html><span style=\"color:#555555\">" + value.getModId() + ":" + value.getDirectory() + "/" + value.getName() + "</span></html>");
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
}
