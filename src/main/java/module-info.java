module org.example.weathermonitoringapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires owm.japis;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;

    opens org.example.weathermonitoringapplication to javafx.fxml;
    exports org.example.weathermonitoringapplication;
}