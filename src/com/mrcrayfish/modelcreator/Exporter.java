package com.mrcrayfish.modelcreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Exporter
{
	private List<String> textureList = new ArrayList<String>();

	// If for mod, can include modid
	private String modid;

	// Output Directory
	private String outputPath;

	// Model Variables
	private CuboidManager manager;
	private String modelName;

	// Child Variables
	private String childName;

	public Exporter(CuboidManager manager, String outputPath, String outputName)
	{
		this.manager = manager;
		this.modelName = outputName;
		this.outputPath = outputPath;
		compileTextureList();
	}

	public void assignChild(String childName)
	{
		this.childName = childName;
	}

	public void setModId(String modid)
	{
		this.modid = modid;
	}

	public void export()
	{
		File path = new File(outputPath);
		if (path.exists() && path.isDirectory())
		{
			FileWriter fw;
			BufferedWriter writer;
			try
			{
				fw = new FileWriter(new File(path, modelName + "_model.json"));
				writer = new BufferedWriter(fw);
				writeComponents(writer, manager);
				writer.close();
				fw.close();

				/*
				 * fw = new FileWriter(new File(path, modelName + ".json"));
				 * writer = new BufferedWriter(fw); writeChild(writer);
				 * writer.close(); fw.close();
				 */
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void compileTextureList()
	{
		for (Cuboid cuboid : manager.getAllCuboids())
		{
			for (Face face : cuboid.getAllFaces())
			{
				if (!textureList.contains(face.getTextureLocation() + face.getTextureName()))
				{
					textureList.add(face.getTextureLocation() + face.getTextureName());
				}
			}
		}
	}

	private void writeComponents(BufferedWriter writer, CuboidManager manager) throws IOException
	{
		writer.write("{");
		writer.newLine();
		writer.write(space(1) + "\"__comment\": \"Model generated using MrCrayfish's Model Creator (http://mrcrayfish.com/modelcreator/)\",");
		writer.newLine();
		if (!manager.getAmbientOcc())
		{
			writer.write("\"ambientocclusion\": " + manager.getAmbientOcc() + ",");
			writer.newLine();
		}
		writeTextures(writer);
		writer.newLine();
		writer.write(space(1) + "\"elements\": [");
		for (int i = 0; i < manager.getCuboidCount(); i++)
		{
			writer.newLine();
			writer.write(space(2) + "{");
			writer.newLine();
			writeElement(writer, manager.getCuboid(i));
			writer.newLine();
			writer.write(space(2) + "}");
			if (i != manager.getCuboidCount() - 1)
				writer.write(",");
		}
		writer.newLine();
		writer.write(space(1) + "]");
		writer.newLine();
		writer.write("}");
	}

	private void writeTextures(BufferedWriter writer) throws IOException
	{
		writer.write(space(1) + "\"textures\": {");
		writer.newLine();
		for (String texture : textureList)
		{
			writer.write(space(2) + "\"" + textureList.indexOf(texture) + "\": \"" + texture + "\"");
			if (textureList.indexOf(texture) != textureList.size() - 1)
			{
				writer.write(",");
			}
			writer.newLine();
		}
		writer.write(space(1) + "},");
	}

	private void writeElement(BufferedWriter writer, Cuboid cuboid) throws IOException
	{
		writer.write(space(3) + "\"name\": \"" + cuboid.toString() + "\",");
		writer.newLine();
		writeBounds(writer, cuboid);
		writer.newLine();
		if (!cuboid.isShaded())
		{
			writeShade(writer, cuboid);
			writer.newLine();
		}
		if (cuboid.getRotation() > 0)
		{
			writeRotation(writer, cuboid);
			writer.newLine();
		}
		writeFaces(writer, cuboid);

	}

	private void writeBounds(BufferedWriter writer, Cuboid cuboid) throws IOException
	{
		writer.write(space(3) + "\"from\": [ " + cuboid.getStartX() + ", " + cuboid.getStartY() + ", " + (16.0 - (cuboid.getStartZ() + cuboid.getDepth())) + " ], ");
		writer.newLine();
		writer.write(space(3) + "\"to\": [ " + (cuboid.getStartX() + cuboid.getWidth()) + ", " + (cuboid.getStartY() + cuboid.getHeight()) + ", " + (16.0 - cuboid.getStartZ()) + " ], ");
	}

	private void writeShade(BufferedWriter writer, Cuboid cuboid) throws IOException
	{
		writer.write(space(3) + "\"shade\": " + cuboid.isShaded() + ",");
	}

	private void writeRotation(BufferedWriter writer, Cuboid cuboid) throws IOException
	{
		writer.write(space(3) + "\"rotation\": { ");
		writer.write("\"origin\": [ " + cuboid.getOriginX() + ", " + cuboid.getOriginY() + ", " + cuboid.getOriginZ() + " ], ");
		writer.write("\"axis\": \"" + Cuboid.parseAxis(cuboid.getPrevAxis()) + "\", ");
		writer.write("\"angle\": " + cuboid.getRotation());
		if (cuboid.shouldRescale())
		{
			writer.write(", \"rescale\": " + cuboid.shouldRescale());
		}
		writer.write(" },");
	}

	private void writeFaces(BufferedWriter writer, Cuboid cuboid) throws IOException
	{
		writer.write(space(3) + "\"faces\": {");
		writer.newLine();
		for (Face face : cuboid.getAllFaces())
		{
			writer.write(space(4) + "\"" + Face.getFaceName(face.getSide()) + "\": { ");
			writer.write("\"texture\": \"#" + textureList.indexOf(face.getTextureLocation() + face.getTextureName()) + "\"");
			writer.write(", \"uv\": [ " + ((16.0 - cuboid.getFaceDimension(face.getSide()).getWidth()) - face.getStartU()) + ", " + ((16.0 - cuboid.getFaceDimension(face.getSide()).getHeight()) - face.getStartV()) + ", " + (((16.0 - cuboid.getFaceDimension(face.getSide()).getWidth()) - face.getStartU()) + face.getEndU()) + ", " + (((16.0 - cuboid.getFaceDimension(face.getSide()).getHeight()) - face.getStartV()) + face.getEndV()) + " ]");
			if (face.getRotation() > 0)
				writer.write(", \"rotation\": " + (int) face.getRotation());
			if (face.isCullfaced())
				writer.write(", \"cullface\": \"" + Face.getFaceName(face.getSide()) + "\"");
			writer.write(" }");
			if (face.getSide() != cuboid.getLastValidFace())
			{
				writer.write(",");
				writer.newLine();
			}
		}
		writer.newLine();
		writer.write(space(3) + "}");
	}

	private void writeChild(BufferedWriter writer) throws IOException
	{
		writer.write("{");
		writer.newLine();
		writer.write(space(1) + "\"parent\": \"block/" + modelName + "\",");
		writer.newLine();
		writer.write(space(1) + "\"textures\": {");
		writer.newLine();
		for (int i = 0; i < textureList.size(); i++)
		{
			writer.write(space(2) + "\"" + i + "\": \"block/" + textureList.get(i) + "\"");
			if (i != textureList.size() - 1)
			{
				writer.write(",");
			}
			writer.newLine();
		}
		writer.write(space(1) + "}");
		writer.write("}");
	}

	private String space(int size)
	{
		String space = "";
		for (int i = 0; i < size; i++)
		{
			space += "    ";
		}
		return space;
	}
}
