package Gra;



import javax.swing.*;
import java.awt.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Michał Janczyk
 * @author Piotr Kierzek
 */
/**
 * Klasa rysujaca pole gry.
 */
public class PanelGry extends JPanel implements KeyListener {
    /**
     * Zmienna przechowujaca pola w grze.
     */
    static int[][] plansza=new int[Odczytywanie.odczytana_dlugosc_planszy][Odczytywanie.odczytana_szerokosc_planszy];

    /**
     * Zmienna przechowujaca maksymalna liczbe scian.
     */
    static int MAX_liczba_scian;  //62

    /**
     * Zmienna przechowujaca maksymalna liczbe przeszkód.
     */
    static int MAX_liczba_przeszkod;

    /**
     * Domyslna szerokosc okna gry.
     */
    private static final int DEFAULT_WIDTH = Odczytywanie.odczytana_szerokosc_planszy*Odczytywanie.odczytana_dlugosc_kroku;

    /**
     * Domyslna wysokosc okna gry.
     */
    private static final int DEFAULT_HEIGHT = Odczytywanie.odczytana_dlugosc_planszy*Odczytywanie.odczytana_dlugosc_kroku;


    /**
     * Bufor graficzny.
     */
    private Graphics buffer;

    /**
     * Obrazek buforowany.
     */
    private BufferedImage imgb;


    /**
     * Lista przechowujaca obiekty scian.
     */
    private ArrayList<Sciana> sciany2;

    /**
     * Lista przechowujaca obiekty miejsc na przeszkody.
     */
    private ArrayList<Przeszkoda> przeszkody2;
    
    /**
     * Lista przechowujaca wszystkie pola.
     */
    private ArrayList<Pole> swiat;

    /**
     * Zmienna przechowujaca obiekt Gracza.
     */
    private Gracz gracz;

    /**
     * Zmienna przechowujaca obiekt kończący poziom.
     */
    private Obiekt koniecPoziomu;

    /**
     * Zmienne przechowujace obiekty-bonusy.
     */
    private Bonus bonus1, bonus2, bonus3, bonus4;
    
    /**
     * Zmienna sluzaca do skalowania ruchu gracza w pionie.
     */
    double stepY = 0;
    /**
     * Zmienna sluzaca do skalowania ruchu gracza w poziomie.
     */
    double stepX = 0;

    /**
     * Zmienna sluzaca do skalowania zmiany pozycji w poziomie.
     */
    private double pozycjaX= DEFAULT_WIDTH/Odczytywanie.odczytana_szerokosc_planszy;
    
    /**
     * Zmienna sluzaca do sklaowania zmiany pozycji w pionie.
     */
    private double pozycjaY= DEFAULT_HEIGHT/Odczytywanie.odczytana_dlugosc_planszy;

    /**
     * Zmienna sluzaca do stwierdzenia, czy nastąpiła kolizja za ścianą.
     */
    static boolean trafiono_na_sciane = false;

    /**
     * Zmienna sluzaca do ustalenia początkowej pozycji X gracza.
     */
    static int poczatkoweX;

    /**
     * Zmienna sluzaca do ustalenia początkowej pozycji Y gracza.
     */
    static int poczatkoweY;

    /**
     * Zmienna sluzaca do ustalenia, czy gracz zdobył bonus 1.
     */
    private boolean zdobytoBonus1 = false;
    
    /**
     * Zmienna sluzaca do ustalenia, czy gracz zdobył bonus 2.
     */
    private boolean zdobytoBonus2 = false;
    
    /**
     * Zmienna sluzaca do ustalenia, czy gracz zdobył bonus 3.
     */
    private boolean zdobytoBonus3 = false;
    
    /**
     * Zmienna sluzaca do ustalenia, czy gracz zdobył bonus 4.
     */
    private boolean zdobytoBonus4 = false;

    /**
     * Dwuwymiarowa tablica intów zawierająca informacje o tym, jakiego typu kafelek ma gdzie być.
     */
    int[][] sciana = new int[Odczytywanie.odczytana_dlugosc_planszy][Odczytywanie.odczytana_szerokosc_planszy];


    /**
     * Obrazek sciany.
     */
    Image scianaIMG = new ImageIcon("ZasobyGraficzne/sciana.jpg").getImage();
    /**
     * Obrazek tla.
     */
    Image podlogaIMG = new ImageIcon("ZasobyGraficzne/podloga2.jpg").getImage();

