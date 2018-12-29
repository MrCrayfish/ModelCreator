package com.mrcrayfish.modelcreator.panels;

import com.mrcrayfish.modelcreator.Exporter;
import com.mrcrayfish.modelcreator.Icons;
import com.mrcrayfish.modelcreator.Properties;
import com.mrcrayfish.modelcreator.StateManager;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.util.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OriginPanel extends JPanel implements IElementUpdater
{
    private ElementManager manager;

    private JButton btnPlusX;
    private JButton btnPlusY;
    private JButton btnPlusZ;
    private JTextField xOriginField;
    private JTextField yOriginField;
    private JTextField zOriginField;
    private JButton btnNegX;
    private JButton btnNegY;
    private JButton btnNegZ;

    public OriginPanel(ElementManager manager)
    {
        this.manager = manager;
        this.setLayout(new GridLayout(3, 3, 4, 4));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Origin</b></html>"));
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
        xOriginField = new JTextField();
        yOriginField = new JTextField();
        zOriginField = new JTextField();
        btnNegX = new JButton(Icons.arrow_down);
        btnNegY = new JButton(Icons.arrow_down);
        btnNegZ = new JButton(Icons.arrow_down);
    }

    private void initProperties()
    {
        Font defaultFont = new Font("SansSerif", Font.BOLD, 20);
        xOriginField.setSize(new Dimension(62, 30));
        xOriginField.setFont(defaultFont);
        xOriginField.setHorizontalAlignment(JTextField.CENTER);
        xOriginField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        selectedElement.setOriginX((Parser.parseDouble(xOriginField.getText(), selectedElement.getOriginX())));
                        manager.updateValues();
                        StateManager.pushState(manager);
                    }
                }
            }
        });
        xOriginField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    selectedElement.setOriginX((Parser.parseDouble(xOriginField.getText(), selectedElement.getOriginX())));
                    manager.updateValues();
                }
            }
        });

        yOriginField.setSize(new Dimension(62, 30));
        yOriginField.setFont(defaultFont);
        yOriginField.setHorizontalAlignment(JTextField.CENTER);
        yOriginField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        selectedElement.setOriginY((Parser.parseDouble(yOriginField.getText(), selectedElement.getOriginY())));
                        manager.updateValues();
                        StateManager.pushState(manager);
                    }
                }
            }
        });
        yOriginField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    selectedElement.setOriginY((Parser.parseDouble(yOriginField.getText(), selectedElement.getOriginY())));
                    manager.updateValues();
                }
            }
        });

        zOriginField.setSize(new Dimension(62, 30));
        zOriginField.setFont(defaultFont);
        zOriginField.setHorizontalAlignment(JTextField.CENTER);
        zOriginField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        selectedElement.setOriginZ((Parser.parseDouble(zOriginField.getText(), selectedElement.getOriginZ())));
                        manager.updateValues();
                        StateManager.pushState(manager);
                    }
                }
            }
        });
        zOriginField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    selectedElement.setOriginZ((Parser.parseDouble(zOriginField.getText(), selectedElement.getOriginZ())));
                    manager.updateValues();
                }
            }
        });

        btnPlusX.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
                {
                    selectedElement.addOriginX(0.1F);
                }
                else
                {
                    selectedElement.addOriginX(1.0F);
                }
                xOriginField.setText(Exporter.FORMAT.format(selectedElement.getOriginX()));
                StateManager.pushStateDelayed(manager, Properties.ORIGIN_X);
            }
        });
        btnPlusX.setPreferredSize(new Dimension(62, 30));
        btnPlusX.setFont(defaultFont);
        btnPlusX.setToolTipText("<html>Increases the X origin.<br><b>Hold shift for decimals</b></html>");

        btnPlusY.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
                {
                    selectedElement.addOriginY(0.1F);
                }
                else
                {
                    selectedElement.addOriginY(1.0F);
                }
                yOriginField.setText(Exporter.FORMAT.format(selectedElement.getOriginY()));
                StateManager.pushStateDelayed(manager, Properties.ORIGIN_Y);
            }
        });
        btnPlusY.setPreferredSize(new Dimension(62, 30));
        btnPlusY.setFont(defaultFont);
        btnPlusY.setToolTipText("<html>Increases the Y origin.<br><b>Hold shift for decimals</b></html>");

        btnPlusZ.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
                {
                    selectedElement.addOriginZ(0.1F);
                }
                else
                {
                    selectedElement.addOriginZ(1.0F);
                }
                zOriginField.setText(Exporter.FORMAT.format(selectedElement.getOriginZ()));
                StateManager.pushStateDelayed(manager, Properties.ORIGIN_Z);
            }
        });
        btnPlusZ.setPreferredSize(new Dimension(62, 30));
        btnPlusZ.setFont(defaultFont);
        btnPlusZ.setToolTipText("<html>Increases the Z origin.<br><b>Hold shift for decimals</b></html>");

        btnNegX.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
                {
                    selectedElement.addOriginX(-0.1F);
                }
                else
                {
                    selectedElement.addOriginX(-1.0F);
                }
                xOriginField.setText(Exporter.FORMAT.format(selectedElement.getOriginX()));
                StateManager.pushStateDelayed(manager, Properties.ORIGIN_Z);
            }
        });
        btnNegX.setPreferredSize(new Dimension(62, 30));
        btnNegX.setFont(defaultFont);
        btnNegX.setToolTipText("<html>Decreases the X origin.<br><b>Hold shift for decimals</b></html>");

        btnNegY.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
                {
                    selectedElement.addOriginY(-0.1F);
                }
                else
                {
                    selectedElement.addOriginY(-1.0F);
                }
                yOriginField.setText(Exporter.FORMAT.format(selectedElement.getOriginY()));
                StateManager.pushStateDelayed(manager, Properties.ORIGIN_Y);
            }
        });
        btnNegY.setPreferredSize(new Dimension(62, 30));
        btnNegY.setFont(defaultFont);
        btnNegY.setToolTipText("<html>Decreases the Y origin.<br><b>Hold shift for decimals</b></html>");

        btnNegZ.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                if((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
                {
                    selectedElement.addOriginZ(-0.1F);
                }
                else
                {
                    selectedElement.addOriginZ(-1.0F);
                }
                zOriginField.setText(Exporter.FORMAT.format(selectedElement.getOriginZ()));
                StateManager.pushStateDelayed(manager, Properties.ORIGIN_Z);
            }
        });
        btnNegZ.setPreferredSize(new Dimension(62, 30));
        btnNegZ.setFont(defaultFont);
        btnNegZ.setToolTipText("<html>Decreases the Z origin.<br><b>Hold shift for decimals</b></html>");
    }

    private void addComponents()
    {
        this.add(btnPlusX);
        this.add(btnPlusY);
        this.add(btnPlusZ);
        this.add(xOriginField);
        this.add(yOriginField);
        this.add(zOriginField);
        this.add(btnNegX);
        this.add(btnNegY);
        this.add(btnNegZ);
    }

    @Override
    public void updateValues(Element cube)
    {
        if(cube != null)
        {
            xOriginField.setEnabled(true);
            yOriginField.setEnabled(true);
            zOriginField.setEnabled(true);
            xOriginField.setText(Exporter.FORMAT.format(cube.getOriginX()));
            yOriginField.setText(Exporter.FORMAT.format(cube.getOriginY()));
            zOriginField.setText(Exporter.FORMAT.format(cube.getOriginZ()));
        }
        else
        {
            xOriginField.setEnabled(false);
            yOriginField.setEnabled(false);
            zOriginField.setEnabled(false);
            xOriginField.setText("");
            yOriginField.setText("");
            zOriginField.setText("");
        }
    }
}
