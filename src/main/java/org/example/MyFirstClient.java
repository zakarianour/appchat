package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MyFirstClient {
    public static void main(String[] args) throws Exception {
        System.out.printf("......\n");
        Socket ss = new Socket("localhost", 1234);
        InputStream is = ss.getInputStream();
        OutputStream os = ss.getOutputStream();
        Scanner sc = new Scanner(System.in);
        System.out.printf("Enter a number: ");
        int nbr = sc.nextInt();
        System.out.printf("I will send the number : "+nbr+" to the server\n");
        os.write(nbr);
        System.out.printf("I'm waiting th server\n");
        int response = is.read();
        System.out.printf("The server response is : "+response+"\n");
    }
}