/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serwer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Michał Janczyk
 * @author Piotr Kierzek
 * 
 */

/**
 * Klasa zawierająca definicje komend rozpoznawanych przez serwer oraz to, co ma po nich nastąpić.
 * 
 */
public class Komendy {
    /**
     * Metoda interpretująca wiadomości przesyłane między klientem a serwerem.
     * @param command Sucha wiadomość przesłana od klienta, potrzebująca interpretacji.
     * @return Zwracana jest wiadomość od klienta.
     */
    public static String akcja(String command){
        String komenda_poprawna = command;
        String komenda_przyslana= command;


//        if(command.contains("POBIERZ_POZIOM:")){
//            komenda_przyslana=command;   
//            komenda_poprawna=("POBIERZ_POZIOM");
//        }
            if(command.contains("WYNIK_GRY:")){
            komenda_przyslana=command;
            komenda_poprawna="WYNIK_GRY";
        }


        String wiadomosc;
        switch (komenda_poprawna){
            case "ZALOGUJ":
                wiadomosc=zaloguj();
                break;
            case "POBIERZ_USTAWIENIA":
                wiadomosc=pobierzUstawienia();
                break;

            case "POBIERZ_WYNIKI":
                wiadomosc=pobierzWyniki();
                break;

//            case "POBIERZ_POZIOM":
//                String str[] = komenda_przyslana.split(":");
//                wiadomosc=pobierzPoziom(Integer.parseInt(str[1]));
//                break;
            case "WYNIK_GRY":

                String str1[] = komenda_przyslana.split(":");
                String str2[] = str1[1].split("@");
                String input[] = str2[1].split(",");
                System.out.println(input[0]);
                wiadomosc=aktualizacjaWynikow(input[0],Integer.parseInt(input[1].trim()),Integer.parseInt(input[2].trim()));
                break;

            default:
                wiadomosc="NIEPRAWIDLOWA_KOMENDA";
        }
        return wiadomosc;
    }

    /**
     * Metoda obsługująca logowanie
     * @return wiadomość od serwera
     */
    private static String zaloguj() {
        String wiadomosc;
        wiadomosc = "ZALOGOWANY";
        return wiadomosc;
        }


    /**
     * Metoda obsługująca pobranie ustawień
     * @return odpowiedź serwera
     */
    private static String pobierzUstawienia(){
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("PlikiKonfiguracyjne\\Ustawienia.txt"))){
            String currentLine;
            while ((br.readLine()) != null) {
                currentLine =br.readLine();
                System.out.println(currentLine);
                sb.append(currentLine+"@");
            }
			br.close();
        }
        catch (Exception e){

        }

        return sb.toString();
    }



    /**
     * Metoda obsługująca pobranie listy wyników
     * @return odpowiedź serwera
     */

    private static String pobierzWyniki(){

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("PlikiKonfiguracyjne\\Wyniki.txt"))){
            String currentLine;

            br.readLine();
            while ((currentLine = br.readLine()) != null) {
                sb.append(currentLine+"@");
            }
			br.close();
        }
        catch (Exception e){

        }

        return sb.toString();
    }


//    /**
//     * Metoda pobierajďż˝ca dane poziomuy
//     * @param nr_poziomu numer poziomu
//     * @return odpowiedďż˝ od serwera
//     */

