package com.mrcrayfish.modelcreator.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.mrcrayfish.modelcreator.StateManager;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.util.ComponentUtil;

public class ElementExtraPanel extends JPanel implements IValueUpdater
{
	private static final long serialVersionUID = 1L;

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

	public void initComponents()
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

	public void addComponents()
	{
		add(btnShade);
	}

	@Override
	public void updateValues(Element cube)
	{
		if (cube != null)
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
