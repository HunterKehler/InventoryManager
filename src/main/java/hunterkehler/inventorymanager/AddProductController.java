/**
 * This is the Controller class for AddProduct.fxml
 * @author Hunter Kehler
 */
package hunterkehler.inventorymanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * This Java class handles all ButtonEvents such as adding an associatedPart, removing an associatedPart,
 * saving the Product, exiting to the Main Menu, and catching errors when entering data for the
 * Product
 */
public class AddProductController {
    /**
     * An integer used to keep track of IDs used by new Products
     */
    public static int idGenProduct = 1;
    /**
     * An ObservableList of Parts to contain all selected Parts for the Product
     */
    public ObservableList<Part> associatedPartList = FXCollections.observableArrayList();
    /**
     * A TextField used for containing and inputting the name of the Product
     */
    @FXML
    private TextField productsNameTextField;
    /**
     * A TextField used for containing and inputting the Inventory Level of the Product
     */
    @FXML
    private TextField productsInvTextField;
    /**
     * A TextField used for containing and inputting the price of the Product
     */
    @FXML
    private TextField productsCostTextField;
    /**
     * A TextField used for containing and inputting the maximum inventory of the Product
     */
    @FXML
    private TextField productsMaxTextField;
    /**
     * A TextField used for containing and inputting the minimum inventory of the Product
     */
    @FXML
    private TextField productsMinTextField;
    /**
     * A TextField used for searching for parts in the parts table
     */
    @FXML
    private TextField productsSearchTextField;
    /**
     * The top TableView that contains all selectable Parts
     */
    @FXML
    private TableView<Part> partsTopTable;
    /**
     * The leftmost TableColumn that shows all parts id
     */
    @FXML
    private TableColumn<Part, Integer> partsTopID;
    /**
     * The second TableColumn that shows all parts names
     */
    @FXML
    private TableColumn<Part, String> partsTopName;
    /**
     * The third TableColumn that shows all parts inventory level
     */
    @FXML
    private TableColumn<Part, Integer> partsTopInventory;
    /**
     * The rightmost TableColumn that shows all parts pricing
     */
    @FXML
    private TableColumn<Part, Double> partsTopCost;
    /**
     * The bottom TableView that contains all Parts associated with the Product
     */
    @FXML
    private TableView<Part> partsBottomTable;
    /**
     * The leftmost TableColumn that shows associatedParts id
     */
    @FXML
    private TableColumn<Part, Integer> partsBottomID;
    /**
     * The second TableColumn that shows all associatedParts names
     */
    @FXML
    private TableColumn<Part, String> partsBottomName;
    /**
     * The third TableColumn that shows all associatedParts inventory level
     */
    @FXML
    private TableColumn<Part, Integer> partsBottomInventory;
    /**
     * The rightmost TableColumn that shows all associatedParts pricing
     */
    @FXML
    private TableColumn<Part, Double> partsBottomCost;

    /**
     * Registers when a user types in either a string or int and searches the Inventory
     * export the results to a TableView. Shows nothing if there is no matching part id or names
     * @param event ENTER or RETURN is pressed while the search bar is active
     */
    @FXML
    void onAddProductSearching(ActionEvent event) {
        ObservableList<Part> searchedPartList = FXCollections.observableArrayList();

        try {
            searchedPartList = Inventory.lookupPart(productsSearchTextField.getText());
            if (Inventory.lookupPart(Integer.parseInt(productsSearchTextField.getText())) != null &&
                    !(searchedPartList.contains(Inventory.lookupPart(Integer.parseInt
                            (productsSearchTextField.getText()))))) {
                searchedPartList.add(Inventory.lookupPart(Integer.parseInt(productsSearchTextField.getText())));
            }
        } catch (NumberFormatException e) {
            searchedPartList = Inventory.lookupPart(productsSearchTextField.getText());
        }

        if (searchedPartList.isEmpty()) {
            Alert partSearchFailAlert = new Alert(Alert.AlertType.ERROR, "No parts has matching name or ID.");
            partSearchFailAlert.showAndWait();
            partsTopTable.setItems(Inventory.getAllParts());
            return;
        }
        partsTopTable.setItems(searchedPartList);
    }

