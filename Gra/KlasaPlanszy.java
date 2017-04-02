package Gra;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;
/**
 * @author Michał Janczyk
 * @author Piotr Kierzek
 */
/**
 * Klasa sluzaca jako ramka, na ktorej rysujemy plansze gry.
 */
public class KlasaPlanszy extends JPanel {
    /**
     * Zmienna przechowujaca obiekt klasy Kulka.
     */
    private Kulka kulka;

    /**
     * Zmienna przechowujaca okno z informacjami o przebiegu gry.
     */
    private Info info;


    /**
     * Zmienna przechowujaca obiekt klasy rysujacej okno z gra.
     */
    static public PanelGry gra;

    /**
     * Konstuktor z parametrami klasy KlasaPlanszy.  Tutaj tworzymy obiekty z oknem gry i oknem przebiegu gry.
     * Tutaj tworzymy obiekt klasy Kulka odczytujacy wszystkie informacje z plikow.
     * @param on parametr mowiacy o udanym polaczeniu
     * @param serwer_Socket gniazdo serwera
     * @param nr_poziomu numer poziomu do wlaczenia
     */

    public KlasaPlanszy(boolean on, Socket serwer_Socket,int nr_poziomu) {


        kulka = new Kulka(on,serwer_Socket,nr_poziomu);
        setLayout(new BorderLayout());

        gra = new PanelGry();
        add(BorderLayout.CENTER, gra);

        info = new Info();

        info.dodajInfo(GlowneOkno.aktualny_poziom, Gracz.liczba_punktow, Gracz.liczba_bonusow,
                Odczytywanie.odczytana_ilosc_zyc,Odczytywanie.odczytany_czas_trwania_gry, Odczytywanie.odczytana_liczba_zginiec);

        add(BorderLayout.EAST, info);



    }
//    public static void powtorzPlansze(){
//        gra = new PanelGry();
//        add(BorderLayout.CENTER, gra);
//    }

}


/**
 * Klasa, ktorej uzywamy jako panelu z informacjami o przebiegu podczas gry.
 */
class Info extends JPanel{
    /**
     *  Ramka wyswietlajaca aktualny poziom.
     */
    static JLabel aktualnyPoziom;

    /**
     * Ramka wyswietlajaca
     */
    static JLabel liczbaPunktow;

    /**
     * Ramka wyswietlajaca
     */
    static JLabel zdobyteBonusy;

    /**
     * Ramka wyswietlajaca
     */
    static JLabel iloscZyc;

    /**
     * Ramka wyswietlajaca
     */
    static JLabel czasTrwania;

    /**
     * Ramka wyswietlajaca
     */
    static JLabel liczbaZginiec;

    /**
     * Ramka wyswietlajaca informacje o przycisku uzywanym do pauzy.
     */
    JLabel pauza = new JLabel("Pauza - Spacja");

    /**
     * Zmienna przechowujaca obrazek tla okna z informacjami.
     */

    Image img = new ImageIcon("ZasobyGraficzne/panelpodloga.png").getImage();

    /**
     * Konstruktor bezparametrowy z informacjami o grze. Znajduje sie po prawej stronie planszy.
     * Dodawane sa tutaj wszystkie ramki.
     */
    public Info(){
        setSize(2*Odczytywanie.odczytana_dlugosc_kroku,this.getHeight());
        aktualnyPoziom = new JLabel();
        liczbaPunktow = new JLabel();
        zdobyteBonusy = new JLabel();
        iloscZyc = new JLabel();
        czasTrwania = new JLabel();
        liczbaZginiec = new JLabel();

        add(aktualnyPoziom);
        add(liczbaPunktow);
        add(zdobyteBonusy);
        add(iloscZyc);
        add(czasTrwania);
        add(liczbaZginiec);
        add(pauza);
    }

