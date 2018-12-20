package com.mrcrayfish.modelcreator.panels;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.mrcrayfish.modelcreator.*;
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
	private DefaultListModel<ElementEntry> model = new DefaultListModel<>();
	private JList<ElementEntry> list = new JElementList();
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
		btnAdd.addActionListener(e -> this.newElement());
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
				model.addElement(new ElementEntry(new Element(model.getElementAt(selected).element)));
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

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFixedCellHeight(26);
		list.setModel(model);
		list.addListSelectionListener(e ->
		{
			Element selectedElement = getSelectedElement();
			if (selectedElement != null)
			{
				tabbedPane.updateValues();
				name.setEnabled(true);
				name.setText(selectedElement.getName());
				list.ensureIndexIsVisible(list.getSelectedIndex());
			}
		});
		list.setCellRenderer(new ElementCellRenderer());

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
			return model.getElementAt(i).element;
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
			list.add(model.getElementAt(i).element);
		}
		return list;
	}

	@Override
	public Element getElement(int index)
	{
		return model.getElementAt(index).element;
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
			list.repaint();
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
		model.addElement(new ElementEntry(e));
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
			this.model.addElement(new ElementEntry(new Element(element)));
		}
		this.setSelectedElement(state.getSelectedIndex());
		this.ambientOcc = state.isAmbientOcclusion();
		this.particle = state.getParticleTexture();
		this.updateValues();
	}

	public void newElement()
	{
		model.addElement(new ElementEntry(new Element(1, 1, 1)));
		list.setSelectedIndex(model.size() - 1);
		StateManager.pushState(creator.getElementManager());
	}

	private Rectangle expandRectangle(Rectangle r, int amount)
	{
		return new Rectangle(r.x - amount, r.y - amount, r.width + amount * 2, r.height + amount * 2);
	}

	public static class ElementEntry
	{
		private Element element;
		private JPanel panel;
		private JLabel visibility;
		private JLabel name;

		public ElementEntry(Element element)
		{
			this.element = element;
			this.createPanel();
		}

		private void createPanel()
		{
			panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.LEFT));

			visibility = new JLabel();
			visibility.setIcon(element.isVisible() ? Icons.light_on : Icons.light_off);
			panel.add(visibility);

			name = new JLabel(element.getName());
			panel.add(name);
		}

		public Element getElement()
		{
			return element;
		}

		public JPanel getPanel()
		{
			return panel;
		}

		public JLabel getVisibility()
		{
			return visibility;
		}

		public JLabel getName()
		{
			return name;
		}

		public void toggleVisibility()
		{
			element.setVisible(!element.isVisible());
			visibility.setIcon(element.isVisible() ? Icons.light_on : Icons.light_off);
		}
	}
}
