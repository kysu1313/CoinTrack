package models.viewModels;

import models.SingleCoin;
import models.UserCoin;
import interfaces.TableInterface;
import java.lang.reflect.Field;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import static controllers.Tab1Controller.DEBUG;
import controllers.assistantControllers.TabAssistantController;
import models.CoinRankApi;
import models.SingleMarket;

/**
 * General class for making & displaying tables.
 *
 * @author Kyle
 * @param <T>
 */
public class TableClass<T> implements TableInterface{

    private final TableView TABLE_VIEW;
    private final String CURRENCY;
    private final long CURRENCY_RATE;
    private final LinkedList<String> COLUMN_NAMES;
    private final WebView WEB_VIEW;
    private CoinRankApi cri;
    private LinkedList<SingleCoin> coinList;
    private static LinkedList<SingleCoin> staticCoinList;
    // List of columns to be added
    private LinkedList<TableColumn> cols;
    private ObservableList<Object> obvObjList;

    private LinkedList<T> listT;
    private LinkedList<?> openList;
    private ObservableList<SingleCoin> obvList;

    private ObservableList<SingleCoin> obvSingleCoinList;
    private ObservableList<UserCoin> obvUserCoinList;
    private ObservableList<SingleMarket> obvsingleMarketList;
    private TableColumn changeCol;
    private Class myClass;
    private LinkedList<Object> objList;
    private TabAssistantController tas;


    public TableClass(String _classType, LinkedList<T> _objList, TableView _tableViewT1, WebView _webView, LinkedList<String> _columnNames, long _currencyRate) {

        this.obvSingleCoinList = FXCollections.observableArrayList();
        this.obvUserCoinList = FXCollections.observableArrayList();
        this.obvsingleMarketList = FXCollections.observableArrayList();

        this.tas = new TabAssistantController();

        if(_classType.equals("SingleCoin")){
            this.myClass = SingleCoin.class;
            this.openList = (LinkedList<SingleCoin>)_objList;
        } else if(_classType.equals("UserCoin")){
            this.myClass = UserCoin.class;
            this.openList = (LinkedList<UserCoin>)_objList;
        } else if (_classType.equals("SingleMarket")){
            this.myClass = SingleMarket.class;
            this.openList = (LinkedList<SingleMarket>)_objList;
        }

        this.TABLE_VIEW = _tableViewT1;
        this.CURRENCY = null;
        this.COLUMN_NAMES = _columnNames;
        this.CURRENCY_RATE = _currencyRate;
        this.WEB_VIEW = _webView;
        staticCoinList = CoinRankApi.getStaticCoinList();
        this.coinList = null;
        this.cols = new LinkedList<>();
        buildTableGeneral();
    }

    /**
     * The table created by this constructor includes a WebView that displays
     * the logo of the coin when it is double clicked.
     *
     * Mainly used in Tab 1
     *
     * @param _tableViewT1
     * @param _coinList
     * @param _webView
     * @param _columnNames
     * @param _currency
     * @param _currencyRate
     */
    public TableClass(TableView _tableViewT1, LinkedList<SingleCoin> _coinList, WebView _webView, LinkedList<String> _columnNames, String _currency, long _currencyRate) {

//        this.cri = new CoinRankApi();
//        this.cri.join();
//        this.coinList = this.cri.getCoinList();



        this.TABLE_VIEW = _tableViewT1;
        this.CURRENCY = _currency;
        this.COLUMN_NAMES = _columnNames;
        this.CURRENCY_RATE = _currencyRate;
        this.WEB_VIEW = _webView;
        staticCoinList = CoinRankApi.getStaticCoinList();
        this.coinList = _coinList;
        this.cols = new LinkedList<>();
        buildTable();
    }

    /**
     * This table is a slimmed down version that does not include a WebView.
     *
     * Currently it is used on the dashboard.
     *
     * @param _tableViewT1
     * @param _coinList
     * @param _columnNames
     */
    public TableClass(TableView<SingleCoin> _tableViewT1, LinkedList<SingleCoin> _coinList, LinkedList<String> _columnNames) {
        this.TABLE_VIEW = _tableViewT1;
        this.COLUMN_NAMES = _columnNames;
        this.CURRENCY = "USD";
        this.CURRENCY_RATE = 1;
        this.WEB_VIEW = null;
        this.coinList = _coinList;
        this.cols = new LinkedList<>();
        buildTable();
    }

    private void buildTableGeneral() {
//        Class<SingleCoin> sclass = SingleCoin.class;
        this.COLUMN_NAMES.forEach((item) -> {
            TableColumn col1 = new TableColumn(item);
            this.cols.add(col1);
        });
        Field[] params = this.myClass.getDeclaredFields();
        for (int i = 0; i < params.length; i++) {
            System.out.println(params[i].getName());

        }
        this.cols.forEach((item) -> {
            for (Field fld : params) {
                if (item.getText().toLowerCase().replace(" ", "").equals(fld.getName().toLowerCase())) {
                    item.setCellValueFactory(new PropertyValueFactory<>(fld.getName()));
                    System.out.println(fld.getName());
                }
            }
        });
        setCurrency();
        // Add columns to tableView
        this.TABLE_VIEW.getColumns().addAll(this.cols);
        this.obvObjList = FXCollections.observableArrayList(this.openList);
    }

