/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javafx.scene.control.Alert;

/**
 *
 * @author 
 */
public class AlertMessages {
    
    /**
     * This method is used to show the alert message of Error type.
     * @param title title of alert box
     * @param message to display
     */
    public static void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
    
    /**
     * This method is used to show the alert message of INFORMATION type
     * @param title title of alert box
     * @param message to display
     */
    public static void showInformationMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}