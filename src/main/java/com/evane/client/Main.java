package com.evane.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            // Socket incoming stream
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            // Socket outgoing stream
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            ClientRunnable clientRun = new ClientRunnable(socket);

            Scanner scanner = new Scanner(System.in);

            new Thread(clientRun).start();

            String userInput;

            // loop closes when user enters exit command
            while (true) {
                // Print server response
                System.out.println(inputStream.readUTF());
                // Get keyboard input
                System.out.print("> ");
                userInput = scanner.nextLine();
                // Send request to the server
                outputStream.writeUTF(userInput);
                // Wait a bit for server to respond
                Thread.sleep(500);

                if (!userInput.equals("exit")) break;
            }

            System.out.println("Closing this connection...");
            socket.close();
            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            System.out.println("Exception occurred in client main: " + e.getStackTrace());
        }
    }
}