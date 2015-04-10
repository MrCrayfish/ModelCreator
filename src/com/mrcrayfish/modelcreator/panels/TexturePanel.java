package com.mrcrayfish.modelcreator.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;
import com.mrcrayfish.modelcreator.texture.ClipboardTexture;
import com.mrcrayfish.modelcreator.texture.TextureCallback;
import com.mrcrayfish.modelcreator.texture.TextureManager;
import com.mrcrayfish.modelcreator.util.Clipboard;

public class TexturePanel extends JPanel implements TextureCallback
{
	private static final long serialVersionUID = 1L;

	private ElementManager manager;

	private JButton btnSelect;
	private JButton btnClear;
	private JButton btnCopy;
	private JButton btnPaste;

	public TexturePanel(ElementManager manager)
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

		btnSelect = new JButton("Image...");
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
		btnSelect.setToolTipText("Opens the Texture Manager");

		btnClear = new JButton("Clear");
		btnClear.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1)
				{
					manager.getSelectedCuboid().setAllTextures(null, null);
				}
				else
				{
					manager.getSelectedCuboid().getSelectedFace().setTexture(null);
				}
			}
		});
		btnClear.setFont(defaultFont);
		btnClear.setToolTipText("<html>Clears the texture from this face.<br><b>Hold shift to clear all faces</b></html>");

		btnCopy = new JButton("Copy");
		btnCopy.addActionListener(e ->
		{
			if (manager.getSelectedCuboid() != null)
			{
				Face face = manager.getSelectedCuboid().getSelectedFace();
				Clipboard.copyTexture(face.getTextureLocation(), face.getTextureName());
			}
		});
		btnCopy.setFont(defaultFont);
		btnCopy.setToolTipText("Copies the texture on this face to clipboard");

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
						manager.getSelectedCuboid().setAllTextures(texture.getLocation(), texture.getTexture());
					}
					else
					{
						Face face = manager.getSelectedCuboid().getSelectedFace();
						face.setTexture(texture.getTexture());
						face.setTextureLocation(texture.getLocation());
					}
				}
			}
		});
		btnPaste.setFont(defaultFont);
		btnPaste.setToolTipText("<html>Pastes the clipboard texture to this face.<br><b>Hold shift to paste to all faces</b></html>");
	}

	public void addComponents()
	{
		add(btnSelect);
		add(btnClear);
		add(btnCopy);
		add(btnPaste);
	}

	@Override
	public void callback(boolean success, String texture)
	{
		if (success)
			if (manager.getSelectedCuboid() != null)
			{
				manager.getSelectedCuboid().getSelectedFace().setTexture(texture);
			}
	}
}
