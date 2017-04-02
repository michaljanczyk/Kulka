package Gra;




import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

/**
 * @author Michał Janczyk
 * @author Piotr Kierzek
 */

/**
 * Klasa odpowiedzialna za inicjacje planszy oraz poziomów.
 */
public class Kulka{
    /***
     * Zmienna przechowująca liczbe pozimów.
     */
    static int liczba_poziomow;

    /**
     * Zmienna przechowująca wartość bonusów.
     */
    static int pkt_bonus;
    
    /**
     * Zmienna przechowująca ilość dodawanych punktów za śmierć.
     */
    static int pkt_smierc;

    /***
     * Zmienna przechowująca numer poziomu.
     */
    static int numer_poziomu=1;
    
    /***
     * Obiekt klasy odczytywanie. Zniego uzyskamy parametry potrzebne do działania gry, które sa zodczytane z plików konfiguracyjnych.
     */
    static Odczytywanie odcz;
    /***
     * Obiekt klasy gracz.
     */
    static Gracz gracz;
    /***
     * Zmienna określająca czy pauza gry jest włączona, na początku, ustawiona na false.
     */
    static boolean pauza_gry=false;

    /**
     * Inkrementator.
     */
    int j=0;

    /***
     * Gniazdo sewera, wykorzystane jesli program działa z trybie online.
     */
    private static Socket serwer_socket;
    
    /***
     *Zmienna służąca do określenia konca gry, Jeśli jest true to nastepił koniec, jeśli false, to gra dalej trwa.
     */
    static boolean KONIEC_GRY = false;

    /***
     * Konstruktor z parametrem. Działa różnie w zależności od tego czy jest online czy nie.
     * Ustawia dane i gracza odpowiednio. przygotowuje gre.
     * @param on Określa czy pobieramy potrzebne informacje z serwera, czy nie jesteśmy połączeni i musimy pobierać je z plików.
     * @param socket gniazdo serwera
     * @param nr_poziomu określa, dane z ktorego pliku mją zosatć oddczytane. Który poziom odczytujemy.
     */
    public Kulka(boolean on, Socket socket, int nr_poziomu) {
        serwer_socket=socket;

        JPanel panel_z_gra = new JPanel();
        panel_z_gra.setLayout(new BorderLayout());

        odcz = new Odczytywanie();

        if(on){
            odcz.odczytajPoziom(nr_poziomu);
            odcz.odczytajPunkty();
            //pobierzPoziomzSerwera(serwer_socket);
            //pobierzUstawieniaZSerwera(serwer_socket);
        }else{
            odcz.odczytajPoziom(nr_poziomu);
            odcz.odczytajPunkty();
        }



        ustawDane(odcz.odczytana_liczba_poziomow, odcz.odczytane_pkt_za_bonus, odcz.odczytane_pkt_za_umarcie);

        GlowneOkno.maks_liczba_poziomow=odcz.odczytana_liczba_poziomow;

        gracz = new Gracz();
        gracz.ustawGracza(odcz.odczytana_liczba_punktow);
        gracz.ustawNick(Gracz.nick);

    }

    /***
     * Metoda służąca do wysłania odpowiedniego żądania do serwera i odpowiedniemu przetworzeniu otrzymanej odpowiedzi.
     * Poszczególne dane sa oddzielone @, ważna jest ich kolejność. Z odczytanych danych tworzona jest plansza gry.
     * @param socket gniazdo sewera,
     */
    private void pobierzPoziomzSerwera(Socket socket){
        try {

            System.out.println(socket);

            OutputStream os = socket.getOutputStream();

            PrintWriter pw = new PrintWriter(os, true);

            pw.println("POBIERZ_POZIOM:"+numer_poziomu);

            InputStream is = socket.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String komenda = br.readLine();



            String str[] = komenda.split("@");

            Odczytywanie.odczytana_szerokosc_planszy=Integer.parseInt(str[0]);
            Odczytywanie.odczytana_dlugosc_planszy=Integer.parseInt(str[1]);
            Odczytywanie.odczytana_dlugosc_kroku=Integer.parseInt(str[2]);
            Odczytywanie.odczytany_identyfikator_planszy=Integer.parseInt(str[3]);
            Odczytywanie.odczytany_numer_planszy=Integer.parseInt(str[4]);


            for(int i=0;i<10;i++){
                String[] wierszePlanszy = str[i+7].split(",");

                int k =0;


                for (String w : wierszePlanszy)
                {

                    PanelGry.plansza[j][k] = Integer.parseInt(w);

                    k++;
                }
                System.out.println();
                j++;
                if(j==10) j=0;
            }








        } catch (IOException ex) {
            System.out.println("Błąd typu I/O przy pobieraniu listy z serwera");
        }


    }

