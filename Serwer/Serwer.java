/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serwer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Klasa opisująca interfejs serwera i jego interakcję z użytkownikiem/zarządcą serwera.
 * @author Michał Janczyk
 * @author Piotr Kierzek
 */
public class Serwer extends JFrame {
    /**
     * Numer portu serwera.
     */
    private int port_serwera;
    /**
    * Przycisk włączający działanie sieciowe.
    */
    private JButton przycisk_wlacz;
    /**
     * Zmienna opisująca obecny stan serwera (online/offline).
     */
    private static boolean serwer_online=false;
    /**
     * Konstruktor bezparametrowy klasy.
     */
    public Serwer(){
        setTitle("Aplikacja Serwera");
        przycisk_wlacz = new JButton("Wlacz serwer");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                wlaczSerwer();


            }
        });
        przycisk_wlacz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!serwer_online){
                    serwer_online=true;
                t.start();}
                else{
                    JFrame serwer_juz_wlaczony = new JFrame("Serwer jest podlaczony!");
                    JButton ok= new JButton("Ok");
                    serwer_juz_wlaczony.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    serwer_juz_wlaczony.add(new JLabel("Serwer jest juz wlaczony!"));
                    serwer_juz_wlaczony.add(ok);
                    serwer_juz_wlaczony.setLayout(new FlowLayout());
                    serwer_juz_wlaczony.setSize(200,80);



                    ok.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            serwer_juz_wlaczony.dispose();
                        }
                    });
                    pack();
                    serwer_juz_wlaczony.setResizable(false);
                    serwer_juz_wlaczony.setVisible(true);
                }

            }
        });



        setLayout(new FlowLayout());
        add(przycisk_wlacz);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setResizable(false);
        setVisible(true);
    }

    /**
     * Metoda inicjalizująca sieciową działalność serwera.
     */
    public void wlaczSerwer() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("PlikiKonfiguracyjne\\ip.txt"));
            port_serwera = Integer.parseInt(br.readLine());
            ServerSocket serverSocket = new ServerSocket(port_serwera);
            System.out.println("Serwer jest online! Oczekiwanie na polaczenie...");
            br.close();
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new Watek(socket)).start();
            }

        }
        catch (IOException e){
            System.out.println("Serwer nie mogl zostac uruchomiony");
            System.err.println(e);
        }
    }



}