    /**
     * Obrazek gracza.
     */
    Image kulkaIMG = new ImageIcon("ZasobyGraficzne/kulka.jpg").getImage();

    /**
     * Obrazek przeszkody.
     */
    Image przeszkodaIMG = new ImageIcon("ZasobyGraficzne/przeszkoda.gif").getImage();

    /**
     * Obrazek bonusa.
     */
    Image bonusIMG = new ImageIcon("ZasobyGraficzne/bonus.gif").getImage();

    /**
     * Obrazek kafelka końca poziomu.
     */
    Image koniecIMG = new ImageIcon("ZasobyGraficzne/koniec.jpg").getImage();


    /**
     * Metoda mowiaca o zdarzeniach po wcisnieciu odpowiednich klawiszy.
     * @param e obiekt zdarzenia wcisniecia klawisza
     */
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE) { // dodac zatrzymanie czasu
            if (!Kulka.pauza_gry) {
                Kulka.pauza_gry = true;

                repaint();
            } else if (Kulka.pauza_gry) {
                Kulka.pauza_gry = false;

                repaint();
            }
        }

        if (!Kulka.pauza_gry) {

            Runnable r = new AkcjaRunnable(e);
            Thread pressT = new Thread(r);
            pressT.start();
        }

    }

    /**
     * Metoda opisująca stanie się po puszczeniu klawisza.
     * @param e zdarzenie
     */
    public void keyReleased(KeyEvent e) {
        Gracz.predkosc = 1;
    }
    
    /**
     * Metoda opisująca co powinno się stać przy wciśnięciu przycisku (opis potrzebny pry dziedziczeniu po KeyListener).
     * @param e zdarzenie
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Metoda odczytująca liczbę pól na podstawie pliku konfiguracyjnego.
     */
    public void odczytajLiczbePol() {
        for (int i = 0; i < plansza.length; i++) {
            for (int j = 0; j < plansza[i].length; j++) {
                if(plansza[i][j] == 0)
                    MAX_liczba_scian++;
                else if(plansza[i][j] == 4)
                    MAX_liczba_przeszkod++;

            }
        }
    }

    /**
     * Metoda ustawiająca sciany, skrzynki na odpowiednich miejscach na planszy. W niej dochodzi do zmiany pozycji w czasie ruchu.
     * Pierwszym argumentem metody setBounds jest pozioma pozycja pola. Jest to szerokosc okna podzielona przez
     * szerokosc planszy (ilosc kolumn w wierszu) i pomnozona przez numer wiersza tablicy planszy, co daje nam odleglosc miedzy kolejnymi polami.
     * <p>
     * Drugim argumentem analogiczne jest pozycja pionowa.
     * <p>
     * Trzecim argumentem jest szerokosc jednego pola.
     * Jest to szerokość okna podzielona przez odczytana szerokosc planszy (ilosc kolumn w wierszu planszy).
     * <p>
     * Czwartym argumentem jest analogicznie wysokość jednego pola.
     */
    public void ustawPozycjePol() {

        int s = 0;
        int p = 0;

        for (int i = 0; i < plansza.length; i++) {
            for (int j = 0; j < plansza[i].length; j++) {
                sciana[i][j] = 0;
                switch (plansza[i][j]) {
                    case 0: //sciana

                        /**
                         * Ustawianie pozycji oraz wymiarów każdej ściany.
                         */
                        sciany2.get(s).setBounds(((DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy) * j),
                                (((DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy) * i)), Odczytywanie.odczytana_dlugosc_kroku,
                                Odczytywanie.odczytana_dlugosc_kroku);
                        sciana [i][j] = 1;
                        s++;


                        if (s == MAX_liczba_scian) s = 0;
                        break;

                    case 2: //gracz

                        stepY = gracz.pozY * (pozycjaY / (double) Odczytywanie.odczytana_dlugosc_kroku);
                        stepX = gracz.pozX * (pozycjaX / (double) Odczytywanie.odczytana_dlugosc_kroku);

                        poczatkoweX = j;
                        poczatkoweY = i;


                        gracz.setBounds((int) ((DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy) * j + stepX),
                                (int) ((DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy) * i + stepY),
                                DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy, DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy);
                        break;

                    case 3: //bonus1

                        /**
                         * Ustawianie pozycji oraz wymiarów bonusa.
                         */
                        bonus1.setBounds(((DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy) * j),
                                (((DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy) * i)), Odczytywanie.odczytana_dlugosc_kroku,
                                Odczytywanie.odczytana_dlugosc_kroku);
                        break;

                    case 4: //przeszkoda

                        /**
                         * Ustawianie pozycji oraz wymiarów każdej przeszkody.
                         */
                        przeszkody2.get(p).setBounds(((DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy) * j),
                                (((DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy) * i)), Odczytywanie.odczytana_dlugosc_kroku,
                                Odczytywanie.odczytana_dlugosc_kroku);
                        p++;

                        if (p == MAX_liczba_przeszkod) p = 0;
                        break;
                    case 5: //bonus2

                        /**
                         * Ustawianie pozycji oraz wymiarów bonusa.
                         */
                        bonus2.setBounds(((DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy) * j),
                                (((DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy) * i)), Odczytywanie.odczytana_dlugosc_kroku,
                                Odczytywanie.odczytana_dlugosc_kroku);
                        break;
                    case 6: //bonus3

                        /**
                         * Ustawianie pozycji oraz wymiarów bonusa.
                         */
                        bonus3.setBounds(((DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy) * j),
                                (((DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy) * i)), Odczytywanie.odczytana_dlugosc_kroku,
                                Odczytywanie.odczytana_dlugosc_kroku);
                        break;
                    case 7: //bonus4

                        /**
                         * Ustawianie pozycji oraz wymiarów bonusa.
                         */
                        bonus4.setBounds(((DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy) * j),
                                (((DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy) * i)), Odczytywanie.odczytana_dlugosc_kroku,
                                Odczytywanie.odczytana_dlugosc_kroku);
                        break;

                    case 9: //koniec planszy

                        /**
                         * Ustawianie pozycji oraz wymiarów konca planszy.
                         */
                        koniecPoziomu.setBounds(((DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy) * j),
                                (((DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy) * i)), Odczytywanie.odczytana_dlugosc_kroku,
                                Odczytywanie.odczytana_dlugosc_kroku);
                        break;
                }
            }
        }
    }


    /**
     * Konstruktor bezparametrowy panelu gry.
     * Tworzy liste elementów, dodaje je do jednej wspólnej listy "swiat".
     * W tym miejscu dodajemy słuchaczy wydarzeń przycisków.
     */
    public PanelGry() {

        sciany2 = new ArrayList<Sciana>();
      //  bonus1 = new ArrayList<Bonus>();
        bonus1 = new Bonus();
        bonus2 = new Bonus();
        bonus3 = new Bonus();
        bonus4 = new Bonus();
        przeszkody2 = new ArrayList<Przeszkoda>();
        gracz = new Gracz();
        koniecPoziomu = new Obiekt();
        swiat = new ArrayList<Pole>();


        odczytajLiczbePol();
        for (int i = 0; i < MAX_liczba_scian; i++)
            sciany2.add(new Sciana());
        for (int i = 0; i < MAX_liczba_przeszkod; i++)
            przeszkody2.add(new Przeszkoda());


        swiat.addAll(sciany2);
        swiat.addAll(przeszkody2);

        swiat.add(bonus1);
        swiat.add(bonus2);
        swiat.add(bonus3);
        swiat.add(bonus4);
        swiat.add(koniecPoziomu);
        swiat.add(gracz);

        setVisible(true);

    }

    /**
     * Przeciążona metoda paintComponent, tworzy plansze.
     * Najpierw obliczana jest tutaj nowa pozycja wszystkich elementów.
     * Jezeli wlaczono pauze, to rysowany jest napis Pauza
     * Następnie są one rysowane.
     * @param g kontekst graficzny
     *
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        GlowneOkno.klasa_planszy.setFocusable(true);
        GlowneOkno.klasa_planszy.requestFocusInWindow();


        if (!Kulka.KONIEC_GRY) {
            ustawPozycjePol();
            tworzplansze(buffer);
            g.drawImage(this.imgb, 0, 0, this.getWidth(), this.getHeight(), null);
            this.buffer.clearRect(0, 0, this.getWidth(), this.getHeight());
        }

        if(Kulka.pauza_gry){
            g.setFont(new Font("Serif",Font.PLAIN,50));
            g.setColor(Color.WHITE);
            g.drawString("PAUZA",this.getWidth()/3,this.getHeight()/2);

        }

    }


    /**
     * Metoda tworzaca bufor do rysowania.
     */
    public void init() {
        this.imgb = new BufferedImage(DEFAULT_WIDTH,DEFAULT_HEIGHT,2);
        this.buffer = this.imgb.getGraphics();
    }


    /**
     * Metoda do wywolania metody tworzacej bufor na samym poczatku.
     */
    public void addNotify() {
        super.addNotify();

        this.init();
    }

    /**
     * Metoda rysuje w odpowiednich miejscach odpowiednie obrazki symbolizujące różne obiekty.
     * Rysowanie odbywa się poprzez odczytywanie pozycji oraz rozmiarów odpowiednich obiektów.
     *
     * @param g kontekst graficzny
     */
    public void tworzplansze(Graphics g){


        g.drawImage(podlogaIMG, 0, 0, DEFAULT_WIDTH/Odczytywanie.odczytana_szerokosc_planszy, DEFAULT_HEIGHT/Odczytywanie.odczytana_dlugosc_planszy, this);

        for (int i =0;i<Odczytywanie.odczytana_dlugosc_planszy;i++){
            for (int j =0; j<Odczytywanie.odczytana_szerokosc_planszy;j++) {
                g.copyArea(0, 0, DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy, DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy,
                        (DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy)*j, (DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy)*i);
            }
        }



        for(int k =0; k <swiat.size();k++){
            if(swiat.get(k) instanceof Sciana){
                g.drawImage(scianaIMG,(int)swiat.get(k).getX(),(int)swiat.get(k).getY(),(int) swiat.get(k).getWidth(),
                        (int) swiat.get(k).getHeight(),this);

            }
            else if(swiat.get(k) instanceof Bonus){
                g.drawImage(bonusIMG,(int)swiat.get(k).getX(),(int)swiat.get(k).getY(),(int) swiat.get(k).getWidth(),
                        (int) swiat.get(k).getHeight(),this);

            }
            else if(swiat.get(k) instanceof Przeszkoda){
                g.drawImage(przeszkodaIMG,(int)swiat.get(k).getX(),(int)swiat.get(k).getY(),(int) swiat.get(k).getWidth(),
                        (int) swiat.get(k).getHeight(),this);

            }
            else if(swiat.get(k) instanceof Gracz){

                g.drawImage(kulkaIMG,(int)gracz.getX(),(int)gracz.getY(),(int) gracz.getWidth(),
                        (int) gracz.getHeight(),this);

            }
            else if(swiat.get(k) instanceof Obiekt) {

                g.drawImage(koniecIMG, (int) koniecPoziomu.getX(), (int) koniecPoziomu.getY(), (int) koniecPoziomu.getWidth(),
                        (int) koniecPoziomu.getHeight(), this);
            }

        }
    }


    /**
     * Klasa z definicją wątku odpowiedzialnego za ruch.
     */
    class AkcjaRunnable implements Runnable {

        /**
         * Zmienna przechowujaca wcisniety klawisz.
         */
        KeyEvent klawis;

        /**
         * Konstruktor z parametrami.
         *
         * @param klawi wciśnięty przycisk
         */
        public AkcjaRunnable(KeyEvent klawi) {

            klawis = klawi;

        }

        /**
         * Przedefiniowana metoda run, która wywołuje ruch.
         */
        public void run() {
            akcja(klawis);

        }
    }

    /**
     * Metoda sluzaca do uspienia watku.
     * @param ms ilosc miliseknd
     */
    public void sleeep(int ms){
        try{
            Thread.sleep(ms);
        }catch(InterruptedException e){

        }
    }

    /**
     * Metoda inicjujaca czynnosci potrzebne do zakonczenia poziomu lub calej gry.
     */
    public void KoniecGry(){
        Kulka.KONIEC_GRY = true;
            ObrazekKoncaPoziomu.panel_z_wynikami.setText("Twój wynik:" + Gracz.laczna_liczba_punktow);
            GlowneOkno.layout_kartkowy.show(GlowneOkno.zbior_paneli, "5");
            GlowneOkno.podniesPoziom();
//        }
    }

    public void resetujGracza(){
        Gracz.pozX = 0;
        Gracz.pozY = 0;

        Info.aktualizujZginiecia();

        trafiono_na_sciane = false;
    }


    /**
     * Główna metoda odpowiedzialna za ruch w grze. Najpierw sprawdzany jest wciśnięty przycisk, potem zaczynamy wykonywać przesuwanie gracza.
     *
     * Plan do zaliczenia projektu:
     * Po każdym przesunięciu sprawdzana jest kolizja z otoczeniem (sciany, bonusy, przeszkody).
     * Jeżeli dojdzie do kolizji ze ściana, to gracz nie rusza się.
     * Jeżeli dojdzie do kolizji z bonusem, zostaje odjęty czas. Jeśli z przeszkodą zabijającą, czas zostanie dodany.
     * Jeżeli dojdzie do kolizji z końcem poziomu, zmienia się poziom na następny
     *
     * @param e wciśnięty przycik, który decyduje o odpowiednim ruchu
     */
    public synchronized void akcja(KeyEvent e) {
        int kod_klucza= e.getKeyCode();
//        for (int j = 0; j < swiat.size(); j++)  //sprawdzam wszystkie pola
//            if (gracz.dolnaKolizja(swiat.get(j)) || gracz.gornaKolizja(swiat.get(j)) || gracz.prawaKolizja(swiat.get(j)) || gracz.lewaKolizja(swiat.get(j)))   //kolizja gracza z jakimkolwiek obiektem po lewej stronie
//                if (swiat.get(j) instanceof Sciana)   //kolizja gracza ze sciana
//                    trafiono_na_sciane = true;
        Gracz.obecneX = poczatkoweX;
        Gracz.obecneY = poczatkoweY;
        int s=0;

        if (kod_klucza == KeyEvent.VK_DOWN || kod_klucza == KeyEvent.VK_UP || kod_klucza == KeyEvent.VK_LEFT || kod_klucza == KeyEvent.VK_RIGHT )
        {
            try {
                gracz.keyPressed(e);
                gracz.przesun();
                gracz.aktualizujWynik();

                while(s != MAX_liczba_scian) {
                    if (sciany2.get(s).getBounds().intersects(gracz))
                        trafiono_na_sciane = true;
                    s++;
                }
                s=0;

                if (bonus1.getBounds().intersects(gracz) && !zdobytoBonus1) {
                    Info.aktualizujBonusy();
                    swiat.remove(bonus1);
                    zdobytoBonus1 = true;
                }
                if (bonus2.getBounds().intersects(gracz) && !zdobytoBonus2) {
                    Info.aktualizujBonusy();
                    swiat.remove(bonus2);
                    Gracz.liczba_zyc++;
                    zdobytoBonus2 = true;
                }
                if (bonus3.getBounds().intersects(gracz) && !zdobytoBonus3) {
                    Info.aktualizujBonusy();
                    swiat.remove(bonus3);
                    zdobytoBonus3 = true;
                }
                if (bonus4.getBounds().intersects(gracz) && !zdobytoBonus4) {
                    Info.aktualizujBonusy();
                    swiat.remove(bonus4);
                    zdobytoBonus4 = true;
                }

                s=0;

                while(s != MAX_liczba_przeszkod) { // tylko giniecie od przeszkod, bo od wszystkich przeszkod sie ginie
                    if (przeszkody2.get(s).getBounds().intersects(gracz))
                        trafiono_na_sciane = true;
                    s++;
                }

                if (trafiono_na_sciane)
                    resetujGracza();

                if (koniecPoziomu.getBounds().intersects(gracz))
                    if(GlowneOkno.aktualny_poziom <= GlowneOkno.maks_liczba_poziomow){
                       GlowneOkno.podniesPoziom();
                       GlowneOkno.layout_kartkowy.show(GlowneOkno.zbior_paneli, "4");
                        resetujGracza();
                        zdobytoBonus1 = false;
                        zdobytoBonus2 = false;
                        zdobytoBonus3 = false;
                        zdobytoBonus4 = false;
                        Gracz.predkosc = 0;
                    }else {
                       ObrazekKoncaPoziomu.panel_z_wynikami.setText("Twój wynik:" + Gracz.laczna_liczba_punktow);
                       GlowneOkno.layout_kartkowy.show(GlowneOkno.zbior_paneli, "5");
                       GlowneOkno.podniesPoziom();
                    }

                if(Gracz.liczba_zyc == 0)
                    KoniecGry();
                System.out.print(trafiono_na_sciane+ " y - " + Gracz.obecneY + " x - " + Gracz.obecneX + "     " );
                repaint();
                Thread.sleep(1);
            }
         catch (InterruptedException ex)
         {
            ex.printStackTrace();
         }
        }
        sleeep(5);

    }
}







