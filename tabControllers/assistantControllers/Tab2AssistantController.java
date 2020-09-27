

package tabControllers.assistantControllers;

import coinClasses.CoinRankApi;
import coinClasses.ConnectToDatabase;
import coinClasses.SingleCoin;
import coinClasses.UserCoin;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

/**
 * This Class contains additional methods used in Tab2 to display data to the
 * screen.
 *
 * The main purpose of this class is to keep the main tab controllers as clean
 * as possible.
 *
 * @author Kyle
 */
public class Tab2AssistantController {


    private final boolean DEBUG = tabControllers.Tab1Controller.DEBUG;

    /**
     * Creates a LinkedList of SingleCoins.
     *
     * This method has been moved to Tab2AssistantController in an effort to
     * keep this controller as clean as possible.
     */
    public void PieChart(CoinRankApi coinList, int pieChartCoins, ComboBox<String> comboBox, ObservableList<PieChart.Data> pieChartData, PieChart pieChart) {
        // Make sure the thread is finished
        coinList.join();
        LinkedList<SingleCoin> temp = coinList.getSortedCoinList();
        pieChartCoins = Integer.parseInt(comboBox.getValue());
        // Loops over SingleCoin list and adds data to pieChart
        for (int i = 0; i <= pieChartCoins - 1; i++) {
            SingleCoin coin = temp.get(i);
            double price = Double.parseDouble(coin.getPrice());
            // Allow 5 decimal places
            double rounded = (double) Math.round(price * 100000d) / 100000d;
            pieChartData.add(new PieChart.Data(coin.getName(), rounded));
        }
        pieChart.setData(pieChartData);
    }

    public void multiBarChart(BarChart _barChart, LinkedList<LinkedHashMap<Double, String>> _linkedMap, int _numCoins, LinkedList<UserCoin> userCoinList) {
        LinkedList<XYChart.Series> serLst = new LinkedList<>();
        LinkedList<String> colors = new LinkedList<>();
        int len = _linkedMap.size();
        int iter = 0;
        for (int i = 0; i < _linkedMap.size(); i++){
            XYChart.Series series = new XYChart.Series();
            int count = 0;
            for (Map.Entry<Double, String> entry : _linkedMap.get(i).entrySet()) {
                if (count > _numCoins){break;}
                long tempLong = Long.parseLong(entry.getValue());
                Date d = new Date(tempLong);
                String date = "" + d;
                double price = entry.getKey();
                series.getData().add(new XYChart.Data("", price)); // date
                count++;
            }
            series.setName(userCoinList.get(iter).getName());
            iter++;
            serLst.add(series);
            // Create random colors for each bar
            Random rand = new Random();
            colors.add(String.format("#%06x", rand.nextInt(0xffffff + 1)));
        }
            
        
        _barChart.getData().addAll(serLst);
        _linkedMap.forEach((item) -> {
            int count = 0;
            for (Map.Entry<Double, String> entry : item.entrySet()) {
                if (count >= _numCoins) {break;}
                double price = entry.getKey();
                Node n = _barChart.lookup(".data" + count + ".chart-bar");
                n.setStyle("-fx-bar-fill: " + colors.get(count));
                count++;
            }
        });
//        var labels = barChart.xAxis().labels();
//        labels.enabled(false);
    }

