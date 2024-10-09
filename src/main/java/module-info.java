module fourcorp.buildflow {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens fourcorp.buildflow to javafx.fxml;
    exports fourcorp.buildflow;
}