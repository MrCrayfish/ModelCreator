package com.mrcrayfish.modelcreator.util;

import com.google.gson.Gson;
import com.mrcrayfish.modelcreator.ProjectManager;
import com.mrcrayfish.modelcreator.element.ElementManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Util
{
    private static final List<String> minecraftVersions;

    static
    {
        List<String> versionList = new ArrayList<>();
        File file = getMinecraftDirectory();
        if(file != null && file.exists() && file.isDirectory())
        {
            File versions = null;
            for(File folder : getSubFolders(file))
            {
                if(folder.getName().equals("versions"))
                {
                    versions = folder;
                    break;
                }
            }
            if(versions != null)
            {
                for(File folder : getSubFolders(versions))
                {
                    File json = getFile(folder, folder.getName() + ".json");
                    if(json != null && !isLegacyAssets(json))
                    {
                        if(hasFile(folder, folder.getName() + ".jar"))
                        {
                            versionList.add(folder.getName());
                        }
                    }
                }
            }
        }
        minecraftVersions = versionList;
    }

    public static List<String> getMinecraftVersions()
    {
        return minecraftVersions;
    }

    public static void openUrl(String url)
    {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
        {
            try
            {
                desktop.browse(new URL(url).toURI());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void loadModelFromJar(ElementManager manager, Class<?> clazz, String name)
    {
        try
        {
            InputStream is = clazz.getClassLoader().getResourceAsStream(name + ".model");
            File file = File.createTempFile(name + ".model", "");
            FileOutputStream fos = new FileOutputStream(file);

            byte[] buffer = new byte[1024];

            int len;
            while((len = is.read(buffer)) > 0)
            {
                fos.write(buffer, 0, len);
            }

            fos.close();
            is.close();

            ProjectManager.loadProject(manager, file.getAbsolutePath());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void extractMinecraftAssets(String version, Window window)
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Extract Destination");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setApproveButtonText("Select");
        int returnVal = chooser.showOpenDialog(window);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = chooser.getSelectedFile();
            if(file.isDirectory())
            {
                File extractionFolder = new File(file, version);
                File jar = new File(getMinecraftDirectory(), "versions/" + version + "/" + version + ".jar");
                extractZipFiles(jar, zipEntry -> zipEntry.getName().startsWith("assets/"), window, extractionFolder);
            }
        }
    }

    private static void extractZipFiles(File zipFile, Predicate<ZipEntry> conditions, Window window, File extractionFolder)
    {
        final boolean[] cancelled = {false};

        JDialog dialog = new JDialog(window, "Extracting Assets", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                cancelled[0] = true;
            }
        });

        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setPreferredSize(new Dimension(300, 60));
        dialog.add(panel);

        JLabel labelProcessing = new JLabel("Processing");
        panel.add(labelProcessing);

        JLabel labelFile = new JLabel();
        panel.add(labelFile);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setForeground(new Color(129, 192, 0));
        panel.add(progressBar);

        layout.putConstraint(SpringLayout.NORTH, labelProcessing, 10, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, labelProcessing, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, labelFile, 10, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, labelFile, 5, SpringLayout.EAST, labelProcessing);
        layout.putConstraint(SpringLayout.EAST, labelFile, -10, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.NORTH, progressBar, 10, SpringLayout.SOUTH, labelProcessing);
        layout.putConstraint(SpringLayout.WEST, progressBar, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST, progressBar, -10, SpringLayout.EAST, panel);

        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);

        new Thread(() ->
        {
            List<ZipEntry> entries = new ArrayList<>();
            try
            {
                ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
                ZipEntry ze;
                while((ze = zis.getNextEntry()) != null)
                {
                    if(cancelled[0])
                    {
                        return;
                    }
                    if(conditions != null && !conditions.test(ze))
                    {
                        continue;
                    }
                    entries.add(ze);
                }
                zis.closeEntry();
                zis.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            if(entries.size() > 0)
            {
                SwingUtilities.invokeLater(() -> progressBar.setMaximum(entries.size()));
            }

            if(cancelled[0])
            {
                return;
            }

            try
            {
                ZipFile f = new ZipFile(zipFile);
                for(int i = 0; i < entries.size(); i++)
                {
                    if(cancelled[0])
                    {
                        return;
                    }

                    ZipEntry entry = entries.get(i);
                    SwingUtilities.invokeLater(() -> labelFile.setText(entry.getName()));

                    InputStream is = f.getInputStream(entry);

                    File file = new File(extractionFolder, entry.getName());
                    file.getParentFile().mkdirs();
                    file.createNewFile();

                    byte[] buffer = new byte[8192];
                    FileOutputStream fos = new FileOutputStream(file);
                    int len;
                    while((len = is.read(buffer)) > 0)
                    {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();

                    final int value = i;
                    SwingUtilities.invokeLater(() -> progressBar.setValue(value + 1));
                }
                SwingUtilities.invokeLater(window::dispose);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }).start();

        dialog.setVisible(true);
    }

    public static File getMinecraftDirectory()
    {
        String userHome = System.getProperty("user.home", ".");
        OperatingSystem os = getOS();
        switch(os)
        {
            case WINDOWS:
                String appDataDir = System.getenv("APPDATA");
                if(appDataDir != null)
                {
                    return new File(appDataDir, ".minecraft");
                }
            case MAC:
                return new File(userHome, "Library/Application Support/minecraft");
            case LINUX:
                return new File(userHome, ".minecraft");
            default:
                return null;
        }
    }

    public static OperatingSystem getOS()
    {
        String name = System.getProperty("os.name").toLowerCase();
        if(name.contains("win"))
        {
            return OperatingSystem.WINDOWS;
        }
        if(name.contains("mac"))
        {
            return OperatingSystem.MAC;
        }
        if(name.contains("solaris"))
        {
            return OperatingSystem.SOLARIS;
        }
        if(name.contains("sunos"))
        {
            return OperatingSystem.SOLARIS;
        }
        if(name.contains("linux"))
        {
            return OperatingSystem.LINUX;
        }
        if(name.contains("unix"))
        {
            return OperatingSystem.LINUX;
        }
        return OperatingSystem.UNKNOWN;
    }

    private static File[] getSubFolders(File parent)
    {
        return parent.listFiles((dir, name) -> dir.isDirectory());
    }

    private static boolean hasFile(File parent, String targetName)
    {
        File[] files = parent.listFiles((dir, name) -> name.equals(targetName));
        return files != null && files.length == 1;
    }

    private static File getFile(File parent, String targetName)
    {
        File[] files = parent.listFiles((dir, name) -> name.equals(targetName));
        return Arrays.stream(files).filter(file -> !file.isDirectory() && file.getName().equals(targetName)).findFirst().orElse(null);
    }

    private static boolean hasFolder(File parent, String targetName)
    {
        File[] files = parent.listFiles((dir, name) -> name.equals(targetName));
        return files != null && Arrays.stream(files).anyMatch(File::isDirectory);
    }

    private static boolean isLegacyAssets(File file)
    {
        try
        {
            String json = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            Gson gson = new Gson();
            VersionProperties properties = gson.fromJson(json, VersionProperties.class);
            return properties != null && properties.assetIndex != null && "legacy".equals(properties.assetIndex.id);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private static class VersionProperties
    {
        public AssetIndex assetIndex;
    }

    private static class AssetIndex
    {
        private String id;
    }
}
