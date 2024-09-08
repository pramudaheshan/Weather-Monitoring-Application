package org.example.weathermonitoringapplication;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WeatherServer {
    private ServerSocket serverSocket;
    private final String API_KEY = "YOUR_API_KEY"; // Replace with your OpenWeatherMap API key

    public WeatherServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void run() {
            try {
                String city = in.readLine();
                String weatherData = getWeatherData(city);
                out.println(weatherData);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String getWeatherData(String city) throws IOException {
            String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(apiUrl);
                try (CloseableHttpResponse response = client.execute(request)) {
                    HttpEntity entity = response.getEntity();
                    String json = EntityUtils.toString(entity);

                    // Extract relevant data from JSON response
                    return parseWeatherData(json);
                }
            }
        }

        private String parseWeatherData(String json) {
            // Simple JSON parsing (requires org.json library or similar)
            // For more advanced parsing, consider using a library like Jackson or Gson
            org.json.JSONObject jsonObject = new org.json.JSONObject(json);
            org.json.JSONObject main = jsonObject.getJSONObject("main");
            org.json.JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);

            String temperature = main.getString("temp") + "°C";
            String condition = weather.getString("description");
            String humidity = main.getString("humidity") + "%";
            String windSpeed = jsonObject.getJSONObject("wind").getString("speed") + " m/s";

            return temperature + "," + condition + "," + humidity + "," + windSpeed;
        }
    }

    public static void main(String[] args) {
        try {
            WeatherServer server = new WeatherServer(12345);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}