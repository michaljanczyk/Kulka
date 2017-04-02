package Gra;

import java.awt.*;
/**
 * @author Michał Janczyk
 * @author Piotr Kierzek
 */

/**
 * Abstrakcyjna klasa, po ktorej dziedziczą klasy opisujące zachowania wszystkich pól na planszy.
 */
abstract class Pole extends Rectangle {

    public Pole(){}

    /**
     * Metoda służy do sprawdzania czy dochodzi do dolnej kolizji z którymś z obiektów dziedziczącym o polu.
     * Polega na sprawdzeniu odpowiednich współrzędnych dwóch obiektów.
     * @param pole Obiekt dla którego sprawdzamy kolizje
     * @return Zwraca true albo false w zależności czy doszło do kolizji czy nie .
     */
    public boolean dolnaKolizja(Pole pole){
        if(((this.getY() +this.getHeight()) == pole.getY()) && (this.getX() == pole.getX())) {

            return true;
        }
        else{
            return false;
        }
    }
    /**
     * Metoda służy do sprawdzania czy dochodzi do kolizji z góry.
     * Polega na sprawdzeniu odpowiednich współrzędnych dwóch obiektów.
     * @param pole Obiekt dla którego sprawdzamy kolizje
     * @return Zwraca true albo false w zależności czy doszło do kolizji czy nie .
     */
    public boolean gornaKolizja(Pole pole){
        if((this.getY() == pole.getY() + pole.getHeight()) && (this.getX() == pole.getX())){
            return true;
        }
        else{
            return false;
        }

    }
    /**
     * Metoda służy do sprawdzania czy dochodzi do kolizji z prawej.
     * Polega na sprawdzeniu odpowiednich współrzędnych dwóch obiektów.
     * @param pole Obiekt dla którego sprawdzamy kolizje
     * @return Zwraca true albo false w zależności czy doszło do kolizji czy nie .
     */
    public boolean prawaKolizja(Pole pole){
        if((this.getX() + getWidth() == pole.getX()) && (this.getY() == pole.getY())){
            return true;
        }
        else{
            return false;
        }

    }
    /**
     * Metoda służy do sprawdzania czy dochodzi do kolizji z lewej strony.
     * Polega na sprawdzeniu odpowiednich współrzędnych dwóch obiektów.
     * @param pole Obiekt dla którego sprawdzamy kolizje
     * @return Zwraca true albo false w zależności czy doszło do kolizji czy nie
     */
    public boolean lewaKolizja(Pole pole){
        if((this.getX() == pole.getX() + pole.getWidth()) && (this.getY() == pole.getY())){
            return true;
        }
        else{
            return false;
        }

    }


}
