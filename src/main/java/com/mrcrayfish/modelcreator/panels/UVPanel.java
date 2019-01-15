package com.mrcrayfish.modelcreator.panels;

import com.mrcrayfish.modelcreator.*;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.util.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UVPanel extends JPanel implements IElementUpdater
{
    private ElementManager manager;
    private JButton btnPlusX;
    private JButton btnPlusY;
    private JTextField xStartField;
    private JTextField yStartField;
    private JButton btnNegX;
    private JButton btnNegY;

    private JButton btnPlusXEnd;
    private JButton btnPlusYEnd;
    private JTextField xEndField;
    private JTextField yEndField;
    private JButton btnNegXEnd;
    private JButton btnNegYEnd;

    public UVPanel(ElementManager manager)
    {
        this.manager = manager;
        this.setBackground(ModelCreator.BACKGROUND);
        this.setLayout(new GridLayout(3, 4, 4, 4));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ModelCreator.BACKGROUND, 5), "<html><b>UV</b></html>"));
        this.setMaximumSize(new Dimension(186, 124));
        this.initComponents();
        this.initProperties();
        this.addComponents();
    }

    private void initComponents()
    {
        btnPlusX = new JButton(Icons.arrow_up);
        btnPlusY = new JButton(Icons.arrow_up);
        xStartField = new JTextField();
        yStartField = new JTextField();
        btnNegX = new JButton(Icons.arrow_down);
        btnNegY = new JButton(Icons.arrow_down);

        btnPlusXEnd = new JButton(Icons.arrow_up);
        btnPlusYEnd = new JButton(Icons.arrow_up);
        xEndField = new JTextField();
        yEndField = new JTextField();
        btnNegXEnd = new JButton(Icons.arrow_down);
        btnNegYEnd = new JButton(Icons.arrow_down);
    }

    private void initProperties()
    {
        Font defaultFont = new Font("SansSerif", Font.BOLD, 20);
        xStartField.setSize(new Dimension(62, 30));
        xStartField.setFont(defaultFont);
        xStartField.setHorizontalAlignment(JTextField.CENTER);
        xStartField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        Face face = selectedElement.getSelectedFace();
                        face.setStartU(Parser.parseDouble(xStartField.getText(), face.getStartU()));
                        face.updateEndUV();
                        manager.updateValues();
                        StateManager.pushState(manager);
                    }

                }
            }
        });
        xStartField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    Face face = selectedElement.getSelectedFace();
                    face.setStartU(Parser.parseDouble(xStartField.getText(), face.getStartU()));
                    face.updateEndUV();
                    manager.updateValues();
                }
            }
        });

        yStartField.setSize(new Dimension(62, 30));
        yStartField.setFont(defaultFont);
        yStartField.setHorizontalAlignment(JTextField.CENTER);
        yStartField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        Face face = selectedElement.getSelectedFace();
                        face.setStartV(Parser.parseDouble(yStartField.getText(), face.getStartV()));
                        face.updateEndUV();
                        manager.updateValues();
                        StateManager.pushState(manager);
                    }
                }
            }
        });
        yStartField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    Face face = selectedElement.getSelectedFace();
                    face.setStartV(Parser.parseDouble(yStartField.getText(), face.getStartV()));
                    face.updateEndUV();
                    manager.updateValues();
                }
            }
        });

        xEndField.setSize(new Dimension(62, 30));
        xEndField.setFont(defaultFont);
        xEndField.setHorizontalAlignment(JTextField.CENTER);
        xEndField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        Face face = selectedElement.getSelectedFace();
                        face.setEndU(Parser.parseDouble(xEndField.getText(), face.getEndU()));
                        face.updateEndUV();
                        manager.updateValues();
                        StateManager.pushState(manager);
                    }
                }
            }
        });
        xEndField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    Face face = selectedElement.getSelectedFace();
                    face.setEndU(Parser.parseDouble(xEndField.getText(), face.getEndU()));
                    face.updateEndUV();
                    manager.updateValues();
                }
            }
        });

        yEndField.setSize(new Dimension(62, 30));
        yEndField.setFont(defaultFont);
        yEndField.setHorizontalAlignment(JTextField.CENTER);
        yEndField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        Face face = selectedElement.getSelectedFace();
                        face.setEndV(Parser.parseDouble(yEndField.getText(), face.getEndV()));
                        face.updateEndUV();
                        manager.updateValues();
                        StateManager.pushState(manager);
                    }
                }
            }
        });
        yEndField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {

                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    Face face = selectedElement.getSelectedFace();
                    face.setEndV(Parser.parseDouble(yEndField.getText(), face.getEndV()));
                    face.updateEndUV();
                    manager.updateValues();
                    StateManager.pushState(manager);
                }
            }
        });

        btnPlusX.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    face.addTextureX(0.1);
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    face.addTextureX(0.01);
                }
                else
                {
                    face.addTextureX(1.0);
                }
                selectedElement.updateEndUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.START_U);
            }
        });

        btnPlusX.setSize(new Dimension(62, 30));
        btnPlusX.setFont(defaultFont);
        btnPlusX.setToolTipText("<html>Increases the start U.<br><b>Hold shift for decimals</b></html>");

        btnPlusY.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    face.addTextureY(0.1);
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    face.addTextureY(0.01);
                }
                else
                {
                    face.addTextureY(1.0);
                }
                selectedElement.updateEndUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.START_V);
            }
        });
        btnPlusY.setPreferredSize(new Dimension(62, 30));
        btnPlusY.setFont(defaultFont);
        btnPlusY.setToolTipText("<html>Increases the start V.<br><b>Hold shift for decimals</b></html>");

        btnNegX.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    face.addTextureX(-0.1);
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    face.addTextureX(-0.01);
                }
                else
                {
                    face.addTextureX(-1.0);
                }
                selectedElement.updateEndUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.START_U);
            }
        });
        btnNegX.setSize(new Dimension(62, 30));
        btnNegX.setFont(defaultFont);
        btnNegX.setToolTipText("<html>Decreases the start U.<br><b>Hold shift for decimals</b></html>");

        btnNegY.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    face.addTextureY(-0.1);
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    face.addTextureY(-0.01);
                }
                else
                {
                    face.addTextureY(-1.0);
                }
                selectedElement.updateEndUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.START_V);
            }
        });
        btnNegY.setSize(new Dimension(62, 30));
        btnNegY.setFont(defaultFont);
        btnNegY.setToolTipText("<html>Decreases the start V.<br><b>Hold shift for decimals</b></html>");

        btnPlusXEnd.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    face.addTextureXEnd(0.1);
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    face.addTextureXEnd(0.01);
                }
                else
                {
                    face.addTextureXEnd(1.0);
                }
                selectedElement.updateStartUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.END_U);
            }
        });
        btnPlusXEnd.setSize(new Dimension(62, 30));
        btnPlusXEnd.setFont(defaultFont);
        btnPlusXEnd.setToolTipText("<html>Increases the end U.<br><b>Hold shift for decimals</b></html>");

        btnPlusYEnd.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    face.addTextureYEnd(0.1);
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    face.addTextureYEnd(0.01);
                }
                else
                {
                    face.addTextureYEnd(1.0);
                }
                selectedElement.updateStartUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.END_V);
            }
        });
        btnPlusYEnd.setPreferredSize(new Dimension(62, 30));
        btnPlusYEnd.setFont(defaultFont);
        btnPlusYEnd.setToolTipText("<html>Increases the end V.<br><b>Hold shift for decimals</b></html>");

        btnNegXEnd.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    face.addTextureXEnd(-0.1);
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    face.addTextureXEnd(-0.01);
                }
                else
                {
                    face.addTextureXEnd(-1.0);
                }
                selectedElement.updateStartUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.END_U);
            }
        });
        btnNegXEnd.setSize(new Dimension(62, 30));
        btnNegXEnd.setFont(defaultFont);
        btnNegXEnd.setToolTipText("<html>Decreases the end U.<br><b>Hold shift for decimals</b></html>");

        btnNegYEnd.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
                {
                    face.addTextureYEnd(-0.1);
                }
                else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
                {
                    face.addTextureYEnd(-0.01);
                }
                else
                {
                    face.addTextureYEnd(-1.0);
                }
                selectedElement.updateStartUVs();
                manager.updateValues();
                StateManager.pushStateDelayed(manager, PropertyIdentifiers.END_V);
            }
        });
        btnNegYEnd.setSize(new Dimension(62, 30));
        btnNegYEnd.setFont(defaultFont);
        btnNegYEnd.setToolTipText("<html>Decreases the end V.<br><b>Hold shift for decimals</b></html>");
    }

    private void addComponents()
    {
        this.add(btnPlusX);
        this.add(btnPlusY);
        this.add(btnPlusXEnd);
        this.add(btnPlusYEnd);
        this.add(xStartField);
        this.add(yStartField);
        this.add(xEndField);
        this.add(yEndField);
        this.add(btnNegX);
        this.add(btnNegY);
        this.add(btnNegXEnd);
        this.add(btnNegYEnd);
    }

    @Override
    public void updateValues(Element cube)
    {
        if(cube != null)
        {
            xStartField.setEnabled(true);
            yStartField.setEnabled(true);
            xEndField.setEnabled(true);
            yEndField.setEnabled(true);
            xStartField.setText(Exporter.FORMAT.format(cube.getSelectedFace().getStartU()));
            yStartField.setText(Exporter.FORMAT.format(cube.getSelectedFace().getStartV()));
            xEndField.setText(Exporter.FORMAT.format(cube.getSelectedFace().getEndU()));
            yEndField.setText(Exporter.FORMAT.format(cube.getSelectedFace().getEndV()));
        }
        else
        {
            xStartField.setEnabled(false);
            yStartField.setEnabled(false);
            xEndField.setEnabled(false);
            yEndField.setEnabled(false);
            xStartField.setText("");
            yStartField.setText("");
            xEndField.setText("");
            yEndField.setText("");
        }
    }
}
