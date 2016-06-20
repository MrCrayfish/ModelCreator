package com.mrcrayfish.modelcreator.dialog;

import com.mrcrayfish.modelcreator.Icons;
import com.mrcrayfish.modelcreator.util.ResourceUtil;
import com.mrcrayfish.modelcreator.util.Util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WelcomeDialog
{
	public WelcomeDialog(){ }

	public static void show(JFrame parent)
	{
		JPanel dialogContent = getDialogContent();
		JDialog welcomeDialog = getWelcomeDialog(parent, dialogContent);
		showDialog(welcomeDialog);
	}

	private static JPanel getDialogContent()
	{
		JPanel container = new JPanel(new BorderLayout(20, 10));
		container.setBorder(new EmptyBorder(10, 10, 10, 10));

		ImageIcon crayfish = new ImageIcon(ResourceUtil.getResource("sticker.png"));
		container.add(new JLabel(crayfish), BorderLayout.EAST);

		JPanel leftPanel = getLeftPanel();
		container.add(leftPanel, BorderLayout.CENTER);

		JPanel btnGrid = getButtonGrid();
		container.add(btnGrid, BorderLayout.SOUTH);
		return container;
	}

	private static JPanel getLeftPanel()
	{
		JPanel leftPanel = new JPanel(new BorderLayout());

		JLabel title = new JLabel("<html><div style=\"font-size:16px;\"><b>Model Creator</b> by MrCrayfish<div></html>");
		leftPanel.add(title, BorderLayout.NORTH);

		JScrollPane message = getScrollableMessage();
		leftPanel.add(message, BorderLayout.CENTER);
		return leftPanel;
	}

	private static JScrollPane getScrollableMessage()
	{
		String message = "Thank you for downloading my program. I hope it encourages"
				+ " you to create awesome models. If you do create something awesome, I"
				+ " would love to see it. You can post your screenshots to me via Twitter"
				+ " or Facebook. If you are unsure how to use anything works, hover your "
				+ "mouse over the component and it will tell you what it does."
				+ "\n\n"
				+ "I've put a lot of work into this program, so if you are "
				+ "feeling generous, you can donate by clicking the button below. Thank you!"
				+ "";
		JTextArea textArea = new JTextArea(message);
		textArea.setEditable(false);
		textArea.setCursor(null);
		textArea.setFocusable(false);
		textArea.setBorder(null);
		textArea.setOpaque(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(null);
		return scrollPane;
	}

	private static JPanel getButtonGrid()
	{
		JPanel btnGrid = new JPanel(new GridLayout(1, 4, 5, 0));
		JButton btnDonate = new JButton("Donate");
		btnDonate.setIcon(Icons.coin);
		btnDonate.addActionListener(a -> Util.openUrl("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=HVXLDWFN4MNA2"));
		btnGrid.add(btnDonate);

		JButton btnTwitter = new JButton("Twitter");
		btnTwitter.setIcon(Icons.twitter);
		btnTwitter.addActionListener(arg0 -> Util.openUrl("https://www.twitter.com/MrCraayfish"));
		btnGrid.add(btnTwitter);

		JButton btnFacebook = new JButton("Facebook");
		btnFacebook.setIcon(Icons.facebook);
		btnFacebook.addActionListener(a -> Util.openUrl("https://www.facebook.com/MrCrayfish"));
		btnGrid.add(btnFacebook);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(a -> SwingUtilities.getWindowAncestor(btnClose).dispose());
		btnGrid.add(btnClose);
		return btnGrid;
	}

	private static JDialog getWelcomeDialog(JFrame parent, JPanel dialogContent)
	{
		JDialog dialog = new JDialog(parent, "Welcome", false);
		dialog.setResizable(false);
		dialog.setPreferredSize(new Dimension(500, 290));
		dialog.add(dialogContent);
		dialog.pack();
		return dialog;
	}

	private static void showDialog(JDialog dialog)
	{
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		dialog.requestFocusInWindow();
	}

}
