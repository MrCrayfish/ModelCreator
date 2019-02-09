package com.mrcrayfish.modelcreator.panels;

import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.Settings;
import com.mrcrayfish.modelcreator.StateManager;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;

public class FaceExtrasPanel extends JPanel implements IElementUpdater
{
    private ElementManager manager;

    private JPanel horizontalBox;
    private JRadioButton boxCullFace;
    private JRadioButton boxFill;
    private JRadioButton boxEnabled;
    private JRadioButton boxAutoUV;
    private JCheckBox checkBoxTintIndex;
    private JSpinner tintIndexSpinner;


    public FaceExtrasPanel(ElementManager manager)
    {
        this.manager = manager;
        this.setBackground(ModelCreator.BACKGROUND);
        this.setLayout(new BorderLayout(0, 5));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ModelCreator.BACKGROUND, 5), "<html><b>Extras</b></html>"));
        this.setMaximumSize(new Dimension(186, 120));
        this.initComponents();
        this.addComponents();
    }

    private void initComponents()
    {
        horizontalBox = new JPanel(new GridLayout(3, 2));
        boxCullFace = ComponentUtil.createRadioButton("Cullface", "<html>Should render face is another block is adjacent<br>Default: Off</html>");
        boxCullFace.setBackground(ModelCreator.BACKGROUND);
        boxCullFace.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                selectedElement.getSelectedFace().setCullface(boxCullFace.isSelected());
                StateManager.pushState(manager);
            }
        });
        boxFill = ComponentUtil.createRadioButton("Fill", "<html>Makes the texture fill the face<br>Default: Off</html>");
        boxFill.setBackground(ModelCreator.BACKGROUND);
        boxFill.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                selectedElement.getSelectedFace().fitTexture(boxFill.isSelected());
                StateManager.pushState(manager);
            }
        });
        boxEnabled = ComponentUtil.createRadioButton("Enable", "<html>Determines if face should be rendered<br>Default: On</html>");
        boxEnabled.setBackground(ModelCreator.BACKGROUND);
        boxEnabled.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                selectedElement.getSelectedFace().setEnabled(boxEnabled.isSelected());
                StateManager.pushState(manager);
            }
        });
        boxAutoUV = ComponentUtil.createRadioButton("Auto UV", "<html>Determines if UV end coordinates should be set based on element size<br>Default: On</html>");
        boxAutoUV.setBackground(ModelCreator.BACKGROUND);
        boxAutoUV.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                selectedElement.getSelectedFace().setAutoUVEnabled(boxAutoUV.isSelected());
                selectedElement.getSelectedFace().updateEndUV();
                manager.updateValues();
                StateManager.pushState(manager);
            }
        });
        horizontalBox.add(boxCullFace);
        horizontalBox.add(boxFill);
        horizontalBox.add(boxEnabled);
        horizontalBox.add(boxAutoUV);

        checkBoxTintIndex = ComponentUtil.createCheckBox("Tint Index", "The tint index to apply to this face. Used in gam", false);
        checkBoxTintIndex.setBackground(ModelCreator.BACKGROUND);
        checkBoxTintIndex.setEnabled(false);
        horizontalBox.add(checkBoxTintIndex);

        SpinnerNumberModel numberModel = new SpinnerNumberModel();
        numberModel.setMinimum(0);
        tintIndexSpinner = new JSpinner(numberModel);
        tintIndexSpinner.setBackground(ModelCreator.BACKGROUND);
        tintIndexSpinner.setEnabled(false);
        tintIndexSpinner.setPreferredSize(new Dimension(75, 24));
        tintIndexSpinner.setValue(0);
        tintIndexSpinner.addChangeListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                selectedElement.getSelectedFace().setTintIndex((int) tintIndexSpinner.getValue());
                StateManager.pushState(manager);
            }
        });
        horizontalBox.add(tintIndexSpinner);

        checkBoxTintIndex.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                boolean selected = checkBoxTintIndex.isSelected();
                selectedElement.getSelectedFace().setTintIndexEnabled(selected);
                tintIndexSpinner.setEnabled(selected);
                StateManager.pushState(manager);
            }
        });
    }

    private void addComponents()
    {
        add(horizontalBox, BorderLayout.NORTH);
    }

    @Override
    public void updateValues(Element cube)
    {
        if(cube != null)
        {
            boxCullFace.setEnabled(true);
            boxCullFace.setSelected(cube.getSelectedFace().isCullfaced());
            boxFill.setEnabled(true);
            boxFill.setSelected(cube.getSelectedFace().shouldFitTexture());
            boxEnabled.setEnabled(true);
            boxEnabled.setSelected(cube.getSelectedFace().isEnabled());
            boxAutoUV.setEnabled(true);
            boxAutoUV.setSelected(cube.getSelectedFace().isAutoUVEnabled());
            checkBoxTintIndex.setEnabled(true);
            checkBoxTintIndex.setSelected(cube.getSelectedFace().isTintIndexEnabled());
            if(cube.getSelectedFace().isTintIndexEnabled())
            {
                tintIndexSpinner.setEnabled(true);
                tintIndexSpinner.setValue(cube.getSelectedFace().getTintIndex());
            }
            else
            {
                tintIndexSpinner.setEnabled(false);
            }
        }
        else
        {
            boxCullFace.setEnabled(false);
            boxCullFace.setSelected(false);
            boxFill.setEnabled(false);
            boxFill.setSelected(false);
            boxEnabled.setEnabled(false);
            boxEnabled.setSelected(false);
            boxAutoUV.setEnabled(false);
            boxAutoUV.setSelected(false);
            checkBoxTintIndex.setEnabled(false);
            checkBoxTintIndex.setSelected(false);
            tintIndexSpinner.setEnabled(false);
            tintIndexSpinner.setValue(0);
        }
    }
}
