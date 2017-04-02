package Gra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import static Gra.ListaNajlepszychWynikow.lista;

/**
 * @author Michał Janczyk
 * @author Piotr Kierzek
 */
/**
 * Klasa sluząca do odczytywania i zapisywania do pliku
 */
public class Odczytywanie {
    /** Parametr przechowujący informacje o ustawionym poziomie trudnosci. 1 - łatwy, 2 - średni, 3 - trudny */
    public static int odczytany_poziom_trudnosci;
    /** Parametr przechowujący ilość żyć gracza */
    public static int odczytana_ilosc_zyc;
    /** Parametr przechowujący czas trwania gry */
    public static int odczytany_czas_trwania_gry;
    /** Parametr przechowujący liczbę zginięć gracza */
    public static int odczytana_liczba_zginiec;
    /** Parametr przechowujący liczbę jaką gracz dostaje za zebranie bonusu, bonus ten odejmuje daną wartość w sekundach od całkowitego wyniku */
    public static int odczytane_pkt_za_bonus;
    /** Parametr przechowujący liczbę punktów, jaką gracz traci po umarciu(zginięciu), punkty te są dodawane do obecnego wyniku */
    public static int odczytane_pkt_za_umarcie;
    /** Parametr przechowujący liczbe punktów przyznawana na początku każdego poziomu graczowi*/
    public static int odczytana_liczba_punktow;
    /** Parametr przechowujący liczbe poziomów dostepnych w grze */
    public static int odczytana_liczba_poziomow;
    /**Odczytana długośc kroku, potrzebna do określania o ile musi się poruszyć postać w czasie jedengo kroku */
    public static int odczytana_dlugosc_kroku;

    // deklarowane zmienne z pliku poziomu, parametry niezmienne

    /** Odczytana liczba elementów w jednej kolumnie, szerokość planszy określona przez elementy */
    public static int odczytana_szerokosc_planszy;
    /** Odczytana liczba elementów w jednym wierszu, długość planszy określona przez elementy */
    public static int odczytana_dlugosc_planszy;
    /** Odczytany identyfikator planszy, każda plansza ma swoją unikalną liczbę, po wpisaniu której gracz może zacząć grę od danego poziomu */
    public static int odczytany_identyfikator_planszy;
    /** Odczytany numer planszy */
    public static int odczytany_numer_planszy; // potrzebne, skoro plansze numerujemy w nazwach plikow???

    /**Zmienna sluzaca do zadecydowania, czy wszystkie pliki zostaly znalezione i mozna poprawnie uruchomic gre */
    public static boolean mozna_uruchomic_gre = true;

    /**
     * Zmienna sluzaca do inkrementacji rzedow planszy podczas jej tworzenia.
     */
    private int j = 0;

