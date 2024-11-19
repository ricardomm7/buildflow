package fourcorp.buildflow;

import fourcorp.buildflow.application.Reader;
import fourcorp.buildflow.ui.Menu;

import java.io.IOException;

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

    public static void main(String[] args) throws IOException {
        //launch();
        try {
            Reader.loadOperations("textFiles/articles.csv");
            Reader.loadMachines("textFiles/workstations.csv");
            Reader.loadItems("textFiles/items.csv");
            Reader.loadSimpleOperations("textFiles/operations.csv");
            Reader.loadBOO("textFiles/boo_v2.csv");
        } catch (Exception e) {
            System.out.println("Error uploading files: " + e.getMessage());
            return;
        }
        Menu menu = new Menu();
        menu.displayMenu();
    }
}