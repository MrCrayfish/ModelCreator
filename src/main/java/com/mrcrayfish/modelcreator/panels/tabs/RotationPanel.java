package com.mrcrayfish.modelcreator.panels.tabs;

import com.mrcrayfish.modelcreator.StateManager;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.panels.IValueUpdater;
import com.mrcrayfish.modelcreator.panels.OriginPanel;
import com.mrcrayfish.modelcreator.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

public class RotationPanel extends JPanel implements IValueUpdater
{
    private ElementManager manager;

    private OriginPanel panelOrigin;
    private JPanel axisPanel;
    private JComboBox<String> axisList;
    private JPanel sliderPanel;
    private JSlider rotation;
    private JPanel extraPanel;
    private JRadioButton btnRescale;

    private DefaultComboBoxModel<String> model;

    private final int ROTATION_MIN = -2;
    private final int ROTATION_MAX = 2;
    private final int ROTATION_INIT = 0;

    public RotationPanel(ElementManager manager)
    {
        this.manager = manager;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.initMenu();
        this.initComponents();
        this.addComponents();
    }

    private void initMenu()
    {
        model = new DefaultComboBoxModel<>();
        model.addElement("<html><div style='padding:5px;color:red;'><b>X</b></html>");
        model.addElement("<html><div style='padding:5px;color:green;'><b>Y</b></html>");
        model.addElement("<html><div style='padding:5px;color:blue;'><b>Z</b></html>");
    }

    private void initComponents()
    {
        panelOrigin = new OriginPanel(manager);

        axisPanel = new JPanel(new GridLayout(1, 1));
        axisPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Axis</b></html>"));
        axisList = new JComboBox<>();
        axisList.setModel(model);
        axisList.setToolTipText("The axis the element will rotate around");
        axisList.addItemListener(e ->
        {
            if(e.getStateChange() == ItemEvent.SELECTED)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null && selectedElement.getPrevAxis() != axisList.getSelectedIndex())
                {
                    selectedElement.setPrevAxis(axisList.getSelectedIndex());
                    StateManager.pushState(manager);
                }
            }
        });
        axisList.setMaximumSize(new Dimension(186, 55));
        axisPanel.setMaximumSize(new Dimension(186, 55));
        axisPanel.add(axisList);

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(-2, new JLabel("-45\u00b0"));
        labelTable.put(-1, new JLabel("-22.5\u00b0"));
        labelTable.put(0, new JLabel("0\u00b0"));
        labelTable.put(1, new JLabel("22.5\u00b0"));
        labelTable.put(2, new JLabel("45\u00b0"));

        sliderPanel = new JPanel(new GridLayout(1, 1));
        sliderPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Rotation</b></html>"));
        rotation = new JSlider(JSlider.HORIZONTAL, ROTATION_MIN, ROTATION_MAX, ROTATION_INIT);
        rotation.setMajorTickSpacing(1);
        rotation.setPaintTicks(true);
        rotation.setPaintLabels(true);
        rotation.setLabelTable(labelTable);
        rotation.addChangeListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                selectedElement.setRotation(rotation.getValue() * 22.5D);
            }
        });
        rotation.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                StateManager.pushState(manager);
            }
        });
        rotation.setToolTipText("<html>The rotation of the element<br>Default: 0</html>");
        sliderPanel.setMaximumSize(new Dimension(190, 80));
        sliderPanel.add(rotation);

        extraPanel = new JPanel(new GridLayout(1, 2));
        extraPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Extras</b></html>"));

        btnRescale = ComponentUtil.createRadioButton("Rescale", "<html>Should scale faces across whole block<br>Default: Off<html>");
        btnRescale.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                selectedElement.setRescale(btnRescale.isSelected());
                StateManager.pushState(manager);
            }
        });
        extraPanel.setMaximumSize(new Dimension(186, 50));
        extraPanel.add(btnRescale);
    }

    private void addComponents()
    {
        add(Box.createRigidArea(new Dimension(188, 5)));
        add(panelOrigin);
        add(axisPanel);
        add(sliderPanel);
        add(extraPanel);
    }

    @Override
    public void updateValues(Element cube)
    {
        panelOrigin.updateValues(cube);
        if(cube != null)
        {
            axisList.setSelectedIndex(cube.getPrevAxis());
            rotation.setEnabled(true);
            rotation.setValue((int) (cube.getRotation() / 22.5));
            btnRescale.setEnabled(true);
            btnRescale.setSelected(cube.shouldRescale());
        }
        else
        {
            rotation.setValue(0);
            rotation.setEnabled(false);
            btnRescale.setSelected(false);
            btnRescale.setEnabled(false);
        }
    }
}
