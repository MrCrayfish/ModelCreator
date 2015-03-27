package com.mrcrayfish.modelcreator;

import java.util.List;

import com.mrcrayfish.modelcreator.texture.PendingTexture;
import com.mrcrayfish.modelcreator.texture.TextureCallback;

public interface CuboidManager
{
	public Cuboid getSelectedCuboid();

	public List<Cuboid> getAllCuboids();

	public Cuboid getCuboid(int index);

	public int getCuboidCount();

	public void updateName();

	public void updateValues();

	public void addPendingTexture(PendingTexture texture);
	
	public boolean getAmbientOcc();
}
