package org.example.weathermonitoringapplication;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

import java.io.IOException;

public class WeatherClientController {
    @FXML private TextField searchField;
    @FXML private ImageView weatherIcon;
    @FXML private Label temperatureLabel;
    @FXML private Label conditionLabel;
    @FXML private ImageView humidityIcon;
    @FXML private Label humidityLabel;
    @FXML private ImageView windIcon;
    @FXML private Label windSpeedLabel;

    private WeatherClient client;

    @FXML
    public void initialize() {
        try {
            client = new WeatherClient("localhost", 12345); // Modify with your server address and port
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearchButton(ActionEvent event) {
        String city = searchField.getText();
        try {
            String response = client.requestWeatherData(city);
            // Parse and display the response
            String[] data = response.split(",");
            if (data.length == 4) {
                // Update labels and images with data from server
                temperatureLabel.setText(data[0]);
                conditionLabel.setText(data[1]);
                humidityLabel.setText("Humidity: " + data[2]);
                windSpeedLabel.setText("Wind: " + data[3]);

                // Update icons based on weather condition (you may use different logic)
                // Example: Update icons based on condition
                String condition = data[1].toLowerCase();
                if (condition.contains("cloud")) {
                    weatherIcon.setImage(new Image(getClass().getResourceAsStream("cloud.png")));
                } else if (condition.contains("sunny")) {
                    weatherIcon.setImage(new Image(getClass().getResourceAsStream("sunny.png")));
                } else {
                    weatherIcon.setImage(new Image(getClass().getResourceAsStream("default.png")));
                }

                // You may also dynamically update humidityIcon and windIcon if needed
            } else {
                // Handle error or empty data
                temperatureLabel.setText("N/A");
                conditionLabel.setText("N/A");
                humidityLabel.setText("Humidity: N/A");
                windSpeedLabel.setText("Wind: N/A");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
