//package controllers;
//
//import coinTrack.CoinTrack;
//import javafx.fxml.FXML;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.net.URL;
//
//public class SystemTrayController {
//
//    @FXML
//    private MenuItem minimizeToTrayBtn;
//
//    @FXML
//    private void handleMinimizeToTray(ActionEvent event) {
//        sendToTray();
//    }
//
//    public void sendToTray() {
//        //Check the SystemTray is supported
//        if (!SystemTray.isSupported()) {
//            System.out.println("SystemTray is not supported");
//            return;
//        }
//        final PopupMenu popup = new PopupMenu();
//        final TrayIcon trayIcon =
//                new TrayIcon(createImage("images/bulb.gif", "tray icon"));
//        final SystemTray tray = SystemTray.getSystemTray();
//
//        // Create a pop-up menu components
//        MenuItem aboutItem = new MenuItem("About");
//        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
//        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
//        Menu displayMenu = new Menu("Display");
//        MenuItem errorItem = new MenuItem("Error");
//        MenuItem warningItem = new MenuItem("Warning");
//        MenuItem infoItem = new MenuItem("Info");
//        MenuItem noneItem = new MenuItem("None");
//        MenuItem exitItem = new MenuItem("Exit");
//
//        //Add components to pop-up menu
//        popup.add(aboutItem);
//        popup.addSeparator();
//        popup.add(cb1);
//        popup.add(cb2);
//        popup.addSeparator();
//        popup.add(displayMenu);
//        displayMenu.add(errorItem);
//        displayMenu.add(warningItem);
//        displayMenu.add(infoItem);
//        displayMenu.add(noneItem);
//        popup.add(exitItem);
//
//        trayIcon.setPopupMenu(popup);
//
//        try {
//            tray.add(trayIcon);
//        } catch (AWTException e) {
//            System.out.println("TrayIcon could not be added.");
//        }
//    }
//
//    protected static Image createImage(String path, String description) {
//        URL imageURL = CoinTrack.class.getResource(path);
//
//        if (imageURL == null) {
//            System.err.println("Resource not found: " + path);
//            return null;
//        } else {
//            return (new ImageIcon(imageURL, description)).getImage();
//        }
//    }
//}
