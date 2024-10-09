package fourcorp.buildflow;

import fourcorp.buildflow.application.Reader;

import static fourcorp.buildflow.application.CalculateProductionTime.calculateTotalProductionTime;

public class HelloController {
   /* @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    */

    public static void main(String[] args) {
        try {
            Reader.loadOperations("textFiles/artigos.csv");
            Reader.loadMachines("textFiles/maquinas.csv");
        } catch (Exception e) {
            System.out.println("Error uploading files: " + e.getMessage());
            return;
        }

        calculateTotalProductionTime();
    }
}