package com.mrcrayfish.modelcreator;

import org.newdawn.slick.opengl.Texture;

public class Face {

	private Texture texture = TextureManager.cobblestone;
	private double textureX = 0;
	private double textureY = 0;
	private boolean fitTexture = false;

	public Face() {
		

	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public void bindTexture() {
		texture.bind();
	}

	public void addTextureX(double amt) {
		this.textureX += amt;
	}

	public void addTextureY(double amt) {
		this.textureY += amt;
	}

	public double getTextureX() {
		return textureX;
	}

	public double getTextureY() {
		return textureY;
	}
	
	public void fitTexture(boolean fitTexture)
	{
		this.fitTexture = fitTexture;
	}
	
	public boolean shouldFitTexture()
	{
		return fitTexture;
	}
}
