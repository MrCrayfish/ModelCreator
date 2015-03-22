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
	}
	
	public void assignChild(String childName)
	{
		this.childName = childName;
	}
	
	public void setModId(String modid)
	{
		this.modid = modid;
	}
	
	public void export(CuboidManager manager, String name)
	{
		try
		{
			FileWriter fw = new FileWriter(new File(name + ".json"));
			BufferedWriter writer = new BufferedWriter(fw);
			writeComponents(writer, manager);
			writer.close();
			fw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void compileTextureList(CuboidManager manager)
	{
		for(Cuboid cuboid : manager.getAllCuboids())
		{
			for(Face face : cuboid.getAllFaces())
			{
				//face.get
			}
		}
	}
	
	private void writeComponents(BufferedWriter writer, CuboidManager manager) throws IOException
	{
		writer.write("{");
		writer.newLine();
		writer.write(space(1) + "\"__comment\": \"Model generated using MrCrayfish's Model manager (http://mrcrayfish.com/modelmanager/)\",");
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

	private void writeElement(BufferedWriter writer, Cuboid cuboid) throws IOException
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

	private void writeBounds(BufferedWriter writer, Cuboid cuboid) throws IOException
	{
		writer.write(space(3) + "\"from\": [ " + cuboid.getStartX() + ", " + cuboid.getStartY() + ", " + cuboid.getStartZ() + " ], ");
		writer.newLine();
		writer.write(space(3) + "\"to\": [ " + (cuboid.getStartX() + cuboid.getWidth()) + ", " + (cuboid.getStartY() + cuboid.getHeight()) + ", " + (cuboid.getStartZ() + cuboid.getDepth()) + " ], ");
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
