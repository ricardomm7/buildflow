module fourcorp.buildflow {
    requires javafx.controls;
    requires javafx.fxml;

    opens fourcorp.buildflow to javafx.fxml;
    exports fourcorp.buildflow;
    exports fourcorp.buildflow.ui;
    opens fourcorp.buildflow.ui to javafx.fxml;
}