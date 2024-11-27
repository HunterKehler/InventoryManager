/**
 * This is the Controller class for ModifyPart.fxml
 * @author Hunter Kehler
 */
package hunterkehler.inventorymanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This Java class handles all ButtonEvents such as adding switching between InHouse and Outsourced,
 * updating the modified Part, exiting to the Main Menu, and catching errors
 * when entering data for the Part
 */
public class ModifyPartController {
    /**
     * An integer used to keep track of the ID used by the modified Part
     */
    int partsId;
    /**
     * An integer used to keep track of the index used by the modified Part
     */
    int partIndex;
    /**
     * A Label that switches titles depending on which RadioButton is currently active
     */
    @FXML
    private Label idToggleLabel;
    /**
     * A RadioButton used to select the creation of an InHouse Part
     */
    @FXML
    private RadioButton inHouseRadioButton;
    /**
     * A RadioButton used to select the creation of an Outsourced Part
     */
    @FXML
    private RadioButton outsourcedRadioButton;
    /**
     * A TextField used for containing the ID of the Part
     */
    @FXML
    private TextField partsIdTextField;
    /**
     * A TextField used for containing and inputting the name of the Part
     */
    @FXML
    private TextField partsNameTextField;
    /**
     * A TextField used for containing and inputting the price of the Part
     */
    @FXML
    private TextField partsCostTextField;
    /**
     * A TextField used for containing and inputting the Inventory Level of the Part
     */
    @FXML
    private TextField partsInvTextField;
    /**
     * A TextField used for containing and inputting the maximum inventory of the Part
     */
    @FXML
    private TextField partsMaxTextField;
    /**
     * A TextField used for containing and inputting the minimum inventory of the Part
     */
    @FXML
    private TextField partsMinTextField;
    /**
     * A TextField used for containing and inputting either the Machine ID or Company Name for the Part
     */
    @FXML
    private TextField partsToggleIdTextField;
    /**
     * Changes the idToggleLabel to Machine ID when the InHouse RadioButton is clicked on
     */
    @FXML
    void onInHouseRadioButtonClick() {
        idToggleLabel.setText("Machine ID");
    }
    /**
     * Changes the idToggleLabel to Company Name when the Outsourced RadioButton is clicked on
     */
    @FXML
    void onOutsourcedRadioButtonClick() {
        idToggleLabel.setText("Company Name");
    }
    /**
     * Sets up and fills the blank TextFields with the stats of the original Part
     * @param selectedPart the Part selected for modification
     * @param index the index of the Part selected for modification
     */
    public void importData(Part selectedPart, int index) {
        if (selectedPart instanceof InHouse) {
            inHouseRadioButton.setSelected(true);
            idToggleLabel.setText("Machine ID");
            partsToggleIdTextField.setText(Integer.toString(((InHouse)selectedPart).getMachineId()));
        } else if (selectedPart instanceof Outsourced) {
            outsourcedRadioButton.setSelected(true);
            idToggleLabel.setText("Company Name");
            partsToggleIdTextField.setText(((Outsourced)selectedPart).getCompanyName());
        }
        partsId = selectedPart.getId();
        partsIdTextField.setText(Integer.toString(selectedPart.getId()));
        partsNameTextField.setText(selectedPart.getName());
        partsInvTextField.setText(Integer.toString(selectedPart.getStock()));
        partsCostTextField.setText(Double.toString(selectedPart.getPrice()));
        partsMaxTextField.setText(Integer.toString(selectedPart.getMax()));
        partsMinTextField.setText(Integer.toString(selectedPart.getMin()));
        partIndex = index;
    }
    /**
     * Checks several errors to make sure the Part is viable for saving, and creates the Part
     * and replaces the original Part with the newly formed Part in the Inventory
     * and returns to the Main Menu
     * @param event button clicked
     * @throws IOException FXMLLoader.load() may throw an IOException
     */
    @FXML
    void onSaveButtonClick(ActionEvent event) throws IOException {

        String errorString = "The inventory ";
        String errorDataType = "must be an integer.";

        if (partsNameTextField.getText().isEmpty() || partsInvTextField.getText().isEmpty() ||
                partsCostTextField.getText().isEmpty() || partsMaxTextField.getText().isEmpty() ||
                partsMinTextField.getText().isEmpty() || partsToggleIdTextField.getText().isEmpty()) {
            Alert partMissingInfoAlert = new Alert(Alert.AlertType.ERROR, "Please enter all of the text fields.");
            partMissingInfoAlert.setTitle("Part Modification Error");
            partMissingInfoAlert.setHeaderText("An Error has occurred");
            partMissingInfoAlert.showAndWait();
            return;
        }

        String partName = partsNameTextField.getText();

        try {
            int partInv = Integer.parseInt(partsInvTextField.getText());
            errorString = "The price/cost ";
            errorDataType = "must be a double.";
            double partPrice = Double.parseDouble(partsCostTextField.getText());
            errorString = "The maximum ";
            errorDataType = "must be an integer.";
            int partMax = Integer.parseInt(partsMaxTextField.getText());
            errorString = "The minimum ";
            errorDataType = "must be an integer.";
            int partMin = Integer.parseInt(partsMinTextField.getText());

            if (partInv > partMax) {
                Alert partSmallMaxAlert = new Alert(Alert.AlertType.ERROR,
                        "Inventory level cannot be larger than maximum.");
                partSmallMaxAlert.setTitle("Part Modification Error");
                partSmallMaxAlert.setHeaderText("An Error has occurred");
                partSmallMaxAlert.showAndWait();
                return;
            }
            if (partMin > partInv) {
                Alert partSmallInvAlert = new Alert(Alert.AlertType.ERROR,
                        "Inventory level cannot be smaller than minimum.");
                partSmallInvAlert.setTitle("Part Modification Error");
                partSmallInvAlert.setHeaderText("An Error has occurred");
                partSmallInvAlert.showAndWait();
                return;
            }

            if (inHouseRadioButton.isSelected()) {
                errorString = "The machine ID ";
                errorDataType = "must be an integer.";
                Inventory.updatePart(partIndex, new InHouse(partsId, partName, partPrice, partInv, partMin, partMax,
                        Integer.parseInt(partsToggleIdTextField.getText())));
            } else if (outsourcedRadioButton.isSelected()) {
                Inventory.updatePart(partIndex, new Outsourced(partsId, partName, partPrice, partInv, partMin, partMax,
                        partsToggleIdTextField.getText()));
            }

        } catch (NumberFormatException e) {
            Alert partWrongTypeAlert = new Alert(Alert.AlertType.ERROR, errorString + errorDataType);
            partWrongTypeAlert.setTitle("Part Modification Error");
            partWrongTypeAlert.setHeaderText("An Error has occurred");
            partWrongTypeAlert.showAndWait();
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
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }
}

