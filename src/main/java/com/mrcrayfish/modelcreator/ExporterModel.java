package com.mrcrayfish.modelcreator;

import com.mrcrayfish.modelcreator.display.DisplayProperties;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.texture.TextureEntry;

import javax.xml.soap.Text;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExporterModel extends Exporter
{
    private Map<String, String> textureMap = new HashMap<>();
    private boolean optimize = true;
    private boolean includeNames = true;
    private boolean displayProps = true;
    private boolean includeNonTexturedFaces = false;

    public ExporterModel(ElementManager manager)
    {
        super(manager);
        compileTextureList();
    }

    public void setOptimize(boolean optimize)
    {
        this.optimize = optimize;
    }

    public void setIncludeNames(boolean includeNames)
    {
        this.includeNames = includeNames;
    }

    public void setDisplayProps(boolean displayProps)
    {
        this.displayProps = displayProps;
    }

    public void setIncludeNonTexturedFaces(boolean includeNonTexturedFaces)
    {
        this.includeNonTexturedFaces = includeNonTexturedFaces;
    }

    private void compileTextureList()
    {
        for(Element cuboid : manager.getAllElements())
        {
            for(Face face : cuboid.getAllFaces())
            {
                if(face.getTexture() != null && face.isEnabled() && (!optimize || face.isVisible(manager)))
                {
                    TextureEntry entry = face.getTexture();
                    textureMap.put(entry.getId(), entry.getModId() + ":" + entry.getDirectory() + "/" + entry.getName());
                }
            }
        }
    }

    @Override
    protected void write(BufferedWriter writer) throws IOException
    {
        writer.write("{");
        writer.newLine();

        writer.write(space(1) + "\"__comment\": \"Model generated using MrCrayfish's Model Creator (https://mrcrayfish.com/tools?id=mc)\",");
        writer.newLine();

        if(!manager.getAmbientOcc())
        {
            writer.write("\"ambientocclusion\": " + manager.getAmbientOcc() + ",");
            writer.newLine();
        }

        writeTextures(writer);
        writer.newLine();

        if(displayProps)
        {
            writeDisplayProperties(writer);
            writer.newLine();
        }

        writer.write(space(1) + "\"elements\": [");

        for(int i = 0; i < manager.getElementCount() - 1; i++)
        {
            writeElement(writer, manager.getElement(i));
            writer.write(",");
        }
        if(manager.getElementCount() > 0)
        {
            writeElement(writer, manager.getElement(manager.getElementCount() - 1));
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
        if(manager.getParticle() != null)
        {
            TextureEntry entry = manager.getParticle();
            writer.write(space(2) + "\"particle\": \"" + entry.getModId() + ":");
            if(!entry.getDirectory().isEmpty())
            {
                writer.write(entry.getDirectory() + "/");
            }
            writer.write(entry.getName() + "\"");
            if(textureMap.size() > 0)
            {
                writer.write(",");
            }
            writer.newLine();
        }

        List<String> ids = new ArrayList<>(textureMap.keySet());
        for(int i = 0; i < ids.size() - 1; i++)
        {
            String id = ids.get(i);
            String texture = textureMap.get(id);
            writer.write(space(2) + "\"" + id + "\": \"" + texture + "\"");
            writer.write(",");
            writer.newLine();
        }
        if(ids.size() > 0)
        {
            String id = ids.get(ids.size() - 1);
            String texture = textureMap.get(id);
            writer.write(space(2) + "\"" + id + "\": \"" + texture + "\"");
            writer.newLine();
        }

        writer.write(space(1) + "},");
    }

    private void writeElement(BufferedWriter writer, Element cuboid) throws IOException
    {
        writer.newLine();
        writer.write(space(2) + "{");
        writer.newLine();
        if(includeNames)
        {
            writer.write(space(3) + "\"name\": \"" + cuboid.getName() + "\",");
            writer.newLine();
        }
        writeBounds(writer, cuboid);
        writer.newLine();
        if(!cuboid.isShaded())
        {
            writeShade(writer, cuboid);
            writer.newLine();
        }
        if(cuboid.getRotation() != 0)
        {
            writeRotation(writer, cuboid);
            writer.newLine();
        }
        writeFaces(writer, cuboid);
        writer.newLine();
        writer.write(space(2) + "}");
    }

    private void writeBounds(BufferedWriter writer, Element cuboid) throws IOException
    {
        writer.write(space(3) + "\"from\": [ " + FORMAT.format(cuboid.getStartX()) + ", " + FORMAT.format(cuboid.getStartY()) + ", " + FORMAT.format(cuboid.getStartZ()) + " ], ");
        writer.newLine();
        writer.write(space(3) + "\"to\": [ " + FORMAT.format(cuboid.getStartX() + cuboid.getWidth()) + ", " + FORMAT.format(cuboid.getStartY() + cuboid.getHeight()) + ", " + FORMAT.format(cuboid.getStartZ() + cuboid.getDepth()) + " ], ");
    }

    private void writeShade(BufferedWriter writer, Element cuboid) throws IOException
    {
        writer.write(space(3) + "\"shade\": " + cuboid.isShaded() + ",");
    }

    private void writeRotation(BufferedWriter writer, Element cuboid) throws IOException
    {
        writer.write(space(3) + "\"rotation\": { ");
        writer.write("\"origin\": [ " + cuboid.getOriginX() + ", " + cuboid.getOriginY() + ", " + cuboid.getOriginZ() + " ], ");
        writer.write("\"axis\": \"" + Element.parseAxis(cuboid.getRotationAxis()) + "\", ");
        writer.write("\"angle\": " + cuboid.getRotation());
        if(cuboid.shouldRescale())
        {
            writer.write(", \"rescale\": " + cuboid.shouldRescale());
        }
        writer.write(" },");
    }

    private void writeFaces(BufferedWriter writer, Element cuboid) throws IOException
    {
        writer.write(space(3) + "\"faces\": {");
        writer.newLine();

        /* Creates a list of all the valid faces to export */
        List<Face> validFaces = new ArrayList<>();
        for(Face face : cuboid.getAllFaces())
        {
            if(face.isEnabled() && (includeNonTexturedFaces || face.getTexture() != null) && !optimize || face.isVisible(manager))
            {
                validFaces.add(face);
            }
        }

        /* Writes the valid faces to the writer */
        for(int i = 0; i < validFaces.size() - 1; i++)
        {
            Face face = validFaces.get(i);
            writeFace(writer, face);
            writer.write(",");
            writer.newLine();
        }
        if(validFaces.size() > 0)
        {
            writeFace(writer, validFaces.get(validFaces.size() - 1));
        }

        writer.newLine();
        writer.write(space(3) + "}");
    }

    private void writeFace(BufferedWriter writer, Face face) throws IOException
    {
        writer.write(space(4) + "\"" + Face.getFaceName(face.getSide()) + "\": { ");
        if(face.getTexture() != null)
        {
            writer.write("\"texture\": \"#" + face.getTexture().getId() + "\"");
            writer.write(", \"uv\": [ " + FORMAT.format(face.getStartU()) + ", " + FORMAT.format(face.getStartV()) + ", " + FORMAT.format(face.getEndU()) + ", " + FORMAT.format(face.getEndV()) + " ]");
            if(face.getRotation() > 0)
            {
                writer.write(", \"rotation\": " + face.getRotation() * 90);
            }
            if(face.isCullfaced())
            {
                writer.write(", \"cullface\": \"" + Face.getFaceName(face.getSide()) + "\"");
            }
        }
        writer.write(" }");
    }

    private void writeDisplayProperties(BufferedWriter writer) throws IOException
    {
        Map<String, DisplayProperties.Entry> entries = manager.getDisplayProperties().getEntries();
        List<String> ids = new ArrayList<>();
        entries.forEach((s, entry) ->
        {
            if(entry.isEnabled())
            {
                ids.add(s);
            }
        });

        writer.write(space(1) + "\"display\": {");
        writer.newLine();

        //TODO manually order

        for(int i = 0; i < ids.size() - 1; i++)
        {
            String key = ids.get(i);
            writeDisplayEntry(writer, key, entries.get(key));
            writer.write(",");
            writer.newLine();
        }
        if(ids.size() > 0)
        {
            String key = ids.get(ids.size() - 1);
            writeDisplayEntry(writer, key, entries.get(key));
        }

        writer.newLine();
        writer.write(space(1) + "},");
    }

    private void writeDisplayEntry(BufferedWriter writer, String id, DisplayProperties.Entry entry) throws IOException
    {
        writer.write(space(2) + "\"" + id + "\": {");
        writer.newLine();
        writer.write(space(3) + String.format("\"rotation\": [ %s, %s, %s ],", FORMAT.format(entry.getRotationX()), FORMAT.format(entry.getRotationY()), FORMAT.format(entry.getRotationZ())));
        writer.newLine();
        writer.write(space(3) + String.format("\"translation\": [ %s, %s, %s ],", FORMAT.format(entry.getTranslationX()), FORMAT.format(entry.getTranslationY()), FORMAT.format(entry.getTranslationZ())));
        writer.newLine();
        writer.write(space(3) + String.format("\"scale\": [ %s, %s, %s ]", FORMAT.format(entry.getScaleX()), FORMAT.format(entry.getScaleY()), FORMAT.format(entry.getScaleZ())));
        writer.newLine();
        writer.write(space(2) + "}");
    }
}
