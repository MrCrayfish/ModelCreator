package com.mrcrayfish.modelcreator.component;

import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.display.CanvasRenderer;
import com.mrcrayfish.modelcreator.display.DisplayProperties;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.panels.DisplayEntryPanel;
import com.mrcrayfish.modelcreator.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Author: MrCrayfish
 */
public class DisplayPropertiesDialog extends JDialog
{
    private ModelCreator creator;

    private JTabbedPane tabbedPane;

    public DisplayPropertiesDialog(ModelCreator creator)
    {
        super(creator, "Display Properties", Dialog.ModalityType.MODELESS);
        this.creator = creator;
        this.init();
        this.pack();
        this.setResizable(false);
    }


    private void init()
    {
        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setPreferredSize(new Dimension(400, 490));
        this.add(panel);

        JLabel labelProperties = new JLabel("Presets");
        panel.add(labelProperties);

        JComboBox<DisplayProperties> comboBoxProperties = new JComboBox<>();
        comboBoxProperties.addItem(DisplayProperties.MODEL_CREATOR_BLOCK);
        comboBoxProperties.addItem(DisplayProperties.DEFAULT_BLOCK);
        comboBoxProperties.addItem(DisplayProperties.DEFAULT_ITEM);
        comboBoxProperties.setPreferredSize(new Dimension(0, 24));
        panel.add(comboBoxProperties);

        DisplayProperties properties = creator.getElementManager().getDisplayProperties();

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("GUI", new DisplayEntryPanel(properties.getEntry("gui")));
        tabbedPane.addTab("Ground", new DisplayEntryPanel(properties.getEntry("ground")));
        tabbedPane.addTab("Fixed", new DisplayEntryPanel(properties.getEntry("fixed")));
        tabbedPane.addTab("Head", new DisplayEntryPanel(properties.getEntry("head")));
        tabbedPane.addTab("First Person", new DisplayEntryPanel(properties.getEntry("firstperson_righthand")));
        tabbedPane.addTab("Third Person", new DisplayEntryPanel(properties.getEntry("thirdperson_righthand")));
        tabbedPane.addTab("First Person (Left)", new DisplayEntryPanel(properties.getEntry("firstperson_lefthand")));
        tabbedPane.addTab("Third Person (Left)", new DisplayEntryPanel(properties.getEntry("thirdperson_lefthand")));
        tabbedPane.addChangeListener(e ->
        {
            Component c = tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
            if(c instanceof DisplayEntryPanel)
            {
                DisplayEntryPanel entryPanel = (DisplayEntryPanel) c;
                CanvasRenderer render = DisplayProperties.RENDER_MAP.get(entryPanel.getEntry().getId());
                if(render != null)
                {
                    ModelCreator.setCanvasRenderer(render);
                }
                else
                {
                    ModelCreator.restoreStandardRenderer();
                }
            }
        });
        panel.add(tabbedPane);

        JButton btnApplyProperties = new JButton("Apply");
        btnApplyProperties.setPreferredSize(new Dimension(80, 24));
        btnApplyProperties.addActionListener(e ->
        {
            creator.getElementManager().setDisplayProperties((DisplayProperties) comboBoxProperties.getSelectedItem());
            this.updateValues(creator.getElementManager().getDisplayProperties());
        });
        panel.add(btnApplyProperties);

        JCheckBox checkBoxShowGrid = ComponentUtil.createCheckBox("Show Grid", "Determines whether the grid should render", Menu.shouldRenderGrid);
        checkBoxShowGrid.addActionListener(e -> Menu.shouldRenderGrid = checkBoxShowGrid.isSelected());
        panel.add(checkBoxShowGrid);

        layout.putConstraint(SpringLayout.WEST, labelProperties, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, labelProperties, 2, SpringLayout.NORTH, comboBoxProperties);
        layout.putConstraint(SpringLayout.EAST, comboBoxProperties, -10, SpringLayout.WEST, btnApplyProperties);
        layout.putConstraint(SpringLayout.NORTH, comboBoxProperties, 10, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, comboBoxProperties, 10, SpringLayout.EAST, labelProperties);
        layout.putConstraint(SpringLayout.NORTH, btnApplyProperties, 10, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.EAST, btnApplyProperties, -10, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.WEST, checkBoxShowGrid, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, checkBoxShowGrid, 5, SpringLayout.SOUTH, comboBoxProperties);
        layout.putConstraint(SpringLayout.EAST, tabbedPane, -10, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.NORTH, tabbedPane, 5, SpringLayout.SOUTH, checkBoxShowGrid);
        layout.putConstraint(SpringLayout.WEST, tabbedPane, 10, SpringLayout.WEST, panel);
    }

    public void updateValues(DisplayProperties displayProperties)
    {
        Component[] components = tabbedPane.getComponents();
        for(Component c : components)
        {
            if(c instanceof DisplayEntryPanel)
            {
                DisplayEntryPanel entryPanel = (DisplayEntryPanel) c;
                DisplayProperties.Entry oldEntry = entryPanel.getEntry();
                DisplayProperties.Entry newEntry = displayProperties.getEntry(oldEntry.getId());
                if(newEntry != null)
                {
                    entryPanel.updateValues(newEntry);
                }
            }
        }
    }

    public static void update(ModelCreator creator)
    {
        if(Menu.displayPropertiesDialog != null)
        {
            Menu.displayPropertiesDialog.updateValues(creator.getElementManager().getDisplayProperties());
        }
    }
}
