module com.example.demowork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.demowork to javafx.fxml;
    exports com.example.demowork;
}