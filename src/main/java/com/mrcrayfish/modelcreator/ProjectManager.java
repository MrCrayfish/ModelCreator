package com.mrcrayfish.modelcreator;

import com.mrcrayfish.modelcreator.component.TextureManager;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.texture.TextureEntry;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ProjectManager
{
    public static void loadProject(ElementManager manager, String modelFile)
    {
        TextureManager.clear();
        manager.clearElements();

        File projectFolder = extractFiles(modelFile);
        if(projectFolder != null)
        {
            Project project = new Project(projectFolder);
            Importer importer = new Importer(manager, project.getModel().getPath());
            importer.importFromJSON();
        }
        deleteFolder(projectFolder);
    }

    private static void deleteFolder(File file)
    {
        try
        {
            Files.walk(file.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static File extractFiles(String modelFile)
    {
        try
        {
            Path path = Files.createTempDirectory("ModelCreator");
            File folder = path.toFile();

            ZipInputStream zis = new ZipInputStream(new FileInputStream(modelFile));
            ZipEntry ze;
            while((ze = zis.getNextEntry()) != null)
            {
                File file = new File(folder, ze.getName());
                file.getParentFile().mkdirs();
                file.createNewFile();

                byte[] buffer = new byte[1024];
                FileOutputStream fos = new FileOutputStream(file);

                int len;
                while((len = zis.read(buffer)) > 0)
                {
                    fos.write(buffer, 0, len);
                }

                fos.flush();
                fos.close();
                zis.closeEntry();
            }
            zis.close();

            return folder;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveProject(ElementManager manager, String name)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(name);
            ZipOutputStream zos = new ZipOutputStream(fos);

            File file = getSaveFile(manager);
            addToZipFile(file, zos, "model.json");
            file.delete();

            for(TextureEntry entry : getAllTextures(manager))
            {
                File temp = File.createTempFile(entry.getName(), "");
                BufferedImage image = entry.getSource();
                ImageIO.write(image, "PNG", temp);
                addToZipFile(temp, zos, "assets/" + entry.getModId() + "/textures/" + entry.getDirectory() + "/", entry.getName() + ".png");
                temp.delete();
            }

            //TODO make output animation properties
            /*for(String metaLocation : getMetaLocations(manager))
            {
                if(metaLocation != null)
                {
                    File texture = new File(metaLocation);
                    if(texture.exists())
                    {
                        addToZipFile(texture, zos, "textures/", texture.getName());
                    }
                }
            }*/

            zos.close();
            fos.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static Set<TextureEntry> getAllTextures(ElementManager manager)
    {
        Set<TextureEntry> textureEntries = new HashSet<>();
        for(Element element : manager.getAllElements())
        {
            for(Face face : element.getAllFaces())
            {
                if(face.getTexture() != null)
                {
                    textureEntries.add(face.getTexture());
                }
            }
        }
        return textureEntries;
    }

    private static File getSaveFile(ElementManager manager) throws IOException
    {
        ExporterModel exporter = new ExporterModel(manager);
        exporter.setOptimize(false);
        exporter.setIncludeNonTexturedFaces(true);
        return exporter.writeFile(File.createTempFile("model.json", ""));
    }

    private static void addToZipFile(File file, ZipOutputStream zos, String name) throws IOException
    {
        addToZipFile(file, zos, "", name);
    }

    private static void addToZipFile(File file, ZipOutputStream zos, String folder, String name) throws IOException
    {
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(folder + name);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while((length = fis.read(bytes)) >= 0)
        {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }

    private static class Project
    {
        public File model;
        public File textures;

        public Project(File folder)
        {
            File[] files = folder.listFiles();
            if(files != null)
            {
                for(File file : files)
                {
                    String name = file.getName();
                    if(file.isFile() && name.equals("model.json"))
                    {
                        this.model = file;
                    }
                    else if(file.isDirectory() && name.equals("textures"))
                    {
                        this.textures = file;
                    }
                }
            }
        }

        public File getModel()
        {
            return model;
        }

        public File getTextures()
        {
            return textures;
        }
    }

    private static class ProjectTexture
    {
        private File texture;
        private File meta;

        public ProjectTexture(File texture, File meta)
        {
            this.texture = texture;
            this.meta = meta;
        }

        public File getTexture()
        {
            return texture;
        }

        public File getMeta()
        {
            return meta;
        }
    }
}
