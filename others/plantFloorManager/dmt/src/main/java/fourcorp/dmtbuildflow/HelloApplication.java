package fourcorp.dmtbuildflow;

import fourcorp.dmtbuildflow.application.ReaderToSQL;

public class HelloApplication {
   /*
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
        ReaderToSQL.readExcelAndGenerateSQL("filesIn/Dataset02_v2.xlsx", "sqlFilesOut/02_v2.sql");

        //launch();
    }
}