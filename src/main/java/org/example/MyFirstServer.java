package org.example;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyFirstServer {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(1234);
        System.out.printf("En attend la connexion ...\n");
        Socket s = ss.accept();
        InputStream is = s.getInputStream();
        OutputStream os = s.getOutputStream();
        String address = s.getRemoteSocketAddress().toString();
        System.out.printf("We waiting the number to be send "+address+"\n");
        int number = is.read();
        System.out.printf("We received the number: "+number+"\n");
        int calc = number * 5;
        System.out.printf("I will send the respond:" +calc+"\n");
        os.write(calc);
        s.close();
    }
}