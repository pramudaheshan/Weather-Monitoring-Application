package org.example.weathermonitoringapplication;

public class WeatherData {
    private String temperature;
    private String condition;
    private String humidity;
    private String windSpeed;



    public WeatherData(String temperature, String condition, String humidity, String windSpeed) {
        this.temperature = temperature;
        this.condition = condition;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getCondition() {
        return condition;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    @Override
    public String toString() {
        return temperature + "," + condition + "," + humidity + "," + windSpeed;
    }
}