//    private static String pobierzPoziom(int nr_poziomu){
//        StringBuilder sb = new StringBuilder();
//        try (BufferedReader br = new BufferedReader(new FileReader("PlikiKonfiguracyjne\\"+"Poziom"+nr_poziomu+".txt"))){
//            String currentLine;
//            while ((currentLine=br.readLine()) != null) {
//                if(currentLine.equals("#PLANSZA"))
//                    while(!(currentLine=br.readLine()).equals("#"))
//                    sb.append(currentLine+"@");
//                else {
//                    currentLine=br.readLine();
//                    sb.append(currentLine+"@");
//                }
//            }
//			br.close();
//        }
//        catch (Exception e){
//
//        }
//        return sb.toString();
//    }

    /**
     * Metoda aktualizująca liste najlepszych wyników
     * @param nick nick gracza
     * @param punkty liczba zdobytych punktów
     * @param poziom poziom, na którym gracz skończył rozgrywkę
     * @return odpowiedź od serwera
     */

    private static String aktualizacjaWynikow(String nick, int punkty, int poziom){
        ArrayList<Gracz> lista_najlepszych_wynikow;
        lista_najlepszych_wynikow = new ArrayList<>();
        lista_najlepszych_wynikow=zaladujZPliku(lista_najlepszych_wynikow);
        String odpowiedz="ZAPISANO DO LISTY";
        for(int i=0;i<10;i++){
            if(lista_najlepszych_wynikow.get(i).zwrocPunkty()<punkty){
                odpowiedz="ZAPISANO DO LISTY";
                break;
            }
        }
        

        dodajWynik(lista_najlepszych_wynikow,nick,punkty,poziom);
        return odpowiedz;
    }

    /**
     * Metoda dodająca wynik do listy
     * @param lista_najlepszych_wynikow lista wyników
     * @param nick nick gracza
     * @param punkty liczba punktďów
     * @param poziom poziom, na którym gracz zakończył rozgrywkę
     */

    private static void dodajWynik(ArrayList<Gracz> lista_najlepszych_wynikow,String nick, int punkty, int poziom){

        lista_najlepszych_wynikow.add(new Gracz(nick,punkty,poziom));
        posortujListe(lista_najlepszych_wynikow);
        Collections.reverse(lista_najlepszych_wynikow);
        zapiszWynikiDoPliku(lista_najlepszych_wynikow);
    }

    /**
     * Metoda wczytująca listę wyników z pliku
     * @param lista_najlepszych_wynikow lista wyników
     * @return odpowiedź od serwera
     */

    private static ArrayList<Gracz> zaladujZPliku(ArrayList<Gracz> lista_najlepszych_wynikow) {
            String listaString = pobierzWyniki();
            String str1[] = listaString.split("@");

            for (int i = 0; i < 10; i++) {
                String str2[] = str1[i].split(",");
                lista_najlepszych_wynikow.add(new Gracz(str2[0], Integer.parseInt(str2[1]), Integer.parseInt(str2[2])));
            }

            return lista_najlepszych_wynikow;
        
    }

    /**
     * Metoda zapisujśca wyniki do pliku
     * @param lista_najlepszych_wynikow  lista najlepszych wyników
     */

    private static void zapiszWynikiDoPliku(ArrayList<Gracz> lista_najlepszych_wynikow) {

        try{
            File sciezka = new File("PlikiKonfiguracyjne\\Wyniki.txt");
            BufferedWriter zapis = new BufferedWriter(new FileWriter(sciezka));
            zapis.write("#Najlepsze Wyniki \n");
            for(int i =0; i <10;i++) {
                zapis.write(lista_najlepszych_wynikow.get(i).zwrocNick() + "," + lista_najlepszych_wynikow.get(i).zwrocPunkty() + "," + lista_najlepszych_wynikow.get(i).zwrocPoziom()+"\n");
            }
            zapis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Metoda sortująca liste wyników
     * @param lista_najlepszych_wynikow lista wyników
     */

    private static void posortujListe(ArrayList<Gracz> lista_najlepszych_wynikow) {
        Collections.sort(lista_najlepszych_wynikow, new Comparator<Gracz>() {
            @Override
            public int compare(Gracz g1, Gracz g2)
            {
                // Sortujemy najpierw po poziomie...
                int poPoziomie = Integer.toString(g1.zwrocPoziom()).compareTo(Integer.toString(g2.zwrocPoziom()));
                if (poPoziomie != 0)
                {
                    return poPoziomie;
                }

                // ...następnie po wyniku...
                int poWyniku = Integer.toString(g1.zwrocPunkty()).compareTo(Integer.toString(g2.zwrocPunkty()));
                poWyniku = poWyniku;
                if (poWyniku != 0)
                {
                    return poWyniku;
                }

                // ... a jeśli to i to było takie samo - po nicku.
                return g1.zwrocNick().compareTo(g2.zwrocNick());
        }
        });
    }
}
