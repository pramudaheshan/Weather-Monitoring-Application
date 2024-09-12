package org.example.weathermonitoringapplication;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
            resetData();
            client = new WeatherClient("localhost", 12345);// Modify with your server address and port
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetData(){
        temperatureLabel.setText("N/A");
        conditionLabel.setText("N/A");
        humidityLabel.setText("Humidity: N/A");
        windSpeedLabel.setText("Wind: N/A");
        weatherIcon.setImage(new Image(getClass().getResourceAsStream("/Images/sun.png")));
    }


    @FXML
    private void handleSearchButton(ActionEvent event) {
        String city = searchField.getText().trim(); // Trim extra spaces
        if (city.isEmpty()) {
            showError("Please enter a valid city name.");
            return;
        }

        try {
            String response = client.requestWeatherData(city);

            // Check if the response contains an error message
            if (response.startsWith("Error")) {
                showError(response); // Display the error message returned from the server
                resetData(); // Clear the UI to allow the user to enter a correct city
                return; // Exit if there's an error
            }

            // Parse and validate the response
            String[] data = response.split(",");
            if (data.length == 4 && !data[0].equals("null") && !data[1].equals("null")) {
                // Update labels and images with valid data from server
                temperatureLabel.setText(data[0]);
                conditionLabel.setText(data[1]);
                humidityLabel.setText("Humidity: " + data[2]);
                windSpeedLabel.setText("Wind: " + data[3]);

                // Update weather icons based on condition
                String condition = data[1].toLowerCase();
                if (condition.contains("cloud")) {
                    weatherIcon.setImage(new Image(getClass().getResourceAsStream("/Images/cloud.png")));
                } else if (condition.contains("sunny")) {
                    weatherIcon.setImage(new Image(getClass().getResourceAsStream("/Images/sunny.png")));
                } else {
                    weatherIcon.setImage(new Image(getClass().getResourceAsStream("/Images/sun.png")));
                }
            } else {
                // Handle invalid or empty data
                resetData();
                showError("No valid weather data found for this city. Please try again.");
            }
        } catch (IOException e) {
            showError("Error connecting to the server.");
            e.printStackTrace();
        }
    }

    private void showError(String errorMessage) {
        // Show error message on the GUI
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
