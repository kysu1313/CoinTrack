/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabControllers.assistantControllers.tablesAndLists;

import coinClasses.ConnectToDatabase;
import coinClasses.UserCoin;
import interfaces.ListClassInterface;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import tabControllers.AlertMessages;
import static tabControllers.Tab1Controller.DEBUG;

/**
 *
 * @author Kyle
 */
public class ListClass implements ListClassInterface{
    
    private final String UNAME;
    private ListView list;
    private LinkedList<UserCoin> savedCoins;
    private LinkedList<String> onlineUsers;
    private LinkedList<String> friendList;
    
    public ListClass(String _username) {
        this.UNAME = _username;
        this.friendList = new LinkedList<>();
        this.savedCoins = new LinkedList<>();
        this.friendList = new LinkedList<>();
    }
    
    @Override
    public void populateList(ListView _list, LinkedList<String> _items) {
        if (_items != null && _items.size() > 0) {
            for (int i = 0; i < _items.size(); i++) {
                _list.getItems().add(_items.get(i));
            }
        }

    }

    @Override
    public void populateSavedCoins(ListView _savedCoinList) {
        this.list = _savedCoinList;
        ConnectToDatabase conn = new ConnectToDatabase();
        _savedCoinList.getItems().clear();
        this.savedCoins = conn.getSavedCoins(this.UNAME);
        conn.close();
        if (DEBUG){System.out.println("Populating saved coin list");}
        if (this.savedCoins != null && this.savedCoins.size() > 0) {
            for (int i = 0; i < this.savedCoins.size(); i++) {
                _savedCoinList.getItems().add(this.savedCoins.get(i));
                if (DEBUG){System.out.println("Adding: " + this.savedCoins.get(i));}
            }
        }
        createSavedCoinCells(_savedCoinList);
        addRightClick(_savedCoinList);
    }

    @Override
    public void populateOnlineUsers(ListView _onlineUserList) {
        this.list = _onlineUserList;
        ConnectToDatabase conn = new ConnectToDatabase();
        this.onlineUsers = new LinkedList<>();
        this.onlineUsers = conn.getOnlineUsers();
        conn.close();
        if(DEBUG){System.out.println("total online users: " + this.onlineUsers.size());}
        for (int i = 0; i < this.onlineUsers.size(); i++) {
            _onlineUserList.getItems().add(this.onlineUsers.get(i));
        }
        createOnlineUserCells(_onlineUserList);
        _onlineUserList.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                ContextMenu cm;
                if (event.getButton() == MouseButton.SECONDARY) {
                    cm = new ContextMenu();
                    cm.show(_onlineUserList, event.getScreenX(), event.getScreenY());
                }
            }
        });
        addRightClick(_onlineUserList);
    }

    public void populateFriends(ListView _friendList) {
        this.list = _friendList;
        ConnectToDatabase conn = new ConnectToDatabase();
        this.friendList.clear();
        _friendList.getItems().clear();
        this.friendList = conn.getFriendList(this.UNAME);
        conn.close();
        for (int i = 0; i < this.friendList.size(); i++) {
            _friendList.getItems().add(this.friendList.get(i));
        }
        createFriendListCells(_friendList);
        addRightClick(_friendList);
    }
    
    private void createFriendListCells(ListView _list) {
        _list.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem addFriendItem = new MenuItem();
            addFriendItem.textProperty().bind(Bindings.format("Share coin"));
            addFriendItem.setOnAction(event -> {
                ConnectToDatabase conn = new ConnectToDatabase();
                String friendName = cell.getItem();
                // Do stuff
                conn.close();
            });
            MenuItem removeFriendItem = new MenuItem();
            removeFriendItem.textProperty().bind(Bindings.format("Remove Friend"));
            removeFriendItem.setOnAction(event -> {
                ConnectToDatabase conn = new ConnectToDatabase();
                conn.removeFriend(cell.getText());
                if(DEBUG){System.out.println("Removed " + cell.getText() + " from friend list");}
                populateFriends(_list);
                conn.close();
            });
            contextMenu.getItems().addAll(addFriendItem, removeFriendItem);
            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell ;
        });
    }
    
    private void createSavedCoinCells(ListView _list) {
        ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteCoin = new MenuItem();
            deleteCoin.textProperty().bind(Bindings.format("Delete"));
            deleteCoin.setOnAction(event -> {
                ConnectToDatabase conn = new ConnectToDatabase();
                conn.deleteSavedCoin((UserCoin)_list.getSelectionModel().getSelectedItem());
                populateSavedCoins(_list);
                conn.close();
            });
           
            contextMenu.getItems().addAll(deleteCoin);
            _list.setContextMenu(contextMenu);
    }
    
    private void createOnlineUserCells(ListView _list) {
        _list.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem addFriendItem = new MenuItem();
            addFriendItem.textProperty().bind(Bindings.format("Add Friend"));
            addFriendItem.setOnAction(event -> {
                ConnectToDatabase conn = new ConnectToDatabase();
                String friendName = cell.getItem();
                if (friendName.equals(this.UNAME)) {
                    AlertMessages.showErrorMessage("Add FAIL", "WOW, really? Trying to add yourself as a friend?");
                } else {
                    conn.addFriend(this.UNAME, friendName);
                    populateFriends(_list);
                    if(DEBUG){System.out.println("Added " + friendName + " to friend list");}
                    AlertMessages.showInformationMessage("Add Success", "Added " + friendName + " to friend list!");
                }
                conn.close();
            });
            MenuItem sendMessageItem = new MenuItem();
            sendMessageItem.textProperty().bind(Bindings.format("Send Message"));
            sendMessageItem.setOnAction(event -> {
                // Send a message to a friend
            });
            contextMenu.getItems().addAll(addFriendItem, sendMessageItem);
            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell ;
        });
    }
    
    private void addRightClick(ListView _list) {
        _list.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            ContextMenu cmu = new ContextMenu();
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    cmu.show(_list, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }
    
}