    /**
     * Creates a pie chart from given list of SingleCoins .
     */
    public void PieChartDashboard(LinkedList<SingleCoin> coinList, PieChart pieChart) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        // Loops over SingleCoin list and adds data to pieChart
        for (int i = 0; i <= coinList.size() - 1; i++) {
            SingleCoin coin = coinList.get(i);
            double price = Double.parseDouble(coin.getPrice());
            // Allow 5 decimal places
            double rounded = (double) Math.round(price * 100000d) / 100000d;
            pieChartData.add(new PieChart.Data(coin.getName(), rounded));
        }
        pieChart.setData(pieChartData);
    }

    /**
     * Call database returning a list of all users who are online.
     */
    public void addOnlineUsers(LinkedList<String> onlineUsers, ListView onlineUsersListT2) {
        ConnectToDatabase conn = new ConnectToDatabase();
        onlineUsers = conn.getOnlineUsers();
        conn.close();
        for (int i = 0; i < onlineUsers.size(); i++) {
            onlineUsersListT2.getItems().add(onlineUsers.get(i));
        }
    }

    /**
     * This creates the right click menu on the onlineUsers list. It also maps
     * each button to an action.
     */
    public void listCells(ListView onlineUsersListT2, String uname, TextArea txtAreaT2) {
        onlineUsersListT2.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem addFriendItem = new MenuItem();
            addFriendItem.textProperty().bind(Bindings.format("Add Friend"));
            addFriendItem.setOnAction(event -> {
                ConnectToDatabase conn = new ConnectToDatabase();
                String friendName = cell.getItem();
                if (friendName.equals(uname)) {
                    txtAreaT2.setText("Wow, so lonely. Can't add yourself as a friend..");
                } else {
                    conn.addFriend(uname, friendName);
                    txtAreaT2.setText("Added " + friendName + " to friend list!");
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
            return cell;
        });
    }
    
    /**
     * This creates the right click menu on the onlineUsers list. It also maps
     * each button to an action.
     */
    public void listCells(ListView onlineUsersListT2, String uname) {
        onlineUsersListT2.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem addFriendItem = new MenuItem();
            addFriendItem.textProperty().bind(Bindings.format("Add Friend"));
            addFriendItem.setOnAction(event -> {
                ConnectToDatabase conn = new ConnectToDatabase();
                String friendName = cell.getItem();
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
            return cell;
        });
    }

    /**
     * Call database returning a list of friends.
     */
    public void friendList(LinkedList<String> friendList, String uname, ListView friendsListT2) {
        ConnectToDatabase conn = new ConnectToDatabase();
        friendList = conn.getFriendList(uname);
        conn.close();
        for (int i = 0; i < friendList.size(); i++) {
            friendsListT2.getItems().add(friendList.get(i));
        }
    }

    /**
     * Creates right-clickable cells in the friends list in the accordion.
     */
    public void friendListCells(ListView friendsListT2) {
        friendsListT2.setCellFactory(lv -> {
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
            MenuItem sendMessageItem = new MenuItem();
            sendMessageItem.textProperty().bind(Bindings.format("Send Message"));
            sendMessageItem.setOnAction(event -> {
                // Send a message to a friend
            });
            MenuItem removeFriendItem = new MenuItem();
            sendMessageItem.textProperty().bind(Bindings.format("Remove Friend"));
            sendMessageItem.setOnAction(event -> {
                // Send a message to a friend
            });
            contextMenu.getItems().addAll(addFriendItem, sendMessageItem, removeFriendItem);
            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });
    }

    public Label getLineChartCoords(LineChart<String, Number> lineChart) {
        final Axis<String> xAxis = lineChart.getXAxis();
        final Axis<Number> yAxis = lineChart.getYAxis();
        final Label cursorCoords = new Label();
        final Node chartBackground = lineChart.lookup(".chart-plot-background");
        for (Node n : chartBackground.getParent().getChildrenUnmodifiable()) {
            if (n != chartBackground && n != xAxis && n != yAxis) {
                n.setMouseTransparent(true);
            }
        }
        chartBackground.setOnMouseEntered((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(true);
        });
        chartBackground.setOnMouseMoved((MouseEvent mouseEvent) -> {
            cursorCoords.setText(
                    String.format(
                            "(%.2f, %.2f)",
                            xAxis.getValueForDisplay(mouseEvent.getX()),
                            yAxis.getValueForDisplay(mouseEvent.getY())
                    )
            );
        });
        chartBackground.setOnMouseExited((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(false);
        });
        xAxis.setOnMouseEntered((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(true);
        });
        xAxis.setOnMouseMoved((MouseEvent mouseEvent) -> {
            cursorCoords.setText(
                    String.format(
                            "x = %.2f",
                            xAxis.getValueForDisplay(mouseEvent.getX())
                    )
            );
        });
        xAxis.setOnMouseExited((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(false);
        });

        yAxis.setOnMouseEntered((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(true);
        });
        yAxis.setOnMouseMoved((MouseEvent mouseEvent) -> {
            cursorCoords.setText(
                    String.format(
                            "y = %.2f",
                            yAxis.getValueForDisplay(mouseEvent.getY())
                    )
            );
        });
        yAxis.setOnMouseExited((MouseEvent mouseEvent) -> {
            cursorCoords.setVisible(false);
        });

        return cursorCoords;
    }
}

