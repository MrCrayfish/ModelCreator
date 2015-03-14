package com.mrcrayfish.modelcreator;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class ShapeList implements ListModel<Shape>
{
	private List<Shape> shapes = new ArrayList<Shape>();
	
	@Override
	public int getSize()
	{
		return shapes.size();
	}

	@Override
	public Shape getElementAt(int index)
	{
		return shapes.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {}

	@Override
	public void removeListDataListener(ListDataListener l) {}
}
