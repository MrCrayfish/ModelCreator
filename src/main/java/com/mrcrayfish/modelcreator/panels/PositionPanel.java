package com.mrcrayfish.modelcreator.panels;

import com.mrcrayfish.modelcreator.*;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.util.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PositionPanel extends JPanel implements IElementUpdater
{
    private ElementManager manager;

    private JButton btnPlusX;
    private JButton btnPlusY;
    private JButton btnPlusZ;
    private JTextField xPositionField;
    private JTextField yPositionField;
    private JTextField zPositionField;
    private JButton btnNegX;
    private JButton btnNegY;
    private JButton btnNegZ;

    public PositionPanel(ElementManager manager)
    {
        this.manager = manager;
        this.setBackground(ModelCreator.BACKGROUND);
        this.setLayout(new GridLayout(3, 3, 4, 4));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ModelCreator.BACKGROUND, 5), "<html><b>Position</b></html>"));
        this.setMaximumSize(new Dimension(186, 124));
        this.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        this.initComponents();
        this.initProperties();
        this.addComponents();
    }

    private void initComponents()
    {
        btnPlusX = new JButton(Icons.arrow_up);
        btnPlusY = new JButton(Icons.arrow_up);
        btnPlusZ = new JButton(Icons.arrow_up);
        xPositionField = new JTextField();
        yPositionField = new JTextField();
        zPositionField = new JTextField();
        btnNegX = new JButton(Icons.arrow_down);
        btnNegY = new JButton(Icons.arrow_down);
        btnNegZ = new JButton(Icons.arrow_down);
    }

    private void initProperties()
    {
        Font defaultFont = new Font("SansSerif", Font.BOLD, 20);
        SidebarPanel.initIncrementableField(xPositionField, defaultFont);
        xPositionField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        selectedElement.setStartX(Parser.parseDouble(xPositionField.getText(), selectedElement.getStartX()));
                        selectedElement.updateEndUVs();
                        manager.updateValues();
                        StateManager.pushState(manager);
                    }

                }
            }
        });
        xPositionField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    selectedElement.setStartX(Parser.parseDouble(xPositionField.getText(), selectedElement.getStartX()));
                    selectedElement.updateEndUVs();
                    manager.updateValues();
                }
            }
        });

        SidebarPanel.initIncrementableField(yPositionField, defaultFont);
        yPositionField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        StateManager.pushState(manager);
                        selectedElement.setStartY(Parser.parseDouble(yPositionField.getText(), selectedElement.getStartY()));
                        selectedElement.updateEndUVs();
                        manager.updateValues();
                    }

                }
            }
        });
        yPositionField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    StateManager.pushState(manager);
                    selectedElement.setStartY(Parser.parseDouble(yPositionField.getText(), selectedElement.getStartY()));
                    selectedElement.updateEndUVs();
                    manager.updateValues();
                }
            }
        });

        SidebarPanel.initIncrementableField(zPositionField, defaultFont);
        zPositionField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        StateManager.pushState(manager);
                        selectedElement.setStartZ(Parser.parseDouble(zPositionField.getText(), selectedElement.getStartZ()));
                        selectedElement.updateEndUVs();
                        manager.updateValues();
                    }

                }
            }
        });
        zPositionField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    StateManager.pushState(manager);
                    selectedElement.setStartZ(Parser.parseDouble(zPositionField.getText(), selectedElement.getStartZ()));
                    selectedElement.updateEndUVs();
                    manager.updateValues();
                }
            }
        });

        SidebarPanel.initIncrementButton(btnPlusX, defaultFont, "X position", true);
        btnPlusX.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    selectedElement.addStartX(ctrl == 0 ? 0.1 : 0.01);
                }
                else
                {
                    selectedElement.addStartX(1.0);
                }
                xPositionField.setText(Exporter.FORMAT.format(selectedElement.getStartX()));
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.POS_X);
            }
        });

        SidebarPanel.initIncrementButton(btnPlusY, defaultFont, "Y position", true);
        btnPlusY.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    selectedElement.addStartY(ctrl == 0 ? 0.1 : 0.01);
                }
                else
                {
                    selectedElement.addStartY(1.0);
                }
                yPositionField.setText(Exporter.FORMAT.format(selectedElement.getStartY()));
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.POS_Y);
            }
        });

        SidebarPanel.initIncrementButton(btnPlusZ, defaultFont, "Z position", true);
        btnPlusZ.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    selectedElement.addStartZ(ctrl == 0 ? 0.1 : 0.01);
                }
                else
                {
                    selectedElement.addStartZ(1.0);
                }
                zPositionField.setText(Exporter.FORMAT.format(selectedElement.getStartZ()));
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.POS_Z);
            }
        });

        SidebarPanel.initIncrementButton(btnNegX, defaultFont, "X position", false);
        btnNegX.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    selectedElement.addStartX(ctrl == 0 ? -0.1 : -0.01);
                }
                else
                {
                    selectedElement.addStartX(-1.0);
                }
                xPositionField.setText(Exporter.FORMAT.format(selectedElement.getStartX()));
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.POS_X);
            }
        });

        SidebarPanel.initIncrementButton(btnNegY, defaultFont, "Y position", false);
        btnNegY.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    selectedElement.addStartY(ctrl == 0 ? -0.1 : -0.01);
                }
                else
                {
                    selectedElement.addStartY(-1.0);
                }
                yPositionField.setText(Exporter.FORMAT.format(selectedElement.getStartY()));
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.POS_Y);
            }
        });

        SidebarPanel.initIncrementButton(btnNegZ, defaultFont, "Z position", false);
        btnNegZ.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    selectedElement.addStartZ(ctrl == 0 ? -0.1 : -0.01);
                }
                else
                {
                    selectedElement.addStartZ(-1.0F);
                }
                zPositionField.setText(Exporter.FORMAT.format(selectedElement.getStartZ()));
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.POS_Z);
            }
        });
    }

    private void addComponents()
    {
        this.add(btnPlusX);
        this.add(btnPlusY);
        this.add(btnPlusZ);
        this.add(xPositionField);
        this.add(yPositionField);
        this.add(zPositionField);
        this.add(btnNegX);
        this.add(btnNegY);
        this.add(btnNegZ);
    }

    @Override
    public void updateValues(Element cube)
    {
        if(cube != null)
        {
            xPositionField.setEnabled(true);
            yPositionField.setEnabled(true);
            zPositionField.setEnabled(true);
            xPositionField.setText(Exporter.FORMAT.format(cube.getStartX()));
            yPositionField.setText(Exporter.FORMAT.format(cube.getStartY()));
            zPositionField.setText(Exporter.FORMAT.format(cube.getStartZ()));
        }
        else
        {
            xPositionField.setEnabled(false);
            yPositionField.setEnabled(false);
            zPositionField.setEnabled(false);
            xPositionField.setText("");
            yPositionField.setText("");
            zPositionField.setText("");
        }
    }
}
