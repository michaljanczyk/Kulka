/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gra;

import java.util.Comparator;

/**
 * @author Michal Janczyk
 * @author Piotr Kierzek
 */

/**
 * Klasa służąca do porównywania wyników zdobytych przez graczy aby umożliwić ich sortowanie: najpierw za ostatni poziom,
 * potem za zdobyty wynik, na końcu za nick.
 */
public class Porownywacz implements Comparator<Gracz> {
    @Override
    public int compare(Gracz g1, Gracz g2)
    {
        // Sortujemy najpierw po poziomie...
        int poPoziomie = Integer.toString(g1.poz_konc_do_listy).compareTo(Integer.toString(g2.poz_konc_do_listy));
        if (poPoziomie != 0)
        {
            return poPoziomie;
        }

        // ...następnie po wyniku...
        int poWyniku = Integer.toString(g1.pkt_do_listy).compareTo(Integer.toString(g2.pkt_do_listy));
        poWyniku = 0-poWyniku;
        if (poWyniku != 0)
        {
            return poWyniku;
        }

        // ... a jeśli to i to było takie samo - po nicku.
        return g1.nick_do_listy.compareTo(g2.nick_do_listy);
    }
}
