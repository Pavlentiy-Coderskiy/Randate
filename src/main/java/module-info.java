module com.rd.rdate {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.rd.rdate to javafx.fxml;
    exports com.rd.rdate;
}