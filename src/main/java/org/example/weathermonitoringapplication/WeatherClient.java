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

            String line = in.readLine(); // Read the first line
            StringBuilder responseBuilder = new StringBuilder();

            if (line.toLowerCase().contains("error")) {
                return line;

            } else {
                // Append the first line (which we already read)
                responseBuilder.append(line).append("\n");

                // Now read the remaining lines
                while ((line = in.readLine()) != null) {
                    responseBuilder.append(line).append("\n");
                    if (line.isEmpty()) {  // Stop if an empty line is encountered
                        break;
                    }
                }

                // Return the full response
                return responseBuilder.toString().trim();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Error: Server timeout.");
            return "Error: Timeout";
        }
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }


    //Use this for testing
    public static void main(String[] args) {
        try {
            WeatherClient client = new WeatherClient("localhost", 12345);
            String response = client.requestWeatherData("f");
            System.out.println(response);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
