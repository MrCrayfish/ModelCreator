package com.mrcrayfish.modelcreator;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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

		JPanel container = new JPanel(new BorderLayout(20, 10));
		container.setBorder(new EmptyBorder(10, 10, 10, 10));

		ImageIcon crayfish = new ImageIcon("res/sticker.png");
		container.add(new JLabel(crayfish), BorderLayout.EAST);

		JPanel leftPanel = new JPanel(new BorderLayout());

		JLabel title = new JLabel("<html><b>Model Creator</b> by MrCrayfish</html>");
		title.setFont(customFont);
		leftPanel.add(title, BorderLayout.NORTH);

		JLabel message = new JLabel();
		message.setText("<html>Thank you for downloading my program. I hope it encourages" + " you to create awesome models. If you do create something awesome, I" + " would love to see it. You can post your screenshots to me via Twitter" + " or Facebook. If you are unsure how to use anything works, hover your " + "mouse over the component and it will tell you what it does." + "<br><br>I've put a lot of work into this program, so if you are "
				+ "feeling generious, you can donate by clicking the button below. Thank you!</html>");
		leftPanel.add(message, BorderLayout.CENTER);

		container.add(leftPanel, BorderLayout.CENTER);

		JPanel btnGrid = new JPanel(new GridLayout(1, 4, 5, 0));
		JButton btnDonate = new JButton("Donate");
		btnDonate.addActionListener(a ->
		{
			openUrl("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=HVXLDWFN4MNA2");
		});
		btnGrid.add(btnDonate);

		JButton btnTwitter = new JButton("Twitter");
		btnTwitter.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				openUrl("https://www.twitter.com/MrCraayfish");
			}
			
		});
		btnGrid.add(btnTwitter);

		JButton btnFacebook = new JButton("Facebook");
		btnFacebook.addActionListener(a ->
		{
			openUrl("https://www.facebook.com/MrCrayfish");
		});
		btnGrid.add(btnFacebook);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(a ->
		{
			SwingUtilities.getWindowAncestor(btnClose).dispose();
		});
		btnGrid.add(btnClose);

		// container.add(header, BorderLayout.NORTH);
		container.add(btnGrid, BorderLayout.SOUTH);

		JDialog dialog = new JDialog(parent, "Welcome", false);
		// dialog.setLayout(new BorderLayout());
		dialog.setResizable(false);
		dialog.setPreferredSize(new Dimension(500, 290));
		dialog.add(container);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		dialog.requestFocusInWindow();
	}
	
	private static void openUrl(String url)
	{
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
		{
			try
			{
				desktop.browse(new URL(url).toURI());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
