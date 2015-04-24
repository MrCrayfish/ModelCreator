package com.mrcrayfish.modelcreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.texture.PendingTexture;
import com.mrcrayfish.modelcreator.texture.TextureManager;

public class ProjectManager
{
	public static void loadProject(ElementManager manager, String modelFile)
	{
		manager.clearElements();
		
		File[] files = extractFiles(modelFile);
		for (File file : files)
		{
			if (file.getAbsolutePath().contains("model.json"))
			{
				Importer importer = new Importer(manager, file.getAbsolutePath());
				importer.importFromJSON();
			}
			else if(!file.getAbsolutePath().contains(".mcmeta"))
			{
				File metaFile = null;
				for(File mfile : files) {
					if(mfile.getAbsolutePath().startsWith(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".")))) {
						if(mfile.getAbsolutePath().contains(".mcmeta")) {
							metaFile = mfile;
						}
					}
				}
				manager.addPendingTexture(new PendingTexture(file.getAbsolutePath(), metaFile, null));
			}
		}
	}

	private static File[] extractFiles(String modelFile)
	{
		List<File> files = new ArrayList<File>();
		try
		{
			ZipInputStream zis = new ZipInputStream(new FileInputStream(modelFile));

			ZipEntry ze = null;
			while ((ze = zis.getNextEntry()) != null)
			{
				System.out.println(ze.getName());
				File file = File.createTempFile(ze.getName(), "");
				file.mkdirs();

				byte[] buffer = new byte[1024];
				FileOutputStream fos = new FileOutputStream(file);

				int len;
				while ((len = zis.read(buffer)) > 0)
				{
					fos.write(buffer, 0, len);
				}

				fos.close();

				files.add(file);
			}

			zis.closeEntry();
			zis.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
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
			addToZipFile(file, zos);
			file.delete();

			for (String location : getTextureLocations(manager))
			{
				File texfile = new File(location);
				addToZipFile(texfile, zos, "textures/");
				File metafile = new File(texfile.getParentFile(), texfile.getName()+".mcmeta");
				if(metafile.exists()) {
					addToZipFile(metafile, zos, "textures/");
				}
			}

			zos.close();
			fos.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	private static String[] getTextureLocations(ElementManager manager)
	{
		List<String> locations = new ArrayList<String>();
		for (Element cuboid : manager.getAllElements())
		{
			for (Face face : cuboid.getAllFaces())
			{
				String texture = TextureManager.getTextureLocation(face.getTextureName());
				System.out.println(texture);
				if (texture != null)
				{
					if (!locations.contains(texture))
					{
						locations.add(texture);
					}
				}
			}
		}
		return locations.toArray(new String[0]);
	}

	private static File getSaveFile(ElementManager manager)
	{
		return new Exporter(manager).writeJSONFile(new File("model.json"));
	}

	private static void addToZipFile(File file, ZipOutputStream zos) throws FileNotFoundException, IOException
	{
		addToZipFile(file, zos, "");
	}

	private static void addToZipFile(File file, ZipOutputStream zos, String folder) throws FileNotFoundException, IOException
	{
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(folder + file.getName());
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0)
		{
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
}