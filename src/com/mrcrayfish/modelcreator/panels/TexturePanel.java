package com.mrcrayfish.modelcreator.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.newdawn.slick.opengl.Texture;

import com.mrcrayfish.modelcreator.CuboidManager;
import com.mrcrayfish.modelcreator.Face;
import com.mrcrayfish.modelcreator.texture.ClipboardTexture;
import com.mrcrayfish.modelcreator.texture.TextureCallback;
import com.mrcrayfish.modelcreator.texture.TextureManager;
import com.mrcrayfish.modelcreator.util.Clipboard;

public class TexturePanel extends JPanel implements TextureCallback
{
	private static final long serialVersionUID = 1L;

	private CuboidManager manager;

	private JButton btnSelect;
	private JButton btnClear;
	private JButton btnCopy;
	private JButton btnPaste;

	public TexturePanel(CuboidManager manager)
	{
		this.manager = manager;
		setLayout(new GridLayout(2, 2, 4, 4));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Texture"));
		setMaximumSize(new Dimension(186, 90));
		initComponents();
		addComponents();
	}

	public void initComponents()
	{
		Font defaultFont = new Font("SansSerif", Font.BOLD, 14);

		// textureManager = new TextureManagerDialog(((SidebarPanel)
		// manager).getCreator());

		btnSelect = new JButton("Select...");
		btnSelect.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSelect.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				String texture = TextureManager.display(manager);
				if (texture != null)
				{
					manager.getSelectedCuboid().getSelectedFace().setTexture(texture);
				}
			}
		});
		btnSelect.setFont(defaultFont);

		btnClear = new JButton("Clear");
		btnClear.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					manager.getSelectedCuboid().setAllTextures(null);
				}
				else
				{
					manager.getSelectedCuboid().getSelectedFace().setTexture(null);
				}
			}
		});
		btnClear.setFont(defaultFont);

		btnCopy = new JButton("Copy");
		btnCopy.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				String texture = manager.getSelectedCuboid().getSelectedFace().getTextureName();
				Clipboard.copyTexture(texture);
			}
		});
		btnCopy.setFont(defaultFont);

		btnPaste = new JButton("Paste");
		btnPaste.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				ClipboardTexture texture = Clipboard.getTexture();
				if (texture != null)
				{
					if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
					{
						manager.getSelectedCuboid().setAllTextures(texture.getTexture());
					}
					else
					{
						Face face = manager.getSelectedCuboid().getSelectedFace();
						face.setTexture(texture.getTexture());
						face.setTextureModId(texture.getModid());

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
		if (manager.getSelectedCuboid() != null)
		{
			manager.getSelectedCuboid().getSelectedFace().setTexture(texture);
		}
	}
}
