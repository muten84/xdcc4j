package it.luigibifulco.xdcc4j.downloader.system;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class TrayIconHelper {

	// Obtain the image URL
	protected static Image createImage(String path, String description) {
		URL imageURL = TrayIconHelper.class.getResource(path);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else {
			return (new ImageIcon(imageURL, description)).getImage();
		}
	}

	public static void createAndShowGUI() {
		// Check the SystemTray support
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(createImage("x-mark.png",
				"xDCC Downloader"));
		final SystemTray tray = SystemTray.getSystemTray();

		// Create a popup menu components
		MenuItem launchItem = new MenuItem("Launch...");
		MenuItem aboutItem = new MenuItem("About");
		CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
		CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
		Menu displayMenu = new Menu("Display");
		MenuItem errorItem = new MenuItem("Error");
		MenuItem warningItem = new MenuItem("Warning");
		MenuItem infoItem = new MenuItem("Info");
		MenuItem noneItem = new MenuItem("None");
		MenuItem exitItem = new MenuItem("Exit");

		// Add components to popup menu
		popup.add(launchItem);
		popup.add(aboutItem);
		popup.addSeparator();
		// popup.add(cb1);
		// popup.add(cb2);
		popup.addSeparator();
		popup.add(displayMenu);
		displayMenu.add(errorItem);
		displayMenu.add(warningItem);
		displayMenu.add(infoItem);
		displayMenu.add(noneItem);
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);
		trayIcon.setImageAutoSize(true);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
			return;
		}

		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(null,
				// "Lanciare il browser...");
				launchBrowser();

			}
		});

		launchItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				launchBrowser();
			}
		});

		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(
								null,
								"Xdcc Downloader Web Distribution\n"
										+ "Author: Luigi Bifulco\n"
										+ "Contact: bifulcoluigi@gmail.com"
										+ "Web Site: https://github.com/muten84/xdcc4j/");
			}
		});

		// cb1.addItemListener(new ItemListener() {
		// public void itemStateChanged(ItemEvent e) {
		// int cb1Id = e.getStateChange();
		// if (cb1Id == ItemEvent.SELECTED) {
		// trayIcon.setImageAutoSize(true);
		// } else {
		// trayIcon.setImageAutoSize(false);
		// }
		// }
		// });

		cb2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int cb2Id = e.getStateChange();
				if (cb2Id == ItemEvent.SELECTED) {
					trayIcon.setToolTip("Sun TrayIcon");
				} else {
					trayIcon.setToolTip(null);
				}
			}
		});

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuItem item = (MenuItem) e.getSource();
				// TrayIcon.MessageType type = null;
				System.out.println(item.getLabel());
				if ("Error".equals(item.getLabel())) {
					// type = TrayIcon.MessageType.ERROR;
					trayIcon.displayMessage("Sun TrayIcon Demo",
							"This is an error message",
							TrayIcon.MessageType.ERROR);

				} else if ("Warning".equals(item.getLabel())) {
					// type = TrayIcon.MessageType.WARNING;
					trayIcon.displayMessage("Sun TrayIcon Demo",
							"This is a warning message",
							TrayIcon.MessageType.WARNING);

				} else if ("Info".equals(item.getLabel())) {
					// type = TrayIcon.MessageType.INFO;
					trayIcon.displayMessage("Sun TrayIcon Demo",
							"This is an info message",
							TrayIcon.MessageType.INFO);

				} else if ("None".equals(item.getLabel())) {
					// type = TrayIcon.MessageType.NONE;
					trayIcon.displayMessage("Sun TrayIcon Demo",
							"This is an ordinary message",
							TrayIcon.MessageType.NONE);
				}
			}
		};

		errorItem.addActionListener(listener);
		warningItem.addActionListener(listener);
		infoItem.addActionListener(listener);
		noneItem.addActionListener(listener);

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
				System.exit(0);
			}
		});

	}

	private static void launchBrowser() {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(
						new URI("http://127.0.0.1:8888/xdcc4j/"));
			} catch (IOException | URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
