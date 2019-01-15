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
        xSizeField.setSize(new Dimension(62, 30));
        xSizeField.setFont(defaultFont);
        xSizeField.setHorizontalAlignment(JTextField.CENTER);
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

        ySizeField.setSize(new Dimension(62, 30));
        ySizeField.setFont(defaultFont);
        ySizeField.setHorizontalAlignment(JTextField.CENTER);
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

        zSizeField.setSize(new Dimension(62, 30));
        zSizeField.setFont(defaultFont);
        zSizeField.setHorizontalAlignment(JTextField.CENTER);
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

        btnPlusX.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    selectedElement.addWidth(0.1);
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    selectedElement.addWidth(0.01);
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
        btnPlusX.setPreferredSize(new Dimension(62, 30));
        btnPlusX.setFont(defaultFont);
        btnPlusX.setToolTipText("<html>Increases the width.<br><b>Hold shift for decimals</b></html>");

        btnPlusY.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    selectedElement.addHeight(0.1);
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    selectedElement.addHeight(0.01);
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
        btnPlusY.setPreferredSize(new Dimension(62, 30));
        btnPlusY.setFont(defaultFont);
        btnPlusY.setToolTipText("<html>Increases the height.<br><b>Hold shift for decimals</b></html>");

        btnPlusZ.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    selectedElement.addDepth(0.1);
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    selectedElement.addDepth(0.01);
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
        btnPlusZ.setPreferredSize(new Dimension(62, 30));
        btnPlusZ.setFont(defaultFont);
        btnPlusZ.setToolTipText("<html>Increases the depth.<br><b>Hold shift for decimals</b></html>");

        btnNegX.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    selectedElement.setWidth(Math.max(0.0, selectedElement.getWidth() - 0.1));
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    selectedElement.setWidth(Math.max(0.0, selectedElement.getWidth() - 0.01));
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
        btnNegX.setPreferredSize(new Dimension(62, 30));
        btnNegX.setFont(defaultFont);
        btnNegX.setToolTipText("<html>Decreases the width.<br><b>Hold shift for decimals</b></html>");

        btnNegY.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    selectedElement.setHeight(Math.max(0.0, selectedElement.getHeight() - 0.1));
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    selectedElement.setHeight(Math.max(0.0, selectedElement.getHeight() - 0.01));
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
        btnNegY.setPreferredSize(new Dimension(62, 30));
        btnNegY.setFont(defaultFont);
        btnNegY.setToolTipText("<html>Decreases the height.<br><b>Hold shift for decimals</b></html>");

        btnNegZ.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    selectedElement.setDepth(Math.max(0.0, selectedElement.getDepth() - 0.1));
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    selectedElement.setDepth(Math.max(0.0, selectedElement.getDepth() - 0.01));
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
        btnNegZ.setPreferredSize(new Dimension(62, 30));
        btnNegZ.setFont(defaultFont);
        btnNegZ.setToolTipText("<html>Decreases the depth.<br><b>Hold shift for decimals</b></html>");
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
