package com.mrcrayfish.modelcreator.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.mrcrayfish.modelcreator.Cuboid;
import com.mrcrayfish.modelcreator.CuboidManager;
import com.mrcrayfish.modelcreator.panels.tabs.ElementPanel;
import com.mrcrayfish.modelcreator.panels.tabs.FacePanel;
import com.mrcrayfish.modelcreator.panels.tabs.RotationPanel;
import com.mrcrayfish.modelcreator.texture.PendingTexture;

public class SidebarPanel extends JPanel implements CuboidManager
{
	private static final long serialVersionUID = 1L;

	private SpringLayout layout;

	private DefaultListModel<Cuboid> model = new DefaultListModel<Cuboid>();
	private JList<Cuboid> list = new JList<Cuboid>();
	private JScrollPane scrollPane;
	private JButton btnAdd = new JButton("Add");
	private JButton btnRemove = new JButton("Remove");
	private JTextField name = new JTextField();
	private CuboidTabbedPane tabbedPane = new CuboidTabbedPane(this);

	public SidebarPanel()
	{
		setLayout(layout = new SpringLayout());
		setPreferredSize(new Dimension(200, 800));
		initComponents();
	}

	public void initComponents()
	{
		layout = new SpringLayout();

		Font defaultFont = new Font("SansSerif", Font.BOLD, 14);
		btnAdd.addActionListener(e ->
		{
			model.addElement(new Cuboid(1, 1, 1));
			list.setSelectedIndex(model.size() - 1);
		});
		btnAdd.setPreferredSize(new Dimension(90, 30));
		btnAdd.setFont(defaultFont);
		add(btnAdd);

		btnRemove.addActionListener(e ->
		{
			int selected = list.getSelectedIndex();
			if (selected != -1)
			{
				model.remove(selected);
				name.setText("");
				name.setEnabled(false);
				tabbedPane.updateValues();
			}
		});
		btnRemove.setFont(defaultFont);
		btnRemove.setPreferredSize(new Dimension(90, 30));
		add(btnRemove);

		name.setPreferredSize(new Dimension(190, 30));
		name.setToolTipText("Placeholder");
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
			Cuboid cube = getSelectedCuboid();
			if (cube != null)
			{
				tabbedPane.updateValues();
				name.setEnabled(true);
				name.setText(cube.toString());
			}
		});

		scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(190, 200));
		add(scrollPane);

		tabbedPane.add("Element", new ElementPanel(this));
		tabbedPane.add("Rotation", new RotationPanel(this));
		tabbedPane.add("Faces", new FacePanel(this));
		tabbedPane.setPreferredSize(new Dimension(190, 385));
		tabbedPane.setTabPlacement(JTabbedPane.TOP);
		add(tabbedPane);
	}

	public void setLayoutConstaints()
	{
		layout.putConstraint(SpringLayout.NORTH, name, 245, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, name, 998, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 998, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, btnAdd, 210, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, btnAdd, 998, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, btnRemove, 210, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, btnRemove, 1097, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, tabbedPane, 281, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, tabbedPane, 998, SpringLayout.WEST, this);
	}

	@Override
	public Cuboid getSelectedCuboid()
	{
		int i = list.getSelectedIndex();
		if (i != -1)
			return (Cuboid) model.getElementAt(i);
		return null;
	}

	@Override
	public List<Cuboid> getAllCuboids()
	{
		List<Cuboid> list = new ArrayList<Cuboid>();
		for (int i = 0; i < model.size(); i++)
		{
			list.add(model.getElementAt(i));
		}
		return list;
	}

	@Override
	public Cuboid getCuboid(int index)
	{
		return model.getElementAt(index);
	}

	@Override
	public int getCuboidCount()
	{
		return model.size();
	}

	@Override
	public void updateName()
	{
		String newName = name.getText();
		if (newName.isEmpty())
			newName = "Cuboid";
		Cuboid cube = getSelectedCuboid();
		if (cube != null)
		{
			cube.setName(newName);
			name.setText(newName);
			list.updateUI();
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
		// TODO Auto-generated method stub

	}
}
