package com.mrcrayfish.modelcreator.texture;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.mrcrayfish.modelcreator.CuboidManager;
import com.mrcrayfish.modelcreator.panels.SidebarPanel;

public class TextureManager
{
	private static Map<String, Texture> textureCache = new HashMap<String, Texture>();

	public static Texture cobblestone;
	public static Texture dirt;

	public static void init()
	{
		try
		{
			loadTexture("brick");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static Texture loadTexture(String name) throws IOException
	{
		FileInputStream is = new FileInputStream(new File("res/" + name + ".png"));
		Texture texture = TextureLoader.getTexture("PNG", is);
		textureCache.put(name, texture);
		return texture;
	}

	public static synchronized void putTexture(String name, Texture texture)
	{
		textureCache.put(name, texture);
	}

	public static synchronized Texture getTexture(String name)
	{
		return textureCache.get(name);
	}

	public static final int SELECT = 0;
	public static final int BROWSE = 1;
	public static final int DELETE = 2;
	public static final int CLOSE = 3;

	private static int option = CLOSE;

	public static int display(CuboidManager manager)
	{
		JButton btnSelect = new JButton("Select");

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
					manager.addPendingTexture(new PendingTexture(chooser.getSelectedFile().getAbsolutePath(), manager));
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(a ->{
			option = CLOSE;
			SwingUtilities.getWindowAncestor(btnClose).dispose();
		});

		JDialog dialog = new JDialog(((SidebarPanel) manager).getCreator(), "Texture Manager", false);
		dialog.setLayout(new BorderLayout());
		dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		dialog.setPreferredSize(new Dimension(640, 480));
		dialog.add(btnSelect, BorderLayout.WEST);
		dialog.add(btnImport, BorderLayout.CENTER);
		dialog.add(btnClose, BorderLayout.EAST);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);

		return option;
	}
}
