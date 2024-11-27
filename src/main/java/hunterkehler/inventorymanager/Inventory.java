/**
 * Created class Inventory.java
 *
 * @author Hunter Kehler
 */
package hunterkehler.inventorymanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is what stores the Parts and Products of the company.
 */
 public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

     /**
      * Adds a new Part to the allParts ObservableList
      * @param newPart Part to be added to the list
      */
     public static void addPart(Part newPart) { allParts.add(newPart); }
     /**
      * Adds a new Product to the allProducts ObservableList
      * @param newProduct Product to be added to the list
      */
     public static void addProduct(Product newProduct) { allProducts.add(newProduct); }

     /**
      * Searches for the Part with a matching ID in allParts
      * @param partId the ID used the search through the list
      * @return either the part with a matching id or null if no part is found
      */
     public static Part lookupPart(int partId) {
         for (Part allPart : allParts) {
             if (allPart.getId() == partId) {
                 return allPart;
             }
         }
         return null;
     }

     /**
      * Searches for the Products with a matching ID in allProducts
      * @param productId the ID used the search through the list
      * @return either the product with a matching id or null if no product is found
      */
     public static Product lookupProduct(int productId) {
         for (Product allProduct : allProducts) {
            if (allProduct.getId() == productId) {
                return allProduct;
            }
         }
      return null;
     }

     /**
      * Searches for the Part with a name that contains the string in allParts
      * and adds the Part to an ObservableList
      * @param partName the string used the search through the list
      * @return the ObservableList with all matches
      */
     public static ObservableList<Part> lookupPart(String partName) {
         ObservableList<Part> searchedPartList = FXCollections.observableArrayList();
         for (Part allPart : allParts) {
             if (allPart.getName().toLowerCase().contains(partName.toLowerCase())) {
                 searchedPartList.add(allPart);
             }
         }
         return searchedPartList;
     }
     /**
      * Searches for the Products with a name that contains the string in allProducts
      * and adds the Product to an ObservableList
      * @param productName the string used the search through the list
      * @return the ObservableList with all matches
      */
     public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> searchedProductList = FXCollections.observableArrayList();
        for (Product allProduct : allProducts) {
           if (allProduct.getName().toLowerCase().contains(productName.toLowerCase())) {
             searchedProductList.add(allProduct);
           }
       }
       return searchedProductList;
     }

     /**
      * Updates a modified Part by replacing the old Part with the new Part in the allParts ObservableList
      * @param index the index of Part being replaced
      * @param selectedPart the Part that is replacing the original Part
      */
     public static void updatePart(int index, Part selectedPart) { allParts.set(index, selectedPart); }

     /**
      * Updates a modified Product by replacing the old Product with the new Product in the allProducts ObservableList
      * @param index the index of the Product being replaced
      * @param newProduct the Product that is replacing the original Product
      */
     public static void updateProduct(int index, Product newProduct) { allProducts.set(index, newProduct); }

     /**
      * Removes a Part from the allParts ObservableList when an exact match is found
      * @param index the index of the Part being removed
      * @param selectedPart the Part selected for deletion
      * @return true if the Part is found and deleted, false if no Part with that index is found
      */
     public static boolean deletePart(int index, Part selectedPart) {
         if (allParts.get(index).equals(selectedPart)) {
             return allParts.remove(selectedPart);
         }
         return false;
     }

     /**
      * Removes a Product from the allProducts ObservableList when an exact match is found
      * @param index the index of the Product being removed
      * @param selectedProduct the Product selected for deletion
      * @return true if the Product is found and deleted, false if no Product with that index is found
      */
     public static boolean deleteProduct(int index, Product selectedProduct) {
         if (allProducts.get(index).equals(selectedProduct)) {
             return allProducts.remove(selectedProduct);
         }
         return false;
     }

     /**
      * @return the allParts ObservableList
      */
     public static ObservableList<Part> getAllParts() { return allParts; }

     /**
      * @return the allProducts ObservableList
      */
     public static ObservableList<Product> getAllProducts() { return allProducts; }
 }
