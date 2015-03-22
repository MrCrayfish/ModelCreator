package com.mrcrayfish.modelcreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Exporter
{
	public static void export(ModelCreator creator, String name)
	{
		try
		{
			FileWriter fw = new FileWriter(new File(name + ".json"));
			BufferedWriter writer = new BufferedWriter(fw);
			writeComponents(writer, creator);
			writer.close();
			fw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void compileTextureList(ModelCreator creator)
	{
		for(Cuboid cuboid : creator.getAllCuboids())
		{
			for(Face face : cuboid.getAllFaces())
			{
				//face.get
			}
		}
	}
	
	private static void writeComponents(BufferedWriter writer, ModelCreator creator) throws IOException
	{
		writer.write("{");
		writer.newLine();
		writer.write(space(1) + "\"__comment\": \"Model generated using MrCrayfish's Model Creator (http://mrcrayfish.com/modelcreator/)\",");
		writer.newLine();
		writer.write(space(1) + "\"elements\": [");
		for (int i = 0; i < creator.getCuboidCount(); i++)
		{
			writer.newLine();
			writer.write(space(2) + "{");
			writer.newLine();
			writeElement(writer, creator.getCuboid(i));
			writer.newLine();
			writer.write(space(2) + "}");
			if (i != creator.getCuboidCount() - 1)
				writer.write(",");
		}
		writer.newLine();
		writer.write(space(1) + "]");
		writer.newLine();
		writer.write("}");
	}

	private static void writeElement(BufferedWriter writer, Cuboid cuboid) throws IOException
	{
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

	private static void writeBounds(BufferedWriter writer, Cuboid cuboid) throws IOException
	{
		writer.write(space(3) + "\"from\": [ " + cuboid.getStartX() + ", " + cuboid.getStartY() + ", " + cuboid.getStartZ() + " ], ");
		writer.newLine();
		writer.write(space(3) + "\"to\": [ " + (cuboid.getStartX() + cuboid.getWidth()) + ", " + (cuboid.getStartY() + cuboid.getHeight()) + ", " + (cuboid.getStartZ() + cuboid.getDepth()) + " ], ");
	}

	private static void writeShade(BufferedWriter writer, Cuboid cuboid) throws IOException
	{
		writer.write(space(3) + "\"shade\": " + cuboid.isShaded() + ",");
	}

	private static void writeRotation(BufferedWriter writer, Cuboid cuboid) throws IOException
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

	private static void writeFaces(BufferedWriter writer, Cuboid cuboid) throws IOException
	{
		writer.write(space(3) + "\"faces\": {");
		writer.newLine();
		for (Face face : cuboid.getAllFaces())
		{
			writer.write(space(4) + "\"" + Face.getFaceName(face.getSide()) + "\": { ");
			writer.write("\"texture\": \"#placeholder\" ");
			writer.write(", \"uv\": [ " + face.getStartU() + ", " + face.getStartV() + ", " + face.getEndU() + ", " + face.getEndV() + " ] ");
			if (face.isCullfaced())
				writer.write(", \"cullface\": \"" + Face.getFaceName(face.getSide()) + "\" ");
			writer.write("}");
			if (face.getSide() != cuboid.getLastValidFace())
			{
				writer.write(",");
				writer.newLine();
			}
		}
		writer.newLine();
		writer.write(space(3) + "}");
	}

	private static String space(int size)
	{
		String space = "";
		for (int i = 0; i < size; i++)
		{
			space += "    ";
		}
		return space;
	}
}
