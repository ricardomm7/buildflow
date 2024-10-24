module fourcorp.dmtbuildflow {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;


    opens fourcorp.dmtbuildflow to javafx.fxml;
    exports fourcorp.dmtbuildflow;
    exports fourcorp.dmtbuildflow.ui;
    opens fourcorp.dmtbuildflow.ui to javafx.fxml;
}