package com.mrcrayfish.modelcreator.util;

import java.text.DecimalFormat;
import java.text.ParseException;

public class Parser
{
	private static DecimalFormat df = new DecimalFormat("#.#");
	public static double parseDouble(String text, double def)
	{
		double value;
		try
		{
			value = df.parse(text).doubleValue();
		}
		catch (ParseException e)
		{
			value = def;
		}
		return value;
	}
}
