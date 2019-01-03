package com.mrcrayfish.modelcreator;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.mrcrayfish.modelcreator.display.CanvasRenderer;
import com.mrcrayfish.modelcreator.display.DisplayProperties;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.panels.DisplayEntryPanel;
import com.mrcrayfish.modelcreator.screenshot.PendingScreenshot;
import com.mrcrayfish.modelcreator.screenshot.Screenshot;
import com.mrcrayfish.modelcreator.screenshot.Uploader;
import com.mrcrayfish.modelcreator.util.KeyboardUtil;
import com.mrcrayfish.modelcreator.util.Util;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
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
    private JMenuItem itemDisplayProps;
    private JMenuItem itemOptimise;

    /* Share */
    private JMenu menuScreenshot;
    private JMenuItem itemSaveToDisk;
    private JMenuItem itemShareFacebook;
    private JMenuItem itemShareTwitter;
    private JMenuItem itemShareReddit;
    private JMenuItem itemImgurLink;

    /* Extras */
    private JMenu menuHelp;
    private JMenuItem itemExtractAssets;
    private JMenu menuDeveloper;
    private JMenuItem itemJavaCode;
    private JMenu menuExamples;
    private JMenuItem itemModelCauldron;
    private JMenuItem itemModelChair;
    private JMenuItem itemDonate;
    private JMenuItem itemGitHub;

    public static boolean isDisplayPropsShowing = false;

    public Menu(ModelCreator creator)
    {
        this.creator = creator;
        initMenu();
    }

    private void initMenu()
    {
        menuFile = new JMenu("File");
        {
            itemNew = createItem("New", "New Model", KeyEvent.VK_N, Icons.new_, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
            itemLoad = createItem("Load Project...", "Load Project from File", KeyEvent.VK_S, Icons.load, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
            itemSave = createItem("Save Project...", "Save Project to File", KeyEvent.VK_S, Icons.disk, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
            itemImport = createItem("Import JSON...", "Import Model from JSON", KeyEvent.VK_I, Icons.import_);
            itemExport = createItem("Export JSON...", "Export Model to JSON", KeyEvent.VK_E, Icons.export);
            itemSettings = createItem("Settings", "Change the settings of the Model Creator", KeyEvent.VK_M, Icons.settings, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK));
            itemExit = createItem("Exit", "Exit Application", KeyEvent.VK_E, Icons.exit);
        }

        menuEdit = new JMenu("Edit");
        {
            itemUndo = createItem("Undo", "Undos the previous action", KeyEvent.VK_Z, Icons.coin, KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
            itemRedo = createItem("Redo", "Redos the previous action", KeyEvent.VK_Y, Icons.coin, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        }

        menuModel = new JMenu("Model");
        {
            itemDisplayProps = createItem("Display Properties", "Change the display properties of the model", KeyEvent.VK_D, Icons.texture, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK));
            itemOptimise = createItem("Optimize", "Performs basic optimizion by disabling faces that aren't visible", KeyEvent.VK_O, Icons.coin, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
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
            itemExtractAssets = createItem("Extract Assets...", "Extract Minecraft assets so you can get access to block and item textures", KeyEvent.VK_O, Icons.extract);
            menuDeveloper = new JMenu("Mod Developer");
            menuDeveloper.setIcon(Icons.mojang);
            {
                itemJavaCode = createItem("Generate Java Code...", "Generate Java code for selection and collisions boxes", KeyEvent.VK_J, Icons.java);
            }
            menuExamples = new JMenu("Examples");
            menuExamples.setIcon(Icons.new_);
            {
                itemModelCauldron = createItem("Cauldron", "<html>Model by MrCrayfish<br><b>Private use only</b></html>", KeyEvent.VK_C, Icons.model_cauldron);
                itemModelChair = createItem("Chair", "<html>Model by MrCrayfish<br><b>Private use only</b></html>", KeyEvent.VK_C, Icons.model_chair);
            }
            itemDonate = createItem("Donate (Patreon)", "Pledge to MrCrayfish", KeyEvent.VK_D, Icons.patreon);
            itemGitHub = createItem("Source Code", "View Source Code", KeyEvent.VK_G, Icons.github);
        }

        initActions();

        menuExamples.add(itemModelCauldron);
        menuExamples.add(itemModelChair);

        menuDeveloper.add(itemJavaCode);

        menuHelp.add(itemExtractAssets);
        menuHelp.add(menuDeveloper);
        menuHelp.addSeparator();
        menuHelp.add(menuExamples);
        menuHelp.addSeparator();
        menuHelp.add(itemGitHub);
        menuHelp.add(itemDonate);

        menuEdit.add(itemUndo);
        menuEdit.add(itemRedo);
        menuEdit.addMenuListener(new MenuListener()
        {
            @Override
            public void menuSelected(MenuEvent e)
            {
                itemRedo.setEnabled(StateManager.canRestoreNextState());
                itemUndo.setEnabled(StateManager.canRestorePreviousState());
            }

            @Override
            public void menuDeselected(MenuEvent e)
            {
            }

            @Override
            public void menuCanceled(MenuEvent e)
            {
            }
        });

        menuModel.add(itemDisplayProps);
        menuModel.add(itemOptimise);

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
        menuFile.add(itemSettings);
        menuFile.addSeparator();
        menuFile.add(itemExit);

        add(menuFile);
        add(menuEdit);
        add(menuModel);
        add(menuScreenshot);
        add(menuHelp);
    }

    private void initActions()
    {
        itemNew.addActionListener(a -> newProject(creator));

        itemLoad.addActionListener(a -> loadProject(creator));

        itemSave.addActionListener(a -> saveProject(creator));

        itemImport.addActionListener(a -> importJson(creator));

        itemExport.addActionListener(a -> exportJson(creator));

        itemJavaCode.addActionListener(a -> exportJavaCode(creator, a));

        itemSettings.addActionListener(a -> settings(creator));

        itemExit.addActionListener(a -> creator.close());

        itemDisplayProps.addActionListener(a -> displayProperties(creator));

        itemOptimise.addActionListener(a -> optimizeModel(creator));

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
                    creator.activeSidebar = null;
                    creator.startScreenshot(new PendingScreenshot(chooser.getSelectedFile(), null));
                }
            }
        });

        itemShareFacebook.addActionListener(a ->
        {
            creator.activeSidebar = null;
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
            creator.activeSidebar = null;
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
            creator.activeSidebar = null;
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
            creator.activeSidebar = null;
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

        itemExtractAssets.addActionListener(a -> extractAssets(creator));

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

    private JMenuItem createItem(String name, String tooltip, int mnemonic, Icon icon)
    {
        return createItem(name, tooltip, mnemonic, icon, null);
    }

    private JMenuItem createItem(String name, String tooltip, int mnemonic, Icon icon, KeyStroke shortcut)
    {
        JMenuItem item = new JMenuItem(name);
        item.setToolTipText(tooltip);
        item.setMnemonic(mnemonic);
        item.setIcon(icon);

        if(shortcut != null)
        {
            String shortcutText = KeyboardUtil.convertKeyStokeToString(shortcut);
            item.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            JLabel label = new JLabel("<html><p style='color:#666666;font-size:9px'>" + shortcutText + "<p></html>");
            item.add(label);
            Dimension size = new Dimension((int) Math.ceil(item.getPreferredSize().getWidth() + label.getPreferredSize().getWidth()) + 10, 20);
            item.setPreferredSize(size);
        }

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

    public static void newProject(ModelCreator creator)
    {
        int returnVal = JOptionPane.showConfirmDialog(creator, "You current work will be cleared, are you sure?", "Note", JOptionPane.YES_NO_OPTION);
        if(returnVal == JOptionPane.YES_OPTION)
        {
            StateManager.clear();
            creator.getElementManager().reset();
            creator.getElementManager().updateValues();
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

                StateManager.clear();
                ProjectManager.loadProject(creator.getElementManager(), chooser.getSelectedFile().getAbsolutePath());
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
            int count = 0;
            ElementManager manager = creator.getElementManager();
            for(Element element : manager.getAllElements())
            {
                for(Face face : element.getAllFaces())
                {
                    if(face.isEnabled() && !face.isVisible(manager))
                    {
                        count++;
                        face.setEnabled(false);
                    }
                }
            }
            if(count > 0)
            {
                StateManager.pushState(manager);
            }
            JOptionPane.showMessageDialog(null, "<html>Optimizing the model disabled <b>" + count + "</b> faces</html>", "Optimization Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void importJson(ModelCreator creator)
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

                StateManager.clear();
                Importer importer = new Importer(creator.getElementManager(), chooser.getSelectedFile().getAbsolutePath());
                importer.importFromJSON();
                StateManager.pushState(creator.getElementManager());
            }
            creator.getElementManager().updateValues();
        }
    }

    private static JCheckBox createCheckBox(String text, String tooltip, boolean selected)
    {
        JCheckBox checkBoxDisplayProps = new JCheckBox(text);
        checkBoxDisplayProps.setToolTipText(tooltip);
        checkBoxDisplayProps.setSelected(selected);
        checkBoxDisplayProps.setIcon(Icons.light_off);
        checkBoxDisplayProps.setRolloverIcon(Icons.light_off);
        checkBoxDisplayProps.setSelectedIcon(Icons.light_on);
        checkBoxDisplayProps.setRolloverSelectedIcon(Icons.light_on);
        return checkBoxDisplayProps;
    }

    public static void exportJson(ModelCreator creator)
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

        JComponent optionSeparator = DefaultComponentFactory.getInstance().createSeparator("Export Options");
        exportDir.add(optionSeparator);

        JCheckBox checkBoxOptimize = createCheckBox("Optimize Model", "Removes unnecessary faces that can't been seen in the model", true);
        exportDir.add(checkBoxOptimize);

        JCheckBox checkBoxDisplayProps = createCheckBox("Include Display Properties", "Adds the display definitions (first-person, third-person, etc) to the model file", true);
        exportDir.add(checkBoxDisplayProps);

        JCheckBox checkBoxElementNames = createCheckBox("Include Element Names", "The name of each element will be added to it's entry in the json model elements array. Useful for identifying elements, and when importing back into Model Creator, it will use those names", true);
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

                ExporterModelJSON exporter = new ExporterModelJSON(creator.getElementManager());
                exporter.setOptimize(checkBoxOptimize.isSelected());
                exporter.setDisplayProps(checkBoxDisplayProps.isSelected());
                exporter.setIncludeNames(checkBoxElementNames.isSelected());
                if(exporter.writeJSONFile(modelFile) == null)
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

    private static void exportJavaCode(ModelCreator creator, ActionEvent actionEvent)
    {
        JCheckBox includeAABBs = createCheckBox("Generate AABB Fields", "Include decelerations of the AABBs fields that represent the model's elements", true);
        JCheckBox includeMethods = createCheckBox("Generate Methods", "Include bounds, raytracing, & collision methods", true);
        JCheckBox useBoundsHelper = createCheckBox("Use Bounds Helper", "Fields and methods use MrCrayfish's Bounds helper class, and target his code-base", false);
        JCheckBox generateRotatedBounds = createCheckBox("Make Rotatable", "Use Bounds helper class to create AABB rotation arrays for each element", false);

        useBoundsHelper.addActionListener(e -> generateRotatedBounds.setEnabled(useBoundsHelper.isSelected()));

        String mcTooltip = "Seclect Minecraft version";
        JPanel panelMC = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel mcLabel = new JLabel("MC Version");
        mcLabel.setForeground(Color.GRAY);
        mcLabel.setToolTipText(mcTooltip);
        panelMC.add(mcLabel);

        JComboBox<String> mcVersion = new JComboBox<>(new String[]{"1.12", "1.13"});
        mcVersion.setEnabled(false);
        mcVersion.setToolTipText(mcTooltip);
        mcVersion.setPreferredSize(new Dimension(60, 24));
        panelMC.add(mcVersion);

        JPanel panelMain = new JPanel();
        panelMain.setLayout(new GridLayout(1, 2));
        panelMain.add(includeAABBs);
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
        if (!includeAABBs.isSelected() && !includeMethods.isSelected())
        {
            JOptionPane.showMessageDialog(creator, "Either AxisAlignedBBs or methods must be selected.", "None Selected", JOptionPane.OK_OPTION);
            return;
        }
        ExporterJavaCodeTXT exporter = new ExporterJavaCodeTXT(creator, includeAABBs.isSelected(), includeMethods.isSelected(), useBoundsHelper.isSelected(), generateRotatedBounds.isSelected());
        if (returnValDestination == JOptionPane.CLOSED_OPTION)
            return;

        if (returnValDestination == JOptionPane.OK_OPTION)
        {
            try
            {
                exporter.writeComponentsToClipboard();
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

    public static void settings(ModelCreator creator)
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

        JLabel labelUndoLimit = new JLabel("Undo / Redo Limit");
        generalPanel.add(labelUndoLimit);

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
        generalPanel.add(undoLimitSpinner);

        JSeparator separator = new JSeparator();
        generalPanel.add(separator);

        String path = Settings.getAssetsDir() != null ? Settings.getAssetsDir() : "";
        JPanel texturePathPanel = createDirectorySelector("Assets Path", dialog, path);
        generalPanel.add(texturePathPanel);

        generalSpringLayout.putConstraint(SpringLayout.WEST, labelUndoLimit, 10, SpringLayout.WEST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.NORTH, labelUndoLimit, 2, SpringLayout.NORTH, undoLimitSpinner);
        generalSpringLayout.putConstraint(SpringLayout.NORTH, undoLimitSpinner, 10, SpringLayout.NORTH, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.WEST, undoLimitSpinner, 5, SpringLayout.EAST, labelUndoLimit);
        generalSpringLayout.putConstraint(SpringLayout.WEST, separator, 0, SpringLayout.WEST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.EAST, separator, 0, SpringLayout.EAST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.NORTH, separator, 10, SpringLayout.SOUTH, undoLimitSpinner);
        generalSpringLayout.putConstraint(SpringLayout.EAST, texturePathPanel, -10, SpringLayout.EAST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.WEST, texturePathPanel, 10, SpringLayout.WEST, generalPanel);
        generalSpringLayout.putConstraint(SpringLayout.NORTH, texturePathPanel, 10, SpringLayout.SOUTH, separator);

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

    public static void extractAssets(ModelCreator creator)
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

    private static void displayProperties(ModelCreator creator)
    {
        isDisplayPropsShowing = true;
        ModelCreator.setCanvasRenderer(DisplayProperties.RENDER_MAP.get("gui"));

        JDialog dialog = new JDialog(creator, "Display Properties", Dialog.ModalityType.MODELESS);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                isDisplayPropsShowing = false;
                ModelCreator.restoreStandardRenderer();
            }
        });

        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setPreferredSize(new Dimension(400, 445));
        dialog.add(panel);

        JLabel labelProperties = new JLabel("Presets");
        panel.add(labelProperties);

        JComboBox<DisplayProperties> comboBoxProperties = new JComboBox<>();
        comboBoxProperties.addItem(DisplayProperties.MODEL_CREATOR_BLOCK);
        comboBoxProperties.addItem(DisplayProperties.DEFAULT_BLOCK);
        comboBoxProperties.addItem(DisplayProperties.DEFAULT_ITEM);
        comboBoxProperties.setPreferredSize(new Dimension(0, 24));
        panel.add(comboBoxProperties);

        DisplayProperties properties = creator.getElementManager().getDisplayProperties();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("GUI", new DisplayEntryPanel(properties.getEntry("gui")));
        tabbedPane.addTab("Ground", new DisplayEntryPanel(properties.getEntry("ground")));
        tabbedPane.addTab("Fixed", new DisplayEntryPanel(properties.getEntry("fixed")));
        tabbedPane.addTab("Head", new DisplayEntryPanel(properties.getEntry("head")));
        tabbedPane.addTab("First Person", new DisplayEntryPanel(properties.getEntry("firstperson_righthand")));
        tabbedPane.addTab("Third Person", new DisplayEntryPanel(properties.getEntry("thirdperson_righthand")));
        tabbedPane.addChangeListener(e ->
        {
            Component c = tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
            if(c instanceof DisplayEntryPanel)
            {
                DisplayEntryPanel entryPanel = (DisplayEntryPanel) c;
                CanvasRenderer render = DisplayProperties.RENDER_MAP.get(entryPanel.getEntry().getId());
                if(render != null)
                {
                    ModelCreator.setCanvasRenderer(render);
                }
                else
                {
                    ModelCreator.restoreStandardRenderer();
                }
            }
        });
        panel.add(tabbedPane);

        JButton btnApplyProperties = new JButton("Apply");
        btnApplyProperties.setPreferredSize(new Dimension(80, 24));
        btnApplyProperties.addActionListener(e ->
        {
            creator.getElementManager().setDisplayProperties((DisplayProperties) comboBoxProperties.getSelectedItem());
            DisplayProperties displayProperties = creator.getElementManager().getDisplayProperties();
            Component[] components = tabbedPane.getComponents();
            for(Component c : components)
            {
                if(c instanceof DisplayEntryPanel)
                {
                    DisplayEntryPanel entryPanel = (DisplayEntryPanel) c;
                    DisplayProperties.Entry oldEntry = entryPanel.getEntry();
                    entryPanel.updateValues(displayProperties.getEntry(oldEntry.getId()));
                }
            }
        });
        panel.add(btnApplyProperties);

        layout.putConstraint(SpringLayout.WEST, labelProperties, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, labelProperties, 2, SpringLayout.NORTH, comboBoxProperties);
        layout.putConstraint(SpringLayout.EAST, comboBoxProperties, -10, SpringLayout.WEST, btnApplyProperties);
        layout.putConstraint(SpringLayout.NORTH, comboBoxProperties, 10, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, comboBoxProperties, 10, SpringLayout.EAST, labelProperties);
        layout.putConstraint(SpringLayout.NORTH, btnApplyProperties, 10, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.EAST, btnApplyProperties, -10, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.EAST, tabbedPane, -10, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.NORTH, tabbedPane, 10, SpringLayout.SOUTH, comboBoxProperties);
        layout.putConstraint(SpringLayout.WEST, tabbedPane, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.SOUTH, tabbedPane, -10, SpringLayout.SOUTH, panel);

        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setLocation(dialog.getLocation().x - 500, dialog.getLocation().y);
        dialog.requestFocus();
        dialog.setVisible(true);
    }
}
