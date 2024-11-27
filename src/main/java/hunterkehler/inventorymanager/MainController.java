/**
 * This is the Controller class for MainMenu.fxml
 * @author Hunter Kehler
 */

package hunterkehler.inventorymanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
/**
 * This Java class handles ButtonEvents that lead to new menus for
 * adding, modifying, or deleting Parts and Products, along with
 * showing the Parts and Products in TableViews and allowing
 * the user to search through both tables, and exit the program.
 */
public class MainController implements Initializable {
    /**
     * The TableView that shows all the Parts in Inventory
     */
    @FXML
    private TableView<Part> partsTable;
    /**
     * A TableColumn used to display the ID of all the Parts in Inventory
     */
    @FXML
    private TableColumn<Part, Integer> partsID;
    /**
     * A TableColumn used to display the name of all the Parts in Inventory
     */
    @FXML
    private TableColumn<Part, String> partsName;
    /**
     * A TableColumn used to display the Inventory Level of all the Parts in Inventory
     */
    @FXML
    private TableColumn<Part, Integer> partsInventory;
    /**
     * A TableColumn used to display the pricing of all the Parts in Inventory
     */
    @FXML
    private TableColumn<Part, Double> partsCost;
    /**
     * A TextField used to search throughout the partsTable TableView, takes integers and strings
     */
    @FXML
    private TextField partsSearchTextField;
    /**
     * The TableView that shows all the Products in Inventory
     */
    @FXML
    private TableView<Product> productsTable;
    /**
     * A TableColumn used to display the ID of all the Products in Inventory
     */
    @FXML
    private TableColumn<Product, Integer> productID;
    /**
     * A TableColumn used to display the name of all the Products in Inventory
     */
    @FXML
    private TableColumn<Product, String> productName;
    /**
     * A TableColumn used to display the Inventory Level of all the Products in Inventory
     */
    @FXML
    private TableColumn<Product, Integer> productInventory;
    /**
     * A TableColumn used to display the pricing of all the Products in Inventory
     */
    @FXML
    private TableColumn<Product, Double> productCost;
    /**
     * A TextField used to search throughout the partsTable TableView, takes integers and strings
     */
    @FXML
    private TextField productsSearchTextField;
    /**
     * Opens the Add Part window
     * @param event button clicked
     * @throws IOException FXMLLoader.load() may throw an IOException
     */
    @FXML
    void onPartsAddButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * Checks if a Part is selected in partsTable
     * If a Part is selected, the Modify Part window is opened
     * @param event button clicked
     * @throws IOException loader.load() may throw an IOException
     */
    @FXML
    void onPartsModifyButtonClick(ActionEvent event) throws IOException {

        if (partsTable.getSelectionModel().getSelectedItem() == null) {
            Alert partModifyFailAlert = new Alert(Alert.AlertType.ERROR, "No part was selected for modification.");
            partModifyFailAlert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPart.fxml"));
        Parent scene = loader.load();
        ModifyPartController modifyPart = loader.getController();

        if (partsTable.getSelectionModel().getSelectedItem() instanceof InHouse) {
            modifyPart.importData(partsTable.getSelectionModel().getSelectedItem(),
                    partsTable.getSelectionModel().getSelectedIndex());
        } else if (partsTable.getSelectionModel().getSelectedItem() instanceof Outsourced) {
            modifyPart.importData(partsTable.getSelectionModel().getSelectedItem(),
                    partsTable.getSelectionModel().getSelectedIndex());
        } else { return; }

        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * Checks if a Part in partsTable is selected
     * If a Part is selected, prompts the user if the user wishes to delete the selected Part
     * If Yes is picked, the Part is removed from Inventory. Otherwise, the Part is kept.
     * @param event button clicked
     */
    @FXML
    void onPartsDeleteButtonClick(ActionEvent event) {

        if (partsTable.getSelectionModel().getSelectedItem() == null) {
            Alert partDeleteFailAlert = new Alert(Alert.AlertType.ERROR, "No part was selected for deletion.");
            partDeleteFailAlert.showAndWait();
            return;
        }

        Alert partDeleteAlert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete " +
                partsTable.getSelectionModel().getSelectedItem().getName() + "?", ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = partDeleteAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            Inventory.deletePart(partsTable.getSelectionModel().getSelectedIndex(),
                    partsTable.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Searches through the partsTable based on what the user searches
     * If it's a number, the ID and names are searched
     * Otherwise, only names are searched
     * If no matches are found, an error is thrown
     * @param event ENTER or RETURN is pressed while the search bar is active
     */
    @FXML
    void onPartsSearching(ActionEvent event) {
        ObservableList<Part> searchedPartList = FXCollections.observableArrayList();

        try {
            searchedPartList = Inventory.lookupPart(partsSearchTextField.getText());
            if (Inventory.lookupPart(Integer.parseInt(partsSearchTextField.getText())) != null &&
                    !(searchedPartList.contains(Inventory.lookupPart(Integer.parseInt
                            (partsSearchTextField.getText()))))) {
                searchedPartList.add(Inventory.lookupPart(Integer.parseInt(partsSearchTextField.getText())));
            }
        } catch (NumberFormatException e) {
            searchedPartList = Inventory.lookupPart(partsSearchTextField.getText());
        }

        if (searchedPartList.isEmpty()) {
            Alert partSearchFailAlert = new Alert(Alert.AlertType.ERROR, "No parts has matching name or ID.");
            partSearchFailAlert.showAndWait();
            partsTable.setItems(Inventory.getAllParts());
            return;
        }
        partsTable.setItems(searchedPartList);
    }

    /**
     * Opens the Product Add Window
     * @param event button clicked
     * @throws IOException loader.load() may throw an IOException
     */
    @FXML
    void onProductsAddButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
        Parent scene = loader.load();
        AddProductController addProduct = loader.getController();
        addProduct.fillTable();
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Checks if a Product is selected in productsTable
     * If a Product is selected, the Modify Product window is opened
     * @param event button clicked
     * @throws IOException loader.load() may throw an IOException
     */
    @FXML
    void onProductsModifyButtonClick(ActionEvent event) throws IOException {

        if (productsTable.getSelectionModel().getSelectedItem() == null) {
            Alert productModifyFailAlert = new Alert(Alert.AlertType.ERROR,
                    "No product was selected for modification.");
            productModifyFailAlert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProduct.fxml"));
        Parent scene = loader.load();
        ModifyProductController modifyProduct = loader.getController();
        modifyProduct.fillTable(productsTable.getSelectionModel().getSelectedItem(),
                productsTable.getSelectionModel().getSelectedIndex());
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Checks if a Product in productsTable is selected
     * If a Product is selected, prompts the user if the user wishes to delete the selected Product
     * If Yes is picked, the Product is removed from Inventory. Otherwise, the Product is kept.
     * @param event button clicked
     */
    @FXML
    void onProductsDeleteButtonClick(ActionEvent event){
        if (productsTable.getSelectionModel().getSelectedItem() == null) {
            Alert productDeleteFailAlert = new Alert(Alert.AlertType.ERROR, "No product was selected for deletion.");
            productDeleteFailAlert.showAndWait();
            return;
        }

        if (!productsTable.getSelectionModel().getSelectedItem().getAllAssociatedParts().isEmpty()) {
            Alert productDeleteFailAlert = new Alert(Alert.AlertType.ERROR, "This product cannot be deleted.\n" +
                    "Please delete all associated parts from this product.");
            productDeleteFailAlert.showAndWait();
            return;
        }

        Alert productDeleteAlert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete " +
                productsTable.getSelectionModel().getSelectedItem().getName() + "?", ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = productDeleteAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            Inventory.deleteProduct(productsTable.getSelectionModel().getSelectedIndex(),
                    productsTable.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Searches through the productsTable based on what the user searches
     * If it's a number, the ID and names are searched
     * Otherwise, only names are searched
     * If no matches are found, an error is thrown
     * @param event ENTER or RETURN is pressed while the search bar is active
     */
    @FXML
    void onProductsSearching(ActionEvent event) {
        ObservableList<Product> searchedProductList = FXCollections.observableArrayList();

        try {
            searchedProductList = Inventory.lookupProduct(productsSearchTextField.getText());
            if (Inventory.lookupProduct(Integer.parseInt(productsSearchTextField.getText())) != null &&
                    !(searchedProductList.contains(Inventory.lookupProduct(Integer.parseInt
                            (productsSearchTextField.getText()))))) {
                searchedProductList.add(Inventory.lookupProduct(Integer.parseInt(productsSearchTextField.getText())));
            }
        } catch (NumberFormatException e) {
            searchedProductList = Inventory.lookupProduct(productsSearchTextField.getText());
        }

        if (searchedProductList.isEmpty()) {
            Alert productSearchFailAlert = new Alert(Alert.AlertType.ERROR, "No products has matching name or ID.");
            productSearchFailAlert.showAndWait();
            productsTable.setItems(Inventory.getAllProducts());
            return;
        }
            productsTable.setItems(searchedProductList);
    }

    /**
     * Prompts the user if the user wishes to close the window
     * If YES is selected, the application closes
     * If NO is selected, the application stays open
     * @param event button clicked
     */
    @FXML
    void onExitButtonClick(ActionEvent event) {
        Alert exitAlert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to exit the program?",
                ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = exitAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) { System.exit(0);  }

    }

    /**
     * Fills the partsTable and productsTable with both ObservableLists in Inventory
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partsTable.setItems(Inventory.getAllParts());
        partsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTable.setItems(Inventory.getAllProducts());
        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCost.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
