package Gra;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Michał Janczyk
 * @author Piotr Kierzek
 */

/**
 * Klasa przechowujaca informacje o graczu.
 */
public class Gracz extends Pole{
    /**
     * Zmienna przechowujaca nick gracza.
     */
    static String nick;

    /**
     * Zmienna przechowujaca liczbe punktow gracza w danym poziomie.
     */
    static int liczba_punktow;

    /**
     * Zmienna przechowujaca numer najwyższego dotąd osiągniętego poziomu.
     */
    static int najwyzszy_osiagniety_poziom = 0;

    /**
     * Zmienna przechowujaca sume punktow uzyskana przez gracza ze wszystkich poziomow, ktore przeszedl.
     */
    static int laczna_liczba_punktow;

    /**
     * Zmienna przechowujaca liczbę śmierci gracza.
     */
    static int liczba_zginiec = 0;

    /**
     * Zmienna przechowujaca liczbę zebranych przez gracza bonusów.
     */
    static int liczba_bonusow = 0;

    /**
     * Zmienna przechowujaca liczbę żyć, które gracz aktualnie ma.
     */
    static int liczba_zyc;
    
    /**
     * Zmienna pozwalajaca na wypisanie punktow zdobytych przez graczy w liscie najlepszych wynikow.
     */
    public int pkt_do_listy;

    /**
     * Zmienna użyta do sortowania najlepszych wyników po poziomie zakończenia.
     */
    public int poz_konc_do_listy;

    /**
     * Zmienna pozwalajaca na wypisanie nickow graczy w liscie najlepszych wynikow.
     */
    public String nick_do_listy;

    /**
     * Zmienne pozwalajace na okreslenie, w ktora strone ma poruszac sie gracz.
     */
    private boolean gora,dol,prawo,lewo;

    /**
     * Pozycja pozioma gracza.
     */
    public static int pozX=0;

    /**
     * Pozycja pionowa gracza.
     */
    public static int pozY=0;

    /**
     * Zmienna uzyta do przyspieszenia kulki.
     */
    public static int predkosc = 1;

    /**
     * Maksymalna możliwa prędkość kulki.
     */
    public int predkosc_maks = 10;

    /**
     * Obecne położenie we współrzędnych X kulki.
     */
    static int obecneY;

    /**
     * Obecne położenie we współrzędnych Y kulki.
     */
    static int obecneX;

    /**
     * Zmienne potrzebne do obsługu sterowania przez strzałki.
     */
    static boolean [] keys = new boolean[4];

    /**
     * Konstruktor bezparametrowy tworzący obiekt gracza.
     * */
    public Gracz(){ }

    /**
     * Konstruktor z parametrami tworzacy obiekt gracza. Uzywany do wypisania wynikow w liscie najlepszych wynikow.
     * @param lp liczba punktow
     * @param nicz nick
     * @param pk poziom zakonczenia gry
     */
    public Gracz(int pk, int lp, String nicz){
        pkt_do_listy=lp;
        nick_do_listy=nicz;
        poz_konc_do_listy=pk;
    }
    
    /**
     * Metoda sprawdzająca, czy obecny poziom osiągnięty przez gracza jest najwyższym osiągniętym i ewentualnie aktualizująca
     * tę informację.
     */
    public void aktualizujWynik(){
        if(GlowneOkno.aktualny_poziom > najwyzszy_osiagniety_poziom){
            najwyzszy_osiagniety_poziom = GlowneOkno.aktualny_poziom;
        }
        
        poz_konc_do_listy = najwyzszy_osiagniety_poziom;
        System.out.print(poz_konc_do_listy + "\n");
    }


