package com.mrcrayfish.modelcreator.texture;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.panels.SidebarPanel;

public class TextureManager
{
	private static List<TextureEntry> textureCache = new ArrayList<TextureEntry>();

	public static Texture cobblestone;
	public static Texture dirt;

	public static File lastLocation = null;

	public static boolean loadExternalTexture(String path, String fileName, File metaFile) throws IOException
	{
		/*FileInputStream is = new FileInputStream(new File(path + "/" + fileName));
		Texture texture = TextureLoader.getTexture("PNG", is);
		is.close();
		if (texture.getImageHeight() % 16 != 0 | texture.getImageWidth() % 16 != 0)
		{
			texture.release();
			return false;
		}
		ImageIcon image = upscale(new ImageIcon(path + "/" + fileName));
		textureCache.add(new TextureEntry(fileName.replace(".png", ""), texture, image));*/


		File file = new File(path + File.separator + fileName);
		if(file.exists()) {
			BufferedImage bimage = ImageIO.read(file);

			JsonObject animation = null;
			JsonObject textureObj = null;
			File mcMetaFile = metaFile;
			if(mcMetaFile==null || !mcMetaFile.exists()) {
				mcMetaFile = new File(path + File.separator + fileName + ".mcmeta");
			}
			if(mcMetaFile.exists()) {
				JsonParser parser = new JsonParser();
				JsonElement read = parser.parse(new FileReader(mcMetaFile));

				if(read.isJsonObject()) {
					JsonObject mcMeta = read.getAsJsonObject();
					if(mcMeta.has("animation") && mcMeta.get("animation").isJsonObject()) {
						animation = mcMeta.get("animation").getAsJsonObject();
					}
					if(mcMeta.has("texture") && mcMeta.get("texture").isJsonObject()) {
						textureObj = mcMeta.get("texture").getAsJsonObject();
					}
				}
			}

			TextureEntry entry = null;
			if(animation==null) {
				Texture texture = BufferedImageUtil.getTexture("", bimage);
				ImageIcon image = upscale(new ImageIcon(path + "/" + fileName));

				entry = new TextureEntry(fileName.replace(".png", "").replaceAll("\\d*$", ""), texture, image, path + "/" + fileName);
			} else {
				entry = new TextureEntry(fileName.replace(".png", "").replaceAll("\\d*$", ""), path + "/" + fileName);

				//Split animation frames
				int fWidth = 16;
				int fHeight = 16;
				if(animation.has("width") && animation.get("width").isJsonPrimitive()) {
					fWidth = animation.get("width").getAsInt();
				}
				if(animation.has("height") && animation.get("height").isJsonPrimitive()) {
					fHeight = animation.get("height").getAsInt();
				}
				int xpos = 0;
				while(xpos + fWidth <= bimage.getWidth()) {
					int ypos = 0;
					while(ypos + fHeight <= bimage.getHeight()) {
						BufferedImage subImage = bimage.getSubimage(xpos, ypos, fWidth, fHeight);
						ImageIcon iconImage = upscale(new ImageIcon(subImage));
						Texture texture = BufferedImageUtil.getTexture("", subImage);

						entry.addTexture(texture, iconImage);

						ypos += fHeight;
					}

					xpos += fWidth;
				}

				//set variables
				int frametime = 1;
				if(animation.has("frametime") && animation.get("frametime").isJsonPrimitive()) {
					frametime = animation.get("frametime").getAsInt();
				}
				entry.setFrameTime(frametime);

				if(animation.has("interpolate") && animation.get("interpolate").isJsonPrimitive()) {
					boolean interpolate = animation.get("interpolate").getAsBoolean();
					entry.setInterpolate(interpolate);
				}

				//set frames
				if(animation.has("frames") && animation.get("frames").isJsonArray()) {
					JsonArray frames = animation.get("frames").getAsJsonArray();
					if(frames.size()>0) {
						List<Integer> frameList = new ArrayList<Integer>();
						Map<Integer, Integer> customTimes = new HashMap<Integer, Integer>();

						for(int i=0; i<frames.size(); i++) {
							JsonElement frame = frames.get(i);

							int index = 0;
							int time = frametime;
							if(frame.isJsonPrimitive()) {
								index = frame.getAsInt();
							} else if(frame.isJsonObject()) {
								JsonObject frameObj = frame.getAsJsonObject();

								if(frameObj.has("index") && frameObj.get("index").isJsonPrimitive()) {
									index = frameObj.get("index").getAsInt();
								}
								if(frameObj.has("time") && frameObj.get("time").isJsonPrimitive()) {
									time = frameObj.get("time").getAsInt();
								}
							}

							frameList.add(index);
							if(time!=frametime) {
								customTimes.put(frameList.size()-1, time);
							}
						}

						entry.setFrames(frameList);
						entry.setCustomTimes(customTimes);
					}
				}
			}

			//texture vars
			if(textureObj!=null) {
				if(textureObj.has("blur") && textureObj.get("blur").isJsonPrimitive()) {
					boolean blur = textureObj.get("blur").getAsBoolean();
					entry.setBlurred(blur);
				}
			}

			if(entry!=null) {
				textureCache.add(entry);
			}
		}
		return true;
	}

	public static ImageIcon upscale(ImageIcon source)
	{
		Image img = source.getImage();
		Image newimg = img.getScaledInstance(256, 256, java.awt.Image.SCALE_FAST);
		return new ImageIcon(newimg);
	}

	public static TextureEntry getTextureEntry(String name)
	{
		for (TextureEntry entry : textureCache)
		{
			if (entry.getName().equalsIgnoreCase(name))
			{
				return entry;
			}
		}
		return null;
	}

	public static Texture getTexture(String name)
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

	public static String getTextureLocation(String name)
	{
		for (TextureEntry entry : textureCache)
		{
			if (entry.getName().equalsIgnoreCase(name))
			{
				return entry.getLocation();
			}
		}
		return null;
	}

	public static ImageIcon getIcon(String name)
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

	public static String display(ElementManager manager)
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
		JButton btnSelect = new JButton("Apply");
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

		JButton btnImport = new JButton("Import");
		btnImport.addActionListener(a ->
		{
			JFileChooser chooser = new JFileChooser();
			if (lastLocation != null)
				chooser.setCurrentDirectory(lastLocation);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				lastLocation = chooser.getSelectedFile().getParentFile();
				try
				{
					manager.addPendingTexture(new PendingTexture(chooser.getSelectedFile().getAbsolutePath(), null, new TextureCallback()
					{
						@Override
						public void callback(boolean success, String texture)
						{
							if (success)
							{
								model.insertElementAt(texture.replace(".png", ""), 0);
							}
							else
							{
								JOptionPane error = new JOptionPane();
								error.setMessage("Width and height must be a multiple of 16.");
								JDialog dialog = error.createDialog(btnImport, "Texture Error");
								dialog.setLocationRelativeTo(null);
								dialog.setModal(false);
								dialog.setVisible(true);
							}
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
