package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Luke Burton
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
      * @param newPart the part to add to the inventory
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     *
     * @param newProduct the product to add to the inventory
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     *
     * @param partId the id of the part to find
     * @return the part with the matching id
     */
    public static Part lookupPart(int partId) {

        for (Part part : getAllParts()) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     *
     * @param productId the id of the product to find
     * @return the product with the matching id
     */
    public static Product lookupProduct(int productId) {

        for (Product product : getAllProducts()) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     *
     * @param partName the name of the part to find
     * @return the part with the matching name
     */
    public static ObservableList<Part> lookupPart(String partName) {

        ObservableList<Part> parts = FXCollections.observableArrayList();

        for (Part part : getAllParts()) {
            if (part.getName().equals(partName)) {
                parts.add(part);
            }
        }
        return parts;
    }

    /**
     *
     * @param productName the name of the product to find
     * @return the product with the matching name
     */
    public static ObservableList<Product> lookupProduct(String productName) {

        ObservableList<Product> products = FXCollections.observableArrayList();

        for (Product product : getAllProducts()) {
            if (product.getName().equals(productName)) {
                products.add(product);
            }
        }
        return products;
    }

    /**
     *
     * @param index the index to set
     * @param selectedPart the part to change index
     */
    public static void updatePart(int index, Part selectedPart) {

        allParts.set(index, selectedPart);
    }

    /**
     *
     * @param index the index to set
     * @param selectedProduct the product to change index
     */
    public static void updateProduct(int index, Product selectedProduct) {

        allProducts.set(index, selectedProduct);
    }

    /**
     *
     * @param selectedPart the part to be deleted
     * @return true if deleted, false if not
     */
    public static boolean deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
        return false;
    }

    /**
     *
     * @param selectedProduct the part to be deleted
     * @return true if deleted, false if not
     */
    public static boolean deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
        return false;
    }

    /**
     *
     * @return all parts in the allParts list
     */
    public static ObservableList<Part> getAllParts() {

        return allParts;
    }

    /**
     *
     * @return all products in the allProducts list
     */
    public static ObservableList<Product> getAllProducts() {

        return allProducts;
    }


}
