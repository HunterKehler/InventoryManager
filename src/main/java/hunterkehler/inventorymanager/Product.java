/**
 * Created class Product.java
 *
 * @author Hunter Kehler
 */
package hunterkehler.inventorymanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Products are composed of one or more Part.
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Default Product constructor
     * @param id the id of the Product
     * @param name the name of the Product
     * @param price the price of the Product
     * @param stock the inventory level of the Product
     * @param min the minimum stock of the Product
     * @param max the maximum stock of the Product
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @param Part added to associatedParts
     */
    public void addAssociatedPart(Part Part) {
        associatedParts.add(Part);
    }

    /**
     * @param selectedAssociatedPart removed from associatedParts
     * @return boolean
     */
    public boolean deleteAssociatedParts(Part selectedAssociatedPart) {
        for (Part associatedPart : associatedParts) {
            if (associatedPart == selectedAssociatedPart) {
                return associatedParts.remove(selectedAssociatedPart);
            }
        }
        return false;
    }

    /**
     * @return associatedParts
     */
    public ObservableList<Part> getAllAssociatedParts() { return associatedParts; }
}
