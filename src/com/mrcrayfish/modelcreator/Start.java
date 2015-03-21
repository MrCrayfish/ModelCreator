package com.mrcrayfish.modelcreator;

import java.io.File;

import javax.swing.UIManager;

public class Start
{
	public static void main(String[] args)
	{
		System.setProperty("org.lwjgl.util.Debug", "true");
		System.setProperty("org.lwjgl.librarypath", new File("natives/windows").getAbsolutePath());
		try
		{
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		new ModelCreator("Model Creator");
	}
}
