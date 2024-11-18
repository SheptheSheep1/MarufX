module com.shep.marufx {
    requires com.fasterxml.jackson.databind;
    requires MaterialFX;


    opens com.shep.marufx to javafx.fxml;
    exports com.shep.marufx;
}