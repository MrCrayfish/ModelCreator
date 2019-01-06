package com.mrcrayfish.modelcreator;

import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.texture.PendingTexture;
import com.mrcrayfish.modelcreator.texture.TextureManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ProjectManager
{
    public static void loadProject(ElementManager manager, String modelFile)
    {
        manager.clearElements();

        Project project = new Project(extractFiles(modelFile));

        Importer importer = new Importer(manager, project.getModel().getAbsolutePath());
        importer.importFromJSON();

        for(ProjectTexture texture : project.getTextures())
        {
            manager.addPendingTexture(new PendingTexture(texture.getTexture(), texture.getMeta()));
        }
    }

    private static File[] extractFiles(String modelFile)
    {
        List<File> files = new ArrayList<>();
        try
        {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(modelFile));

            ZipEntry ze;
            while((ze = zis.getNextEntry()) != null)
            {
                File file = File.createTempFile(ze.getName(), "");
                file.mkdirs();
                file.deleteOnExit();

                byte[] buffer = new byte[1024];
                FileOutputStream fos = new FileOutputStream(file);

                int len;
                while((len = zis.read(buffer)) > 0)
                {
                    fos.write(buffer, 0, len);
                }

                fos.close();

                files.add(file);
            }

            zis.closeEntry();
            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return files.toArray(new File[0]);
    }

    public static void saveProject(ElementManager manager, String name)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(name);
            ZipOutputStream zos = new ZipOutputStream(fos);

            File file = getSaveFile(manager);
            addToZipFile(file, zos, "model.json");
            file.deleteOnExit();

            for(String textureLocation : getTextureLocations(manager))
            {
                if(textureLocation != null)
                {
                    File texture = new File(textureLocation);
                    if(texture.exists())
                    {
                        addToZipFile(texture, zos, "textures/", texture.getName());
                    }
                }
            }

            for(String metaLocation : getMetaLocations(manager))
            {
                if(metaLocation != null)
                {
                    File texture = new File(metaLocation);
                    if(texture.exists())
                    {
                        addToZipFile(texture, zos, "textures/", texture.getName());
                    }
                }
            }

            zos.close();
            fos.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static String[] getTextureLocations(ElementManager manager)
    {
        List<String> locations = new ArrayList<>();
        for(Element cuboid : manager.getAllElements())
        {
            for(Face face : cuboid.getAllFaces())
            {
                String location = TextureManager.getTextureLocation(face.getTextureName());
                if(!locations.contains(location))
                {
                    locations.add(location);
                }
            }
        }
        return locations.toArray(new String[0]);
    }

    private static String[] getMetaLocations(ElementManager manager)
    {
        List<String> locations = new ArrayList<>();
        for(Element cuboid : manager.getAllElements())
        {
            for(Face face : cuboid.getAllFaces())
            {
                String location = TextureManager.getMetaLocation(face.getTextureName());
                if(!locations.contains(location))
                {
                    locations.add(location);
                }
            }
        }
        return locations.toArray(new String[0]);
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
        private List<ProjectTexture> textures;

        public Project(File[] files)
        {
            textures = new ArrayList<>();

            for(File file : files)
            {
                if(file.getAbsolutePath().contains("model.json"))
                {
                    this.model = file;
                }
                else if(!file.getAbsolutePath().contains(".mcmeta"))
                {
                    File metaFile = null;
                    for(File mfile : files)
                    {
                        if(mfile.getAbsolutePath().startsWith(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("."))))
                        {
                            if(mfile.getAbsolutePath().contains(".mcmeta"))
                            {
                                metaFile = mfile;
                            }
                        }
                    }
                    textures.add(new ProjectTexture(file, metaFile));
                }
            }
        }

        public File getModel()
        {
            return model;
        }

        public List<ProjectTexture> getTextures()
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
