package org.phoenix.proxiserver;
import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyThread extends Thread{
    private Socket clientSocket;

    public ProxyThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            // Read request from client
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            StringBuilder requestBuilder = new StringBuilder();
            String line;
            while ((line = clientReader.readLine()) != null && !line.isEmpty()) {
                requestBuilder.append(line).append("\r\n");
            }
            String request = requestBuilder.toString();
            System.out.println("Request from client: " + request);

            // Extract URLs from request
            String[] lines = request.split("\r\n");
            for (String reqLine : lines) {
                if (reqLine.startsWith("GET ")) {
                    String[] parts = reqLine.split(" ");
                    String url = parts[1];
                    forwardRequest(url);
                }
            }

            // Close connections
            clientReader.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void forwardRequest(String url) {
        try {
            // Establish connection to destination server
            url = url.substring(1);
            URL destinationURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) destinationURL.openConnection();

            // Get response from destination server
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = serverReader.readLine()) != null) {
                responseBuilder.append(line);
                responseBuilder.append("\r\n");
            }
            String response = responseBuilder.toString();

            // Send response back to client
            PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            clientWriter.println("HTTP/1.1 200 OK");
            clientWriter.println("Content-Length: " + response.length());
            clientWriter.println("Content-Type: text/html");
            clientWriter.println();
            clientWriter.println(response);

            // Close connections
            serverReader.close();
            clientWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
