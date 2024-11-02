module com.shep.marufx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.shep.marufx to javafx.fxml;
    exports com.shep.marufx;
}