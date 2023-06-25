package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MySecondServerMultiThread extends Thread{
    private int clientNbr;
    public static void main(String[] args) {
        new MySecondServerMultiThread().start();
    }

    @Override
    public void run(){
        try {
            ServerSocket ss = new ServerSocket(1234);
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

                while (true){
                    String UserRequest = br.readLine();
                    pw.println("La taille de cette caracteres est : "+UserRequest.length());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}