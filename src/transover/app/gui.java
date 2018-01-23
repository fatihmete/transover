package transover.app;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class gui extends JFrame {

    public static final int JFRAME_WIDTH = 400;
    public static final int JFRAME_HEIGHT = 300;
    public static JTextPane metinalanı;
    public String metin = getClipboard();


    public gui() throws UnsupportedFlavorException, InterruptedException, IOException {

       setGui();
       setVisible(false);
       kopyalananMetniTakipEt();


    }

    public void setGui(){

        int[] screen = getScreenSize();

        Container icerikpaneli;


        setTitle("Transover");
        setSize(JFRAME_WIDTH ,JFRAME_HEIGHT);
        setUndecorated(true);
        setResizable(false);
        //PointerInfo fareKonumu = MouseInfo.getPointerInfo();
        //setLocation(fareKonumu.getLocation().x, fareKonumu.getLocation().y);
        System.out.println();
        setLocation(screen[0]-JFRAME_WIDTH, screen[1]-JFRAME_HEIGHT);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        icerikpaneli = getContentPane();
        icerikpaneli.setLayout(new BorderLayout());
        icerikpaneli.setBackground(Color.ORANGE);




        metinalanı = new JTextPane();
        metinalanı.setContentType("text/html");
        metinalanı.setBackground(Color.orange);
        metinalanı.setEditable(false);

        //metinalanı.setHorizontalTextPosition(JLabel.CENTER);
        //metinalanı.setHorizontalTextPosition(SwingConstants.CENTER);

        //metinalanı.setUI(JLabel.);
        icerikpaneli.add(metinalanı,BorderLayout.CENTER);


       JButton dugme = new JButton();
       JButton dugmeKapat = new JButton();
       dugmeKapat.setText("Kapat");
       dugme.setText("Gizle");
       dugme.setBackground(Color.orange);
       dugmeKapat.setBackground(Color.orange);

       dugme.addMouseListener(new MouseAdapter(){
           @Override
           public void mouseClicked(MouseEvent e){
               setVisible(false);

           }

       });
        dugmeKapat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
       icerikpaneli.add(dugme,BorderLayout.NORTH);
       icerikpaneli.add(dugmeKapat,BorderLayout.SOUTH);


    }

    public void translate() {
        URL url = null;

        System.setProperty("http.agent", "Chrome");
        try {
            url = new URL("http://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=tr&dt=t&q=" + getClipboard());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            for (String line; (line = reader.readLine()) != null; ) {
                //System.out.println(line);
                line = line.substring(0, line.indexOf("\",\""));
                line = line.substring(line.indexOf("\"") + 1, line.length());
                metinalanı.setText("<html><div style='color:black; padding:5px; font-size:18px; overflow-y: scroll; font-family: Calibri; '>" + line + "</div></html>");


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void kopyalananMetniTakipEt() throws IOException, UnsupportedFlavorException, InterruptedException {

        //Arakda sürekli çalışan bir döngü oluşturuyoruz. 0,8 saniyede bir kopyalanan metni kontrol ediyor.
        while (1<2) {
            Thread.sleep(800);

           //System.out.println("Kopyalanan Metin: " + getClipboard());
            //System.out.println("Metin: " + metin);

            if (metin.equals(getClipboard())==false) {

                //System.out.println("metin ile kopyalanan metin farklı");
                PointerInfo fareKonumu = MouseInfo.getPointerInfo();
                setLocation(fareKonumu.getLocation().x+50, fareKonumu.getLocation().y+50);
                translate();
                metin = getClipboard();
                setVisible(true);

            }
        }

    }

    public static String getClipboard() throws IOException, UnsupportedFlavorException {
        String data = (String) Toolkit.getDefaultToolkit()
                .getSystemClipboard().getData(DataFlavor.stringFlavor);
        data  = data.replace(" ","%20");
        data = data.replace("\n","%20");
        data = data.replace("\t","%20");

        return data;

    }

    private static int[] getScreenSize(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int[] screen = {(int)screenSize.getWidth(),  (int)screenSize.getHeight()};
        return screen;

    }



}
