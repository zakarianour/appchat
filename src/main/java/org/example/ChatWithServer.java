package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatWithServer extends Thread{
    private int clientNbr;
    private List<Communication> clientsConnectes = new ArrayList<Communication>();
    public static void main(String[] args) {
        new org.example.ChatWithServer().start();
    }

    @Override
    public void run(){
        try {
            ServerSocket ss = new ServerSocket(1234);
            System.out.printf("Le serveur essaie de démarrer ....");
            while (true){
                Socket s = ss.accept();
                ++clientNbr;
                Communication NewCommunication = new Communication(s,clientNbr);
                clientsConnectes.add(NewCommunication);
                NewCommunication.start();
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
                pw.println("Envoyer le message que vous voulez ...");

                while (true){
                    String UserRequest = br.readLine();
                    if(UserRequest.contains("=>")){
                        String[] userMessage = UserRequest.split("=>");
                        if(userMessage.length == 2){
                            int numeroClient = Integer.parseInt(userMessage[0]);
                            String msg = userMessage[1];
                            Send(msg, s, numeroClient);
                        }
                    }else {
                        Send(UserRequest, s, -1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void Send(String UserRequest, Socket socket, int nbr) throws Exception {
            for (Communication client: clientsConnectes){
                if (client.s != socket){
                    if (client.clientNbr == nbr || nbr == -1){
                        PrintWriter pw = new PrintWriter(client.s.getOutputStream(), true);
                        pw.println(UserRequest);
                    }
                }
            }
        }
    }
}