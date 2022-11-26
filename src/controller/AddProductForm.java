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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *
 * @author Luke Burton
 */
public class AddProductForm implements Initializable {
    public static int id = 1;
    public boolean idIsUnique;
    public TextField partSearch;
    public TextField idText;
    public TextField nameText;
    public TextField inventoryText;
    public TextField priceText;
    public TextField maxText;
    public TextField minText;
    public TableColumn<Part, Integer> addPartId;
    public TableColumn<Part, String> addPartName;
    public TableColumn<Part, Integer> addPartInventory;
    public TableColumn<Part, Double> addPartPrice;
    public TableColumn<Part, Integer> associatedPartId;
    public TableColumn<Part, String> associatedPartName;
    public TableColumn<Part, Integer> associatedPartInventory;
    public TableColumn<Part, Double> associatedPartPrice;
    public Button addPart;
    public Button removePart;
    public Button save;
    public Button cancel;
    public TableView<Part> addPartsTable;
    public TableView<Part> associatedPartsTable;
    public ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    public Part selectedPart;

    /**
     * initialize the form and set up the two table views.
     *
     * @param url the path for the form
     * @param resourceBundle the data to be internationalized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Screen AddProductForm Initialized");

        addPartsTable.setItems(Inventory.getAllParts());

        addPartId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        addPartName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        addPartInventory.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        addPartPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));

        associatedPartId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        associatedPartInventory.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        associatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
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
            if (part.getName().contains(partial) || String.valueOf(part.getId()).contains(partial)) {
                resultParts.add(part);
            }
        }
        if (resultParts.size() == 0) {
            Alert error = new Alert(Alert.AlertType.ERROR, "No parts were found.");
            error.setTitle("Error");
            error.showAndWait();
            return;
        }
        addPartsTable.setItems(resultParts);
    }

    /**
     * onAddPart takes the selected Part in the Parts table and adds it to the list of Parts to be
     * associated with the Product. The associatedPartsTable view is then updated.
     *
     * @param actionEvent
     */
    public void onAddPart(ActionEvent actionEvent) {
        selectedPart = addPartsTable.getSelectionModel().getSelectedItem();
        associatedParts.add(selectedPart);
        associatedPartsTable.setItems(associatedParts);

    }

    /**
     * onRemovePart prompts the user for confirmation, then takes the selected Part in the associatedPartsTable view and removes it from the list of Parts to be
     * associated with the Product. The associatedPartsTable view is then updated.
     *
     * @param actionEvent
     */
    public void onRemovePart(ActionEvent actionEvent) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this associated part?");
        confirm.setTitle("Confirm remove");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
            associatedParts.remove(selectedPart);
            associatedPartsTable.setItems(associatedParts);
        }
    }

    /**
     * onCancel prompts the user for confirmation, then switches to the Main form without saving product data.
     *
     * @param actionEvent the event triggered by the Cancel button.
     * @throws IOException makes sure the i/o operation is not interrupted.
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {

        System.out.println("button Cancel clicked");

        Alert cancel = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel adding this product?");
        cancel.setTitle("Confirm cancel");
        Optional<ButtonType> result = cancel.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK)
        { Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 935,416));
            stage.show();
        }
    }

    /**
     * onSave creates variables to hold new product data, and tries to assign them from their respective
     * text fields. Several validation functions are run, and if each field is valid, a new product is created
     * then added to the inventory. If a field is invalid, a prompt displays detailing the error.
     *
     * @param actionEvent the event triggered by the Save button.
     */
    public void onSave(ActionEvent actionEvent) {
        int newProductId = generateUniqueId();
        String newProductName = null;
        double newProductPrice;
        int newProductStock;
        int newProductMin;
        int newProductMax;
        ObservableList<Part> newProductAssociatedParts;


        try {
            newProductName = nameText.getText();
            newProductPrice = Double.parseDouble(priceText.getText());
            newProductStock = Integer.parseInt(inventoryText.getText());
            newProductMin = Integer.parseInt(minText.getText());
            newProductMax = Integer.parseInt(maxText.getText());
            newProductAssociatedParts = associatedParts;

            if (!validateName(newProductName)) {
                showAlert(1);
                return;
            }

            if (!validateRange(newProductMin, newProductMax, newProductStock)) {
                return;
            }

            Product newProduct = new Product(newProductId, newProductName, newProductPrice, newProductStock, newProductMin, newProductMax);

            for (Part part : newProductAssociatedParts)
            {
                newProduct.addAssociatedPart(part);
            }

            Inventory.addProduct(newProduct);

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 935,416));
            stage.show();
        }
        catch (Exception e)
        {
            showAlert(5);
        }

    }

    /**
     * validateName returns true if the name is not an empty string.
     *
     * @param name the string to be validated.
     * @return true if non-empty, false if empty.
     */
    public boolean validateName(String name)
    {
        return !name.isEmpty();
    }

    /**
     * validateRange makes sure min is less than max and that stock is between min and max, inclusive.
     * Returns boolean value based on results.
     *
     * @param min the min number to be compared.
     * @param max the max number to be compared.
     * @param stock the stock number to be compared.
     * @return true if all checks valid, false otherwise.
     */
    public boolean validateRange(int min, int max, int stock)
    {
        boolean rangeIsValid = false;
        if (min >= max)
        {
            showAlert(4);
        }

        else if (stock < min || stock > max)
        {
            showAlert(3);
        }

        else
        {
            rangeIsValid = true;
        }
        return rangeIsValid;
    }

    /**
     * generateUniqueId looks through the existing inventory of products and compares the current new product ID to all
     * existing IDs. If they match, the new product ID is incremented. If the new product ID is unique, it is returned.
     *
     * @return
     */
    private int generateUniqueId() {
        for (Product product : Inventory.getAllProducts())
        {
            if (product.getId() == id)
            {
                id++;
            }
            else
            {
                idIsUnique = true;
            }
        }
        return id;
    }

    /**
     * showAlert uses a switch to display certain error messages to be used by other functions.
     *
     * @param alert the alert number to be selected and displayed.
     */
    public void showAlert(int alert)
    {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText("Invalid input");
        switch (alert)
        {
            case 1:
                error.setContentText("Name must not be empty.");
                break;
            case 2:
                error.setContentText("Price must be a decimal number.");
                break;
            case 3:
                error.setContentText("Inv must be an integer between Min and Max.");
                break;
            case 4:
                error.setContentText("Min must be an integer less than Max.");
                break;
            case 5:
                error.setContentText("Check that all inputs are valid:\nPrice must be a decimal number.\nInv, Min, & Max must be integers.");
        }
        error.showAndWait();
    }
}
