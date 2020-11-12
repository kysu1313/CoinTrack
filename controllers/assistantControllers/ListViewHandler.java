/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.assistantControllers;

import models.ConnectToDatabase;
import models.SingleCoin;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import controllers.AlertMessages;

/**
 *
 * @author Kyle
 */
public class ListViewHandler {
    
    private ListView list;
    private TableView table;
    private ContextMenu cmu;
    private MenuItem item1;
    private MenuItem item2;
    private MenuItem item3;
    private String uname;
    
    public ListViewHandler(ListView _lst, String _username, String _option1, String _option2, String _option3) {
        this.list = _lst;
        this.uname = _username;
        addRightClick(_option1, _option2, _option3);
    }
    
    public ListViewHandler(TableView<SingleCoin>  table, String _username, String _option1, String _option2, String _option3) {
        this.table = table;
        this.uname = _username;
    }
    
    private void addRightClick(String _option1, String _option2, String _option3) {
        ContextMenu cmu = new ContextMenu();
        this.item1 = new MenuItem();
        this.item2 = new MenuItem();
        this.item3 = new MenuItem();
        item1.textProperty().bind(Bindings.format(_option1));
        item2.textProperty().bind(Bindings.format(_option2));
        item3.textProperty().bind(Bindings.format(_option3));
        
        list.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
//            cmu.getItems().addAll(item1, item2, item3);
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    cmu.show(list, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }
    
    public void tableCells(TableView<SingleCoin>  table) {
        item1.setOnAction(event -> {
            SingleCoin item = table.getSelectionModel().getSelectedItem();
                saveCoin(this.uname, item.getId());
        });
    }
    
    private void saveCoin(String userName, int coinID) {
        ConnectToDatabase dbConn = new ConnectToDatabase();
        if (dbConn.insertSavedCoin(userName, coinID)) {
            AlertMessages.showInformationMessage("Save Coin", "Coin saved successfully.");
        }
        dbConn.close();
    }
    
}
