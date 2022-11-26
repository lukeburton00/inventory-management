package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *
 * @author Luke Burton
 */
public class ModifyPartForm implements Initializable {

    public Part selectedPart;
    public RadioButton inHouse;
    public RadioButton outSourced;

    public TextField idText;
    public TextField nameText;
    public TextField inventoryText;
    public TextField priceText;
    public TextField maxText;
    public TextField minText;
    public TextField machineText;

    public Button save;
    public Button cancel;

    public Label machineLabel;

    /**
     * initialize the form and load in data from the selected part. Sets the correct radio button and label.
     *
     * @param url the path for the form
     * @param resourceBundle the data to be internationalized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedPart = MainForm.selectedPart;

        idText.setText(String.valueOf(selectedPart.getId()));
        nameText.setText(selectedPart.getName());
        inventoryText.setText(String.valueOf(selectedPart.getStock()));
        priceText.setText(String.valueOf(selectedPart.getPrice()));
        maxText.setText(String.valueOf(selectedPart.getMax()));
        minText.setText(String.valueOf(selectedPart.getMin()));

        if (selectedPart instanceof InHouse) {
            inHouse.setSelected(true);
            machineText.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
        }

        if (selectedPart instanceof Outsourced) {
            outSourced.setSelected(true);
            machineLabel.setText("Company Name");
            machineText.setText(((Outsourced) selectedPart).getCompanyName());
        }

    }

    /**
     * onInHouse sets the bottom form label to "Machine ID" when the InHouse radio button is selected.
     *
     * @param actionEvent the event triggered by the InHouse radio button.
     */
    public void onInHouse(ActionEvent actionEvent) {
        machineLabel.setText("Machine ID");
    }

    /**
     * onOutsourced sets the bottom form label to "Company Name" when the OutSourced radio button is selected.
     *
     * @param actionEvent the event triggered by the OutSourced radio button
     */
    public void onOutSourced(ActionEvent actionEvent) {
        machineLabel.setText("Company Name");
    }


    /**
     * onSave creates variables to hold new part data, and tries to assign them from their respective
     * text fields. Several validation functions are run, and if each field is valid, a new part is created
     * then added to the inventory. The original part is then deleted.
     * If a field is invalid, a prompt displays detailing the error.
     *
     * @param actionEvent the event triggered by the Save button.
     */
    public void onSave(ActionEvent actionEvent) {
        int newPartId = selectedPart.getId();
        String newPartName = null;
        double newPartPrice;
        int newPartStock;
        int newPartMin;
        int newPartMax;
        int newPartMachineId;
        String newPartCompanyName;

        try {
            newPartName = nameText.getText();
            newPartPrice = Double.parseDouble(priceText.getText());
            newPartStock = Integer.parseInt(inventoryText.getText());
            newPartMin = Integer.parseInt(minText.getText());
            newPartMax = Integer.parseInt(maxText.getText());

            if (!validateName(newPartName)) {
                showAlert(1);
                return;
            }

            if (!validateRange(newPartMin, newPartMax, newPartStock)) {
                return;
            }
            if (inHouse.isSelected()) {
                newPartMachineId = Integer.parseInt(machineText.getText());
                InHouse newPart = new InHouse(newPartId, newPartName, newPartPrice, newPartStock, newPartMin, newPartMax, newPartMachineId);

                Inventory.addPart(newPart);
            }

            if (outSourced.isSelected()) {
                newPartCompanyName = machineText.getText();
                Outsourced newPart = new Outsourced(newPartId, newPartName, newPartPrice, newPartStock, newPartMin, newPartMax, newPartCompanyName);
                Inventory.addPart(newPart);
            }
            Inventory.deletePart(selectedPart);
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
     * onCancel prompts the user for confirmation, then switches to the Main form without saving part data.
     *
     * @param actionEvent the event triggered by the Cancel button.
     * @throws IOException makes sure the i/o operation is not interrupted.
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {

        System.out.println("button Cancel clicked");

        Alert cancel = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel changes to this part?");
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
