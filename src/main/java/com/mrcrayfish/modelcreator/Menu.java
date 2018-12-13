package com.mrcrayfish.modelcreator;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mrcrayfish.modelcreator.screenshot.PendingScreenshot;
import com.mrcrayfish.modelcreator.screenshot.Screenshot;
import com.mrcrayfish.modelcreator.screenshot.ScreenshotCallback;
import com.mrcrayfish.modelcreator.screenshot.Uploader;
import com.mrcrayfish.modelcreator.util.Util;

public class Menu extends JMenuBar
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

	/* File */
	private JMenu menuFile;
	private JMenuItem itemNew;
	private JMenuItem itemLoad;
	private JMenuItem itemSave;
	private JMenuItem itemImport;
	private JMenuItem itemExport;
	private JMenuItem itemTexturePath;
	private JMenuItem itemExit;

	/* Options */
	private JMenu menuOptions;
	private JMenuItem itemTransparency;

	/* Share */
	private JMenu menuScreenshot;
	private JMenuItem itemSaveToDisk;
	private JMenuItem itemShareFacebook;
	private JMenuItem itemShareTwitter;
	private JMenuItem itemShareReddit;
	private JMenuItem itemImgurLink;

	/* Extras */
	private JMenu menuHelp;
	private JMenu menuExamples;
	private JMenuItem itemModelCauldron;
	private JMenuItem itemModelChair;
	private JMenuItem itemDonate;
	private JMenuItem itemPM;
	private JMenuItem itemMF;
	private JMenuItem itemGitHub;

	public Menu(ModelCreator creator)
	{
		this.creator = creator;
		initMenu();
	}

	private void initMenu()
	{
		menuFile = new JMenu("File");
		{
			itemNew = createItem("New", "New Model", KeyEvent.VK_N, Icons.new_);
			itemLoad = createItem("Load Project...", "Load Project from File", KeyEvent.VK_S, Icons.load);
			itemSave = createItem("Save Project...", "Save Project to File", KeyEvent.VK_S, Icons.disk);
			itemImport = createItem("Import JSON...", "Import Model from JSON", KeyEvent.VK_I, Icons.import_);
			itemExport = createItem("Export JSON...", "Export Model to JSON", KeyEvent.VK_E, Icons.export);
			itemTexturePath = createItem("Set Texture Path...", "Set the base path to look for textures", KeyEvent.VK_S, Icons.texture);
			itemExit = createItem("Exit", "Exit Application", KeyEvent.VK_E, Icons.exit);
		}

		menuOptions = new JMenu("Options");
		{
			itemTransparency = createCheckboxItem("Transparency", "Enables transparent rendering in program", KeyEvent.VK_E, ModelCreator.transparent, Icons.transparent);
		}

		menuScreenshot = new JMenu("Screenshot");
		{
			itemSaveToDisk = createItem("Save to Disk...", "Save screenshot to disk.", KeyEvent.VK_S, Icons.disk);
			itemShareFacebook = createItem("Share to Facebook", "Share a screenshot of your model Facebook.", KeyEvent.VK_S, Icons.facebook);
			itemShareTwitter = createItem("Share to Twitter", "Share a screenshot of your model to Twitter.", KeyEvent.VK_S, Icons.twitter);
			itemShareReddit = createItem("Share to Minecraft Subreddit", "Share a screenshot of your model to Minecraft Reddit.", KeyEvent.VK_S, Icons.reddit);
			itemImgurLink = createItem("Get Imgur Link", "Get an Imgur link of your screenshot to share.", KeyEvent.VK_G, Icons.imgur);
		}

		menuHelp = new JMenu("More");
		{
			menuExamples = new JMenu("Examples");
			menuExamples.setIcon(Icons.new_);
			{
				itemModelCauldron = createItem("Cauldron", "<html>Model by MrCrayfish<br><b>Private use only</b></html>", KeyEvent.VK_C, Icons.model_cauldron);
				itemModelChair = createItem("Chair", "<html>Model by MrCrayfish<br><b>Private use only</b></html>", KeyEvent.VK_C, Icons.model_chair);
			}
			itemDonate = createItem("Donate (Patreon)", "Pledge to MrCrayfish", KeyEvent.VK_D, Icons.patreon);
			itemPM = createItem("Planet Minecraft", "Open PMC Post", KeyEvent.VK_P, Icons.planet_minecraft);
			itemMF = createItem("Minecraft Forum", "Open MF Post", KeyEvent.VK_M, Icons.minecraft_forum);
			itemGitHub = createItem("Github", "View Source Code", KeyEvent.VK_G, Icons.github);
		}

		initActions();

		menuExamples.add(itemModelCauldron);
		menuExamples.add(itemModelChair);

		menuHelp.add(menuExamples);
		menuHelp.addSeparator();
		menuHelp.add(itemPM);
		menuHelp.add(itemMF);
		menuHelp.add(itemGitHub);
		menuHelp.addSeparator();
		menuHelp.add(itemDonate);

		menuOptions.add(itemTransparency);

		menuScreenshot.add(itemSaveToDisk);
		menuScreenshot.add(itemShareFacebook);
		menuScreenshot.add(itemShareTwitter);
		menuScreenshot.add(itemShareReddit);
		menuScreenshot.add(itemImgurLink);

		menuFile.add(itemNew);
		menuFile.addSeparator();
		menuFile.add(itemLoad);
		menuFile.add(itemSave);
		menuFile.addSeparator();
		menuFile.add(itemImport);
		menuFile.add(itemExport);
		menuFile.addSeparator();
		menuFile.add(itemTexturePath);
		menuFile.addSeparator();
		menuFile.add(itemExit);

		add(menuFile);
		add(menuOptions);
		add(menuScreenshot);
		add(menuHelp);
	}

	private void initActions()
	{
		itemNew.addActionListener(a ->
		{
			int returnVal = JOptionPane.showConfirmDialog(creator, "You current work will be cleared, are you sure?", "Note", JOptionPane.YES_NO_OPTION);
			if (returnVal == JOptionPane.YES_OPTION)
			{
				creator.getElementManager().reset();
				creator.getElementManager().updateValues();
			}
		});

		itemLoad.addActionListener(a ->
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Output Directory");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setApproveButtonText("Load");

			FileNameExtensionFilter filter = new FileNameExtensionFilter("Model (.model)", "model");
			chooser.setFileFilter(filter);

			String dir = Settings.getModelDir();

			if (dir != null)
			{
				chooser.setCurrentDirectory(new File(dir));
			}

			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				if (creator.getElementManager().getElementCount() > 0)
				{
					returnVal = JOptionPane.showConfirmDialog(null, "Your current project will be cleared, are you sure you want to continue?", "Warning", JOptionPane.YES_NO_OPTION);
				}
				if (returnVal != JOptionPane.NO_OPTION && returnVal != JOptionPane.CLOSED_OPTION)
				{
					File location = chooser.getSelectedFile().getParentFile();
					Settings.setModelDir(location.toString());

					ProjectManager.loadProject(creator.getElementManager(), chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		itemSave.addActionListener(a ->
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Output Directory");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setApproveButtonText("Save");

			FileNameExtensionFilter filter = new FileNameExtensionFilter("Model (.model)", "model");
			chooser.setFileFilter(filter);
			String dir = Settings.getModelDir();

			if (dir != null)
			{
				chooser.setCurrentDirectory(new File(dir));
			}

			int returnVal = chooser.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				if (chooser.getSelectedFile().exists())
				{
					returnVal = JOptionPane.showConfirmDialog(null, "A file already exists with that name, are you sure you want to override?", "Warning", JOptionPane.YES_NO_OPTION);
				}
				if (returnVal != JOptionPane.NO_OPTION && returnVal != JOptionPane.CLOSED_OPTION)
				{
					File location = chooser.getSelectedFile().getParentFile();
					Settings.setModelDir(location.toString());

					String filePath = chooser.getSelectedFile().getAbsolutePath();
					if (!filePath.endsWith(".model"))
						filePath += ".model";
					ProjectManager.saveProject(creator.getElementManager(), filePath);
				}
			}
		});

		itemImport.addActionListener(e ->
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Input File");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setApproveButtonText("Import");

			FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON (.json)", "json");
			chooser.setFileFilter(filter);

			String dir = Settings.getJSONDir();

			if (dir != null)
			{
				chooser.setCurrentDirectory(new File(dir));
			}

			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				if (creator.getElementManager().getElementCount() > 0)
				{
					returnVal = JOptionPane.showConfirmDialog(null, "Your current project will be cleared, are you sure you want to continue?", "Warning", JOptionPane.YES_NO_OPTION);
				}
				if (returnVal != JOptionPane.NO_OPTION && returnVal != JOptionPane.CLOSED_OPTION)
				{
					File location = chooser.getSelectedFile().getParentFile();
					Settings.setJSONDir(location.toString());

					Importer importer = new Importer(creator.getElementManager(), chooser.getSelectedFile().getAbsolutePath());
					importer.importFromJSON();
				}
				creator.getElementManager().updateValues();
			}
		});

		itemExport.addActionListener(e ->
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Output Directory");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setApproveButtonText("Export");

			FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON (.json)", "json");
			chooser.setFileFilter(filter);

			String dir = Settings.getJSONDir();

			if (dir != null)
			{
				chooser.setCurrentDirectory(new File(dir));
			}

			int returnVal = chooser.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				if (chooser.getSelectedFile().exists())
				{
					returnVal = JOptionPane.showConfirmDialog(null, "A file already exists with that name, are you sure you want to override?", "Warning", JOptionPane.YES_NO_OPTION);
				}
				if (returnVal != JOptionPane.NO_OPTION && returnVal != JOptionPane.CLOSED_OPTION)
				{
					File location = chooser.getSelectedFile().getParentFile();
					Settings.setJSONDir(location.toString());

					String filePath = chooser.getSelectedFile().getAbsolutePath();
					if (!filePath.endsWith(".json"))
						chooser.setSelectedFile(new File(filePath + ".json"));
					Exporter exporter = new Exporter(creator.getElementManager());
					exporter.export(chooser.getSelectedFile());
				}
			}
		});

		itemTexturePath.addActionListener(e ->
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Texture Path");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			if (ModelCreator.texturePath != null)
			{
				chooser.setCurrentDirectory(new File(ModelCreator.texturePath));
			}

			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				ModelCreator.texturePath = chooser.getSelectedFile().getAbsolutePath();
			}
		});

		itemExit.addActionListener(e ->
		{
			creator.close();
		});

		itemTransparency.addActionListener(a ->
		{
			ModelCreator.transparent ^= true;
			Settings.setTransparencyMode(ModelCreator.transparent);
			if (ModelCreator.transparent)
				JOptionPane.showMessageDialog(null, "<html>Enabled transparency mode. Transparent textures do not represent the same as in Minecraft.<br> " + "It depends if the model you are overwriting, allows transparent<br>" + "textures in the code. Blocks like Grass and Stone don't allow<br>" + "transparency, where as Glass and Cauldron do. Please take this into<br>" + "consideration when designing. Transparency is now turned on.<html>", "Rendering Warning", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(null, "<html>Disabled transparency mode</html>", "Transparency mode", JOptionPane.INFORMATION_MESSAGE);
		});

		itemSaveToDisk.addActionListener(a ->
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Output Directory");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setApproveButtonText("Save");

			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG (.png)", "png");
			chooser.setFileFilter(filter);

			String dir = Settings.getScreenshotDir();

			if (dir != null)
			{
				chooser.setCurrentDirectory(new File(dir));
			}

			int returnVal = chooser.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				if (chooser.getSelectedFile().exists())
				{
					returnVal = JOptionPane.showConfirmDialog(null, "A file already exists with that name, are you sure you want to override?", "Warning", JOptionPane.YES_NO_OPTION);
				}
				if (returnVal != JOptionPane.NO_OPTION && returnVal != JOptionPane.CLOSED_OPTION)
				{
					File location = chooser.getSelectedFile().getParentFile();
					Settings.setScreenshotDir(location.toString());

					String filePath = chooser.getSelectedFile().getAbsolutePath();
					if (!filePath.endsWith(".png"))
						chooser.setSelectedFile(new File(filePath + ".png"));
					creator.activeSidebar = null;
					creator.startScreenshot(new PendingScreenshot(chooser.getSelectedFile(), null));
				}
			}
		});

		itemShareFacebook.addActionListener(a ->
		{
			creator.activeSidebar = null;
			creator.startScreenshot(new PendingScreenshot(null, new ScreenshotCallback()
			{
				@Override
				public void callback(File file)
				{
					try
					{
						String url = Uploader.upload(file);
						Screenshot.shareToFacebook(url);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}));
		});

		itemShareTwitter.addActionListener(a ->
		{
			creator.activeSidebar = null;
			creator.startScreenshot(new PendingScreenshot(null, new ScreenshotCallback()
			{
				@Override
				public void callback(File file)
				{
					try
					{
						String url = Uploader.upload(file);
						Screenshot.shareToTwitter(url);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}));
		});

		itemShareReddit.addActionListener(a ->
		{
			creator.activeSidebar = null;
			creator.startScreenshot(new PendingScreenshot(null, new ScreenshotCallback()
			{
				@Override
				public void callback(File file)
				{
					try
					{
						String url = Uploader.upload(file);
						Screenshot.shareToReddit(url);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}));
		});

		itemImgurLink.addActionListener(a ->
		{
			creator.activeSidebar = null;
			creator.startScreenshot(new PendingScreenshot(null, new ScreenshotCallback()
			{
				@Override
				public void callback(File file)
				{
					SwingUtilities.invokeLater(new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								String url = Uploader.upload(file);

								JOptionPane message = new JOptionPane();
								String title;

								if (url != null && !url.equals("null"))
								{
									StringSelection text = new StringSelection(url);
									Toolkit.getDefaultToolkit().getSystemClipboard().setContents(text, null);
									title = "Success";
									message.setMessage("<html><b>" + url + "</b> has been copied to your clipboard.</html>");
								}
								else
								{
									title = "Error";
									message.setMessage("Failed to upload screenshot. Check your internet connection then try again.");
								}

								JDialog dialog = message.createDialog(Menu.this, title);
								dialog.setLocationRelativeTo(null);
								dialog.setModal(false);
								dialog.setVisible(true);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					});
				}
			}));
		});

		itemMF.addActionListener(a ->
		{
			JOptionPane.showMessageDialog(null, "This option has not been added yet. Please wait until the next preview.");
		});

		itemPM.addActionListener(a ->
		{
			JOptionPane.showMessageDialog(null, "This option has not been added yet. Please wait until the next preview.");
		});

		itemGitHub.addActionListener(a ->
		{
			Util.openUrl(Constants.URL_GITHUB);
		});

		itemDonate.addActionListener(a ->
		{
			Util.openUrl(Constants.URL_DONATE);
		});

		itemModelCauldron.addActionListener(a ->
		{
			Util.loadModelFromJar(creator.getElementManager(), getClass(), "models/cauldron");
		});

		itemModelChair.addActionListener(a ->
		{
			Util.loadModelFromJar(creator.getElementManager(), getClass(), "models/modern_chair");
		});
	}

	private JMenuItem createItem(String name, String tooltip, int mnemonic, Icon icon)
	{
		JMenuItem item = new JMenuItem(name);
		item.setToolTipText(tooltip);
		item.setMnemonic(mnemonic);
		item.setIcon(icon);
		return item;
	}

	private JMenuItem createCheckboxItem(String name, String tooltip, int mnemonic, boolean checked, Icon icon)
	{
		JMenuItem item = new JCheckBoxMenuItem(name, checked);
		item.setToolTipText(tooltip);
		item.setMnemonic(mnemonic);
		item.setIcon(icon);

		return item;
	}
}