    /**
     * Metoda ustawiajaca parametry ramek oraz dodajaca domyslne dane na poczatku gry.
     * @param aktpoz aktualny poziom
     * @param lp liczba punktow
     * @param zb liczba zdobytych bonusow
     * @param iz ilosc zyc
     * @param ct czas trwania
     * @param lz liczba zginiec
     */
    public void dodajInfo(int aktpoz,int lp,int zb,int iz,int ct,int lz){
        aktualnyPoziom.setFont(new Font("sanserif", Font.PLAIN, 12));
        liczbaPunktow.setFont(new Font("sanserif", Font.PLAIN, 12));
        zdobyteBonusy.setFont(new Font("sanserif", Font.PLAIN, 12));
        iloscZyc.setFont(new Font("sanserif", Font.PLAIN, 12));
        czasTrwania.setFont(new Font("sanserif", Font.PLAIN, 12));
        liczbaZginiec.setFont(new Font("sanserif", Font.PLAIN, 12));
        pauza.setFont(new Font("sanserif", Font.PLAIN, 12));

        aktualnyPoziom.setText("Poziom: "+aktpoz);
        liczbaPunktow.setText("Liczba punktow: "+ lp);
        zdobyteBonusy.setText("Zdobytych bonusow: "+ zb);
        iloscZyc.setText("Ilosc zyc: "+ iz);
        czasTrwania.setText("Czas trwania gry: "+ ct);
        liczbaZginiec.setText("Liczba zginiec: "+ lz);

        aktualnyPoziom.setForeground(Color.WHITE);
        liczbaPunktow.setForeground(Color.WHITE);
        zdobyteBonusy.setForeground(Color.WHITE);
        czasTrwania.setForeground(Color.WHITE);
        iloscZyc.setForeground(Color.WHITE);
        liczbaZginiec.setForeground(Color.WHITE);
        pauza.setForeground(Color.WHITE);
    }

    /**
     * Metoda slużąca do aktualizacji w ramce info dot czasu, który upłynął w trakcie gry.
     */
    public static void aktualizujCzas(){
        czasTrwania.setText("Czas trwania gry: "+ GlowneOkno.elapsedSeconds);
    }
    
    /**
     * Metoda slużąca do aktualizacji w ramce info dot ilości punktów zdobytych przez gracza.
     */
    public static void aktualizujPunkty(){
        double czasTrwania = GlowneOkno.elapsedSeconds;
        int czas = (int)czasTrwania;
        int punkty;
        punkty = Odczytywanie.odczytana_liczba_punktow + czas + Odczytywanie.odczytane_pkt_za_umarcie * Gracz.liczba_zginiec - Odczytywanie.odczytane_pkt_za_bonus * Gracz.liczba_bonusow;
        liczbaPunktow.setText("Liczba punktow: " + punkty );
        Gracz.laczna_liczba_punktow = punkty;
    }
    
    /**
     * Metoda aktualizujaca informacje w ramce na temat liczby śmierci.
     */
    public static void aktualizujZginiecia(){
        ++Gracz.liczba_zginiec;
        --Gracz.liczba_zyc;
        int zginiecia = Odczytywanie.odczytana_liczba_zginiec + Gracz.liczba_zginiec;
        liczbaZginiec.setText("Liczba zginiec: "+ zginiecia);
        iloscZyc.setText("Ilosc zyc: "+ Gracz.liczba_zyc);
    }

    /**
     * Metoda aktualizujaca informacje w ramce na temat liczby zdobytych bonusow.
     */
    public static void aktualizujBonusy(){
        ++Gracz.liczba_bonusow;
        int bonusy = Gracz.liczba_bonusow;
        zdobyteBonusy.setText("Zdobytych bonusow: "+ bonusy);
    }

    /**
     * Metoda rysujaca obrazek tla w oknie z informacjami.
     * @param g kontekst graficzny
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.drawImage(img,0,0,3*Odczytywanie.odczytana_dlugosc_kroku,this.getHeight(),this);
    }

    /**
     * Metoda, ktora ustawia preferowany rozmiar panelu
     * @return Rozmiar panelu dostosowany do zmieniajacej sie wysokosci i okreslonej szerokosci.
     */
    public Dimension getPreferredSize(){
        return new Dimension(3*Odczytywanie.odczytana_dlugosc_kroku,this.getHeight());
    }
}
