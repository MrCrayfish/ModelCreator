package com.mrcrayfish.modelcreator.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.mrcrayfish.modelcreator.Icons;
import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.StateManager;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.ElementManagerState;
import com.mrcrayfish.modelcreator.panels.tabs.ElementPanel;
import com.mrcrayfish.modelcreator.panels.tabs.FacePanel;
import com.mrcrayfish.modelcreator.panels.tabs.RotationPanel;
import com.mrcrayfish.modelcreator.texture.PendingTexture;

public class SidebarPanel extends JPanel implements ElementManager
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

	// Swing Variables
	private SpringLayout layout;
	private DefaultListModel<Element> model = new DefaultListModel<Element>();
	private JList<Element> list = new JList<Element>();
	private JScrollPane scrollPane;
	private JPanel btnContainer;
	private JButton btnAdd = new JButton();
	private JButton btnRemove = new JButton();
	private JButton btnDuplicate = new JButton();
	private JTextField name = new JTextField();
	private CuboidTabbedPane tabbedPane = new CuboidTabbedPane(this);

	private String particle = null;
	private boolean ambientOcc = true;

	public SidebarPanel(ModelCreator creator)
	{
		this.creator = creator;
		this.setLayout(layout = new SpringLayout());
		this.setPreferredSize(new Dimension(200, 760));
		this.initComponents();
		this.setLayoutConstaints();
	}

	public void initComponents()
	{
		Font defaultFont = new Font("SansSerif", Font.BOLD, 14);

		btnContainer = new JPanel(new GridLayout(1, 3, 4, 0));
		btnContainer.setPreferredSize(new Dimension(190, 30));

		btnAdd.setIcon(Icons.cube);
		btnAdd.setToolTipText("New Element");
		btnAdd.addActionListener(e ->
		{
			model.addElement(new Element(1, 1, 1));
			list.setSelectedIndex(model.size() - 1);
			StateManager.pushState(creator.getElementManager());
		});
		btnAdd.setPreferredSize(new Dimension(30, 30));
		btnContainer.add(btnAdd);

		btnRemove.setIcon(Icons.bin);
		btnRemove.setToolTipText("Remove Element");
		btnRemove.addActionListener(e ->
		{
			int selected = list.getSelectedIndex();
			if (selected != -1)
			{
				model.remove(selected);
				name.setText("");
				name.setEnabled(false);
				tabbedPane.updateValues();
				list.setSelectedIndex(selected);
				StateManager.pushState(creator.getElementManager());
			}
		});
		btnRemove.setPreferredSize(new Dimension(30, 30));
		btnContainer.add(btnRemove);

		btnDuplicate.setIcon(Icons.copy);
		btnDuplicate.setToolTipText("Duplicate Element");
		btnDuplicate.addActionListener(e ->
		{
			int selected = list.getSelectedIndex();
			if (selected != -1)
			{
				model.addElement(new Element(model.getElementAt(selected)));
				list.setSelectedIndex(model.getSize() - 1);
				StateManager.pushState(creator.getElementManager());
			}
		});
		btnDuplicate.setFont(defaultFont);
		btnDuplicate.setPreferredSize(new Dimension(30, 30));
		btnContainer.add(btnDuplicate);
		add(btnContainer);

		name.setPreferredSize(new Dimension(190, 30));
		name.setToolTipText("Element Name");
		name.setEnabled(false);
		name.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					updateName();
				}
			}
		});
		name.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				updateName();
			}
		});
		add(name);

		list.setModel(model);
		list.addListSelectionListener(e ->
		{
			Element selectedElement = getSelectedElement();
			if (selectedElement != null)
			{
				tabbedPane.updateValues();
				name.setEnabled(true);
				name.setText(selectedElement.getName());
			}
		});

		scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(190, 170));
		add(scrollPane);

		tabbedPane.setBackground(new Color(127, 132, 145));
		tabbedPane.setForeground(Color.WHITE);
		tabbedPane.add("Element", new ElementPanel(this));
		tabbedPane.add("Rotation", new RotationPanel(this));
		tabbedPane.add("Faces", new FacePanel(this));
		tabbedPane.setPreferredSize(new Dimension(190, 500));
		tabbedPane.setTabPlacement(JTabbedPane.TOP);
		tabbedPane.addChangeListener(c ->
		{
			if (tabbedPane.getSelectedIndex() == 2)
			{
				creator.setSidebar(ModelCreator.uvSidebar);
			}
			else
			{
				creator.setSidebar(null);
			}
		});
		add(tabbedPane);
	}

	public void setLayoutConstaints()
	{
		layout.putConstraint(SpringLayout.NORTH, name, 212, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, btnContainer, 176, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, tabbedPane, 250, SpringLayout.NORTH, this);
	}

	@Override
	public Element getSelectedElement()
	{
		int i = list.getSelectedIndex();
		if (model.getSize() > 0 && i >= 0 && i < model.getSize())
			return model.getElementAt(i);
		return null;
	}

	@Override
	public void setSelectedElement(int pos)
	{
		if (pos < model.size())
		{
			if (pos >= 0)
			{
				list.setSelectedIndex(pos);
			}
			else
			{
				list.clearSelection();
			}
			updateValues();
		}
	}

	@Override
	public List<Element> getAllElements()
	{
		List<Element> list = new ArrayList<Element>();
		for (int i = 0; i < model.size(); i++)
		{
			list.add(model.getElementAt(i));
		}
		return list;
	}

	@Override
	public Element getElement(int index)
	{
		return model.getElementAt(index);
	}

	@Override
	public int getElementCount()
	{
		return model.size();
	}

	@Override
	public void updateName()
	{
		String newName = name.getText();
		if (newName.isEmpty())
			newName = "Cuboid";
		Element selectedElement = getSelectedElement();
		if (selectedElement != null)
		{
			selectedElement.setName(newName);
			name.setText(newName);
			list.updateUI();
			StateManager.pushState(creator.getElementManager());
		}
	}

	@Override
	public void updateValues()
	{
		tabbedPane.updateValues();
	}

	@Override
	public void addPendingTexture(PendingTexture texture)
	{
		creator.pendingTextures.add(texture);
	}

	public ModelCreator getCreator()
	{
		return creator;
	}

	@Override
	public boolean getAmbientOcc()
	{
		return ambientOcc;
	}

	@Override
	public void setAmbientOcc(boolean occ)
	{
		ambientOcc = occ;
	}

	@Override
	public void clearElements()
	{
		model.clear();
	}

	@Override
	public void addElement(Element e)
	{
		model.addElement(e);
	}

	@Override
	public void setParticle(String texture)
	{
		this.particle = texture;
	}

	@Override
	public String getParticle()
	{
		return particle;
	}

	@Override
	public void reset()
	{
		this.clearElements();
		ambientOcc = true;
		particle = null;
	}

	@Override
	public void restoreState(ElementManagerState state)
	{
		this.reset();
		for(Element element : state.getElements())
		{
			this.model.addElement(new Element(element));
		}
		this.setSelectedElement(state.getSelectedIndex());
		this.ambientOcc = state.isAmbientOcclusion();
		this.particle = state.getParticleTexture();
		this.updateValues();
	}
}