    /**
     * This method creates the columns for the table.
     *
     * The parameter names for the class of objects to be displayed are linked
     * to each column. i.e: column1.getName() == class.parameter.name
     *
     * The column names must be the same as the parameter names. They can,
     * however, have capital letters and spaces.
     *
     * @param colList
     */
    private void buildTable() {
        Class<SingleCoin> sclass = SingleCoin.class;
        this.COLUMN_NAMES.forEach((item) -> {
            TableColumn col1 = new TableColumn(item);
            this.cols.add(col1);
        });
        Field[] params = sclass.getDeclaredFields();

        this.cols.forEach((item) -> {
            for (Field fld : params) {
                if (item.getText().toLowerCase().replace(" ", "").equals(fld.getName())) {
                    item.setCellValueFactory(new PropertyValueFactory<>(item.getText().toLowerCase().replace(" ", "")));
                }
            }
        });
        setCurrency();
        // Add columns to tableView
        this.TABLE_VIEW.getColumns().addAll(this.cols);
        this.obvList = FXCollections.observableArrayList(this.coinList);
    }

    /**
     * Add red / green colored text to indicate price changes.
     * @param _colorUp
     * @param _colorDown
     */
    @Override
    public void colorChangeCol(String _colorUp, String _colorDown) {
        this.cols.forEach((item) -> {
            if (item.getText().equalsIgnoreCase("change")) {
                item.setCellFactory(new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn param) {
                        return new TableCell<SingleCoin, Number>() {
                            @Override
                            protected void updateItem(Number item, boolean empty) {
                                // calling super here is very important - don't skip this!
                                super.updateItem(item, empty);
                                // Change color based on data
                                if (!isEmpty()) {
                                    this.setStyle("-fx-text-fill: " + _colorUp + ";-fx-font-weight: bold;");
                                    if (item.toString().contains("-")) {
                                        this.setStyle("-fx-text-fill: " + _colorDown + ";-fx-font-weight: bold;");
                                    }
                                    setText(item.toString());
                                }
                            }
                        };
                    }
                });
            }
        });
    }

    /**
     * Set the currency rate and adjust all prices in the table.
     * @param currRate
     */
    private void setCurrency() {
        // Change text color of "change" column if positive or negative change.
        // Default: col5 and col3
        this.cols.forEach((item) -> {
            if (item.getText().equalsIgnoreCase("price") && this.myClass == SingleCoin.class) {
                item.setCellFactory(new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn param) {
                        return new TableCell<SingleCoin, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                // Change color based on data
                                if (!isEmpty()) {
                                    // This is a SUPER hacky way to change prices LOL
                                    String newPrice = String.format("%.5f", Float.parseFloat(item) * Float.parseFloat("" + CURRENCY_RATE));
                                    setText(newPrice);
                                }
                            }
                        };
                    }
                });
            }
        });
    }

    /**
     * Add a double click that displays the currencies logo.
     */
    @Override
    public void addDoubleClick() {
        this.TABLE_VIEW.setRowFactory(tv -> {
            TableRow<SingleCoin> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        SingleCoin rowData = row.getItem();
                        System.out.println(rowData);
                        String imgPath = rowData.getIconUrl();

                        // Attempting to resize the coin logo image.
                        WEB_VIEW.setPrefHeight(56);
                        WEB_VIEW.setPrefWidth(56);
                        WEB_VIEW.getEngine().load(imgPath);
                    }
                }
            });
            return row;
        });
    }

    /**
     * This creates the right click menu on the onlineUsers list.
     * It also maps each button to an action.
     */
    @Override
    public void createTableCells(String _username, ListView _savedCoinsList, LinkedList<UserCoin> _savedCoins) {
        ContextMenu cm2 = new ContextMenu();
        MenuItem mi1 = new MenuItem("Save Coin");
        TabAssistantController tas = new TabAssistantController();
        mi1.setOnAction(event -> {
                SingleCoin item = (SingleCoin) TABLE_VIEW.getSelectionModel().getSelectedItem();
                tas.saveCoin(_username, item.getId());
                if(DEBUG){System.out.println("Added " + item.getName() + " to saved coin list");}
                tas.populateSavedCoins(_savedCoinsList, _savedCoins);
            });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        this.TABLE_VIEW.setContextMenu(menu);
        tas.populateSavedCoins(_savedCoinsList, _savedCoins);

        cm2.getItems().add(mi1);
        MenuItem mi2 = new MenuItem("Share Coin");
        cm2.getItems().add(mi2);
        MenuItem mi3 = new MenuItem("Track Coin");
        cm2.getItems().add(mi3);
        this.TABLE_VIEW.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cm2.show(TABLE_VIEW, t.getScreenX(), t.getScreenY());
                }
            }
        });
    }

    /**
     * Display the table.
     */
    @Override
    public void displayTable() {
        this.TABLE_VIEW.setItems(this.obvObjList);
        this.TABLE_VIEW.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        this.TABLE_VIEW.setItems(this.obvList);openList
//        this.TABLE_VIEW.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

}
