package com.mrcrayfish.modelcreator.util;

import com.mrcrayfish.modelcreator.Icons;
import com.mrcrayfish.modelcreator.Processor;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class ComponentUtil
{
    public static JRadioButton createRadioButton(String label, String toolTip)
    {
        JRadioButton radioButton = new JRadioButton(label);
        radioButton.setToolTipText(toolTip);
        radioButton.setIcon(Icons.light_off);
        radioButton.setRolloverIcon(Icons.light_off);
        radioButton.setSelectedIcon(Icons.light_on);
        radioButton.setRolloverSelectedIcon(Icons.light_on);
        return radioButton;
    }

    public static JCheckBox createCheckBox(String text, String tooltip, boolean selected)
    {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setToolTipText(tooltip);
        checkBox.setSelected(selected);
        checkBox.setIcon(Icons.light_off);
        checkBox.setRolloverIcon(Icons.light_off);
        checkBox.setSelectedIcon(Icons.light_on);
        checkBox.setRolloverSelectedIcon(Icons.light_on);
        return checkBox;
    }

    public static JPanel createDirectorySelector(String label, Component parent, String defaultDir, Processor<File> processor)
    {
        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setPreferredSize(new Dimension(100, 24));

        JTextField textFieldDestination = new JTextField();
        textFieldDestination.setPreferredSize(new Dimension(100, 24));
        textFieldDestination.setText(defaultDir);
        textFieldDestination.setEditable(false);
        textFieldDestination.setFocusable(false);
        textFieldDestination.setCaretPosition(0);
        panel.add(textFieldDestination);

        JButton btnBrowserDir = new JButton("Browse");
        btnBrowserDir.setPreferredSize(new Dimension(80, 24));
        btnBrowserDir.setIcon(Icons.load);
        btnBrowserDir.addActionListener(e ->
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select a Folder");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setApproveButtonText("Select");
            chooser.setCurrentDirectory(new File(defaultDir));
            int returnVal = chooser.showOpenDialog(parent);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = chooser.getSelectedFile();
                if(file != null)
                {
                    if(processor != null)
                    {
                        if(processor.run(file))
                        {
                            return;
                        }
                    }
                    textFieldDestination.setText(file.getAbsolutePath());
                }
            }
        });
        panel.add(btnBrowserDir);

        JLabel labelExportDir = new JLabel(label);
        panel.add(labelExportDir);

        layout.putConstraint(SpringLayout.NORTH, textFieldDestination, 0, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, textFieldDestination, 10, SpringLayout.EAST, labelExportDir);
        layout.putConstraint(SpringLayout.EAST, textFieldDestination, -10, SpringLayout.WEST, btnBrowserDir);
        layout.putConstraint(SpringLayout.NORTH, labelExportDir, 3, SpringLayout.NORTH, textFieldDestination);
        layout.putConstraint(SpringLayout.WEST, labelExportDir, 0, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, btnBrowserDir, 0, SpringLayout.NORTH, textFieldDestination);
        layout.putConstraint(SpringLayout.EAST, btnBrowserDir, 0, SpringLayout.EAST, panel);

        return panel;
    }

    public static JPanel createFileSelector(String label, Component parent, String defaultDir, FileFilter filter, Processor<File> processor)
    {
        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setPreferredSize(new Dimension(100, 24));

        JTextField textFieldDestination = new JTextField();
        textFieldDestination.setPreferredSize(new Dimension(100, 24));
        textFieldDestination.setText(defaultDir);
        textFieldDestination.setEditable(false);
        textFieldDestination.setFocusable(false);
        textFieldDestination.setCaretPosition(0);
        panel.add(textFieldDestination);

        JButton btnBrowserDir = new JButton("Browse");
        btnBrowserDir.setPreferredSize(new Dimension(80, 24));
        btnBrowserDir.setIcon(Icons.load);
        btnBrowserDir.addActionListener(e ->
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select a Folder");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setApproveButtonText("Select");
            chooser.setCurrentDirectory(new File(defaultDir));
            if(filter != null)
            {
                chooser.setFileFilter(filter);
            }
            int returnVal = chooser.showOpenDialog(parent);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = chooser.getSelectedFile();
                if(file != null)
                {
                    if(processor != null)
                    {
                        if(processor.run(file))
                        {
                            return;
                        }
                    }
                    textFieldDestination.setText(file.getAbsolutePath());
                }
            }
        });
        panel.add(btnBrowserDir);

        JLabel labelExportDir = new JLabel(label);
        panel.add(labelExportDir);

        layout.putConstraint(SpringLayout.NORTH, textFieldDestination, 0, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, textFieldDestination, 10, SpringLayout.EAST, labelExportDir);
        layout.putConstraint(SpringLayout.EAST, textFieldDestination, -10, SpringLayout.WEST, btnBrowserDir);
        layout.putConstraint(SpringLayout.NORTH, labelExportDir, 3, SpringLayout.NORTH, textFieldDestination);
        layout.putConstraint(SpringLayout.WEST, labelExportDir, 0, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, btnBrowserDir, 0, SpringLayout.NORTH, textFieldDestination);
        layout.putConstraint(SpringLayout.EAST, btnBrowserDir, 0, SpringLayout.EAST, panel);

        return panel;
    }

    public static Rectangle expandRectangle(Rectangle r, int amount)
    {
        return new Rectangle(r.x - amount, r.y - amount, r.width + amount * 2, r.height + amount * 2);
    }
}
