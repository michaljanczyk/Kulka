package Gra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.Socket;

/**
 * @author Michał Janczyk
 * @author Piotr Kierzek
 */

/**
 * Klasa opisujaca dzialanie glownego okna menu.
 */
public class GlowneOkno extends JFrame {

    /**
     * Zmienna przechowujaca layout pozwalajacy na podmienianie paneli
     */
    static CardLayout layout_kartkowy = new CardLayout();

    /**
     * Zmienna przechowujaca panel, ktory przechowuje wszystkie okna gry.
     */
    static JPanel zbior_paneli;

    /**
     * Zmienna przechowujaca okno menu glownego.
     */
    static Menu menu_glowne;

    /**
     * Zmienna przechowujaca okno, w ktorym użytkownik podaje swoj nick w grze.
     */
    static Nick nick;

    /**
     * Zmienna przechowujaca okno, w ktorym wyswietlane sa najlepsze wyniki.
     */
    static ListaNajlepszychWynikow lista;

    /**
     * Zmienna przechowujaca okno, w ktorym przechowywane jest okno konca poziomu i konca gry.
     */
    static ObrazekKoncaPoziomu obrazekkonca;

    /**
     * Zmienna przechowujaca okno planszy gry.
     */
    static KlasaPlanszy klasa_planszy;

    /**
     * Zmienna przechowujaca wysokosc okna.
     */
    static int screenHeight;

    /**
     * Zmienna przechowujaca szerokosc okna.
     */
    static int screenWidth;
    /**
     * Zmienna przechowujaca aktualny poziom gry.
     */
    static int aktualny_poziom=1;

    /**
     * Zmienna przehchowujaca maksymalna liczbe poziomow.
     */
    static int maks_liczba_poziomow = 1;

    /**
     * Zmienna przechowujaca informacje o aktualnym przyspieszeniu(tylko z nazwy, w rzeczywistosci predkosc) grawitacyjnym kulki
     * dokładniej jest to czas uśpienia wątku w [ms]
     */
    private int przyspieszenie_grawitacyjne;
    
    /**
     * Zmienna włączająca/wyłączająca grawitację.
     */
    private boolean grawitacjaWlaczona = true;

    /**
     * Socket serwera
     */
    private static Socket serwerSocket;
    
    /**
     * Zmienna określająca czy gra jest w trybie offline czy online.
     */
    private static boolean online;

    /**
     * Zmienna zawierająca czas startowy (do licznika czasu).
     */
    static long tStart;
    /**
     * Zmienna opisująca ilośc sekund, które upłynęły (do licznika czasu).
     */
    static double elapsedSeconds;
    /**
     * Zmienna zawierająca czas końcowy (do licznika czasu).
     */
    private long tEnd = 0;
    /**
     * Zmienna zawierająca długość czasu, który upłynął (do licznika czasu).
     */
    private long tDelta = 0;


