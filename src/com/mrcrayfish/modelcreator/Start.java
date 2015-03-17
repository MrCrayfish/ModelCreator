package com.mrcrayfish.modelcreator;

import javax.swing.UIManager;

public class Start
{
	public static void main(String[] args)
	{
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
