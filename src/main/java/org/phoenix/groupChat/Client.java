package org.phoenix.groupChat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String username;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket socket;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            closeEverything(socket, reader, writer);
        }
    }

    private void sendMessage() {
        try {
            writer.write(username);
            writer.newLine();
            writer.flush();

            Scanner sc = new Scanner(System.in);
            while (socket.isConnected()) {
                String message = sc.nextLine();
                writer.write(username + ": " + message);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, reader, writer);
        }
    }

    private void listenMessages() {
        new Thread(() -> {
            try {
                String message;
                while (socket.isConnected()) {
                    message = reader.readLine();
                    System.out.println(message);
                }
            } catch (IOException e) {
                closeEverything(socket, reader, writer);
            }
        }).start();

    }

    public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
        try {
            if (socket != null) socket.close();
            if (reader != null) reader.close();
            if (writer != null) writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a username: ");
        String username = sc.nextLine();

        Socket socket = new Socket("localhost", 7777);

        Client client = new Client(socket, username);
        client.listenMessages();
        client.sendMessage();
    }
}
