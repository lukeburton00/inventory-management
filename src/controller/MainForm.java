package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;
import model.Part;
import model.Product;
import model.Inventory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *
 * @author Luke Burton
 */
public class MainForm implements Initializable {
    public static Part selectedPart;
    public static Product selectedProduct;

    public Button addPart;
    public Button modifyPart;
    public Button deletePart;

    public Button addProduct;
    public Button modifyProduct;
    public Button deleteProduct;

    public TextField partSearch;
    public TableView<Part> partsTable;
    public TableColumn<Part, Integer> partId;
    public TableColumn<Part, String> partName;
    public TableColumn<Part, Integer> partInventory;
    public TableColumn<Part, Double> partPrice;

    public TextField productSearch;
    public TableView<Product> productsTable;
    public TableColumn<Product, Integer> productId;
    public TableColumn<Product, String> productName;
    public TableColumn<Product, Integer> productInventory;
    public TableColumn<Product, Double> productPrice;
    public Button exit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsTable.setItems(Inventory.getAllParts());
        productsTable.setItems(Inventory.getAllProducts());

        partId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));

        productId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        productInventory.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
    }

    /**
     *onAddPart loads the AddPart form when the addPart button is clicked.
     *
     * @param actionEvent the event triggered by the addPart button.
     * @throws IOException makes sure i/o operation is not interrupted.
     */
    public void onAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addPartForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 700,700));
        stage.show();
    }

    /**
     * onModifyPart loads the ModifyPart form when the modifyPart button is clicked
     * and a part is selected. If not, an alert is displayed.
     *
     * @param actionEvent the event triggered by the modifyPart button.
     * @throws IOException makes sure i/o operation is not interrupted.
     */
    public void onModifyPart(ActionEvent actionEvent) throws IOException {
        selectedPart = partsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null)
        {
            Alert error = new Alert(Alert.AlertType.ERROR, "No selected part. Nothing to modify.");
            error.setTitle("Error");
            error.showAndWait();
            return;
        }

        Parent root = FXMLLoader.load(getClass().getResource("/view/modifyPartForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 700,700));
        stage.show();
    }

    /**
     * onDeletePart sees if selectedPart exists, and alerts if not. Then asks for confirmation
     * before removing selectedPart from the inventory by calling Inventory.deletePart()
     *
     * @param actionEvent the event triggered by the deletePart button.
     */
    public void onDeletePart(ActionEvent actionEvent) {
        selectedPart = partsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert error = new Alert(Alert.AlertType.ERROR, "No selected part. Nothing was deleted.");
            error.setTitle("Error");
            error.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + selectedPart.getName() + "?");
        confirm.setTitle("Confirm delete");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Inventory.deletePart(selectedPart);
            System.out.println("item " + selectedPart.getName() + " deleted.");
        }
        partsTable.setItems(Inventory.getAllParts());
    }

    /**
     * onAddProduct loads the AddProduct form when the addProduct button is clicked.
     *
     * @param actionEvent the event triggered by the addProduct button.
     * @throws IOException makes sure the i/o operation is not interrupted.
     */
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addProductForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1091,579));
        stage.show();
    }

    /**
     * onModifyProduct loads the ModifyProduct form when the modifyProduct button is clicked
     * and a product is selected. If not, an alert is displayed.
     *
     * @param actionEvent the event triggered by the modifyProduct button.
     * @throws IOException makes sure the i/o operation is not interrupted.
     */
    public void onModifyProduct(ActionEvent actionEvent) throws IOException {
        selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null)
        {
            Alert error = new Alert(Alert.AlertType.ERROR, "No selected product. Nothing to modify.");
            error.setTitle("Error");
            error.showAndWait();
            return;
        }

        Parent root = FXMLLoader.load(getClass().getResource("/view/modifyProductForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1091,579));
        stage.show();
    }

    /**
     * onDeleteProduct sees if selectedProduct exists, and alerts if not. Then asks for confirmation
     * before removing selectedProduct from the inventory by calling Inventory.deleteProduct()
     *
     * @param actionEvent the event triggered by the deleteProduct button.
     */
    public void onDeleteProduct(ActionEvent actionEvent) {
        selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null)
        {
            Alert error = new Alert(Alert.AlertType.ERROR, "No selected product. Nothing was deleted.");
            error.setTitle("Error");
            error.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + selectedProduct.getName() + "?");
        confirm.setTitle("Confirm delete");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
                if (selectedProduct.getAllAssociatedParts().isEmpty()) {
                    Inventory.deleteProduct(selectedProduct);
                }
                else {
                    Alert error = new Alert(Alert.AlertType.ERROR, "Product has associated parts. Remove associated parts from product.");
                    error.showAndWait();
                }
        }
        productsTable.setItems(Inventory.getAllProducts());
    }

    /**
     * onPartSearch searches through the list of parts in the inventory, looking for either
     * the string partial or part id given in the TextField. When parts are found, a new
     * list with the found parts is generated and displayed in the table view. If no matching
     * parts are found, an error is displayed. If nothing is typed, all parts are displayed.
     *
     * @param actionEvent the event triggered by activating the partSearch textfield.
     */
    public void onPartSearch(ActionEvent actionEvent) {

        ObservableList<Part> resultParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();
        String partial = partSearch.getText();


        for (Part part : allParts) {
            if (part.getName().contains(partial) || String.valueOf(part.getId()).contains(partial))
            {
                resultParts.add(part);
            }
        }
        if (resultParts.size() == 0) {
            Alert error = new Alert(Alert.AlertType.ERROR, "No parts were found.");
            error.setTitle("Error");
            error.showAndWait();
            return;
        }
        partsTable.setItems(resultParts);
    }

    /**
     * onProductSearch searches through the list of products in the inventory, looking for either
     * the string partial or product id given in the TextField. When products are found, a new
     * list with the found products is generated and displayed in the table view. If no matching
     * products are found, an error is displayed. If nothing is typed, all products are displayed.
     *
     * @param actionEvent the event triggered by activating the productSearch textfield.
     */
    public void onProductSearch(ActionEvent actionEvent) {

        ObservableList<Product> resultProducts = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        String partial = productSearch.getText();

        for (Product product : allProducts) {
            if (product.getName().contains(partial) || String.valueOf(product.getId()).contains(partial)) {
                resultProducts.add(product);
            }
        }
        if (resultProducts.size() == 0) {
            Alert error = new Alert(Alert.AlertType.ERROR, "No products were found.");
            error.setTitle("Error");
            error.showAndWait();
            return;
        }
        productsTable.setItems(resultProducts);
    }

    /**
     * onExit closes the program.
     *
     * @param actionEvent the event triggered by the exit button.
     */
    public void onExit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
