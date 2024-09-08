module org.example.weathermonitoringapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.example.weathermonitoringapplication to javafx.fxml;
    exports org.example.weathermonitoringapplication;
}