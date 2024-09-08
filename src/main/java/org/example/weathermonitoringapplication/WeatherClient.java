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
        out.println(request);
        return in.readLine();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}

