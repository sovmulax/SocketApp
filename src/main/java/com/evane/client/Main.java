package com.evane.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            // reading the input from server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // returning the output to the server : true statement is to flush the buffer
            // otherwise
            // we have to do it manuallyy
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // taking the user input
            Scanner scanner = new Scanner(System.in);
            String userInput = null;
            String response;
            String clientName = "empty";
            int i = 0;

            ClientRunnable clientRun = new ClientRunnable(socket);

            new Thread(clientRun).start();
            // loop closes when user enters exit command

            do {
                i++;

                if (i == 1) {
                    // saisie des données
                    System.out.println(
                            "Choisissez une Option du Menu : \n (1):addPersonne \n (2):getId \n (3):getPersonne \n =>");
                    userInput = scanner.nextLine();
                    clientName = userInput;

                    /* envoie de donnée au serveur */
                    output.println(userInput);

                } else if (i == 2) {
                    // saisie des données
                    System.out.println("Nom : ");
                    userInput = scanner.nextLine();
                    clientName = userInput;

                    /* envoie de donnée au serveur */
                    output.println(userInput);
                } else if (i == 3) {
                    // saisie des données
                    System.out.println("Age : ");
                    userInput = scanner.nextLine();
                    clientName = userInput;

                    /* envoie de donnée au serveur */
                    output.println(userInput);
                }else if (i == 4) {
                    // saisie des données
                    System.out.println("Voulez-Vous continuez ? : \n (no):non \n (exit):oui \n =>");
                    userInput = scanner.nextLine();
                    clientName = userInput;

                    /* envoie de donnée au serveur */
                    output.println(userInput);
                }

            } while (!userInput.equals("exit"));

        } catch (Exception e) {
            System.out.println("Exception occured in client main: " + e.getStackTrace());
        }
    }
}