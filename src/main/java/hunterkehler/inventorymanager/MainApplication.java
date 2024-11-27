/**
 * This is the MainApplication Java Class
 * @author Hunter Kehler
 */
package hunterkehler.inventorymanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This Java class launches when the program is first opened and displays the Main Menu window.
 * JAVADOC LOCATION
 * The JavaDocs are located in a folder called "javadoc" that is outside the src folder
 * It will appear when you open the .zip folder
 */
public class MainApplication extends Application {
    /**
     * Creates a window of the Main Menu
     * @param stage A stage being called to show the Main Menu.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Inventory Manager");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the program
     * First method to be called when the program starts
     * @param args command-line argument
     */
    public static void main(String[] args) {
        launch();
    }
}