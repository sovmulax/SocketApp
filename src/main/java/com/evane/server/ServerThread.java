package com.evane.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import java.lang.reflect.Method;
import java.util.Scanner;

public class ServerThread extends Thread {
    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;

    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.socket = socket;
        this.threadList = threads;
    }

    @Override
    public void run() {
        try {
            DataManager dm = new DataManager();
            Personne p;
            int id;

            // Reading the input from Client
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // variales
            int methode = 0;
            String nom = null;
            int age = 0;
            int i = 0;

            // returning the output to the client : true statement is to flush the buffer
            // otherwise
            // we have to do it manuallyy
            output = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Bienvenue sur l'application");

            // inifite loop for server
            while (true) {
                i++;

                if (i == 1) {
                    String outputString = input.readLine();

                    if (Integer.parseInt(outputString) == 1) {
                        methode = 1;
                        System.out.println("addPersonne");

                    } else if (Integer.parseInt(outputString) == 2) {
                        methode = 2;
                        System.out.println("getId ");

                    } else if (Integer.parseInt(outputString) == 3) {
                        methode = 3;
                        System.out.println("getPersonne ");

                    }

                } else if (i == 2) {
                    String outputString = input.readLine();
                    //output.println("deux" + outputString);
                    nom = outputString;

                    System.out.println("Nom : " + outputString);

                } else if (i == 3) {
                    String outputString = input.readLine();
                    System.out.println("Age : " + outputString);
                    
                    age = Integer.parseInt(outputString);

                    if (methode == 1) {
                        p = new Personne(age, nom);
                        id = dm.getId(p);
                        if(id == -1){
                            id = dm.addPersonne(p);
                            output.println("Vous avez été Ajouté: " + id);
                        }else{
                            output.println("Vous êtes déjà dans la liste. \n votre id : " + id);
                        }
                    } else if (methode == 2) {
                        p = new Personne(age, nom);
                        id = dm.getId(p);
                        output.println("Votre id : " + id);
                    } else if (methode == 3) {
                        p = dm.getPersonne(dm.getId(new Personne(age, nom)));
                        id = dm.addPersonne(p);
                        output.println("Nom : " + p.nom + " Age :" + p.age);
                    }
                    // print le resultat
                    //output.println(outputString);
                }else if (i == 4) {
                    String outputString = input.readLine();
                    if (!outputString.equals("no") ) {
                        output.println("Fin de Session ");
                        i = 0;
                    }

                    System.out.println("Nom : " + outputString);
                }
                
            }

        } catch (Exception e) {
            System.out.println("Error occured " + e.getStackTrace());
        }
    }

    private void printToALlClients(String outputString) {
        for (ServerThread sT : threadList) {
            sT.output.println(outputString);
        }

    }
}
