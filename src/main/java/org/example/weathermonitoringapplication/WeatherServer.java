package org.example.weathermonitoringapplication;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WeatherServer {
    private ServerSocket serverSocket;
    private final String API_KEY = "2813a895f73147c0ae5135429242709";

    public WeatherServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
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
                String city;

                while ((city = in.readLine()) != null) {

                    if (city.equalsIgnoreCase("exit")) {
                        System.out.println("Client requested to terminate the connection.");
                        break;
                    }

                    String weatherData = getWeatherData(city);
                    out.println(weatherData);
                }

            } catch (IOException e) {

                System.err.println("Error in ClientHandler: " + e.getMessage());
                e.printStackTrace();

            } finally {
                try {

                    System.out.println("Closing connection for: " + socket.getInetAddress().getHostAddress());
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error closing resources: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        private String getWeatherData(String city) throws IOException {
            String apiUrl = "https://api.weatherapi.com/v1/forecast.json?key=" + API_KEY + "&q=" + city + "&days=1&aqi=yes&alerts=yes";

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(apiUrl);
                try (CloseableHttpResponse response = client.execute(request)) {
                    HttpEntity entity = response.getEntity();
                    String json = EntityUtils.toString(entity);


                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("error")) {
                        return "Error: Invalid city name. Please try again.";
                    }

                    return parseWeatherData(json);
                }
            }
        }

        private String parseWeatherData(String json) {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject current = jsonObject.getJSONObject("current");
            JSONObject condition = current.getJSONObject("condition");
            JSONObject location = jsonObject.getJSONObject("location");



            String temperature = current.getInt("temp_c") + "°C";
            String weatherCondition = condition.getString("text");
            String humidity = current.getInt("humidity") + "%";
            String windSpeed = current.getInt("wind_kph") + " km/h";
            String localDateTime = location.getString("localtime");

            
            JSONArray forecastHours = jsonObject.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour");
            StringBuilder forecastBuilder = new StringBuilder();

           
            for (int i = 0; i < forecastHours.length(); i += 4) {
                JSONObject hourData = forecastHours.getJSONObject(i);
                String time = hourData.getString("time").split(" ")[1]; 
                String temp = hourData.getDouble("temp_c") + "°C"; 
                String cond = hourData.getJSONObject("condition").getString("text");

                forecastBuilder.append(time).append(", ").append(temp).append(", ").append(cond).append("\n");
            }

            
            String forecastData = forecastBuilder.toString();



           
            return temperature + "," + weatherCondition + "," + humidity + "," + windSpeed + ","  + localDateTime + "," + "\n" + forecastData.trim() + "\n";
        }
    }

    public static void main(String[] args) {
        try {
            WeatherServer server = new WeatherServer(12345);
            System.out.println("Weather server started on port 12345");
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
