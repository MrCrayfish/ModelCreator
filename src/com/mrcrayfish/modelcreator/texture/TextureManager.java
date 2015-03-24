package com.mrcrayfish.modelcreator.texture;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.mrcrayfish.modelcreator.CuboidManager;
import com.mrcrayfish.modelcreator.panels.SidebarPanel;

public class TextureManager
{
	private static List<TextureEntry> textureCache = new ArrayList<TextureEntry>();

	public static Texture cobblestone;
	public static Texture dirt;

	public static void init()
	{
		try
		{
			loadTexture("brick");
			loadTexture("dirt");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void loadTexture(String name) throws IOException
	{
		loadTexture("res", name + ".png");
	}

	public static boolean loadTexture(String path, String name) throws IOException
	{
		System.out.println(path + " " + name);
		FileInputStream is = new FileInputStream(new File(path + "/" + name));
		Texture texture = TextureLoader.getTexture("PNG", is);
		if(texture.getImageHeight() % 16 != 0 | texture.getImageWidth() % 16 != 0)
		{
			texture.release();
			System.out.println("Texture has to be a multiple of 16");
			return false;
		}
		ImageIcon icon = upscale(new ImageIcon(path + "/" + name));
		System.out.println(name.replace(".png", ""));
		textureCache.add(new TextureEntry(name.replace(".png", ""), texture, icon));
		return true;
	}

	public static ImageIcon upscale(ImageIcon source)
	{
		Image img = source.getImage();
		Image newimg = img.getScaledInstance(256, 256, java.awt.Image.SCALE_FAST);
		return new ImageIcon(newimg);
	}

	public static synchronized Texture getTexture(String name)
	{
		for (TextureEntry entry : textureCache)
		{
			if (entry.getName().equalsIgnoreCase(name))
			{
				return entry.getTexture();
			}
		}
		return null;
	}

	public static synchronized ImageIcon getIcon(String name)
	{
		for (TextureEntry entry : textureCache)
		{
			if (entry.getName().equalsIgnoreCase(name))
			{
				return entry.getImage();
			}
		}
		return null;
	}

	private static String texture = null;

	public static String display(CuboidManager manager)
	{
		Font defaultFont = new Font("SansSerif", Font.BOLD, 18);
		
		DefaultListModel<String> model = generate();
		JList<String> list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setCellRenderer(new TextureCellRenderer());
		list.setVisibleRowCount(-1);
		list.setModel(model);
		list.setFixedCellHeight(256);
		list.setFixedCellWidth(256);
		list.setBackground(new Color(221, 221, 228));
		JScrollPane scroll = new JScrollPane(list);
		scroll.getVerticalScrollBar().setVisible(false);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel panel = new JPanel(new GridLayout(1, 3));
		panel.setPreferredSize(new Dimension(1000, 40));
		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(a ->
		{
			if (list.getSelectedValue() != null)
			{
				texture = list.getSelectedValue();
				SwingUtilities.getWindowAncestor(btnSelect).dispose();
			}
		});
		btnSelect.setFont(defaultFont);
		panel.add(btnSelect);

		JButton btnImport = new JButton("Browse");
		btnImport.addActionListener(a ->
		{
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog((SidebarPanel) manager);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				try
				{
					manager.addPendingTexture(new PendingTexture(chooser.getSelectedFile().getAbsolutePath(), new TextureCallback()
					{
						@Override
						public void callback(String texture)
						{
							model.insertElementAt(texture, 0);
						}
					}));
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});
		btnImport.setFont(defaultFont);
		panel.add(btnImport);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(a ->
		{
			texture = null;
			SwingUtilities.getWindowAncestor(btnClose).dispose();
		});
		btnClose.setFont(defaultFont);
		panel.add(btnClose);

		JDialog dialog = new JDialog(((SidebarPanel) manager).getCreator(), "Texture Manager", false);
		dialog.setLayout(new BorderLayout());
		dialog.setResizable(false);
		dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		dialog.setPreferredSize(new Dimension(540, 480));
		dialog.add(scroll, BorderLayout.CENTER);
		dialog.add(panel, BorderLayout.SOUTH);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);

		return texture;
	}

	private static DefaultListModel<String> generate()
	{
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (TextureEntry entry : textureCache)
		{
			model.addElement(entry.getName());
		}
		return model;
	}
}
