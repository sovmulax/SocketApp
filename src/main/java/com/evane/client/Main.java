package com.evane.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String adress = "";
        Scanner scannerr = new Scanner(System.in);
        System.out.println("Entrez votre adresse IP : ");
        adress = scannerr.nextLine();
        if (!ValidateIP((adress))) adress = "localhost";
        
        try (Socket socket = new Socket(adress, 5000)) {
            // Socket incoming stream
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            // Socket outgoing stream
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            // reading the input from server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // returning the output to the server : true statement is to flush the buffer
            // otherwise
            // we have to do it manuallyy
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // taking the user input
            Scanner scanner = new Scanner(System.in);
            String userInput;

            // Loop closes when user enters exit command
            do {
                // Print server response
                System.out.println(inputStream.readUTF());
                // Get keyboard input
                System.out.print("> ");
                userInput = scanner.nextLine();
                // Send request to the server
                outputStream.writeUTF(userInput);
                // Wait a bit for server to respond
                Thread.sleep(500);
            } while (!userInput.equals("exit"));

            System.out.println("Closing connection...");
            socket.close();
            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            System.out.println("Exception occurred in client main: " + e.getStackTrace());
        }
    }
}