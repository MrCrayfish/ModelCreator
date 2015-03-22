package com.mrcrayfish.modelcreator.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.texture.PendingTexture;
import com.mrcrayfish.modelcreator.texture.TextureCallback;
import com.mrcrayfish.modelcreator.util.Clipboard;

public class TexturePanel extends JPanel implements TextureCallback
{
	private static final long serialVersionUID = 1L;

	private ModelCreator creator;

	private JButton btnSelect;
	private JButton btnClear;
	private JButton btnCopy;
	private JButton btnPaste;

	public TexturePanel(ModelCreator creator)
	{
		this.creator = creator;
		setLayout(new GridLayout(2, 2, 4, 4));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Texture"));
		setMaximumSize(new Dimension(186, 90));
		initComponents();
		addComponents();
	}

	public void initComponents()
	{
		Font defaultFont = new Font("SansSerif", Font.BOLD, 14);
		
		btnSelect = new JButton("Import");
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
		btnSelect.setFont(defaultFont);

		btnClear = new JButton("Clear");
		btnClear.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					creator.getSelectedCuboid().setAllTextures(null);
				}
				else
				{
					creator.getSelectedCuboid().getSelectedFace().setTexture(null);
				}
			}
		});
		btnClear.setFont(defaultFont);

		btnCopy = new JButton("Copy");
		btnCopy.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				String texture = creator.getSelectedCuboid().getSelectedFace().getTextureName();
				Clipboard.copyTexture(texture);
			}
		});
		btnCopy.setFont(defaultFont);

		btnPaste = new JButton("Paste");
		btnPaste.addActionListener(e ->
		{
			if (creator.getSelectedCuboid() != null)
			{
				String texture = Clipboard.getTexture();
				if (texture != null)
				{
					if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
					{
						creator.getSelectedCuboid().setAllTextures(texture);
					}
					else
					{
						creator.getSelectedCuboid().getSelectedFace().setTexture(texture);
					}
				}
			}
		});
		btnPaste.setFont(defaultFont);
	}

	public void addComponents()
	{
		add(btnSelect);
		add(btnClear);
		add(btnCopy);
		add(btnPaste);
	}

	@Override
	public void callback(String texture)
	{
		if (creator.getSelectedCuboid() != null)
		{
			creator.getSelectedCuboid().getSelectedFace().setTexture(texture);
		}
	}
}
