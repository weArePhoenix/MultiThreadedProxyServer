package org.phoenix.proxyServer;


import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            // Connect to the server on localhost, port 12345
            Socket socket = new Socket("localhost", 12345);

            // Create input and output streams for communication with the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Create a BufferedReader to read input from console
            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

            // Create a thread to handle server's messages
            Thread serverThread = new Thread(() -> {
                String inputLine;
                try {
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Server: " + inputLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverThread.start();

            // Read messages from console and send them to server
            String userInput;
            while ((userInput = consoleIn.readLine()) != null) {
                out.println(userInput);
            }

            // Close connections
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}