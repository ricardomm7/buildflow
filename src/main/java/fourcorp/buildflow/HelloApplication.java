package fourcorp.buildflow;

import fourcorp.buildflow.application.Reader;

import static fourcorp.buildflow.application.CalculateProductionTime.calculateTotalProductionTime;

public class HelloApplication {
/*
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
     */

    public static void main(String[] args) {
        //launch();
        try {
            Reader.loadOperations("textFiles/articles.csv");
            Reader.loadMachines("textFiles/workstations.csv");
        } catch (Exception e) {
            System.out.println("Error uploading files: " + e.getMessage());
            return;
        }
        calculateTotalProductionTime();
    }
}