package com.mrcrayfish.modelcreator.component;

import com.mrcrayfish.modelcreator.*;
import com.mrcrayfish.modelcreator.display.DisplayProperties;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.screenshot.PendingScreenshot;
import com.mrcrayfish.modelcreator.screenshot.Screenshot;
import com.mrcrayfish.modelcreator.screenshot.Uploader;
import com.mrcrayfish.modelcreator.util.ComponentUtil;
import com.mrcrayfish.modelcreator.util.KeyboardUtil;
import com.mrcrayfish.modelcreator.util.Util;
import org.lwjgl.input.Keyboard;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Menu extends JMenuBar
{
    private ModelCreator creator;

    /* File */
    private JMenu menuFile;
    private JMenuItem itemNew;
    private JMenuItem itemLoad;
    private JMenuItem itemSave;
    private JMenuItem itemImport;
    private JMenuItem itemExport;
    private JMenuItem itemSettings;
    private JMenuItem itemExit;

    /* Edit */
    private JMenu menuEdit;
    private JMenuItem itemUndo;
    private JMenuItem itemRedo;

    /* Model */
    private JMenu menuModel;
    private JMenuItem itemTextureManager;
    private JMenuItem itemDisplayProps;
    private JMenuItem itemOptimise;
    private JMenu menuRotate;
    private JMenuItem itemRotateClockwise;
    private JMenuItem itemRotateCounterClockwise;

    /* Share */
    private JMenu menuScreenshot;
    private JMenuItem itemSaveToDisk;
    private JMenuItem itemShareFacebook;
    private JMenuItem itemShareTwitter;
    private JMenuItem itemShareReddit;
    private JMenuItem itemImgurLink;

    /* Extras */
    private JMenu menuMore;
    private JMenuItem itemExtractAssets;
    private JMenu menuDeveloper;
    private JMenuItem itemJavaCode;
    private JMenu menuExamples;
    private JMenuItem itemModelCauldron;
    private JMenuItem itemModelChair;
    private JMenuItem itemDonate;
    private JMenuItem itemGitHub;

    public static DisplayPropertiesDialog displayPropertiesDialog = null;
    public static boolean shouldRenderGrid = false;

    public Menu(ModelCreator creator)
    {
        this.creator = creator;
        this.initMenu();
    }

    private void initMenu()
    {
        menuFile = new JMenu("File");
        {
            itemNew = createMenuItem("New", "New Model", KeyEvent.VK_N, Icons.new_, KeyEvent.VK_N, Keyboard.KEY_N, InputEvent.CTRL_MASK);
            itemLoad = createMenuItem("Load Project...", "Load Project from File", KeyEvent.VK_S, Icons.load, KeyEvent.VK_O, Keyboard.KEY_O, InputEvent.CTRL_MASK);
            itemSave = createMenuItem("Save Project...", "Save Project to File", KeyEvent.VK_S, Icons.disk, KeyEvent.VK_S, Keyboard.KEY_S, InputEvent.CTRL_MASK);
            itemImport = createMenuItem("Import JSON...", "Import Model from JSON", KeyEvent.VK_I, Icons.import_);
            itemExport = createMenuItem("Export JSON...", "Export Model to JSON", KeyEvent.VK_E, Icons.export);
            itemSettings = createMenuItem("Settings", "Change the settings of the Model Creator", KeyEvent.VK_S, Icons.settings, KeyEvent.VK_S, Keyboard.KEY_S, InputEvent.CTRL_MASK + InputEvent.ALT_MASK);
            itemExit = createMenuItem("Exit", "Exit Application", KeyEvent.VK_E, Icons.exit);
        }

        menuEdit = new JMenu("Edit");
        {
            itemUndo = createMenuItem("Undo", "Undos the previous action", KeyEvent.VK_U, Icons.undo, KeyEvent.VK_Z, Keyboard.KEY_Z, InputEvent.CTRL_MASK);
            itemRedo = createMenuItem("Redo", "Redos the previous action", KeyEvent.VK_R, Icons.redo, KeyEvent.VK_Y, Keyboard.KEY_Y, InputEvent.CTRL_MASK);
        }

        menuModel = new JMenu("Model");
        {
            itemTextureManager = createMenuItem("Texture Manager", "Manage the textures entries for the model", KeyEvent.VK_T, Icons.texture, KeyEvent.VK_T, Keyboard.KEY_T, InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK);
            itemDisplayProps = createMenuItem("Display Properties", "Change the display properties of the model", KeyEvent.VK_D, Icons.gallery, KeyEvent.VK_D, Keyboard.KEY_D, InputEvent.CTRL_MASK + InputEvent.ALT_MASK);
            itemOptimise = createMenuItem("Optimize", "Performs basic optimizion by disabling faces that aren't visible", KeyEvent.VK_O, Icons.optimize, KeyEvent.VK_N, Keyboard.KEY_N, InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK);
            menuRotate = new JMenu("Rotate");
            menuRotate.setMnemonic(KeyEvent.VK_R);
            menuRotate.setIcon(Icons.rotate);
            {
                itemRotateClockwise = createMenuItem("90\u00B0 Clockwise", "Rotates all elements clockwise by 90\u00B0", KeyEvent.VK_C, Icons.rotate_clockwise, KeyEvent.VK_RIGHT, Keyboard.KEY_RIGHT, InputEvent.CTRL_MASK);
                itemRotateCounterClockwise = createMenuItem("90\u00B0 Counter Clockwise", "Rotates all elements counter clockwise by 90\u00B0", KeyEvent.VK_C, Icons.rotate_counter_clockwise, KeyEvent.VK_LEFT, Keyboard.KEY_LEFT, InputEvent.CTRL_MASK);
            }
        }

        menuScreenshot = new JMenu("Screenshot");
        {
            itemSaveToDisk = createMenuItem("Save to Disk...", "Save screenshot to disk.", KeyEvent.VK_D, Icons.disk);
            itemShareFacebook = createMenuItem("Share to Facebook", "Share a screenshot of your model Facebook.", KeyEvent.VK_S, Icons.facebook);
            itemShareTwitter = createMenuItem("Share to Twitter", "Share a screenshot of your model to Twitter.", KeyEvent.VK_S, Icons.twitter);
            itemShareReddit = createMenuItem("Share to Minecraft Subreddit", "Share a screenshot of your model to Minecraft Reddit.", KeyEvent.VK_S, Icons.reddit);
            itemImgurLink = createMenuItem("Get Imgur Link", "Get an Imgur link of your screenshot to share.", KeyEvent.VK_I, Icons.imgur);
        }

        menuMore = new JMenu("More");
        {
            itemExtractAssets = createMenuItem("Extract Assets...", "Extract Minecraft assets so you can get access to block and item textures", KeyEvent.VK_E, Icons.extract);
            menuDeveloper = new JMenu("Mod Developer");
            menuDeveloper.setMnemonic(KeyEvent.VK_M);
            menuDeveloper.setIcon(Icons.mojang);
            {
                itemJavaCode = createMenuItem("Generate Java Code...", "Generate Java code for selection and collisions boxes", KeyEvent.VK_J, Icons.java);
            }
            menuExamples = new JMenu("Examples");
            menuExamples.setMnemonic(KeyEvent.VK_E);
            menuExamples.setIcon(Icons.new_);
            {
                itemModelCauldron = createMenuItem("Cauldron", "<html>Model by MrCrayfish<br><b>Private use only</b></html>", KeyEvent.VK_C, Icons.model_cauldron);
                itemModelChair = createMenuItem("Chair", "<html>Model by MrCrayfish<br><b>Private use only</b></html>", KeyEvent.VK_C, Icons.model_chair);
            }
            itemDonate = createMenuItem("Donate (Patreon)", "Pledge to MrCrayfish", KeyEvent.VK_D, Icons.patreon);
            itemGitHub = createMenuItem("Source Code", "View Source Code", KeyEvent.VK_S, Icons.github);
        }

        this.initActions();

        /* Menu File */
        menuFile.add(itemNew);
        menuFile.addSeparator();
        menuFile.add(itemLoad);
        menuFile.add(itemSave);
        menuFile.addSeparator();
        menuFile.add(itemImport);
        menuFile.add(itemExport);
        menuFile.addSeparator();
        menuFile.add(itemSettings);
        menuFile.addSeparator();
        menuFile.add(itemExit);
        this.add(menuFile);

        /* Menu Edit */
        menuEdit.add(itemUndo);
        menuEdit.add(itemRedo);
        menuEdit.addMenuListener(new MenuAdapter()
        {
            @Override
            public void menuSelected(MenuEvent e)
            {
                itemRedo.setEnabled(StateManager.canRestoreNextState());
                itemUndo.setEnabled(StateManager.canRestorePreviousState());
            }
        });
        this.add(menuEdit);

        /* Menu Model Sub Menus */
        menuRotate.add(itemRotateClockwise);
        menuRotate.add(itemRotateCounterClockwise);

        /* Menu Model */
        menuModel.add(itemTextureManager);
        menuModel.add(itemDisplayProps);
        menuModel.add(itemOptimise);
        menuModel.addSeparator();
        menuModel.add(menuRotate);
        this.add(menuModel);

        /* Menu Screenshots */
        menuScreenshot.add(itemSaveToDisk);
        menuScreenshot.add(itemShareFacebook);
        menuScreenshot.add(itemShareTwitter);
        menuScreenshot.add(itemShareReddit);
        menuScreenshot.add(itemImgurLink);
        this.add(menuScreenshot);

        /* Menu More Sub Menus */
        menuDeveloper.add(itemJavaCode);
        menuExamples.add(itemModelCauldron);
        menuExamples.add(itemModelChair);

        /* Menu More */
        menuMore.add(itemExtractAssets);
        menuMore.add(menuDeveloper);
        menuMore.addSeparator();
        menuMore.add(menuExamples);
        menuMore.addSeparator();
        menuMore.add(itemGitHub);
        menuMore.add(itemDonate);
        this.add(menuMore);
    }

    private void initActions()
    {
        itemNew.addActionListener(a -> newProject(creator));

        itemLoad.addActionListener(a -> loadProject(creator));

        itemSave.addActionListener(a -> saveProject(creator));

        itemImport.addActionListener(a -> showImportJson(creator));

        itemExport.addActionListener(a -> showExportJson(creator));

        itemJavaCode.addActionListener(a -> showExportJavaCode(creator, a));

        itemSettings.addActionListener(a -> showSettings(creator));

        itemExit.addActionListener(a -> creator.close());

        itemTextureManager.addActionListener(a ->
        {
            TextureManager manager = new TextureManager(creator, creator.getElementManager(), Dialog.ModalityType.APPLICATION_MODAL, false);
            manager.setLocationRelativeTo(null);
            manager.setVisible(true);
        });

        itemDisplayProps.addActionListener(a -> showDisplayProperties(creator));

        itemOptimise.addActionListener(a -> optimizeModel(creator));

        itemRotateClockwise.addActionListener(a -> Actions.rotateModel(creator.getElementManager(), true));

        itemRotateCounterClockwise.addActionListener(a -> Actions.rotateModel(creator.getElementManager(), false));

        itemSaveToDisk.addActionListener(a ->
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Output Directory");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setApproveButtonText("Save");

            FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG (.png)", "png");
            chooser.setFileFilter(filter);

            String dir = Settings.getScreenshotDir();

            if(dir != null)
            {
                chooser.setCurrentDirectory(new File(dir));
            }

            int returnVal = chooser.showSaveDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {
                if(chooser.getSelectedFile().exists())
                {
                    returnVal = JOptionPane.showConfirmDialog(null, "A file already exists with that name, are you sure you want to override?", "Warning", JOptionPane.YES_NO_OPTION);
                }
                if(returnVal != JOptionPane.NO_OPTION && returnVal != JOptionPane.CLOSED_OPTION)
                {
                    File location = chooser.getSelectedFile().getParentFile();
                    Settings.setScreenshotDir(location.toString());

                    String filePath = chooser.getSelectedFile().getAbsolutePath();
                    if(!filePath.endsWith(".png"))
                    {
                        chooser.setSelectedFile(new File(filePath + ".png"));
                    }
                    creator.setSidebar(null);
                    creator.startScreenshot(new PendingScreenshot(chooser.getSelectedFile(), null));
                }
            }
        });

        itemShareFacebook.addActionListener(a ->
        {
            creator.setSidebar(null);
            creator.startScreenshot(new PendingScreenshot(null, file ->
            {
                try
                {
                    String url = Uploader.upload(file);
                    Screenshot.shareToFacebook(url);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }));
        });

        itemShareTwitter.addActionListener(a ->
        {
            creator.setSidebar(null);
            creator.startScreenshot(new PendingScreenshot(null, file ->
            {
                try
                {
                    String url = Uploader.upload(file);
                    Screenshot.shareToTwitter(url);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }));
        });

        itemShareReddit.addActionListener(a ->
        {
            creator.setSidebar(null);
            creator.startScreenshot(new PendingScreenshot(null, file ->
            {
                try
                {
                    String url = Uploader.upload(file);
                    Screenshot.shareToReddit(url);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }));
        });

        itemImgurLink.addActionListener(a ->
        {
            creator.setSidebar(null);
            creator.startScreenshot(new PendingScreenshot(null, file -> SwingUtilities.invokeLater(() ->
            {
                try
                {
                    String url = Uploader.upload(file);

                    JOptionPane message = new JOptionPane();
                    String title;

                    if(url != null && !url.equals("null"))
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

                    JDialog dialog = message.createDialog(this, title);
                    dialog.setLocationRelativeTo(null);
                    dialog.setModal(false);
                    dialog.setVisible(true);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            })));
        });

        itemGitHub.addActionListener(a -> Util.openUrl(Constants.URL_GITHUB));

        itemDonate.addActionListener(a -> Util.openUrl(Constants.URL_DONATE));

        itemExtractAssets.addActionListener(a -> showExtractAssets(creator));

        itemModelCauldron.addActionListener(a ->
        {
            StateManager.clear();
            Util.loadModelFromJar(creator.getElementManager(), getClass(), "models/cauldron");
            StateManager.pushState(creator.getElementManager());
        });

        itemModelChair.addActionListener(a ->
        {
            StateManager.clear();
            Util.loadModelFromJar(creator.getElementManager(), getClass(), "models/modern_chair");
            StateManager.pushState(creator.getElementManager());
        });

        itemUndo.addActionListener(a -> StateManager.restorePreviousState(creator.getElementManager()));

        itemRedo.addActionListener(a -> StateManager.restoreNextState(creator.getElementManager()));
    }

    private JMenuItem createMenuItem(String name, String tooltip, int mnemonic, Icon icon)
    {
        return createMenuItem(name, tooltip, mnemonic, icon, -1, -1, -1);
    }

    private JMenuItem createMenuItem(String name, String tooltip, int mnemonic, Icon icon, int awtCode, int keyCode, int modifiers)
    {
        JMenuItem item = new JMenuItem(name);
        item.setToolTipText(tooltip);
        item.setMnemonic(mnemonic);
        item.setIcon(icon);

        if(awtCode != -1 && keyCode != -1 && modifiers != -1)
        {
            KeyStroke shortcut = KeyStroke.getKeyStroke(awtCode, modifiers);
            if(shortcut != null)
            {
                String shortcutText = KeyboardUtil.convertKeyStokeToString(shortcut);
                item.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
                JLabel label = new JLabel("<html><p style='color:#666666;font-size:9px'>" + shortcutText + "<p></html>");
                item.add(label);
                Dimension size = new Dimension((int) Math.ceil(item.getPreferredSize().getWidth() + label.getPreferredSize().getWidth()) + 10, 20);
                item.setPreferredSize(size);
            }

            if(shortcut != null)
            {
                creator.registerKeyAction(new ModelCreator.KeyAction(awtCode, keyCode, (modifier, pressed) ->
                {
                    if(pressed && modifier == modifiers)
                    {
                        for(ActionListener listener : item.getActionListeners())
                        {
                            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, item.getActionCommand(), modifier));
                        }
                    }
                }));
            }
        }

        return item;
    }

    public static void newProject(ModelCreator creator)
    {
        int returnVal = JOptionPane.showConfirmDialog(creator, "You current work will be cleared, are you sure?", "Note", JOptionPane.YES_NO_OPTION);
        if(returnVal == JOptionPane.YES_OPTION)
        {
            TextureManager.clear();
            StateManager.clear();
            creator.getElementManager().reset();
            creator.getElementManager().updateValues();
            DisplayPropertiesDialog.update(creator);
            StateManager.pushState(creator.getElementManager());
        }
    }

    public static void loadProject(ModelCreator creator)
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load Project");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setApproveButtonText("Load");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Model (.model)", "model");
        chooser.setFileFilter(filter);

        String dir = Settings.getModelDir();

        if(dir != null)
        {
            chooser.setCurrentDirectory(new File(dir));
        }

        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            if(creator.getElementManager().getElementCount() > 0)
            {
                returnVal = JOptionPane.showConfirmDialog(null, "Your current project will be cleared, are you sure you want to continue?", "Warning", JOptionPane.YES_NO_OPTION);
            }
            if(returnVal != JOptionPane.NO_OPTION && returnVal != JOptionPane.CLOSED_OPTION)
            {
                File location = chooser.getSelectedFile().getParentFile();
                Settings.setModelDir(location.toString());

                TextureManager.clear();
                StateManager.clear();
                ProjectManager.loadProject(creator.getElementManager(), chooser.getSelectedFile().getAbsolutePath());
                DisplayPropertiesDialog.update(creator);
                StateManager.pushState(creator.getElementManager());
            }
        }
    }

    public static void saveProject(ModelCreator creator)
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Project");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setApproveButtonText("Save");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Model (.model)", "model");
        chooser.setFileFilter(filter);
        String dir = Settings.getModelDir();

        if(dir != null)
        {
            chooser.setCurrentDirectory(new File(dir));
        }

        int returnVal = chooser.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            if(chooser.getSelectedFile().exists())
            {
                returnVal = JOptionPane.showConfirmDialog(null, "A file already exists with that name, are you sure you want to override?", "Warning", JOptionPane.YES_NO_OPTION);
            }
            if(returnVal != JOptionPane.NO_OPTION && returnVal != JOptionPane.CLOSED_OPTION)
            {
                File location = chooser.getSelectedFile().getParentFile();
                Settings.setModelDir(location.toString());

                String filePath = chooser.getSelectedFile().getAbsolutePath();
                if(!filePath.endsWith(".model"))
                {
                    filePath += ".model";
                }
                ProjectManager.saveProject(creator.getElementManager(), filePath);
            }
        }
    }

    public static void optimizeModel(ModelCreator creator)
    {
        int result = JOptionPane.showConfirmDialog(null, "<html>Are you sure you want to optimize the model?<br/>It is recommended you save the project before running this<br/>action, otherwise you will have to re-enable the disabled faces.<html>", "Optimize Confirmation", JOptionPane.YES_NO_OPTION);
        if(result == JOptionPane.YES_OPTION)
        {
            int count = Actions.optimiseModel(creator.getElementManager());
            JOptionPane.showMessageDialog(null, "<html>Optimizing the model disabled <b>" + count + "</b> faces</html>", "Optimization Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void showImportJson(ModelCreator creator)
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Import JSON Model");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setApproveButtonText("Import");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON (.json)", "json");
        chooser.setFileFilter(filter);

        String dir = Settings.getJSONDir();
        if(dir != null)
        {
            chooser.setCurrentDirectory(new File(dir));
        }

        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            if(creator.getElementManager().getElementCount() > 0)
            {
                returnVal = JOptionPane.showConfirmDialog(null, "Your current project will be cleared, are you sure you want to continue?", "Warning", JOptionPane.YES_NO_OPTION);
            }
            if(returnVal != JOptionPane.NO_OPTION && returnVal != JOptionPane.CLOSED_OPTION)
            {
                File location = chooser.getSelectedFile().getParentFile();
                Settings.setJSONDir(location.toString());

                TextureManager.clear();
                StateManager.clear();
                Importer importer = new Importer(creator.getElementManager(), chooser.getSelectedFile().getAbsolutePath());
                importer.importFromJSON();
                StateManager.pushState(creator.getElementManager());
            }
            creator.getElementManager().updateValues();
        }
    }

    public static void showExportJson(ModelCreator creator)
    {
        JDialog dialog = new JDialog(creator, "Export JSON Model", Dialog.ModalityType.APPLICATION_MODAL);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 225));

        SpringLayout springLayout = new SpringLayout();
        JPanel exportDir = new JPanel(springLayout);

        JLabel labelName = new JLabel("Name");
        labelName.setHorizontalAlignment(SwingConstants.RIGHT);
        exportDir.add(labelName);

        JTextField textFieldName = new JTextField();
        textFieldName.setPreferredSize(new Dimension(100, 24));
        textFieldName.setCaretPosition(0);
        exportDir.add(textFieldName);

        JTextField textFieldDestination = new JTextField();
        textFieldDestination.setPreferredSize(new Dimension(100, 24));

        String exportJsonDir = Settings.getExportJSONDir();
        if(exportJsonDir != null)
        {
            textFieldDestination.setText(exportJsonDir);
        }
        else
        {
            String userHome = System.getProperty("user.home", ".");
            textFieldDestination.setText(userHome);
        }

        textFieldDestination.setEditable(false);
        textFieldDestination.setFocusable(false);
        textFieldDestination.setCaretPosition(0);
        exportDir.add(textFieldDestination);

        JButton btnBrowserDir = new JButton("Browse");
        btnBrowserDir.setPreferredSize(new Dimension(80, 24));
        btnBrowserDir.setIcon(Icons.load);
        btnBrowserDir.addActionListener(e ->
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Export Destination");
            chooser.setCurrentDirectory(new File(textFieldDestination.getText()));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setApproveButtonText("Select");
            int returnVal = chooser.showOpenDialog(dialog);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = chooser.getSelectedFile();
                if(file != null)
                {
                    textFieldDestination.setText(file.getAbsolutePath());
                }
            }
        });
        exportDir.add(btnBrowserDir);

        JLabel labelExportDir = new JLabel("Destination");
        exportDir.add(labelExportDir);

        //JComponent optionSeparator = DefaultComponentFactory.getInstance().createSeparator("Export Options");
        JComponent optionSeparator = new JSeparator();
        exportDir.add(optionSeparator);

        JCheckBox checkBoxOptimize = ComponentUtil.createCheckBox("Optimize Model", "Removes unnecessary faces that can't been seen in the model", true);
        exportDir.add(checkBoxOptimize);

        JCheckBox checkBoxDisplayProps = ComponentUtil.createCheckBox("Include Display Properties", "Adds the display definitions (first-person, third-person, etc) to the model file", true);
        exportDir.add(checkBoxDisplayProps);

        JCheckBox checkBoxElementNames = ComponentUtil.createCheckBox("Include Element Names", "The name of each element will be added to it's entry in the json model elements array. Useful for identifying elements, and when importing back into Model Creator, it will use those names", true);
        exportDir.add(checkBoxElementNames);

        JSeparator separator = new JSeparator();
        exportDir.add(separator);

		/* Constraints */

        springLayout.putConstraint(SpringLayout.NORTH, labelName, 3, SpringLayout.NORTH, textFieldName);
        springLayout.putConstraint(SpringLayout.WEST, labelName, 10, SpringLayout.WEST, exportDir);
        springLayout.putConstraint(SpringLayout.EAST, labelName, -5, SpringLayout.WEST, textFieldDestination);
        springLayout.putConstraint(SpringLayout.NORTH, textFieldName, 10, SpringLayout.NORTH, exportDir);
        springLayout.putConstraint(SpringLayout.WEST, textFieldName, 0, SpringLayout.WEST, textFieldDestination);
        springLayout.putConstraint(SpringLayout.EAST, textFieldName, 0, SpringLayout.EAST, textFieldDestination);
        springLayout.putConstraint(SpringLayout.WEST, optionSeparator, 10, SpringLayout.WEST, exportDir);
        springLayout.putConstraint(SpringLayout.EAST, optionSeparator, -10, SpringLayout.EAST, exportDir);
        springLayout.putConstraint(SpringLayout.NORTH, optionSeparator, 10, SpringLayout.SOUTH, textFieldDestination);
        springLayout.putConstraint(SpringLayout.NORTH, btnBrowserDir, 0, SpringLayout.NORTH, textFieldDestination);
        springLayout.putConstraint(SpringLayout.EAST, btnBrowserDir, -10, SpringLayout.EAST, exportDir);
        springLayout.putConstraint(SpringLayout.NORTH, textFieldDestination, 10, SpringLayout.SOUTH, textFieldName);
        springLayout.putConstraint(SpringLayout.WEST, textFieldDestination, 5, SpringLayout.EAST, labelExportDir);
        springLayout.putConstraint(SpringLayout.EAST, textFieldDestination, -10, SpringLayout.WEST, btnBrowserDir);
        springLayout.putConstraint(SpringLayout.NORTH, labelExportDir, 3, SpringLayout.NORTH, textFieldDestination);
        springLayout.putConstraint(SpringLayout.WEST, labelExportDir, 10, SpringLayout.WEST, exportDir);
        springLayout.putConstraint(SpringLayout.NORTH, checkBoxOptimize, 5, SpringLayout.SOUTH, optionSeparator);
        springLayout.putConstraint(SpringLayout.WEST, checkBoxOptimize, 10, SpringLayout.WEST, exportDir);
        springLayout.putConstraint(SpringLayout.NORTH, checkBoxDisplayProps, 0, SpringLayout.SOUTH, checkBoxOptimize);
        springLayout.putConstraint(SpringLayout.WEST, checkBoxDisplayProps, 10, SpringLayout.WEST, exportDir);
        springLayout.putConstraint(SpringLayout.NORTH, checkBoxElementNames, 0, SpringLayout.SOUTH, checkBoxDisplayProps);
        springLayout.putConstraint(SpringLayout.WEST, checkBoxElementNames, 10, SpringLayout.WEST, exportDir);
        springLayout.putConstraint(SpringLayout.WEST, separator, 10, SpringLayout.WEST, exportDir);
        springLayout.putConstraint(SpringLayout.EAST, separator, -10, SpringLayout.EAST, exportDir);
        springLayout.putConstraint(SpringLayout.NORTH, separator, 5, SpringLayout.SOUTH, checkBoxElementNames);
        springLayout.putConstraint(SpringLayout.SOUTH, separator, 5, SpringLayout.SOUTH, exportDir);

        panel.setPreferredSize(panel.getPreferredSize());
        panel.add(exportDir, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dialog.dispose();
            }
        });
        buttons.add(btnCancel);

        JButton btnExport = new JButton("Export");
        btnExport.addActionListener(e ->
        {
            String name = textFieldName.getText().trim();
            if(!textFieldDestination.getText().isEmpty() && !name.isEmpty())
            {
                File destination = new File(textFieldDestination.getText());
                destination.mkdirs();

                File modelFile = new File(destination, textFieldName.getText() + ".json");
                if(modelFile.exists())
                {
                    int returnVal = JOptionPane.showConfirmDialog(dialog, "A file for that name already exists in the directory. Are you sure you want to override it?", "Warning", JOptionPane.YES_NO_OPTION);
                    if(returnVal != JOptionPane.YES_OPTION)
                    {
                        return;
                    }
                }

                try
                {
                    modelFile.createNewFile();
                }
                catch(IOException e1)
                {
                    JOptionPane.showMessageDialog(dialog, "Unable to create the file. Check that your destination folder is writable", "Error", JOptionPane.ERROR_MESSAGE);
                }

                dialog.dispose();

                ExporterModel exporter = new ExporterModel(creator.getElementManager());
                exporter.setOptimize(checkBoxOptimize.isSelected());
                exporter.setDisplayProps(checkBoxDisplayProps.isSelected());
                exporter.setIncludeNames(checkBoxElementNames.isSelected());
                if(exporter.writeFile(modelFile) == null)
                {
                    modelFile.delete();
                    JOptionPane.showMessageDialog(dialog, "An error occured while exporting the model. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    Settings.setExportJSONDir(textFieldDestination.getText());
                    int returnVal = JOptionPane.showOptionDialog(dialog, "Model exported successfully!", "Success", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Open Folder", "Close"}, "Close");
                    if(returnVal == 0)
                    {
                        Desktop desktop = Desktop.getDesktop();
                        try
                        {
                            desktop.open(destination);
                        }
                        catch(IOException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        buttons.add(btnExport);

        panel.add(buttons, BorderLayout.SOUTH);

        dialog.add(panel);

        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static void showExportJavaCode(ModelCreator creator, ActionEvent actionEvent)
    {
        JCheckBox includeFields = ComponentUtil.createCheckBox("Generate Fields", "Include decelerations of the AABBs fields that represent the model's elements", true);
        JCheckBox includeMethods = ComponentUtil.createCheckBox("Generate Methods", "Include bounds, raytracing, & collision methods", true);
        JCheckBox useBoundsHelper = ComponentUtil.createCheckBox("Use Bounds Helper", "Fields and methods use MrCrayfish's Bounds helper class, and target his code-base", false);
        JCheckBox generateRotatedBounds = ComponentUtil.createCheckBox("Make Rotatable", "Use Bounds helper class to create AABB rotation arrays for each element", false);

        useBoundsHelper.addActionListener(e -> generateRotatedBounds.setEnabled(useBoundsHelper.isSelected()));

        String mcTooltip = "Select Minecraft version";
        JPanel panelMC = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel mcLabel = new JLabel("MC Version");
        mcLabel.setToolTipText(mcTooltip);
        panelMC.add(mcLabel);

        JComboBox<ExporterJavaCode.Version> mcVersion = new JComboBox<>(ExporterJavaCode.Version.values());
        mcVersion.setToolTipText(mcTooltip);
        mcVersion.setPreferredSize(new Dimension(60, 24));
        mcVersion.addActionListener(e ->
        {
            ExporterJavaCode.Version version = (ExporterJavaCode.Version) mcVersion.getSelectedItem();
            switch(version)
            {
                case V_1_12:
                    useBoundsHelper.setText("Use Bounds Helper");
                    break;
                default:
                    useBoundsHelper.setText("Use VoxelShapeHelper");
                    break;
            }
        });
        panelMC.add(mcVersion);

        JPanel panelMain = new JPanel();
        panelMain.setLayout(new GridLayout(1, 2));
        panelMain.add(includeFields);
        panelMain.add(includeMethods);

        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
        parent.add(panelMC);

        JPanel controls = new JPanel(new GridLayout(1, 1));
        controls.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "<html><b>Options</b></html>"));
        controls.add(panelMain);
        parent.add(controls);

        if((actionEvent.getModifiers() & ActionEvent.SHIFT_MASK) > 0 && (actionEvent.getModifiers() & ActionEvent.CTRL_MASK) > 0)
        {
            includeMethods.setToolTipText(includeMethods.getToolTipText().replace(", raytracing", ""));
            useBoundsHelper.setForeground(Color.BLACK);
            useBoundsHelper.setSelected(true);
            generateRotatedBounds.setSelected(true);

            JPanel panelCray = new JPanel();
            panelCray.setLayout(new GridLayout(1, 2));
            panelCray.add(useBoundsHelper);
            panelCray.add(generateRotatedBounds);

            controls.setLayout(new GridLayout(2, 1));
            controls.add(panelCray);
        }

        JLabel infoLabel = new JLabel("<html><body><p style='width:260px'>" +
                "Use this tool to generate Java code for selection and collision boxes. " +
                "This includes AxisAlignedBB fields and the required methods for the " +
                "Block class to apply them. It should be noted that elements in the model " +
                "that have been rotated will be ignored when generating." +
                "</p></body></html>");
        JLabel questionLabel = new JLabel("<html><body><p style='width:260px'>" +
                "Would you like the code to be copied to your clipboard or saved to a text file?" +
                "</p></body></html>");

        int returnValDestination = JOptionPane.showOptionDialog(creator, new Object[] {infoLabel, Box.createHorizontalStrut(20), Box.createHorizontalStrut(20), new JSeparator(),
                        parent, new JSeparator(), Box.createHorizontalStrut(20), Box.createHorizontalStrut(20),
                        questionLabel}, "Generate Java Code", JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, new String[] {"Clipboard", "Text File"}, "Clipboard");
        if (!includeFields.isSelected() && !includeMethods.isSelected())
        {
            JOptionPane.showMessageDialog(creator, "Either AxisAlignedBBs or methods must be selected.", "None Selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        ExporterJavaCode exporter = new ExporterJavaCode(creator, includeFields.isSelected(), includeMethods.isSelected(), useBoundsHelper.isSelected(), generateRotatedBounds.isSelected());
        exporter.setVersion((ExporterJavaCode.Version) mcVersion.getSelectedItem());
        if (returnValDestination == JOptionPane.CLOSED_OPTION)
            return;

        if (returnValDestination == JOptionPane.OK_OPTION)
        {
            try
            {
                exporter.writeCodeToClipboard();
            }
            catch (Exception exception)
            {
                JOptionPane.showMessageDialog(creator, "An error occured while copying code to your clipboard.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Output Directory");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setApproveButtonText("Export");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT (.txt)", "txt");
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
                if (!filePath.endsWith(".txt"))
                    chooser.setSelectedFile(new File(filePath + ".txt"));

                exporter.export(chooser.getSelectedFile());
            }
        }
    }

    public static void showSettings(ModelCreator creator)
    {
        JDialog dialog = new JDialog(creator, "Settings", Dialog.ModalityType.APPLICATION_MODAL);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 300));
        dialog.add(panel);

        JTabbedPane tabbedPane = new JTabbedPane();
        panel.add(tabbedPane, BorderLayout.CENTER);

        SpringLayout generalSpringLayout = new SpringLayout();
        JPanel generalPanel = new JPanel(generalSpringLayout);
        tabbedPane.addTab("General", generalPanel);

        JPanel optionsPanel = new JPanel(new GridLayout(1, 2));
        generalPanel.add(optionsPanel);

        JPanel undoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        optionsPanel.add(undoPanel);

        JLabel labelUndoLimit = new JLabel("Undo / Redo Limit");
        undoPanel.add(labelUndoLimit);

        final Boolean[] changed = {false};
        SpinnerNumberModel undoSpinnerNumberModel = new SpinnerNumberModel();
        undoSpinnerNumberModel.setMinimum(1);
        JSpinner undoLimitSpinner = new JSpinner(undoSpinnerNumberModel);
        undoLimitSpinner.setPreferredSize(new Dimension(40, 24));
        undoLimitSpinner.setValue(Settings.getUndoLimit());
        undoLimitSpinner.addChangeListener(e ->
        {
            if(!changed[0])
            {
                JOptionPane.showMessageDialog(dialog, "Increasing the undo/redo limit will increase the amount of memory the program use. Change this setting with caution.", "Warning", JOptionPane.WARNING_MESSAGE);
                changed[0] = true;
            }
        });
        undoPanel.add(undoLimitSpinner);

        JCheckBox checkBoxCardinalPoints = ComponentUtil.createCheckBox("Show Cardinal Points", "", Settings.getCardinalPoints());
        optionsPanel.add(checkBoxCardinalPoints);

        JSeparator separator = new JSeparator();
        generalPanel.add(separator);

        String assetsPath = Settings.getAssetsDir() != null ? Settings.getAssetsDir() : "";
        JPanel texturePathPanel = createDirectorySelector("Assets Path", dialog, assetsPath);
        generalPanel.add(texturePathPanel);

        JSeparator separator2 = new JSeparator();
        generalPanel.add(separator2);

        String imageEditorPath = Settings.getImageEditor() != null ? Settings.getImageEditor() : "";
        JPanel imageEditorPanel = ComponentUtil.createFileSelector("Image Editor", dialog, imageEditorPath, null, null);
        generalPanel.add(imageEditorPanel);

        JLabel labelArguments = new JLabel("Arguments");
        generalPanel.add(labelArguments);

        String imageEditorArgs = Settings.getImageEditorArgs() != null ? Settings.getImageEditorArgs() : "\"%s\"";
        JTextField textFieldArguments = new JTextField(imageEditorArgs);
        textFieldArguments.setPreferredSize(new Dimension(0, 24));
        generalPanel.add(textFieldArguments);

        generalSpringLayout.putConstraint(SpringLayout.WEST, optionsPanel, 5, SpringLayout.WEST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.NORTH, optionsPanel, 5, SpringLayout.NORTH, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.EAST, optionsPanel, 5, SpringLayout.EAST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.WEST, separator, 0, SpringLayout.WEST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.EAST, separator, 0, SpringLayout.EAST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.NORTH, separator, 5, SpringLayout.SOUTH, optionsPanel);
        generalSpringLayout.putConstraint(SpringLayout.EAST, texturePathPanel, -10, SpringLayout.EAST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.WEST, texturePathPanel, 10, SpringLayout.WEST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.NORTH, texturePathPanel, 10, SpringLayout.SOUTH, separator);
        generalSpringLayout.putConstraint(SpringLayout.EAST, separator2, 0, SpringLayout.EAST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.WEST, separator2, 0, SpringLayout.WEST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.NORTH, separator2, 10, SpringLayout.SOUTH, texturePathPanel);
        generalSpringLayout.putConstraint(SpringLayout.EAST, imageEditorPanel, -10, SpringLayout.EAST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.WEST, imageEditorPanel, 10, SpringLayout.WEST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.NORTH, imageEditorPanel, 10, SpringLayout.SOUTH, separator2);
        generalSpringLayout.putConstraint(SpringLayout.WEST, labelArguments, 10, SpringLayout.WEST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.NORTH, labelArguments, 2, SpringLayout.NORTH, textFieldArguments);
        generalSpringLayout.putConstraint(SpringLayout.EAST, textFieldArguments, -10, SpringLayout.EAST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.WEST, textFieldArguments, 20, SpringLayout.EAST, labelArguments);
        generalSpringLayout.putConstraint(SpringLayout.NORTH, textFieldArguments, 10, SpringLayout.SOUTH, imageEditorPanel);

        JPanel colorGrid = new JPanel();
        colorGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane colorScrollPane = new JScrollPane(colorGrid);
        tabbedPane.addTab("Appearance", colorScrollPane);

        colorGrid.add(createColorSelector(dialog, "North Face", Face.getFaceColour(Face.NORTH), createFaceColorProcessor(Face.NORTH)));
        colorGrid.add(createColorSelector(dialog, "East Face", Face.getFaceColour(Face.EAST), createFaceColorProcessor(Face.EAST)));
        colorGrid.add(createColorSelector(dialog, "South Face", Face.getFaceColour(Face.SOUTH), createFaceColorProcessor(Face.SOUTH)));
        colorGrid.add(createColorSelector(dialog, "West Face", Face.getFaceColour(Face.WEST), createFaceColorProcessor(Face.WEST)));
        colorGrid.add(createColorSelector(dialog, "Up Face", Face.getFaceColour(Face.UP), createFaceColorProcessor(Face.UP)));
        colorGrid.add(createColorSelector(dialog, "Down Face", Face.getFaceColour(Face.DOWN), createFaceColorProcessor(Face.DOWN)));

        JButton btnReset = new JButton("Reset Colors");
        btnReset.addActionListener(a ->
        {
            Face.setFaceColors(Settings.DEFAULT_FACE_COLORS);
            dialog.dispose();
            JOptionPane.showMessageDialog(creator, "Colors reset");
        });
        colorGrid.add(btnReset);

        colorGrid.setLayout(new GridLayout(colorGrid.getComponentCount(), 1, 20, 10));

        dialog.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                Settings.setAssetsDir(getDirectoryFromSelector(texturePathPanel));
                Settings.setUndoLimit((int) undoLimitSpinner.getValue());
                Settings.setFaceColors(Face.getFaceColors());
                Settings.setImageEditor(getDirectoryFromSelector(imageEditorPanel));
                Settings.setImageEditorArgs(textFieldArguments.getText());
                Settings.setCardinalPoints(checkBoxCardinalPoints.isSelected());
            }
        });

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.requestFocus();
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static JPanel createDirectorySelector(String label, Component parent, String defaultDir)
    {
        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setPreferredSize(new Dimension(100, 24));

        JTextField textFieldDestination = new JTextField();
        textFieldDestination.setPreferredSize(new Dimension(100, 24));
        textFieldDestination.setText(defaultDir);
        textFieldDestination.setEditable(false);
        textFieldDestination.setFocusable(false);
        textFieldDestination.setCaretPosition(0);
        panel.add(textFieldDestination);

        JButton btnBrowserDir = new JButton("Browse");
        btnBrowserDir.setPreferredSize(new Dimension(80, 24));
        btnBrowserDir.setIcon(Icons.load);
        btnBrowserDir.addActionListener(e ->
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select a Folder");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setApproveButtonText("Select");
            chooser.setCurrentDirectory(new File(defaultDir));
            int returnVal = chooser.showOpenDialog(parent);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = chooser.getSelectedFile();
                if(file != null)
                {
                    textFieldDestination.setText(file.getAbsolutePath());
                }
            }
        });
        panel.add(btnBrowserDir);

        JLabel labelExportDir = new JLabel(label);
        panel.add(labelExportDir);

        layout.putConstraint(SpringLayout.NORTH, textFieldDestination, 0, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, textFieldDestination, 10, SpringLayout.EAST, labelExportDir);
        layout.putConstraint(SpringLayout.EAST, textFieldDestination, -10, SpringLayout.WEST, btnBrowserDir);
        layout.putConstraint(SpringLayout.NORTH, labelExportDir, 3, SpringLayout.NORTH, textFieldDestination);
        layout.putConstraint(SpringLayout.WEST, labelExportDir, 0, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, btnBrowserDir, 0, SpringLayout.NORTH, textFieldDestination);
        layout.putConstraint(SpringLayout.EAST, btnBrowserDir, 0, SpringLayout.EAST, panel);

        return panel;
    }

    public static String getDirectoryFromSelector(JPanel panel)
    {
        for(Component component : panel.getComponents())
        {
            if(component instanceof JTextField)
            {
                return ((JTextField) component).getText();
            }
        }
        return "";
    }

    public static void showExtractAssets(ModelCreator creator)
    {
        JDialog dialog = new JDialog(creator, "Extract Assets", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setPreferredSize(new Dimension(300, 150));
        dialog.add(panel);

        JLabel labelInfo = new JLabel("<html>This tool allows you to extract Minecraft's assets. The versions listed below are the ones you have downloaded with the Java edition of the game.</html>");
        panel.add(labelInfo);

        JLabel labelMinecraftAssets = new JLabel("Minecraft Version");
        panel.add(labelMinecraftAssets);

        JComboBox<String> comboBoxMinecraftVersions = new JComboBox<>();
        comboBoxMinecraftVersions.setPreferredSize(new Dimension(40, 24));
        Util.getMinecraftVersions().forEach(comboBoxMinecraftVersions::addItem);
        panel.add(comboBoxMinecraftVersions);

        JButton btnExtract = new JButton("Extract");
        btnExtract.setIcon(Icons.extract);
        btnExtract.setPreferredSize(new Dimension(80, 24));
        btnExtract.addActionListener(e ->
        {
            Util.extractMinecraftAssets((String) comboBoxMinecraftVersions.getSelectedItem(), dialog);
            dialog.dispose();
        });
        panel.add(btnExtract);

        layout.putConstraint(SpringLayout.NORTH, labelInfo, 10, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.EAST, labelInfo, -10, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.WEST, labelInfo, 10, SpringLayout.WEST, panel);

        layout.putConstraint(SpringLayout.NORTH, labelMinecraftAssets, 2, SpringLayout.NORTH, comboBoxMinecraftVersions);
        layout.putConstraint(SpringLayout.WEST, labelMinecraftAssets, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, comboBoxMinecraftVersions, 15, SpringLayout.SOUTH, labelInfo);
        layout.putConstraint(SpringLayout.WEST, comboBoxMinecraftVersions, 10, SpringLayout.EAST, labelMinecraftAssets);
        layout.putConstraint(SpringLayout.EAST, comboBoxMinecraftVersions, -10, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.SOUTH, btnExtract, -10, SpringLayout.SOUTH, panel);
        layout.putConstraint(SpringLayout.EAST, btnExtract, -10, SpringLayout.EAST, panel);

        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public static JPanel createColorSelector(Window parent, String labelText, int startColor, Processor<Integer> processor)
    {
        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setPreferredSize(new Dimension(200, 30));
        panel.setBackground(new Color(0, 0, 0, 0));

        JLabel label = new JLabel(labelText);
        panel.add(label);

        JPanel colorPanel = new JPanel();
        colorPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        colorPanel.setBackground(new Color(startColor));
        colorPanel.setPreferredSize(new Dimension(24, 24));
        panel.add(colorPanel);

        JButton button = new JButton("Change");
        button.setPreferredSize(new Dimension(80, 24));
        button.addActionListener(e ->
        {
            int color = selectColor(parent, startColor);
            if(processor.run(color))
            {
                colorPanel.setBackground(new Color(color));
            }
        });
        panel.add(button);

        layout.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, label, 0, SpringLayout.VERTICAL_CENTER, panel);
        layout.putConstraint(SpringLayout.EAST, label, 5, SpringLayout.WEST, colorPanel);
        layout.putConstraint(SpringLayout.WEST, colorPanel, 80, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, colorPanel, 0, SpringLayout.VERTICAL_CENTER, panel);
        layout.putConstraint(SpringLayout.EAST, colorPanel, -10, SpringLayout.WEST, button);
        layout.putConstraint(SpringLayout.EAST, button, 0, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, button, 0, SpringLayout.VERTICAL_CENTER, panel);
        return panel;
    }

    private static int selectColor(Window parent, int startColor)
    {
        JDialog dialog = new JDialog(parent, "Select a Color", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        dialog.add(panel);

        JColorChooser colorChooser = new JColorChooser();
        colorChooser.setColor(startColor);
        panel.add(colorChooser, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(buttons, BorderLayout.SOUTH);

        JButton btnExtract = new JButton("Select");
        btnExtract.setIcon(Icons.extract);
        btnExtract.setPreferredSize(new Dimension(80, 24));
        btnExtract.addActionListener(e -> dialog.dispose());
        buttons.add(btnExtract);

        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return colorChooser.getColor().getRGB();
    }

    private static Processor<Integer> createFaceColorProcessor(int side)
    {
        return integer ->
        {
            if(Face.getFaceColour(side) != integer)
            {
                Face.setFaceColor(side, integer);
                return true;
            }
            return false;
        };
    }

    private static void showDisplayProperties(ModelCreator creator)
    {
        DisplayPropertiesDialog dialog = new DisplayPropertiesDialog(creator);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                Menu.displayPropertiesDialog = null;
                ModelCreator.restoreStandardRenderer();
            }
        });
        dialog.setLocationRelativeTo(null);
        dialog.setLocation(dialog.getLocation().x - 500, dialog.getLocation().y);
        dialog.setVisible(true);
        dialog.requestFocus();

        Menu.displayPropertiesDialog = dialog;
        ModelCreator.setCanvasRenderer(DisplayProperties.RENDER_MAP.get("gui"));
    }
}
