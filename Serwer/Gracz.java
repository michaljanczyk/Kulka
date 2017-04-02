/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serwer;

/**
 * @author Michał Janczyk
 * @author Piotr Kierzek
 */

/**
 * 
 * Klasa zawierająca informacje o graczu, używana przy modyfikacji listy najlepszych wyników.
 */
public class Gracz {
    /**
     * Nick gracza
     */
    private String nick;
    /**
     * Liczba punktów
     */
    private int punkty;
    /**
     * Ostatni poziom
     */
    private int poziom;

    /**
     * Konstruktor klasy
     * @param nick nick
     * @param punkty liczba punktów
     * @param poziom ostatni poziom
     */
    public Gracz(String nick, int punkty, int poziom){
        this.nick=nick;
        this.punkty=punkty;
        this.poziom=poziom;
    }

    /**
     * Metoda zwracająca nick gracza.
     * @return nick gracza
     */

    public String zwrocNick() {
        return nick;
    }

    /**
     * Metoda zwracająca l punktów gracza.
     * @return punkty gracza
     */

    public int zwrocPunkty() {
        return punkty;
    }
    
    /**
     * Metoda zwracająca ostatni poziom gracza.
     * @return ostatni poziom gracza
     */

    public int zwrocPoziom() {
        return poziom;
    }
}