    /**
     * Metoda sluzaca do odczytu zasad punktacji z pliku. Nie wolno zmieniac kolejnosci odczytywania.
     * Mozna zmieniac nazwy przed liczbami w pliku konfiguracyjnym, ale nie mozna ich kompletnie usuwac.
     *
     */
    public void odczytajPunkty(){
        try{
            File sciezka1 = new File("PlikiKonfiguracyjne/Ustawienia.txt");
            FileReader plik1 = new FileReader(sciezka1);
            BufferedReader odczyt1 = new BufferedReader(plik1);

            odczyt1.readLine();
            odczytany_poziom_trudnosci = Integer.parseInt(odczyt1.readLine());
            odczyt1.readLine();

            odczytana_ilosc_zyc = Integer.parseInt(odczyt1.readLine());
            odczyt1.readLine();

            odczytany_czas_trwania_gry = Integer.parseInt(odczyt1.readLine());
            odczyt1.readLine();

            odczytana_liczba_zginiec = Integer.parseInt(odczyt1.readLine());
            odczyt1.readLine();

            odczytane_pkt_za_bonus = Integer.parseInt(odczyt1.readLine());
            odczyt1.readLine();

            odczytane_pkt_za_umarcie = Integer.parseInt(odczyt1.readLine());
            odczyt1.readLine();

            odczytana_liczba_punktow = Integer.parseInt(odczyt1.readLine());
            odczyt1.readLine();

            odczytana_liczba_poziomow = Integer.parseInt(odczyt1.readLine());


            odczyt1.close();
        } catch (FileNotFoundException e) {
            oknoBleduPliku("Ustawienia.txt");

        }
        catch(IOException e)
        {
            System.out.println("Plik nie mógł zostać odczytany");
            e.printStackTrace();
        }
    }
    /**
     * Metoda sluzaca do odczytyania listy najlepszych wynikow z pliku.
     * Pierwsza linia w pliku moze miec zmieniona nazwe, ale nie wolno jej usuwac.
     * @param nazwa_pliku nazwa pliku, z ktorego odczytujemy liste najlepszych wynikow
     * @param lista lista, do ktorej zapisywane sa wyniki
     */
    public void odczytajListe(String nazwa_pliku, ArrayList<Gracz> lista){
        try {
            File sciezka2 = new File("PlikiKonfiguracyjne/"+nazwa_pliku);
            FileReader plik2 = new FileReader(sciezka2);
            BufferedReader odczyt2 = new BufferedReader(plik2);
            String wiersz = null;
            odczyt2.readLine();

            for(int i =0 ;i<10;i++) {
                wiersz = odczyt2.readLine();
                tworzListe(wiersz,lista,i);

            }
            odczyt2.close();


        }
        catch (FileNotFoundException e) {
            oknoBleduPliku(nazwa_pliku);

        } catch(IOException e)
        {
            System.out.println("Plik nie mógł zostać odczytany");
            e.printStackTrace();
        }
    }
    /**
     * Metoda sluzaca do odczytu danych potrzebnych do stworzenia nowego poziomu takich jak np. ustawienie elementów
     * Odczytywane sa domyslne wymiary planszy, plansza gry, liczba punktów na start, długość kroku podczas ruchu, itd.
     * Kolejnosc odczytywania parametrow moze byc zmieniana, ale napisy przed liczbami musza byc stale.
     * Na koncu musi byc napis "KONIEC" i nie moze byc to zmieniane.
     * @param nr_poziomu numeru poziomu do odczytania z pliku
     */
    public void odczytajPoziom(int nr_poziomu) {

        try {
            File sciezka3 = new File("PlikiKonfiguracyjne/Poziom" + nr_poziomu + ".txt");
            FileReader plik3 = new FileReader(sciezka3);
            BufferedReader odczyt3 = new BufferedReader(plik3);
            String wiersz;


            while(!((wiersz=odczyt3.readLine()).equals("KONIEC"))){

                if(wiersz.equals("#SZEROKOSC PLANSZY"))         // mozna te ify jednak pozamieniac na switche, ladniej bedzie wygladac, do zrobienia w wolnej chwili
                    odczytana_szerokosc_planszy = Integer.parseInt(odczyt3.readLine());


                else if(wiersz.equals("#WYSOKOSC PLANSZY"))
                    odczytana_dlugosc_planszy = Integer.parseInt(odczyt3.readLine());
                else if(wiersz.equals("#DLUGOSC KROKU"))
                    odczytana_dlugosc_kroku = Integer.parseInt(odczyt3.readLine());

                else if (wiersz.equals("#IDENTYFIKATOR PLANSZY"))
                    odczytany_identyfikator_planszy = Integer.parseInt(odczyt3.readLine());

                else if(wiersz.equals("#NUMER PLANSZY"))
                    odczytany_numer_planszy = Integer.parseInt(odczyt3.readLine());

                else if(wiersz.equals("#PLANSZA")){

                    while(!((wiersz=odczyt3.readLine()).equals("#"))) {

                        tworzTablice(wiersz);

                    }
                }


            }


            odczyt3.close();

        }
        catch (FileNotFoundException e) {
            oknoBleduPliku("Poziom" + nr_poziomu + "*.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda sluzaca do pokazania komunikatu o nie znalezieniu plikow z wynikami lub ustawieniami.
     * @param nazwa nazwa pliku
     */
    public void oknoBleduPliku(String nazwa) {
        JFrame okienko = new JFrame("Nie znaleziono pliku");
        JLabel komunikat;
        mozna_uruchomic_gre=false;

        JButton ok = new JButton("OK");
        okienko.setLayout(new FlowLayout());



        komunikat = new JLabel("Nie znaleziono pliku o nazwie: " + nazwa);

        okienko.setSize(300, 10);
        okienko.add(komunikat);

        okienko.add(ok);

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                okienko.dispose();
            }
        });

        okienko.setResizable(false);
        okienko.pack();
        okienko.setVisible(true);

    }

    /**
     * Metoda sluzaca do tworzenia tablicy dwuwymiarowej z odczytanego wiersza.
     * Najpierw rozdzialamy wiersze z przecinkow, a nastepnie tworzymy rzedy planszy.
     * @param wiersz_danych wiersz danych
     */
    public void tworzTablice(String wiersz_danych){
        String[] wierszePlanszy = wiersz_danych.split(",");
        int i =0;
        for (String w : wierszePlanszy)
        {

            PanelGry.plansza[j][i] = Integer.parseInt(w);

            i++;
        }
        System.out.println();
        j++;
        if(j==10) j=0;
    }
    /**
     * Metoda do zapisu odczytanego wiersza do listy graczy i ustawienia odpowiednich danych graczy
     * @param wiersz_danych wiersz odczytany z plku, w którym sa podane; nick i liczba punktów gracza
     * @param lista lista do ktorej to wpiszemy
     * @param p indeks gracza w liscie
     */
    public void tworzListe(String wiersz_danych,ArrayList<Gracz> lista,int p){
        String[] wierszeListy = wiersz_danych.split(",");


        lista.get(p).ustawNick(wierszeListy[0]);
        lista.get(p).wstawPunktyDoListy(Integer.parseInt(wierszeListy[1]));
        lista.get(p).wstawPoziomKoncowyDoListy(Integer.parseInt(wierszeListy[2]));



    }
    /**
     * Metoda zapisująca liste najlepszych wynikow do pliku.
     * Najpierw musi byc zapisany napis #Najlepsze Wyniki, a nastepnie dopiero zapisywane sa dane gracza.
     * @param nazwa_pliku nazwa pliku, do ktorego zostanie zapisana lista
     * @param lista lista, ktora chcemy zapisac
     */
    public void zapiszListe(String nazwa_pliku,ArrayList<Gracz> lista){
        try{
            File sciezka = new File("PlikiKonfiguracyjne/"+nazwa_pliku);
            BufferedWriter zapis = new BufferedWriter(new FileWriter(sciezka));
            zapis.write("#Najlepsze Wyniki \n");
            for(int i =0; i <10;i++) {
                zapis.write(lista.get(i).zwrocNick() + "," + lista.get(i).zwrocPunkty()+ "," + lista.get(i).zwrocPoziomKoncowy()+"\n");
            }
            zapis.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(e.toString());
            System.out.println("Plik nie mógł zostać odczytany");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}