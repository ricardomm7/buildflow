package fourcorp.buildflow;

import fourcorp.buildflow.application.GraphViz;
import fourcorp.buildflow.application.Reader;
import fourcorp.buildflow.application.GraphViz;
import fourcorp.buildflow.application.ReaderToSQL;
import fourcorp.buildflow.ui.Menu;

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
            //ReaderToSQL.readExcelAndGenerateSQL("textFiles/Dataset01_v1.xlsx", "outFiles/01_v1.sql");
            //ReaderToSQL.readExcelAndGenerateSQL("textFiles/Dataset01_v2.xlsx", "outFiles/01_v2.sql");
            //ReaderToSQL.readExcelAndGenerateSQL("textFiles/Dataset02_v1.xlsx", "outFiles/02_v1.sql");
            ReaderToSQL.readExcelAndGenerateSQL("textFiles/Dataset02_v2.xlsx", "outFiles/02_v2.sql");
            //GraphViz.saveInformation("textFiles/BOM.csv");
            GraphViz.saveInformation("textFiles/graph_other_option.csv");
            String dotFilePath = "outFiles/Graph.dot";
            GraphViz.generateGraph(dotFilePath);
            System.out.println("Graph generated successfully: " + dotFilePath);
        } catch (Exception e) {
            System.out.println("Error uploading files: " + e.getMessage());
            return;
        }
        Menu menu = new Menu();
        menu.displayMenu();
    }
}