package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.*;

/**
 * FUTURE ENHANCEMENT: Upon application close or main form initialize, the data could be parsed to a file or database
 * and thus be preserved across application lifetimes. This would allow a company to keep track of inventory without losing
 * data every application closure.
 *
 * JavaDocs files are located under /c482_pa/javadocs
 *
 * @author Luke Burton
 * Student ID 001679800
 */
public class Main extends Application
{
    /**
     * start loads the Main form.
     *
     * @param stage the stage on which to display the form.
     * @throws Exception the exception thrown if stage load is unsuccessful.
     */
    @Override
    public void start (Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setScene(new Scene(root, 935,416));
        stage.show();
    }

    /**
     * LOGICAL ERROR: addTestData was called in the MainForm class initialize, which created and added new test parts
     * and products every time the main for was loaded, creating duplicates. Moving the function call to the main function
     * corrected the problem, allowing the test data only to be added once per application lifetime.
     * @param args standard param for main
     */
    public static void main(String[] args)
    {
        addTestData();
        launch(args);
    }

    /**
     * addTestData builds two parts and two products and adds them to the inventory as test data
     */
    public static void addTestData() {
        Part part = new InHouse(1,"Tire", 12.00, 1,0,10,150 );
        Part newPart = new Outsourced(2, "Frame", 100.00, 1, 0, 5, "Bill Parts Co");

        Product product = new Product(3, "Bicycle", 400.00, 3, 0, 10);
        Product newProduct = new Product(4, "Motorbike", 2000.00, 1, 0, 10);

        Inventory.addPart(part);
        Inventory.addPart(newPart);

        Inventory.addProduct(product);
        Inventory.addProduct(newProduct);

    }
}