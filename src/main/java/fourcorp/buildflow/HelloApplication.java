package fourcorp.buildflow;

import fourcorp.buildflow.application.Reader;
//import fourcorp.buildflow.application.Simulator;
import fourcorp.buildflow.domain.Product;
import fourcorp.buildflow.repository.Repositories;

import java.util.List;

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
            Reader.loadOperations("textFiles/artigos.csv");
            Reader.loadMachines("textFiles/maquinas.csv");
            /*Simulator simulator = new Simulator();
            List<Product> products = Repositories.getInstance().getProductPriorityRepository().getProductPriorityLine().getByKey(null);
            simulator.createOperationQueues(products);
            simulator.processItems();*/
        } catch (Exception e) {
            System.out.println("Error uploading files: " + e.getMessage());
            return;
        }
        calculateTotalProductionTime();
    }
}