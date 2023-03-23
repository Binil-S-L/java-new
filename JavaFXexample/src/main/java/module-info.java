module com.example.javafxexample {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.javafxexample to javafx.fxml;
    exports com.example.javafxexample;
}