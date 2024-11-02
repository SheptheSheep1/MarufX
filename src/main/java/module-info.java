module com.shep.marufx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.shep.marufx to javafx.fxml;
    exports com.shep.marufx;
}