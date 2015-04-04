package com.mrcrayfish.modelcreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.texture.PendingTexture;
import com.mrcrayfish.modelcreator.texture.TextureCallback;
import com.mrcrayfish.modelcreator.texture.TextureManager;

public class Importer
{
	private Map<String, String> textureMap = new HashMap<String, String>();
	private String[] faceNames = {"north","east","south","west","up","down"};

	// Input File
	private String inputPath;

	// Model Variables
	private ElementManager manager;

	public Importer(ElementManager manager, String outputPath)
	{
		this.manager = manager;
		this.inputPath = outputPath;
		//compileTextureList();
	}

	public void importFromJSON()
	{
		File path = new File(inputPath);
		if (path.exists() && path.isFile())
		{
			FileReader fr;
			BufferedReader reader;
			try
			{
				fr = new FileReader(path);
				reader = new BufferedReader(fr);
				readComponents(reader, manager);
				reader.close();
				fr.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void readComponents(BufferedReader reader, ElementManager manager) throws IOException {
		manager.clearElements();
		
		JsonParser parser = new JsonParser();
		JsonElement read = parser.parse(reader);
		System.out.println(read);
		
		if(read.isJsonObject()) {
			JsonObject obj = read.getAsJsonObject();
			
			if(obj.has("textures") && obj.get("textures").isJsonObject()) {
				JsonObject textures = obj.get("textures").getAsJsonObject();
				
				for(Entry<String, JsonElement> entry : textures.entrySet()) {
					if(entry.getValue().isJsonPrimitive()) {
						String texture = entry.getValue().getAsString();
						textureMap.put(entry.getKey(), texture);
						
						if(new File(ModelCreator.texturePath+File.separator+texture+".png").exists()) {
							ModelCreator.instance.addPendingTexure(new PendingTexture(ModelCreator.texturePath+File.separator+texture+".png", new TextureCallback(){
								@Override
								public void callback(boolean success, String texture) {}
							}));
						}
					}
				}
			}
			
			if(obj.has("elements") && obj.get("elements").isJsonArray()) {
				JsonArray elements = obj.get("elements").getAsJsonArray();
				
				for(int i=0; i<elements.size(); i++) {
					if(elements.get(i).isJsonObject()) {
						readElement(elements.get(i).getAsJsonObject(), manager);
					}
				}
			}
		}
	}
	
	private void readElement(JsonObject obj, ElementManager manager) {
		JsonArray from = null;
		JsonArray to = null;
		if(obj.has("from") && obj.get("from").isJsonArray()) {
			from = obj.get("from").getAsJsonArray();
		}
		if(obj.has("to") && obj.get("to").isJsonArray()) {
			to = obj.get("to").getAsJsonArray();
		}
		
		if(from!=null && to!=null) {
			double x = from.get(0).getAsDouble();
			double y = from.get(1).getAsDouble();
			double z = from.get(2).getAsDouble();
			
			double w = to.get(0).getAsDouble() - x;
			double h = to.get(1).getAsDouble() - y;
			double d = to.get(2).getAsDouble() - z;
			
			Element element = new Element(w, h, d);
			element.setStartX(x);
			element.setStartY(y);
			element.setStartZ(z);
			
			if(obj.has("rotation") && obj.get("rotation").isJsonObject()) {
				JsonObject rot = obj.get("rotation").getAsJsonObject();
				
				if(rot.has("origin") && rot.get("origin").isJsonArray()) {
					JsonArray origin = rot.get("origin").getAsJsonArray();
					
					double ox = origin.get(0).getAsDouble();
					double oy = origin.get(1).getAsDouble();
					double oz = origin.get(2).getAsDouble();
					
					element.setOriginX(ox);
					element.setOriginY(oy);
					element.setOriginZ(oz);
				}
				
				if(rot.has("axis") && rot.get("axis").isJsonPrimitive()) {
					element.setPrevAxis(Element.parseAxisString(rot.get("axis").getAsString()));
				}
				
				if(rot.has("angle") && rot.get("angle").isJsonPrimitive()) {
					element.setRotation(rot.get("angle").getAsDouble());
				}
				
				if(rot.has("rescale") && rot.get("rescale").isJsonPrimitive()) {
					element.setRescale(rot.get("rescale").getAsBoolean());
				}
			}
			
			for(Face face : element.getAllFaces()) {
				face.setEnabled(false);
			}
			
			if(obj.has("faces") && obj.get("faces").isJsonObject()) {
				JsonObject faces = obj.get("faces").getAsJsonObject();
				
				for(String name : faceNames) {
					if(faces.has(name) && faces.get(name).isJsonObject()) {
						readFace(faces.get(name).getAsJsonObject(), name, element);
					}
				}
			}
			
			manager.addElement(element);
		}
	}
	
	private void readFace(JsonObject obj, String name, Element element) {
		Face face = null;
		for(Face f : element.getAllFaces()) {
			if(f.getSide()==Face.getFaceSide(name)) {
				face = f;
			}
		}
		
		if(face!=null) {
			face.setEnabled(true);
			
			//TODO texture,shade,cullface,....
			if(obj.has("uv") && obj.get("uv").isJsonArray()) {
				JsonArray uv = obj.get("uv").getAsJsonArray();
				
				double uStart = uv.get(0).getAsDouble();
				double vStart = uv.get(1).getAsDouble();
				double uEnd = uv.get(2).getAsDouble();
				double vEnd = uv.get(3).getAsDouble();
				
				face.setStartU(uStart);
				face.setStartV(vStart);
				//TODO - enable changing of uv end? Right now it just assumes it to be the width/height of the face
			}
			
			if(obj.has("texture") && obj.get("texture").isJsonPrimitive()) {
				String loc = obj.get("texture").getAsString().replace("#", "");
				
				if(textureMap.containsKey(loc)) {
					String tloc = textureMap.get(loc);
					String location = tloc.substring(0, tloc.lastIndexOf('/')+1);
					String tname = tloc.replace(location, "");
					
					face.setTextureLocation(location);
					face.setTexture(tname);
				}
			}
		}
	}
}
