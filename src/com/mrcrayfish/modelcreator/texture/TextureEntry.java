package com.mrcrayfish.modelcreator.texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.newdawn.slick.opengl.Texture;

public class TextureEntry
{
	private String name;
	private String location;
	private List<Texture> texture = new ArrayList<Texture>();
	private List<ImageIcon> image = new ArrayList<ImageIcon>();
	private List<Integer> frames = new ArrayList<Integer>();
	private Map<Integer, Integer> customTimes = new HashMap<Integer, Integer>();
	private int frametime;
	private boolean blurred = false;
	private boolean interpolate = false;

	public TextureEntry(String name, Texture texture, ImageIcon image, String location) {
		this.name = name;
		this.location = location;
		
		this.texture.add(texture);
		this.image.add(image);
		this.frames.add(0);
		
		frametime = 1;
	}
	
	public TextureEntry(String name, String location) {
		this.name = name;
		this.location = location;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public Texture getTexture()
	{
		return texture.get(getCurrentAnimationFrame());
	}
	
	public Texture getNextTexture()
	{
		return texture.get(getNextAnimationFrame());
	}
	
	public ImageIcon getImage()
	{
		return image.get(getCurrentAnimationFrame());
	}

	public void addTexture(Texture texture, ImageIcon image)
	{
		this.texture.add(texture);
		this.image.add(image);
		this.frames.add(this.texture.size()-1);
	}
	
	public void setFrameTime(int frametime) {
		this.frametime = frametime;
	}
	
	public void setFrames(List<Integer> frameList) {
		frames = new ArrayList<Integer>();
		frames.addAll(frameList);
	}
	public void setCustomTimes(Map<Integer, Integer> times) {
		customTimes = new HashMap<Integer, Integer>();
		customTimes.putAll(times);
	}
	
	public void setBlurred(boolean blur) {
		blurred = blur;
	}
	
	public boolean isBlurred() {
		return blurred;
	}
	
	public void setInterpolate(boolean interpolate) {
		this.interpolate = interpolate;
	}
	
	public boolean isInterpolated() {
		return interpolate;
	}
	
	public int getFrameCount() {
		return frames.size();
	}
	
	public int getCurrentAnimationFrame() {
		long maxTime = 0;
		for(int i=0; i<frames.size(); i++) {
			maxTime += getFrameTime(i);
		}
		
		long animTime = System.currentTimeMillis() % maxTime;
		
		for(int i=0; i<frames.size(); i++) {
			if(animTime<=getFrameTime(i)) {
				return frames.get(i);
			}
			animTime -= getFrameTime(i);
		}
		
		return 0;
	}
	
	public int getNextAnimationFrame() {
		long maxTime = 0;
		for(int i=0; i<frames.size(); i++) {
			maxTime += getFrameTime(i);
		}
		
		long animTime = System.currentTimeMillis() % maxTime;
		
		for(int i=0; i<frames.size(); i++) {
			if(animTime<=getFrameTime(i)) {
				if(i<frames.size()-1)
					return frames.get(i + 1);
				else
					return frames.get(0);
			}
			animTime -= getFrameTime(i);
		}
		
		return 0;
	}
	
	public long getFrameTime(int frame) {
		if(customTimes.containsKey(frame)) {
			return customTimes.get(frame) * 50L;
		}
		return frametime * 50L;
	}
	
	public double getFrameInterpolation() {
		long maxTime = 0;
		for(int i=0; i<frames.size(); i++) {
			maxTime += getFrameTime(i);
		}
		
		long animTime = System.currentTimeMillis() % maxTime;
		
		for(int i=0; i<frames.size(); i++) {
			if(animTime<=getFrameTime(i)) {
				double percent = animTime / (double) getFrameTime(i);
				return percent;
			}
			animTime -= getFrameTime(i);
		}
		
		return 0;
	}
}