package Gra;

import javax.swing.*;
import java.awt.*;
/**
 * @author Michał Janczyk
 * @author Piotr Kierzek
 */
/**
 * Klasa przechowujaca informacje o scianach.
 */
public class Sciana extends Pole {

    /**
     * Obrazek reprezentujcy ściane na planszy gry.
     */
    Image scianaIMG = new ImageIcon("ZasobyGraficzne/sciana2.jpg").getImage();
    /**
     * Pozycja X-owa sciany
     */
    int x;
    /**
     * Pozycja Y-owa sciany
     */
    int y;

    /**
     * Metoda paint, służąca do rysowania ścian.
     * @param g kontekst graficzny
     */
    public void paint(Graphics g){
        g.drawImage(scianaIMG,x,y,50,50,null);

    }


}
