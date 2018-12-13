package com.mrcrayfish.modelcreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;

public class Exporter
{
	/**  decimalformatter for rounding */
	private static final DecimalFormat df = new DecimalFormat("0.00###");
	static {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
	}

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
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
		{
			if (!file.exists())
	     		{
				file.createNewFile();
			}

			writeComponents(writer, manager);

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
				if (face.getTextureName() != null && !face.getTextureName().equals("null") && face.isEnabled())
				{
					if (!textureList.contains(face.getTextureLocation() + face.getTextureName()))
					{
						textureList.add(face.getTextureLocation() + face.getTextureName());
					}
				}
			}
		}
	}

	private void writeComponents(BufferedWriter writer, ElementManager manager) throws IOException
	{
		writer.write("{");
		writer.newLine();
		writer.write(space(1) + "\"__comment\": \"Model generated using MrCrayfish's Model Creator (https://mrcrayfish.com/tools?id=mc)\",");
		writer.newLine();
		if (!manager.getAmbientOcc())
		{
			writer.write("\"ambientocclusion\": " + manager.getAmbientOcc() + ",");
			writer.newLine();
		}
		writeTextures(writer);
		writer.newLine();
		writeDisplay(writer);
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
		if (manager.getParticle() != null)
		{
			writer.write(space(2) + "\"particle\": \"blocks/" + manager.getParticle() + "\"");
			if (textureList.size() > 0)
			{
				writer.write(",");
			}
			writer.newLine();
		}
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
		writer.write(space(3) + "\"from\": [ " + df.format(cuboid.getStartX()) + ", " + df.format(cuboid.getStartY()) + ", " + df.format(cuboid.getStartZ()) + " ], ");
		writer.newLine();
		writer.write(space(3) + "\"to\": [ " + df.format(cuboid.getStartX() + cuboid.getWidth()) + ", " + df.format(cuboid.getStartY() + cuboid.getHeight()) + ", " + df.format(cuboid.getStartZ() + cuboid.getDepth()) + " ], ");
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
			if (face.isEnabled() && textureList.indexOf(face.getTextureLocation() + face.getTextureName()) != -1)
			{
				writer.write(space(4) + "\"" + Face.getFaceName(face.getSide()) + "\": { ");
				writer.write("\"texture\": \"#" + textureList.indexOf(face.getTextureLocation() + face.getTextureName()) + "\"");
				writer.write(", \"uv\": [ " + df.format(face.getStartU()) + ", " + df.format(face.getStartV()) + ", " + df.format(face.getEndU()) + ", " + df.format(face.getEndV()) + " ]");
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

	private void writeDisplay(BufferedWriter writer) throws IOException
	{
		writer.write(space(1) + "\"display\": {");
		writer.newLine();

		writer.write(space(2) + "\"gui\": {");
		writer.newLine();
		writer.write(space(3) + "\"rotation\": [ 30, 225, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"translation\": [ 0, 0, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"scale\": [ 0.625, 0.625, 0.625 ]");
		writer.newLine();
		writer.write(space(2) + "},");
		writer.newLine();

		writer.write(space(2) + "\"ground\": {");
		writer.newLine();
		writer.write(space(3) + "\"rotation\": [ 0, 0, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"translation\": [ 0, 3, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"scale\": [ 0.25, 0.25, 0.25 ]");
		writer.newLine();
		writer.write(space(2) + "},");
		writer.newLine();

		writer.write(space(2) + "\"fixed\": {"); // Item frames
		writer.newLine();
		writer.write(space(3) + "\"rotation\": [ 0, 0, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"translation\": [ 0, 0, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"scale\": [ 0.5, 0.5, 0.5 ]");
		writer.newLine();
		writer.write(space(2) + "},");
		writer.newLine();

		writer.write(space(2) + "\"thirdperson_righthand\": {");
		writer.newLine();
		writer.write(space(3) + "\"rotation\": [ 75, 45, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"translation\": [ 0, 2.5, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"scale\": [ 0.375, 0.375, 0.375 ]");
		writer.newLine();
		writer.write(space(2) + "},");
		writer.newLine();

		writer.write(space(2) + "\"thirdperson_lefthand\": {");
		writer.newLine();
		writer.write(space(3) + "\"rotation\": [ 75, 255, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"translation\": [ 0, 2.5, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"scale\": [ 0.375, 0.375, 0.375 ]");
		writer.newLine();
		writer.write(space(2) + "},");
		writer.newLine();

		writer.write(space(2) + "\"firstperson_righthand\": {");
		writer.newLine();
		writer.write(space(3) + "\"rotation\": [ 0, 45, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"translation\": [ 0, 2.5, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"scale\": [ 0.4, 0.4, 0.4 ]");
		writer.newLine();
		writer.write(space(2) + "},");
		writer.newLine();

		writer.write(space(2) + "\"firstperson_lefthand\": {");
		writer.newLine();
		writer.write(space(3) + "\"rotation\": [ 0, 225, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"translation\": [ 0, 2.5, 0 ],");
		writer.newLine();
		writer.write(space(3) + "\"scale\": [ 0.4, 0.4, 0.4 ]");
		writer.newLine();
		writer.write(space(2) + "}");
		writer.newLine();

		writer.write(space(1) + "},");
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
