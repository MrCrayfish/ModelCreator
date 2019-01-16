package com.mrcrayfish.modelcreator.panels.tabs;

import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.StateManager;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.panels.FaceExtrasPanel;
import com.mrcrayfish.modelcreator.panels.IElementUpdater;
import com.mrcrayfish.modelcreator.panels.TexturePanel;
import com.mrcrayfish.modelcreator.panels.UVPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

public class FacePanel extends JPanel implements IElementUpdater
{
    private ElementManager manager;

    private JPanel menuPanel;
    private JComboBox<String> menuList;
    private UVPanel panelUV;
    private JPanel sliderPanel;
    private JSlider rotation;
    private TexturePanel panelTexture;
    private FaceExtrasPanel panelProperties;

    private final int ROTATION_MIN = 0;
    private final int ROTATION_MAX = 3;
    private final int ROTATION_INIT = 0;

    private DefaultComboBoxModel<String> model;

    public FacePanel(ElementManager manager)
    {
        this.manager = manager;
        setBackground(ModelCreator.BACKGROUND);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initMenu();
        initComponents();
        addComponents();
    }

    private void initMenu()
    {
        model = new DefaultComboBoxModel<>();
        model.addElement("<html><div style='padding:5px;color:rgb(255,0,0);'><b>North</b></html>");
        model.addElement("<html><div style='padding:5px;color:rgb(0,255,0);'><b>East</b></html>");
        model.addElement("<html><div style='padding:5px;color:rgb(0,0,255);'><b>South</b></html>");
        model.addElement("<html><div style='padding:5px;color:rgb(255,187,0);'><b>West</b></html>");
        model.addElement("<html><div style='padding:5px;color:rgb(0,255,255);'><b>Up</b></html>");
        model.addElement("<html><div style='padding:5px;color:rgb(255,0,255);'><b>Down</b></html>");
    }

    private void initComponents()
    {
        menuPanel = new JPanel(new GridLayout(1, 1));
        menuPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ModelCreator.BACKGROUND, 5), "<html><b>Side</b></html>"));
        menuPanel.setMaximumSize(new Dimension(186, 56));
        menuPanel.setBackground(ModelCreator.BACKGROUND);

        menuList = new JComboBox<>();
        menuList.setModel(model);
        menuList.setToolTipText("The face to edit.");
        menuList.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                selectedElement.setSelectedFace(menuList.getSelectedIndex());
                updateValues(selectedElement);
            }
        });
        menuPanel.add(menuList);

        panelTexture = new TexturePanel(manager);
        panelUV = new UVPanel(manager);
        panelProperties = new FaceExtrasPanel(manager);

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("0\u00b0"));
        labelTable.put(1, new JLabel("90\u00b0"));
        labelTable.put(2, new JLabel("180\u00b0"));
        labelTable.put(3, new JLabel("270\u00b0"));

        sliderPanel = new JPanel(new GridLayout(1, 1));
        sliderPanel.setBackground(ModelCreator.BACKGROUND);
        sliderPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ModelCreator.BACKGROUND, 5), "<html><b>Rotation</b></html>"));
        rotation = new JSlider(JSlider.HORIZONTAL, ROTATION_MIN, ROTATION_MAX, ROTATION_INIT);
        rotation.setBackground(ModelCreator.BACKGROUND);
        rotation.setMajorTickSpacing(4);
        rotation.setPaintTicks(true);
        rotation.setPaintLabels(true);
        rotation.setLabelTable(labelTable);
        rotation.addChangeListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                selectedElement.getSelectedFace().setRotation(rotation.getValue());
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
        rotation.setToolTipText("<html>The rotation of the texture<br>Default: 0\u00b0</html>");
        sliderPanel.setMaximumSize(new Dimension(190, 80));
        sliderPanel.add(rotation);
    }

    private void addComponents()
    {
        add(Box.createRigidArea(new Dimension(192, 5)));
        add(menuPanel);
        add(panelTexture);
        add(panelUV);
        add(sliderPanel);
        add(panelProperties);
    }

    @Override
    public void updateValues(Element cube)
    {
        if(cube != null)
        {
            menuList.setSelectedItem(model.getElementAt(cube.getSelectedFaceIndex()));
            menuList.repaint();
            rotation.setEnabled(true);
            rotation.setValue(cube.getSelectedFace().getRotation());
        }
        else
        {
            rotation.setEnabled(false);
            rotation.setValue(0);
        }
        panelUV.updateValues(cube);
        panelProperties.updateValues(cube);
    }
}
