package com.mrcrayfish.modelcreator.panels;

import com.mrcrayfish.modelcreator.*;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.util.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SizePanel extends JPanel implements IElementUpdater
{
    private ElementManager manager;

    private JButton btnPlusX;
    private JButton btnPlusY;
    private JButton btnPlusZ;
    private JTextField xSizeField;
    private JTextField ySizeField;
    private JTextField zSizeField;
    private JButton btnNegX;
    private JButton btnNegY;
    private JButton btnNegZ;

    public SizePanel(ElementManager manager)
    {
        this.manager = manager;
        this.setBackground(ModelCreator.BACKGROUND);
        this.setLayout(new GridLayout(3, 3, 4, 4));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ModelCreator.BACKGROUND, 5), "<html><b>Size</b></html>"));
        this.setMaximumSize(new Dimension(186, 124));
        this.initComponents();
        this.initProperties();
        this.addComponents();
    }

    private void initComponents()
    {
        btnPlusX = new JButton(Icons.arrow_up);
        btnPlusY = new JButton(Icons.arrow_up);
        btnPlusZ = new JButton(Icons.arrow_up);
        xSizeField = new JTextField();
        ySizeField = new JTextField();
        zSizeField = new JTextField();
        btnNegX = new JButton(Icons.arrow_down);
        btnNegY = new JButton(Icons.arrow_down);
        btnNegZ = new JButton(Icons.arrow_down);
    }

    private void initProperties()
    {
        Font defaultFont = new Font("SansSerif", Font.BOLD, 20);
        SidebarPanel.initIncrementableField(xSizeField, defaultFont);
        xSizeField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        selectedElement.setWidth(Parser.parseDouble(xSizeField.getText(), selectedElement.getWidth()));
                        selectedElement.updateEndUVs();
                        manager.updateValues();
                        StateManager.pushState(manager);
                    }

                }
            }
        });
        xSizeField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    selectedElement.setWidth(Parser.parseDouble(xSizeField.getText(), selectedElement.getWidth()));
                    selectedElement.updateEndUVs();
                    manager.updateValues();
                }
            }
        });

        SidebarPanel.initIncrementableField(ySizeField, defaultFont);
        ySizeField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        selectedElement.setHeight(Parser.parseDouble(ySizeField.getText(), selectedElement.getHeight()));
                        selectedElement.updateEndUVs();
                        manager.updateValues();
                        StateManager.pushState(manager);
                    }

                }
            }
        });
        ySizeField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    selectedElement.setHeight(Parser.parseDouble(ySizeField.getText(), selectedElement.getHeight()));
                    selectedElement.updateEndUVs();
                    manager.updateValues();
                }
            }
        });

        SidebarPanel.initIncrementableField(zSizeField, defaultFont);
        zSizeField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        selectedElement.setDepth(Parser.parseDouble(zSizeField.getText(), selectedElement.getDepth()));
                        selectedElement.updateEndUVs();
                        manager.updateValues();
                        StateManager.pushState(manager);
                    }

                }
            }
        });
        zSizeField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    selectedElement.setDepth(Parser.parseDouble(zSizeField.getText(), selectedElement.getDepth()));
                    selectedElement.updateEndUVs();
                    manager.updateValues();
                }
            }
        });

        SidebarPanel.initIncrementButton(btnPlusX, defaultFont, "width", true);
        btnPlusX.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    selectedElement.addWidth(ctrl == 0 ? 0.1 : 0.01);
                }
                else
                {
                    selectedElement.addWidth(1.0);
                }
                selectedElement.updateEndUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.SIZE_X);
            }
        });

        SidebarPanel.initIncrementButton(btnPlusY, defaultFont, "height", true);
        btnPlusY.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    selectedElement.addHeight(ctrl == 0 ? 0.1 : 0.01);
                }
                else
                {
                    selectedElement.addHeight(1.0);
                }
                selectedElement.updateEndUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.SIZE_Y);
            }
        });

        SidebarPanel.initIncrementButton(btnPlusZ, defaultFont, "depth", true);
        btnPlusZ.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    selectedElement.addDepth(ctrl == 0 ? 0.1 : 0.01);
                }
                else
                {
                    selectedElement.addDepth(1.0);
                }
                selectedElement.updateEndUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.SIZE_Z);
            }
        });

        SidebarPanel.initIncrementButton(btnNegX, defaultFont, "width", false);
        btnNegX.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    selectedElement.setWidth(Math.max(0.0, selectedElement.getWidth() - (ctrl == 0 ? 0.1 : 0.01)));
                }
                else
                {
                    selectedElement.setWidth(Math.max(0.0, selectedElement.getWidth() - 1.0));
                }
                selectedElement.updateEndUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.SIZE_X);
            }
        });

        SidebarPanel.initIncrementButton(btnNegY, defaultFont, "height", false);
        btnNegY.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    selectedElement.setHeight(Math.max(0.0, selectedElement.getHeight() - (ctrl == 0 ? 0.1 : 0.01)));
                }
                else
                {
                    selectedElement.setHeight(Math.max(0.0, selectedElement.getHeight() - 1.0));
                }
                selectedElement.updateEndUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.SIZE_Y);
            }
        });

        SidebarPanel.initIncrementButton(btnNegZ, defaultFont, "depth", false);
        btnNegZ.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    selectedElement.setDepth(Math.max(0.0, selectedElement.getDepth() - (ctrl == 0 ? 0.1 : 0.01)));
                }
                else
                {
                    selectedElement.setDepth(Math.max(0.0, selectedElement.getDepth() - 1.0));
                }
                selectedElement.updateEndUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.SIZE_Z);
            }
        });
    }

    private void addComponents()
    {
        this.add(btnPlusX);
        this.add(btnPlusY);
        this.add(btnPlusZ);
        this.add(xSizeField);
        this.add(ySizeField);
        this.add(zSizeField);
        this.add(btnNegX);
        this.add(btnNegY);
        this.add(btnNegZ);
    }

    @Override
    public void updateValues(Element cube)
    {
        if(cube != null)
        {
            xSizeField.setEnabled(true);
            ySizeField.setEnabled(true);
            zSizeField.setEnabled(true);
            xSizeField.setText(Exporter.FORMAT.format(cube.getWidth()));
            ySizeField.setText(Exporter.FORMAT.format(cube.getHeight()));
            zSizeField.setText(Exporter.FORMAT.format(cube.getDepth()));
        }
        else
        {
            xSizeField.setEnabled(false);
            ySizeField.setEnabled(false);
            zSizeField.setEnabled(false);
            xSizeField.setText("");
            ySizeField.setText("");
            zSizeField.setText("");
        }
    }
}
