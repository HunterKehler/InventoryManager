/**
 * This is the Controller class for ModifyProduct.fxml
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
 * replacing the modified Product, exiting to the Main Menu, and
 * catching errors when entering data for the Product
 */
public class ModifyProductController {
    /**
     * A Product temporarily made to aid with modifying the current Product
     */
    Product tempProduct;
    /**
     * An integer that represents the index of the Product currently under modification
     */
    int productIndex;
    /**
     * A TextField used for containing and inputting the name of the Product
     */
    @FXML
    private TextField productsIdTextField;
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
     * associatedParts from the current Product
     * @param event button clicked
     */
    @FXML
    void onProductsAddClick(ActionEvent event) {
        if (partsTopTable.getSelectionModel().getSelectedItem() == null) { return; }
        tempProduct.addAssociatedPart(partsTopTable.getSelectionModel().getSelectedItem());
        partsBottomTable.setItems(tempProduct.getAllAssociatedParts());
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
            tempProduct.deleteAssociatedParts(partsBottomTable.getSelectionModel().getSelectedItem());
            partsBottomTable.setItems(tempProduct.getAllAssociatedParts());
        }
    }

    /**
     * Checks several errors to make sure the Product is viable for saving, and creates the Product
     * and replaces the original Product with the newly formed Product into the Inventory
     * and returns to the Main Menu
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
            productMissingInfoAlert.setTitle("Product Modification Error");
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
                productSmallMaxAlert.setTitle("Product Modification Error");
                productSmallMaxAlert.setHeaderText("An Error has occurred");
                productSmallMaxAlert.showAndWait();
                return;
            }
            if (productMin > productInv) {
                Alert productSmallInvAlert = new Alert(Alert.AlertType.ERROR,
                        "Inventory level cannot be smaller than minimum.");
                productSmallInvAlert.setTitle("Product Modification Error");
                productSmallInvAlert.setHeaderText("An Error has occurred");
                productSmallInvAlert.showAndWait();
                return;
            }

            tempProduct.setName(productName);
            tempProduct.setStock(productInv);
            tempProduct.setPrice(productPrice);
            tempProduct.setMax(productMax);
            tempProduct.setMin(productMin);
            Inventory.updateProduct(productIndex, tempProduct);

        } catch (NumberFormatException e) {
            Alert productWrongTypeAlert = new Alert(Alert.AlertType.ERROR, errorString + errorDataType);
            productWrongTypeAlert.setTitle("Product Modification Error");
            productWrongTypeAlert.setHeaderText("An Error has occurred");
            productWrongTypeAlert.showAndWait();

            return;
        }


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
     * Sets up and fills the tables and fills the blank TextFields with the stats of the original Product
     * @param selectedProduct the Product selected for modification
     * @param index the index of the Product selected for modification
     */
    public void fillTable(Product selectedProduct, int index) {

        tempProduct = new Product(selectedProduct.getId(), selectedProduct.getName(), selectedProduct.getPrice(),
                selectedProduct.getStock(), selectedProduct.getMin(), selectedProduct.getMax());

        for (Part associatedPart : selectedProduct.getAllAssociatedParts()) {
            tempProduct.addAssociatedPart(associatedPart);
        }
        productIndex = index;

        productsIdTextField.setText(Integer.toString(tempProduct.getId()));
        productsNameTextField.setText(tempProduct.getName());
        productsInvTextField.setText(Integer.toString(tempProduct.getStock()));
        productsCostTextField.setText(Double.toString(tempProduct.getPrice()));
        productsMaxTextField.setText(Integer.toString(tempProduct.getMax()));
        productsMinTextField.setText(Integer.toString(tempProduct.getMin()));

        partsTopTable.setItems(Inventory.getAllParts());
        partsTopID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsTopName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsTopInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsTopCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsBottomTable.setItems(tempProduct.getAllAssociatedParts());
        partsBottomID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsBottomName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsBottomInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsBottomCost.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

}