    /***
     * Metoda służąca do pobierania danych, ustawien z serwera.
     * Wysyła ona odpowiednie żądanie"POBIERZ_USTAWIENIA" i odpowednio je odczytuje i przyoisuje wartość do zmiennych.
     * @param socket gniazdo, służące do połączenia się z serwerem
     */
    public void pobierzUstawieniaZSerwera(Socket socket){
        try{ System.out.println(socket);

            OutputStream os = socket.getOutputStream();

            PrintWriter pw = new PrintWriter(os, true);

            pw.println("POBIERZ_USTAWIENIA");

            InputStream is = socket.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String komenda = br.readLine();

            String str[] = komenda.split("@");


            Odczytywanie.odczytany_poziom_trudnosci=Integer.parseInt(str[0]);
            Odczytywanie.odczytana_ilosc_zyc=Integer.parseInt(str[1]);
            Odczytywanie.odczytany_czas_trwania_gry=Integer.parseInt(str[2]);
            Odczytywanie.odczytana_liczba_zginiec =Integer.parseInt(str[3]);
            Odczytywanie.odczytane_pkt_za_bonus=Integer.parseInt(str[4]);
            Odczytywanie.odczytane_pkt_za_umarcie=Integer.parseInt(str[5]);
            Odczytywanie.odczytana_liczba_punktow=Integer.parseInt(str[6]);
            Odczytywanie.odczytana_liczba_poziomow=Integer.parseInt(str[7]);

        } catch (IOException ex) {
            System.out.println("Błąd typu I/O przy pobieraniu listy z serwera");
        }
    }

    /**
     * Metoda służąca do ustawienia danych ogólnych np. punktacja lub liczba poziomów.
     * @param lp  liczba poziomów
     * @param bon  liczba punktow odejmowanych od ogolnego wyniku za bonusy
     * @param death liczba punktow dodawanych za umarcie
     *
     */
    public void ustawDane(int lp, int bon, int death) {
        liczba_poziomow = lp;
        pkt_bonus = bon;
        pkt_smierc = death;

    }


}


/**
 * Klasa wewnetrzna opisujaca wyglad panelow konca gry i konca poziomu.
 */
class ObrazekKoncaPoziomu extends JPanel {

    /**
     * Zmienna przechowujaca obrazek z wygrana poziomu.
     */
    Image wygranaIMG = new ImageIcon("ZasobyGraficzne/wygrana.png").getImage();

    /**
     * Zmienna przechowujaca obrazek z koncem gry.
     */
    Image koniecIMG = new ImageIcon("ZasobyGraficzne/zwyciestwo.png").getImage();

    /**
     * Zmienna przechowujaca panel z wynikami gry.
     */
    static JLabel panel_z_wynikami = new JLabel();
    /**
     * Przycisk inicjujacy nastepny poziom.
     */
    JButton nastepny_poziom = new JButton("Gram dalej!");

    /**
     * Konstruktor opisujacy wyglad okna konca gry i konca poziomu.
     */
    public ObrazekKoncaPoziomu() {
        GridBagConstraints gbc;
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        if(GlowneOkno.aktualny_poziom==GlowneOkno.maks_liczba_poziomow)
        {
            gbc.insets = new Insets(3 * Odczytywanie.odczytana_dlugosc_kroku, 0, 0, 0);
        }else {

            gbc.insets = new Insets(4 * Odczytywanie.odczytana_dlugosc_kroku, 0, 0, 0);
        }

            Font customFont = new Font("Comic Sans", Font.PLAIN, 20);
            panel_z_wynikami.setForeground(Color.WHITE);
            panel_z_wynikami.setFont(customFont);
        add(panel_z_wynikami, gbc);
        gbc.gridy = 1;

        gbc.insets = new Insets(Odczytywanie.odczytana_dlugosc_kroku/2, 0, 0, 0);
            nastepny_poziom.setText("Dzieki za grę!");
            gbc.insets = new Insets(Odczytywanie.odczytana_dlugosc_kroku/3, 0, 0, 0);
            panel_z_wynikami.setText("Twój wynik:" + Gracz.laczna_liczba_punktow);




        nastepny_poziom.setFont(new Font("Comic Sans", Font.PLAIN, 20));
        nastepny_poziom.setBackground(Color.WHITE);
        nastepny_poziom.setPreferredSize(new Dimension(3 * Odczytywanie.odczytana_dlugosc_kroku, Odczytywanie.odczytana_dlugosc_kroku));



        nastepny_poziom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    ListaNajlepszychWynikow.dodajWynikiSortuj(Gracz.najwyzszy_osiagniety_poziom,Gracz.laczna_liczba_punktow, Gracz.nick);
                    System.exit(0);
            }
        });

        add(nastepny_poziom, gbc);


    }


    /**
     * Metoda sluzaca do narysowaniu obrazka tla okna konca poziomu lub gry.
     * @param g kontekst graficzny
     */
    public void paintComponent(Graphics g) {
        if(GlowneOkno.aktualny_poziom==GlowneOkno.maks_liczba_poziomow)
        {
            g.drawImage(koniecIMG, 0, 0, this.getWidth(), this.getHeight(), this);
        }else {
            g.drawImage(wygranaIMG, 0, 0, this.getWidth(), this.getHeight(), this);
        }

    }

}