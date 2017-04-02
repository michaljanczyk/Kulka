/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serwer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Klasa dziedzicząca po Thread, która jest wątkiem serwera.
 * @author Michał Janczyk
 * @author Piotr Kierzek
 */
public class Watek implements Runnable{
    /**
     * Socket.
     */
    private Socket socket;
    /**
     * Strumień wejściowy.
     */
    private BufferedReader input;
    /**
     * Strumień wyjściowy.
     */
    private PrintWriter output;

    /**
     * Konstruktor klasy
     *
     * @param socket socket
     */
    public Watek(Socket socket) {
        this.socket = socket;
    }

    /**
     * Metoda obsługująca wszystkie zdarzenia pomiędzy klientem i serwerem
     */

    @Override
    public void run() {
        try {
            while (true) {
                InputStream inputStream = socket.getInputStream();
                input = new BufferedReader(new InputStreamReader(inputStream));
                OutputStream os = socket.getOutputStream();
                output = new PrintWriter(os, true);

                String od_klienta = input.readLine();

                if (od_klienta != null) {
                    System.out.println("OD KLIENTA: " +od_klienta);
                    String wiadomosc = Komendy.akcja(od_klienta);
                    output.println(wiadomosc);
                    output.flush();
                    System.out.println("DO KLIENTA: " + wiadomosc);
                } else {
                    System.out.println("Pusta komenda");
                }

            }
        } catch (Exception e) {
            System.out.println("WYLOGOWANO");
        }

    }
}