    /**
     * Adds a Part selected from the Parts table to an ObservableList used to track
     * AssociatedParts from the current Product
     * @param event button clicked
     */
    @FXML
    void onProductsAddClick(ActionEvent event) {
        if (partsTopTable.getSelectionModel().getSelectedItem() == null) { return; }
        associatedPartList.add(partsTopTable.getSelectionModel().getSelectedItem());
        partsBottomTable.setItems(associatedPartList);
    }

    /**
     * Removes a Part selected in the Bottom Parts TableView from an ObservableList used to track
     * associatedParts for the current Product
     * @param event button clicked
     */
    @FXML
    void onRemoveAssociatedPartClick(ActionEvent event) {
        if (partsBottomTable.getSelectionModel().getSelectedItem() == null) { return; }

        Alert associatedPartDeleteAlert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to remove " +
                partsBottomTable.getSelectionModel().getSelectedItem().getName() + " from the current product?",
                ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = associatedPartDeleteAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            associatedPartList.remove(partsBottomTable.getSelectionModel().getSelectedItem());
            partsBottomTable.setItems(associatedPartList);
        }

    }

    /**
     * Checks several errors to make sure the Product is viable for saving, and creates the Product
     * and adds the newly formed Product into the Inventory and returns to the Main Menu
     * @param event button clicked
     * @throws IOException FXMLLoader.load() may throw an IOException
     */
    @FXML
    void onSaveButtonClick(ActionEvent event) throws IOException {

        String errorString = "The inventory ";
        String errorDataType = "must be an integer.";

        if (productsNameTextField.getText().isEmpty() || productsInvTextField.getText().isEmpty() ||
                productsCostTextField.getText().isEmpty() || productsMaxTextField.getText().isEmpty() ||
                productsMinTextField.getText().isEmpty()) {
            Alert productMissingInfoAlert = new Alert(Alert.AlertType.ERROR, "Please enter all of the text fields.");
            productMissingInfoAlert.setTitle("Product Addition Error");
            productMissingInfoAlert.setHeaderText("An Error has occurred");
            productMissingInfoAlert.showAndWait();
            return;
        }

        String productName = productsNameTextField.getText();

        try {
            int productInv = Integer.parseInt(productsInvTextField.getText());
            errorString = "The price/cost ";
            errorDataType = "must be a double.";
            double productPrice = Double.parseDouble(productsCostTextField.getText());
            errorString = "The maximum ";
            errorDataType = "must be an integer.";
            int productMax = Integer.parseInt(productsMaxTextField.getText());
            errorString = "The minimum ";
            errorDataType = "must be an integer.";
            int productMin = Integer.parseInt(productsMinTextField.getText());

            if (productInv > productMax) {
                Alert productSmallMaxAlert = new Alert(Alert.AlertType.ERROR,
                        "Inventory level cannot be larger than maximum.");
                productSmallMaxAlert.setTitle("Product Addition Error");
                productSmallMaxAlert.setHeaderText("An Error has occurred");
                productSmallMaxAlert.showAndWait();
                return;
            }
            if (productMin > productInv) {
                Alert productSmallInvAlert = new Alert(Alert.AlertType.ERROR,
                        "Inventory level cannot be smaller than minimum.");
                productSmallInvAlert.setTitle("Product Addition Error");
                productSmallInvAlert.setHeaderText("An Error has occurred");
                productSmallInvAlert.showAndWait();
                return;
            }

            Product tempProduct =
                    new Product(idGenProduct, productName, productPrice, productInv, productMin, productMax);
            for (Part associatedPart : associatedPartList) { tempProduct.addAssociatedPart(associatedPart); }
            Inventory.addProduct(tempProduct);

        } catch (NumberFormatException e) {
            Alert productWrongTypeAlert = new Alert(Alert.AlertType.ERROR, errorString + errorDataType);
            productWrongTypeAlert.setTitle("Product Addition Error");
            productWrongTypeAlert.setHeaderText("An Error has occurred");
            productWrongTypeAlert.showAndWait();

            return;
        }

        ++idGenProduct;

        Parent scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Returns to the Main Menu without saving any inputted data
     * @param event button clicked
     * @throws IOException FXMLLoader.load() may throw an IOException
     */
    @FXML
    void onCancelButtonClick(ActionEvent event) throws IOException {
        Parent scene = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Sets up and fills the Tables
     */
    public void fillTable() {

        partsTopTable.setItems(Inventory.getAllParts());
        partsTopID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsTopName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsTopInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsTopCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsBottomTable.setItems(associatedPartList);
        partsBottomID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsBottomName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsBottomInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsBottomCost.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
