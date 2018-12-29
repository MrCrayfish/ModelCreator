package com.mrcrayfish.modelcreator.panels;

import com.mrcrayfish.modelcreator.display.DisplayProperties;
import com.mrcrayfish.modelcreator.Exporter;
import com.mrcrayfish.modelcreator.Icons;
import com.mrcrayfish.modelcreator.util.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Author: MrCrayfish
 */
public class DisplayEntryPanel extends JPanel implements IDisplayEntryUpdater
{
    private DisplayProperties.Entry entry;
    private JSlider sliderRotationX;
    private JSlider sliderRotationY;
    private JSlider sliderRotationZ;

    public DisplayEntryPanel(DisplayProperties.Entry entry)
    {
        this.entry = entry;
        this.setLayout(new SpringLayout());
        this.initComponents();
    }

    public DisplayProperties.Entry getEntry()
    {
        return entry;
    }

    private void initComponents()
    {
        JPanel sliderPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        sliderPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 0), "<html><b>Rotation</b></html>"));
        sliderPanel.setPreferredSize(new Dimension(0, 180));
        this.add(sliderPanel, BorderLayout.CENTER);

        sliderRotationX = createRotationSlider("X Axis", sliderPanel);
        sliderRotationX.setValue((int) entry.getRotationX());
        sliderRotationX.addChangeListener(e -> entry.setRotationX(sliderRotationX.getValue()));

        sliderRotationY = createRotationSlider("Y Axis", sliderPanel);
        sliderRotationY.setValue((int) entry.getRotationY());
        sliderRotationY.addChangeListener(e -> entry.setRotationY(sliderRotationY.getValue()));

        sliderRotationZ = createRotationSlider("Z Axis", sliderPanel);
        sliderRotationZ.setValue((int) entry.getRotationZ());
        sliderRotationZ.addChangeListener(e -> entry.setRotationZ(sliderRotationZ.getValue()));

        JPanel otherPanel = new JPanel(new GridLayout(1, 2));
        otherPanel.setPreferredSize(new Dimension(0, 120));
        this.add(otherPanel, BorderLayout.SOUTH);

        JPanel translatePanel = new JPanel(new GridLayout(3, 3, 5, 5));
        translatePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 0), "<html><b>Translation</b></html>"));

        Font defaultFont = new Font("SansSerif", Font.BOLD, 20);

        JTextField textFieldTransX = new JTextField();
        textFieldTransX.setText(Exporter.FORMAT.format(entry.getTranslationX()));
        textFieldTransX.setFont(defaultFont);
        textFieldTransX.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField textFieldTransY = new JTextField();
        textFieldTransY.setText(Exporter.FORMAT.format(entry.getTranslationY()));
        textFieldTransY.setFont(defaultFont);
        textFieldTransY.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField textFieldTransZ = new JTextField();
        textFieldTransZ.setText(Exporter.FORMAT.format(entry.getTranslationZ()));
        textFieldTransZ.setFont(defaultFont);
        textFieldTransZ.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton btnTransX = new JButton(Icons.arrow_up);
        btnTransX.addActionListener(e -> 
        {
            if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
            {
                entry.setTranslationX(entry.getTranslationX() + 0.1);
            }
            else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
            {
                entry.setTranslationX(entry.getTranslationX() + 0.01);
            }
            else
            {
                entry.setTranslationX(entry.getTranslationX() + 1.0);
            }
            textFieldTransX.setText(Exporter.FORMAT.format(entry.getTranslationX()));
        });
        translatePanel.add(btnTransX);

        JButton btnTransY = new JButton(Icons.arrow_up);
        btnTransY.addActionListener(e ->
        {
            if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
            {
                entry.setTranslationY(entry.getTranslationY() + 0.1);
            }
            else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
            {
                entry.setTranslationY(entry.getTranslationY() + 0.01);
            }
            else
            {
                entry.setTranslationY(entry.getTranslationY() + 1.0);
            }
            textFieldTransY.setText(Exporter.FORMAT.format(entry.getTranslationY()));
        });
        translatePanel.add(btnTransY);

        JButton btnTransZ = new JButton(Icons.arrow_up);
        btnTransZ.addActionListener(e ->
        {
            if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
            {
                entry.setTranslationZ(entry.getTranslationZ() + 0.1);
            }
            else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
            {
                entry.setTranslationZ(entry.getTranslationZ() + 0.01);
            }
            else
            {
                entry.setTranslationZ(entry.getTranslationZ() + 1.0);
            }
            textFieldTransZ.setText(Exporter.FORMAT.format(entry.getTranslationZ()));
        });
        translatePanel.add(btnTransZ);

        translatePanel.add(textFieldTransX);
        translatePanel.add(textFieldTransY);
        translatePanel.add(textFieldTransZ);

        JButton btnTransXNeg = new JButton(Icons.arrow_down);
        btnTransXNeg.addActionListener(e ->
        {
            if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
            {
                entry.setTranslationX(entry.getTranslationX() - 0.1);
            }
            else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
            {
                entry.setTranslationX(entry.getTranslationX() - 0.01);
            }
            else
            {
                entry.setTranslationX(entry.getTranslationX() - 1.0);
            }
            textFieldTransX.setText(Exporter.FORMAT.format(entry.getTranslationX()));
        });
        translatePanel.add(btnTransXNeg);

        JButton btnTransYNeg = new JButton(Icons.arrow_down);
        btnTransYNeg.addActionListener(e ->
        {
            if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
            {
                entry.setTranslationY(entry.getTranslationY() - 0.1);
            }
            else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
            {
                entry.setTranslationY(entry.getTranslationY() - 0.01);
            }
            else
            {
                entry.setTranslationY(entry.getTranslationY() - 1.0);
            }
            textFieldTransY.setText(Exporter.FORMAT.format(entry.getTranslationY()));
        });
        translatePanel.add(btnTransYNeg);

        JButton btnTransZNeg = new JButton(Icons.arrow_down);
        btnTransZNeg.addActionListener(e ->
        {
            if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
            {
                entry.setTranslationZ(entry.getTranslationZ() - 0.1);
            }
            else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
            {
                entry.setTranslationZ(entry.getTranslationZ() - 0.01);
            }
            else
            {
                entry.setTranslationZ(entry.getTranslationZ() - 1.0);
            }
            textFieldTransZ.setText(Exporter.FORMAT.format(entry.getTranslationZ()));
        });
        translatePanel.add(btnTransZNeg);
        
        otherPanel.add(translatePanel);

        JPanel scalePanel = new JPanel(new GridLayout(3, 3, 5, 5));
        scalePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 0), "<html><b>Scale</b></html>"));

        JTextField textFieldScaleX = new JTextField();
        textFieldScaleX.setText(Exporter.FORMAT.format(entry.getScaleX()));
        textFieldScaleX.setFont(defaultFont);
        textFieldScaleX.setHorizontalAlignment(SwingConstants.CENTER);
        textFieldScaleX.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    entry.setScaleX(Parser.parseDouble(textFieldScaleX.getText(), entry.getScaleX()));
                }
            }
        });

        JTextField textFieldScaleY = new JTextField();
        textFieldScaleY.setText(Exporter.FORMAT.format(entry.getScaleY()));
        textFieldScaleY.setFont(defaultFont);
        textFieldScaleY.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField textFieldScaleZ = new JTextField();
        textFieldScaleZ.setText(Exporter.FORMAT.format(entry.getScaleZ()));
        textFieldScaleZ.setFont(defaultFont);
        textFieldScaleZ.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnScaleX = new JButton(Icons.arrow_up);
        btnScaleX.addActionListener(e ->
        {
            if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
            {
                entry.setScaleX(entry.getScaleX() + 0.1);
            }
            else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
            {
                entry.setScaleX(entry.getScaleX() + 0.01);
            }
            else
            {
                entry.setScaleX(entry.getScaleX() + 1.0);
            }
            textFieldScaleX.setText(Exporter.FORMAT.format(entry.getScaleX()));
        });
        scalePanel.add(btnScaleX);

        JButton btnScaleY = new JButton(Icons.arrow_up);
        btnScaleY.addActionListener(e ->
        {
            if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
            {
                entry.setScaleY(entry.getScaleY() + 0.1);
            }
            else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
            {
                entry.setScaleY(entry.getScaleY() + 0.01);
            }
            else
            {
                entry.setScaleY(entry.getScaleY() + 1.0);
            }
            textFieldScaleY.setText(Exporter.FORMAT.format(entry.getScaleY()));
        });
        scalePanel.add(btnScaleY);

        JButton btnScaleZ = new JButton(Icons.arrow_up);
        btnScaleZ.addActionListener(e ->
        {
            if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
            {
                entry.setScaleZ(entry.getScaleZ() + 0.1);
            }
            else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
            {
                entry.setScaleZ(entry.getScaleZ() + 0.01);
            }
            else
            {
                entry.setScaleZ(entry.getScaleZ() + 1.0);
            }
            textFieldScaleZ.setText(Exporter.FORMAT.format(entry.getScaleZ()));
        });
        scalePanel.add(btnScaleZ);
        
        scalePanel.add(textFieldScaleX);
        scalePanel.add(textFieldScaleY);
        scalePanel.add(textFieldScaleZ);

        JButton btnScaleXNeg = new JButton(Icons.arrow_down);
        btnScaleXNeg.addActionListener(e ->
        {
            if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
            {
                entry.setScaleX(entry.getScaleX() - 0.1);
            }
            else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
            {
                entry.setScaleX(entry.getScaleX() - 0.01);
            }
            else
            {
                entry.setScaleX(entry.getScaleX() - 1.0);
            }
            textFieldScaleX.setText(Exporter.FORMAT.format(entry.getScaleX()));
        });
        scalePanel.add(btnScaleXNeg);

        JButton btnScaleYNeg = new JButton(Icons.arrow_down);
        btnScaleYNeg.addActionListener(e ->
        {
            if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
            {
                entry.setScaleY(entry.getScaleY() - 0.1);
            }
            else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
            {
                entry.setScaleY(entry.getScaleY() - 0.01);
            }
            else
            {
                entry.setScaleY(entry.getScaleY() - 1.0);
            }
            textFieldScaleY.setText(Exporter.FORMAT.format(entry.getScaleY()));
        });
        scalePanel.add(btnScaleYNeg);

        JButton btnScaleZNeg = new JButton(Icons.arrow_down);
        btnScaleZNeg.addActionListener(e ->
        {
            if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) == 0)
            {
                entry.setScaleZ(entry.getScaleZ() - 0.1);
            }
            else if((e.getModifiers() & InputEvent.SHIFT_MASK) > 0 && (e.getModifiers() & InputEvent.CTRL_MASK) > 0)
            {
                entry.setScaleZ(entry.getScaleZ() - 0.01);
            }
            else
            {
                entry.setScaleZ(entry.getScaleZ() - 1.0);
            }
            textFieldScaleZ.setText(Exporter.FORMAT.format(entry.getScaleZ()));
        });
        scalePanel.add(btnScaleZNeg);
        
        otherPanel.add(scalePanel);

        SpringLayout springLayout = (SpringLayout) this.getLayout();
        springLayout.putConstraint(SpringLayout.WEST, sliderPanel, 10, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.NORTH, sliderPanel, 10, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, sliderPanel, -10, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.WEST, otherPanel, 10, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.NORTH, otherPanel, 10, SpringLayout.SOUTH, sliderPanel);
        springLayout.putConstraint(SpringLayout.EAST, otherPanel, -10, SpringLayout.EAST, this);
    }

    @Override
    public void updateValues(DisplayProperties.Entry entry)
    {
        sliderRotationX.setValue((int) entry.getRotationX());
        sliderRotationY.setValue((int) entry.getRotationY());
        sliderRotationZ.setValue((int) entry.getRotationZ());
    }

    private JSlider createRotationSlider(String labelText, JComponent parent)
    {
        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);

        JLabel labelKey = new JLabel(labelText);
        JTextField textFieldValue = new JTextField("0");
        JSlider sliderValue = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);

        labelKey.setPreferredSize(new Dimension(100, 24));
        panel.add(labelKey);

        textFieldValue.setPreferredSize(new Dimension(50, 24));
        textFieldValue.setHorizontalAlignment(SwingConstants.CENTER);
        textFieldValue.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    int value = Parser.parseInt(textFieldValue.getText(), sliderValue.getValue());
                    value = Math.max(0, Math.min(360, value));
                    textFieldValue.setText(String.valueOf(value));
                    sliderValue.setValue(value);
                }
            }
        });
        panel.add(textFieldValue);

        sliderValue.setFocusable(false);
        sliderValue.addChangeListener(e -> {
            textFieldValue.setText(String.valueOf(sliderValue.getValue()));
        });
        panel.add(sliderValue);

        layout.putConstraint(SpringLayout.WEST, labelKey, 5, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, labelKey, 0, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.EAST, textFieldValue, -5, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.NORTH, textFieldValue, 0, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, sliderValue, 0, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, sliderValue, 5, SpringLayout.SOUTH, textFieldValue);
        layout.putConstraint(SpringLayout.EAST, sliderValue, 0, SpringLayout.EAST, panel);

        parent.add(panel);

        return sliderValue;
    }
}
