package com.evane.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {
    private final Socket socket;
    private final ArrayList<ServerThread> threadList;
    private PrintWriter output;

    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.socket = socket;
        this.threadList = threads;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            DataManager dm = new DataManager();
            ArrayList<String> callStack = new ArrayList<>();

            // returning the output to the client : true statement is to flush the buffer
            // otherwise
            // we have to do it manually
            output = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Client connecté !");
            //outputStream.writeUTF("Bienvenue sur l'application");

            // infinite loop for server
            while (true) {
                if (callStack.size() < 1) {
                    System.out.println("Printing menu...");
                    outputStream.writeUTF("Choisissez une option du Menu : \n (1):addPersonne \n (2):getId \n (3):getPersonne \n =>");
                }

                if (callStack.size() == 1) {
                    System.out.println("Get first arg");
                    int menuOption;

                    try {
                        menuOption = Integer.parseInt(callStack.get(0));
                    } catch (NumberFormatException numberFormatException) {
                        menuOption = 0;
                    }

                    switch (menuOption) {
                        case 1:
                            outputStream.writeUTF("Vous voulez ajouter une personne... Veuillez saisir son nom : ");
                            break;
                        case 2:
                            outputStream.writeUTF("Vous voulez récupérer l'ID d'une personne... Veuillez saisir son nom : ");
                            break;
                        case 3:
                            outputStream.writeUTF("Vous voulez récupérer les informations d'une personne... Veuillez saisir son ID : ");
                            break;
                        default:
                            outputStream.writeUTF("Veuillez choisir parmi les options du menu !");
                            callStack.clear();
                    }
                }

                if (callStack.size() == 2) {
                    System.out.println("Get first arg");
                    System.out.println(callStack);
                    int option = Integer.parseInt(callStack.get(0));
                    System.out.println(option);

                    switch (option) {
                        case 1:
                            System.out.println("1");
                        case 2:
                            System.out.println("2");
                            outputStream.writeUTF("Veuillez saisir son age : ");
                            break;
                        case 3:
                            System.out.println("3");
                            Personne p = dm.getPersonne(Integer.parseInt(callStack.get(1)));
                            outputStream.writeUTF(p.toString());
                            callStack.clear();
                            break;
                    }

                    System.out.println("Out");
                }

                if (callStack.size() == 3) {
                    System.out.println("Get second arg");
                    int age = Integer.parseInt(callStack.get(2));
                    String nom = callStack.get(1);
                    Personne p = new Personne(age, nom);

                    switch (Integer.parseInt(callStack.get(0))) {
                        case 1:
                            int addPersonneId = dm.addPersonne(p);
                            outputStream.writeUTF("ID de la personne ajoutée : " + addPersonneId);
                            callStack.clear();
                            break;
                        case 2:
                            int personneId = dm.getId(p);
                            outputStream.writeUTF("ID de la personne recherchée : " + personneId);
                            callStack.clear();
                            break;
                    }
                }

                String request = inputStream.readUTF();
                System.out.println("Client input: " + request);
                System.out.println("Current callstack: " + callStack);
                callStack.add(request);
            }
        } catch (Exception e) {
            System.out.println("Error occurred " + e.getStackTrace());
        }
    }

    private void printToALlClients(String outputString) {
        for (ServerThread sT : threadList) {
            sT.output.println(outputString);
        }
    }
}
