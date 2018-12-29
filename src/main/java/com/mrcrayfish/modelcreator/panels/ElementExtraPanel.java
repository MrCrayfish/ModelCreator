package com.mrcrayfish.modelcreator.panels;

import com.mrcrayfish.modelcreator.StateManager;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;

public class ElementExtraPanel extends JPanel implements IElementUpdater
{
    private ElementManager manager;

    private JRadioButton btnShade;

    public ElementExtraPanel(ElementManager manager)
    {
        this.manager = manager;
        this.setLayout(new GridLayout(1, 2));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(221, 221, 228), 5), "<html><b>Extras</b></html>"));
        this.setMaximumSize(new Dimension(186, 50));
        this.initComponents();
        this.addComponents();
    }

    private void initComponents()
    {
        btnShade = ComponentUtil.createRadioButton("Shade", "<html>Determines if shadows should be rendered<br>Default: On</html>");
        btnShade.addActionListener(e ->
        {
            Element selectedElement = manager.getSelectedElement();
            if(selectedElement != null)
            {
                selectedElement.setShade(btnShade.isSelected());
                StateManager.pushState(manager);
            }
        });
    }

    private void addComponents()
    {
        add(btnShade);
    }

    @Override
    public void updateValues(Element cube)
    {
        if(cube != null)
        {
            btnShade.setEnabled(true);
            btnShade.setSelected(cube.isShaded());
        }
        else
        {
            btnShade.setEnabled(false);
            btnShade.setSelected(false);
        }
    }
}
