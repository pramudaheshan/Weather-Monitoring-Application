package org.example.weathermonitoringapplication;

import java.io.*;
import java.net.*;

public class WeatherClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public WeatherClient(String serverAddress, int serverPort) throws IOException {
        socket = new Socket(serverAddress, serverPort);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String requestWeatherData(String request) throws IOException {
        try {
            out.println(request);
            return in.readLine();
        } catch (SocketTimeoutException e) {
            System.err.println("Error: Server timeout.");
            // Handle timeout (e.g., return an error message)
            return "Error: Timeout";
        }
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }


    /*public static void main(String[] args) {
        try {
            WeatherClient client = new WeatherClient("localhost", 12345);
            String response = client.requestWeatherData("London");
            System.out.println(response);
            response = client.requestWeatherData("Paris");
            System.out.println(response);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}



