package com.mrcrayfish.modelcreator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.newdawn.slick.util.ResourceLoader;

public class WelcomeDialog
{
	public static void show(JFrame parent)
	{
		InputStream inputStream = ResourceLoader.getResourceAsStream("res/bebas_neue.otf");
		Font customFont = null;
		try
		{
			customFont = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(24f);
		}
		catch (FontFormatException | IOException e1)
		{
			e1.printStackTrace();
		}

		JPanel container = new JPanel(new BorderLayout(50, 10));
		container.setBorder(new EmptyBorder(10, 10, 10, 10));

		ImageIcon crayfish = new ImageIcon("res/sticker.png");
		container.add(new JLabel(crayfish), BorderLayout.EAST);

		JLabel title = new JLabel("<html><b>Model Creator</b> by MrCrayfish</html>");
		title.setFont(customFont);
		container.add(title, BorderLayout.NORTH);

		JLabel message = new JLabel();
		message.setText("<html>Thank you for downloading my program. I hope it encourages"
				+ " you to create awesome models. If you do create something awesome, I"
				+ " would love to see it. You can post your screenshots to me via Twitter"
				+ " or Facebook. If you are unsure how to use anything works, hover your "
				+ "mouse over the component and it will tell you what it does."
				+ "<br><br>I've put a lot of work into this program, so if you are "
				+ "feeling generious, you can donate by clicking the button below. Thank you!</html>");
		container.add(message, BorderLayout.CENTER);

		JPanel btnGrid = new JPanel(new GridLayout(1, 4, 5, 0));
		JButton btnDonate = new JButton("Donate");
		btnDonate.addActionListener(a ->
		{
			Desktop desktop = Desktop.getDesktop();
			try
			{
				desktop.browse(new URI("http://www.google.com/"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		btnGrid.add(btnDonate);
		
		JButton btnTwitter = new JButton("Twitter");
		btnGrid.add(btnTwitter);
		
		JButton btnFacebook = new JButton("Facebook");
		btnGrid.add(btnFacebook);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(a ->
		{
			SwingUtilities.getWindowAncestor(btnClose).dispose();
		});
		btnGrid.add(btnClose);

		// container.add(header, BorderLayout.NORTH);
		container.add(btnGrid, BorderLayout.SOUTH);

		JDialog dialog = new JDialog(parent, "Welcome", true);
		// dialog.setLayout(new BorderLayout());
		dialog.setResizable(false);
		dialog.setPreferredSize(new Dimension(500, 300));
		dialog.add(container);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
}
