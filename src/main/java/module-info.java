module hunterkehler.inventorymanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens hunterkehler.inventorymanager to javafx.fxml;
    exports hunterkehler.inventorymanager;
}