    /**
     * Metoda do zmiany pozycji gracza w pionie lub w poziomie.
     */
    public void przesun(){
        if(keys[0]){
            if(predkosc<predkosc_maks) {
                pozY -= predkosc+1;
      //          keys[0] = false;
                predkosc++;
            }
            else{
                pozY -= predkosc_maks+1;
     //           keys[0] = false;
            }
            obecneY = PanelGry.poczatkoweY + pozY/Odczytywanie.odczytana_dlugosc_kroku;
        }

        if(keys[1]){
            if(predkosc<predkosc_maks) {
                pozY += predkosc-1;
     //           keys[1] = false;
                predkosc++;
            }
            else{
                pozY += predkosc_maks-1;
    //            keys[1] = false;
            }
            obecneY = PanelGry.poczatkoweY + pozY/Odczytywanie.odczytana_dlugosc_kroku;
        }

        if(keys[2]){
            if(predkosc<predkosc_maks) {
                pozX += predkosc;
     //           keys[2] = false;
                predkosc++;
            }
            else{
                pozX += predkosc_maks;
    //            keys[2] = false;
            }
            obecneX = PanelGry.poczatkoweX + pozX/Odczytywanie.odczytana_dlugosc_kroku;
        }

        if(keys[3]){
            if(predkosc<predkosc_maks) {
                pozX -= predkosc;
   //             keys[3] = false;
                predkosc++;
            }
            else{
                pozX -= predkosc_maks;
  //              keys[3] = false;
            }
            obecneX = PanelGry.poczatkoweX + pozX/Odczytywanie.odczytana_dlugosc_kroku;
        }
    }

    /**
     * Metoda do zdarzenia wcisniecia przycisku pozwalajaca na zdecydowanie, czy gracz ma ruszyc sie w lewo, w prawo, w gore czy w dol.
     * @param e obiekt zdarzenia wcisniecia strzalki
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            keys[0] = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            keys[1] = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            keys[2] = true;
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            keys[3] = true;
    }
    /**
     * Metoda do obsługi zdarzenia puszczenia przycisku pozwalajaca na zdecydowanie, czy gracz ma przestać ruszać się w lewo, w prawo, w gore czy w dol.
     * @param e obiekt zdarzenia wcisniecia strzalki
     */
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            keys[0] = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            keys[1] = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            keys[2] = false;
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            keys[3] = false;

    }


    /**
     * Metoda zamieniająca nick i liczbe punktów na obiekt typu String, używana przy tworzeniu listy najlepszych wyników
     * @return Nick i liczbe punktów w typie string
     */
    public String toString(){
        return "Nick: "+ nick_do_listy + "      " + "Liczba punktów: " + pkt_do_listy + "      " + "Poziom na którym zakończono grę: " + poz_konc_do_listy;
    }
    /**
     * Metoda zwracająca liczbe punktów
     * @return Liczba Punktow Gracza
     */
    public int zwrocPunkty(){return pkt_do_listy;}
    /**
     * Metoda zwracająca nick gracza
     * @return Nick Gracza
     */
    public String zwrocNick(){return nick_do_listy;}
    /**
     * Meteoda zwracająca poziom, na którym gracz zakończył grę
     * @return Poziom Zakończenia Gry
     */
    public int zwrocPoziomKoncowy(){return poz_konc_do_listy;}


    /**
     * Metoda ustawia nick gracza w liscie najlepszych wynikow.
     * @param n nick gracza
     */
    public void ustawNick(String n){nick_do_listy = n;}

    /**
     * Metoda ustawiająca odpowiednie dane gracza w danym poziomie
     * @param lp liczba punktów na start
     */
    public void ustawGracza(int lp){
        liczba_punktow= lp;
    }

    /**
     * Metoda ustawiajaca liczbe punktow gracza w liscie najlepszych wynikow.
     * @param p liczba punktow
     */
    public void wstawPunktyDoListy(int p){
        pkt_do_listy=p;
    }
    
    /**
     * Metoda ustawiajaca poziom końcowy gracza w liscie najlepszych wynikow.
     * @param pk poziom końcowy
     */
    public void wstawPoziomKoncowyDoListy(int pk){
        poz_konc_do_listy=pk;
    }
}
