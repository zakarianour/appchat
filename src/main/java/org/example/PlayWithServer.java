package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.IllegalFormatException;
import java.util.Random;

public class PlayWithServer extends Thread{
    private int clientNbr;
    private int SecretNbr;
    private boolean fin;
    public static void main(String[] args) {
        new org.example.PlayWithServer().start();
    }

    @Override
    public void run(){
        try {
            ServerSocket ss = new ServerSocket(1234);
            SecretNbr = new Random().nextInt(100);
            System.out.printf("Le serveur essaie de démarrer ....");
            while (true){
                Socket s = ss.accept();
                ++clientNbr;
                new Communication(s,clientNbr).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Communication extends Thread{
        private Socket s;
        private int clientNbr;
        Communication(Socket s, int clientNbr){
            this.s = s;
            this.clientNbr = clientNbr;
        }

        @Override
        public void run(){
            try {
                InputStream is = s.getInputStream(); // octet
                InputStreamReader isr = new InputStreamReader(is); // caractere
                BufferedReader br = new BufferedReader(isr); // chaine de caractere
                String IP = s.getRemoteSocketAddress().toString();
                System.out.printf("Le client numéro : "+clientNbr+" et son IP est : "+IP);

                OutputStream os = s.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                pw.println("Vous etes le client : "+ clientNbr);
                pw.println("Devinez le nombre secret ...");

                while (true){
                    String UserRequest = br.readLine();
                    boolean RequestFormat = false;
                    int UserNbr=0;
                    try {
                        UserNbr = Integer.parseInt(UserRequest);
                        RequestFormat = true;
                    }catch (NumberFormatException e){
                        //e.printStackTrace();
                        pw.println("Vous devez entrer un nombre\n");
                    }

                    if (RequestFormat){
                        System.out.printf("Le client "+clientNbr+" a envoyé le nombre "+UserNbr);
                        if (!fin){
                            if (UserNbr > SecretNbr) pw.println("Votre nombre est supérieur au nombre secret");
                            else if (UserNbr < SecretNbr) pw.println("Votre nombre est inférieur au nombre secret");
                            else {
                                pw.println("Tu es le gagnant");
                                System.out.printf("Le gagnant est le client numéro : "+clientNbr+" et son IP est : "+IP);
                                fin = true;

                            }
                        }
                        else {
                            pw.println("Fin de jeu, le gagnant est le client numéro : "+clientNbr);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}