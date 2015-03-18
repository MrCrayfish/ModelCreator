package com.mrcrayfish.modelcreator.panels;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.newdawn.slick.opengl.Texture;

import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.texture.PendingTexture;
import com.mrcrayfish.modelcreator.texture.TextureLoaderCallback;
import com.mrcrayfish.modelcreator.util.Clipboard;

public class TexturePanel extends JPanel implements TextureLoaderCallback
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

	private JButton btnSelect;
	private JButton btnClear;
	private JButton btnCopy;
	private JButton btnPaste;
	private JButton btnClearAll;
	private JButton btnPasteAll;

	public TexturePanel(ModelCreator creator)
	{
		this.creator = creator;
		setLayout(new GridLayout(3, 2));
		setBorder(BorderFactory.createTitledBorder("Texture"));
		initComponents();
		addComponents();
	}

	public void initComponents()
	{
		btnSelect = new JButton("Select");
		btnSelect.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSelect.addActionListener(e ->
		{
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				try
				{
					creator.pendingTextures.add(new PendingTexture(chooser.getSelectedFile().getAbsolutePath(), this));
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});

		btnClear = new JButton("Clear");
		btnClear.addActionListener(e ->
		{
			creator.getSelectedCube().getSelectedFace().setTexture(null);
		});

		btnCopy = new JButton("Copy");
		btnCopy.addActionListener(e ->
		{
			Texture texture = creator.getSelectedCube().getSelectedFace().getTexture();
			Clipboard.copyTexture(texture);
		});

		btnPaste = new JButton("Paste");
		btnPaste.addActionListener(e ->
		{
			Texture texture = Clipboard.getTexture();
			if (texture != null)
			{
				creator.getSelectedCube().getSelectedFace().setTexture(texture);
			}
		});
		
		btnClearAll = new JButton("Clear All");
		btnClearAll.addActionListener(e ->{
			creator.getSelectedCube().clearAllTextures();
		});
		
		btnPasteAll = new JButton("Paste All");
		btnPasteAll.addActionListener(e ->{
			Texture texture = Clipboard.getTexture();
			if (texture != null)
			{
				creator.getSelectedCube().setAllTextures(texture);
			}
		});
	}

	public void addComponents()
	{
		add(btnSelect);
		add(btnClear);
		add(btnCopy);
		add(btnPaste);
		add(btnClearAll);
		add(btnPasteAll);
	}

	@Override
	public void callback(Texture texture)
	{
		creator.getSelectedCube().getSelectedFace().setTexture(texture);
	}
}
