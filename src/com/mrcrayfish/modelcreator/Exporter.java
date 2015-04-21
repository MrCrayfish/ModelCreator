package com.mrcrayfish.modelcreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;

public class Exporter
{
	private List<String> textureList = new ArrayList<String>();

	// Model Variables
	private ElementManager manager;

	public Exporter(ElementManager manager)
	{
		this.manager = manager;
		compileTextureList();
	}

	public File export(File file)
	{
		File path = file.getParentFile();
		if (path.exists() && path.isDirectory())
		{
			writeJSONFile(file);
		}
		return file;
	}
	
	public File writeJSONFile(File file)
	{
		FileWriter fw;
		BufferedWriter writer;
		try
		{
			if (!file.exists())
			{
				file.createNewFile();
			}
			fw = new FileWriter(file);
			writer = new BufferedWriter(fw);
			writeComponents(writer, manager);
			writer.close();
			fw.close();
			return file;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private void compileTextureList()
	{
		for (Element cuboid : manager.getAllElements())
		{
			for (Face face : cuboid.getAllFaces())
			{
				System.out.println(face.getTextureLocation() + " " + face.getTextureName());
				if (!textureList.contains(face.getTextureLocation() + face.getTextureName()))
				{
					textureList.add(face.getTextureLocation() + face.getTextureName());
				}
			}
		}
	}

	private void writeComponents(BufferedWriter writer, ElementManager manager) throws IOException
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
		for (int i = 0; i < manager.getElementCount(); i++)
		{
			writer.newLine();
			writer.write(space(2) + "{");
			writer.newLine();
			writeElement(writer, manager.getElement(i));
			writer.newLine();
			writer.write(space(2) + "}");
			if (i != manager.getElementCount() - 1)
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

	private void writeElement(BufferedWriter writer, Element cuboid) throws IOException
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
		if (cuboid.getRotation() != 0)
		{
			writeRotation(writer, cuboid);
			writer.newLine();
		}
		writeFaces(writer, cuboid);

	}

	private void writeBounds(BufferedWriter writer, Element cuboid) throws IOException
	{
		writer.write(space(3) + "\"from\": [ " + cuboid.getStartX() + ", " + cuboid.getStartY() + ", " + cuboid.getStartZ() + " ], ");
		writer.newLine();
		writer.write(space(3) + "\"to\": [ " + (cuboid.getStartX() + cuboid.getWidth()) + ", " + (cuboid.getStartY() + cuboid.getHeight()) + ", " + (cuboid.getStartZ() + cuboid.getDepth()) + " ], ");
	}

	private void writeShade(BufferedWriter writer, Element cuboid) throws IOException
	{
		writer.write(space(3) + "\"shade\": " + cuboid.isShaded() + ",");
	}

	private void writeRotation(BufferedWriter writer, Element cuboid) throws IOException
	{
		writer.write(space(3) + "\"rotation\": { ");
		writer.write("\"origin\": [ " + cuboid.getOriginX() + ", " + cuboid.getOriginY() + ", " + cuboid.getOriginZ() + " ], ");
		writer.write("\"axis\": \"" + Element.parseAxis(cuboid.getPrevAxis()) + "\", ");
		writer.write("\"angle\": " + cuboid.getRotation());
		if (cuboid.shouldRescale())
		{
			writer.write(", \"rescale\": " + cuboid.shouldRescale());
		}
		writer.write(" },");
	}

	private void writeFaces(BufferedWriter writer, Element cuboid) throws IOException
	{
		writer.write(space(3) + "\"faces\": {");
		writer.newLine();
		for (Face face : cuboid.getAllFaces())
		{
			if (face.isEnabled())
			{
				writer.write(space(4) + "\"" + Face.getFaceName(face.getSide()) + "\": { ");
				writer.write("\"texture\": \"#" + textureList.indexOf(face.getTextureLocation() + face.getTextureName()) + "\"");
				writer.write(", \"uv\": [ " + face.getStartU() + ", " + face.getStartV() + ", " + face.getEndU() + ", " + face.getEndV() + " ]");
				if (face.getRotation() > 0)
					writer.write(", \"rotation\": " + (int) face.getRotation() * 90);
				if (face.isCullfaced())
					writer.write(", \"cullface\": \"" + Face.getFaceName(face.getSide()) + "\"");
				writer.write(" }");
				if (face.getSide() != cuboid.getLastValidFace())
				{
					writer.write(",");
					writer.newLine();
				}
			}
		}
		writer.newLine();
		writer.write(space(3) + "}");
	}

	/*
	 * private void writeChild(BufferedWriter writer) throws IOException {
	 * writer.write("{"); writer.newLine(); writer.write(space(1) +
	 * "\"parent\": \"block/" + modelName + "\","); writer.newLine();
	 * writer.write(space(1) + "\"textures\": {"); writer.newLine(); for (int i
	 * = 0; i < textureList.size(); i++) { writer.write(space(2) + "\"" + i +
	 * "\": \"block/" + textureList.get(i) + "\""); if (i != textureList.size()
	 * - 1) { writer.write(","); } writer.newLine(); } writer.write(space(1) +
	 * "}"); writer.write("}"); }
	 */

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
