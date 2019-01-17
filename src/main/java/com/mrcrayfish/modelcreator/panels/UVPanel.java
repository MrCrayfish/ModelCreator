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
        SidebarPanel.initIncrementableField(xStartField, defaultFont);
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

        SidebarPanel.initIncrementableField(yStartField, defaultFont);
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

        SidebarPanel.initIncrementableField(xEndField, defaultFont);
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

        SidebarPanel.initIncrementableField(yEndField, defaultFont);
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

        SidebarPanel.initIncrementButton(btnPlusX, defaultFont, "start U", true);
        btnPlusX.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    face.addTextureX(ctrl == 0 ? 0.1 : 0.01);
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

        SidebarPanel.initIncrementButton(btnPlusY, defaultFont, "start V", true);
        btnPlusY.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    face.addTextureY(ctrl == 0 ? 0.1 : 0.01);
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

        SidebarPanel.initIncrementButton(btnNegX, defaultFont, "start U", false);
        btnNegX.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    face.addTextureX(ctrl == 0 ? -0.1 : -0.01);
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

        SidebarPanel.initIncrementButton(btnNegY, defaultFont, "start V", false);
        btnNegY.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    face.addTextureY(ctrl == 0 ? -0.1 : -0.01);
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

        SidebarPanel.initIncrementButton(btnPlusXEnd, defaultFont, "end U", true);
        btnPlusXEnd.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    face.addTextureXEnd(ctrl == 0 ? 0.1 : 0.01);
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

        SidebarPanel.initIncrementButton(btnPlusYEnd, defaultFont, "end V", true);
        btnPlusYEnd.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    face.addTextureYEnd(ctrl == 0 ? 0.1 : 0.01);
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

        SidebarPanel.initIncrementButton(btnNegXEnd, defaultFont, "end U", false);
        btnNegXEnd.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    face.addTextureXEnd(ctrl == 0 ? -0.1 : -0.01);
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

        SidebarPanel.initIncrementButton(btnNegYEnd, defaultFont, "end V", false);
        btnNegYEnd.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                Face face = selectedElement.getSelectedFace();
                int ctrl = e.getModifiers() & InputEvent.CTRL_MASK;
                if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && ctrl >= 0)
                {
                    face.addTextureYEnd(ctrl == 0 ? -0.1 : -0.01);
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
