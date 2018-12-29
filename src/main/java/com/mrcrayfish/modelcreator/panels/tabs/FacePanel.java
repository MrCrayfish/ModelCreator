package com.mrcrayfish.modelcreator.panels.tabs;

import com.mrcrayfish.modelcreator.StateManager;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.panels.FaceExtrasPanel;
import com.mrcrayfish.modelcreator.panels.IElementUpdater;
import com.mrcrayfish.modelcreator.panels.TexturePanel;
import com.mrcrayfish.modelcreator.panels.UVPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    private JPanel panelModId;
    private JTextField modidField;

    private final int ROTATION_MIN = 0;
    private final int ROTATION_MAX = 3;
    private final int ROTATION_INIT = 0;

    private DefaultComboBoxModel<String> model;

    public FacePanel(ElementManager manager)
    {
        this.manager = manager;
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
        menuPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Side</b></html>"));
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
        menuPanel.setPreferredSize(new Dimension(186, 50));
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
        sliderPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Rotation</b></html>"));
        rotation = new JSlider(JSlider.HORIZONTAL, ROTATION_MIN, ROTATION_MAX, ROTATION_INIT);
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

        panelModId = new JPanel(new GridLayout(1, 1));
        panelModId.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Location</b></html>"));
        modidField = new JTextField();
        modidField.setSize(new Dimension(190, 30));
        modidField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Element selectedElement = manager.getSelectedElement();
                    if(selectedElement != null)
                    {
                        selectedElement.getSelectedFace().setTextureLocation(modidField.getText());
                        StateManager.pushState(manager);
                    }
                }
            }
        });
        modidField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                Element selectedElement = manager.getSelectedElement();
                if(selectedElement != null)
                {
                    selectedElement.getSelectedFace().setTextureLocation(modidField.getText());
                }
            }
        });
        modidField.setToolTipText("<html>The specific location of the texture. If you have the<br>" + "texture in a sub folder, write the custom directory<br>" + "here. Can include Mod ID prefix.<br>Default: 'blocks/'</html>");
        panelModId.add(modidField);
    }

    private void addComponents()
    {
        add(Box.createRigidArea(new Dimension(192, 5)));
        add(menuPanel);
        add(panelTexture);
        add(panelUV);
        add(sliderPanel);
        add(panelModId);
        add(panelProperties);
    }

    @Override
    public void updateValues(Element cube)
    {
        if(cube != null)
        {
            menuList.setSelectedIndex(cube.getSelectedFaceIndex());
            modidField.setEnabled(true);
            modidField.setText(cube.getSelectedFace().getTextureLocation());
            rotation.setEnabled(true);
            rotation.setValue(cube.getSelectedFace().getRotation());
        }
        else
        {
            modidField.setEnabled(false);
            modidField.setText("");
            rotation.setEnabled(false);
            rotation.setValue(0);
        }
        panelUV.updateValues(cube);
        panelProperties.updateValues(cube);
    }
}