    /**
     * Konstruktor klasy glownego okna z parametrem gniazda serwera.
     * Jezeli udalo sie polaczyc z serwerem, to mozemy grac online.
     * Jezeli pliki nie zostaly usuniete lub nie zmieniono ich nazw, to okno
     * @param serwerSocket gniazdo serwera
     */
    public GlowneOkno(Socket serwerSocket){

        this.serwerSocket=serwerSocket;
        online=serwerSocket==null?false:true;
        inicjalizacjaUI();
        ustawOkno();
        Gracz.liczba_zyc = Odczytywanie.odczytana_ilosc_zyc;



        // wlaczanie grawitacji. nic stad nie usuwac, bo dziala tak jak jest teraz. po usunieciu Thread.sleep() przestaje dzialac :(
        do {
//            System.out.print(Nick.byl_wcisniety_przycisk);
//            if (Nick.byl_wcisniety_przycisk)
//                wlaczGrawitacje();
//
        try { //odczekanie chwili az grawitacjaWlaczona sie wlaczy <-- juz nie, ale wciaz potrzebne, nie wiem dlaczego xD
            Thread.sleep(1000);
//            System.out.print(Nick.byl_wcisniety_przycisk);
//            wlaczGrawitacje();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
//
        }
        while(!Nick.byl_wcisniety_przycisk);
            if (Nick.byl_wcisniety_przycisk) {
                tStart = System.currentTimeMillis();
                wlaczGrawitacje();
            }
    }

    /**
     * Metoda, ktora ustawia domyslne parametry okna.
     */
    private void ustawOkno() {

        setTitle("Question Mark Block");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400,400));
        pack();
        setVisible(true);
    }

    /**
     * Metoda, która wykonuje się cały czas od momentu uruchomienia gry, symuluje grawitacje.
     */
    private void wlaczGrawitacje(){
        przyspieszenie_grawitacyjne = 50/Odczytywanie.odczytany_poziom_trudnosci; // zmienic dla roznych poziomow trudnosci. 50 dla trudnego, 100 dla sredniego, 200 dla latwego

  //      if (!Kulka.pauza_gry)
            while(grawitacjaWlaczona) {
                try {
                    Thread.sleep(przyspieszenie_grawitacyjne);
                    if(!Kulka.pauza_gry) {
                        Gracz.pozY += 1;
                        tEnd = System.currentTimeMillis();
                        tDelta = tEnd - tStart;
                        elapsedSeconds = tDelta / 1000.0;
                        Info.aktualizujCzas();
                        Info.aktualizujPunkty();

                //        System.out.print(" czas startu " + tStart + ", czas konca " + tEnd + " czas trwania "  + elapsedSeconds);

                    }
                    repaint();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
    }

    /**
     * Metoda, w ktorej ustawiany jest styl okien oraz dodawane sa wszystkie okna oraz ustawiane sa ich parametry.
     * Tutaj tez sprawdzane jest, czy mozna uruchomic okno w przypadku nie znalezienia odpowiednich plikow.
     */
    private void inicjalizacjaUI(){
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(GlowneOkno.this);




        menu_glowne = new Menu();
        lista = new ListaNajlepszychWynikow(online,serwerSocket);
        nick = new Nick();
        klasa_planszy = new KlasaPlanszy(online,serwerSocket,aktualny_poziom);

        obrazekkonca = new ObrazekKoncaPoziomu();

        menu_glowne.setSize(this.getWidth(),this.getHeight());
        lista.setSize(this.getWidth(),this.getHeight());
        nick.setSize(this.getWidth(),this.getHeight());
        klasa_planszy.setSize(this.getWidth(),this.getHeight());
        obrazekkonca.setSize(this.getWidth(),this.getHeight());

        screenHeight = Odczytywanie.odczytana_dlugosc_planszy*Odczytywanie.odczytana_dlugosc_kroku;

        screenWidth = Odczytywanie.odczytana_szerokosc_planszy*Odczytywanie.odczytana_dlugosc_kroku+4*Odczytywanie.odczytana_dlugosc_kroku;

        zbior_paneli = new JPanel();
        zbior_paneli.setLayout(layout_kartkowy);

        if(Odczytywanie.mozna_uruchomic_gre) {



            zbior_paneli.add(menu_glowne, "1");
            zbior_paneli.add(lista, "2");
            zbior_paneli.add(nick, "3");
            zbior_paneli.add(klasa_planszy, "4");
            zbior_paneli.add(obrazekkonca, "5");


            layout_kartkowy.show(zbior_paneli, "1");
            add(zbior_paneli);
        }else{
            this.setVisible(false);
            this.dispose();
        }



    }

    /**
     * Metoda odpowiedzialna za podniesienie poziomu. Jezeli liczba powtorzen osiagnie maksimum, gracz przegrywa i cofa sie do poziomu 1.
     *
     */
    public static void podniesPoziom(){

        aktualny_poziom++;


        if(aktualny_poziom<=maks_liczba_poziomow){

            klasa_planszy= new KlasaPlanszy(online,serwerSocket,aktualny_poziom);
            zbior_paneli.add(klasa_planszy,"4");

            klasa_planszy.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    klasa_planszy.gra.keyPressed(e);
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    klasa_planszy.gra.keyReleased(e);
                    if (e.getKeyCode() == KeyEvent.VK_UP)
                        Gracz.keys[0] = false;
                    if (e.getKeyCode() == KeyEvent.VK_DOWN)
                        Gracz.keys[1] = false;
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                        Gracz.keys[2] = false;
                    if (e.getKeyCode() == KeyEvent.VK_LEFT)
                        Gracz.keys[3] = false;
                }
            });
            klasa_planszy.setFocusable(true);
            klasa_planszy.requestFocusInWindow();}
        else{

            obrazekkonca=new ObrazekKoncaPoziomu();
            zbior_paneli.add(obrazekkonca,"5");
            layout_kartkowy.show(zbior_paneli,"5");
        }
    }

    /**
     * Metoda ustawiajaca preferowany rozmiar okna.
     * @return rozmiar okna
     */
    public Dimension getPreferredSize(){
        return new Dimension(screenWidth, screenHeight);
    }
}
