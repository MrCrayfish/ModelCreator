package com.mrcrayfish.modelcreator.panels;

import com.mrcrayfish.modelcreator.Icons;
import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.StateManager;
import com.mrcrayfish.modelcreator.component.TextureManager;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.texture.TextureEntry;
import com.mrcrayfish.modelcreator.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;

public class GlobalPanel extends JPanel implements IElementUpdater
{
    private ElementManager manager;

    private JRadioButton ambientOcc;
    private JButton btnParticle;

    public GlobalPanel(ElementManager manager)
    {
        this.manager = manager;
        this.setBackground(ModelCreator.BACKGROUND);
        this.setLayout(new GridLayout(2, 1, 0, 5));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ModelCreator.BACKGROUND, 5), "<html><b>Global Properties</b></html>"));
        this.setMaximumSize(new Dimension(186, 80));
        this.initComponents();
        this.addComponents();
    }

    private void initComponents()
    {
        ambientOcc = ComponentUtil.createRadioButton("Ambient Occlusion", "Determine the light for each element");
        ambientOcc.setBackground(ModelCreator.BACKGROUND);
        ambientOcc.setSelected(true);
        ambientOcc.addActionListener(a ->
        {
            manager.setAmbientOcc(ambientOcc.isSelected());
            StateManager.pushState(manager);
        });

        btnParticle = new JButton("Particle");
        btnParticle.setIcon(Icons.texture);
        btnParticle.addActionListener(a ->
        {
            TextureEntry entry = TextureManager.display(((SidebarPanel) manager).getCreator(), manager, Dialog.ModalityType.APPLICATION_MODAL);
            if(entry != null)
            {
                manager.setParticle(entry);
                btnParticle.setText("#" + entry.getKey());
                StateManager.pushState(manager);
            }
        });
    }

    private void addComponents()
    {
        add(ambientOcc);
        add(btnParticle);
    }

    @Override
    public void updateValues(Element cube)
    {
        ambientOcc.setSelected(manager.getAmbientOcc());
        if(manager.getParticle() == null)
        {
            btnParticle.setText("Particle");
        }
        else
        {
            btnParticle.setText("#" + manager.getParticle().getKey());
        }
    }
}
