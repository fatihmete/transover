/*Developer : Fatih Mete
E-Mail : fthylcnmt@gmail.com
Release Date : 24 January 2018
Licence : GPL

TransOver
Copyright (C) 2018  Fatih Mete

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package transOver.fatihmete;

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

public class transOver extends JFrame {

    private static final int JFRAME_WIDTH = 400;
    private static final int JFRAME_HEIGHT = 300;
    private static JTextPane translatedTextPane;
    private String textToBeTranslate = getClipboard();

    public static void main(String[] args) throws UnsupportedFlavorException, InterruptedException, IOException {

        transOver transOverApp = new transOver();
    }


    private transOver() throws UnsupportedFlavorException, InterruptedException, IOException {

        setGui();
        trackTextCopiedToClipboard();

    }

    private void setGui() {

        int[] screen = getScreenSize();

        Container contentPane;

        setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
        setUndecorated(true);
        setResizable(false);
        setLocation(screen[0] - JFRAME_WIDTH, screen[1] - JFRAME_HEIGHT);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(Color.ORANGE);


        translatedTextPane = new JTextPane();
        translatedTextPane.setContentType("text/html");
        translatedTextPane.setBackground(Color.orange);
        translatedTextPane.setEditable(false);

        contentPane.add(translatedTextPane, BorderLayout.CENTER);

        JButton hideButton = new JButton();
        JButton exitButton = new JButton();
        exitButton.setText("Exit");
        hideButton.setText("Hide");
        hideButton.setBackground(Color.orange);
        exitButton.setBackground(Color.orange);

        hideButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);

            }

        });
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        contentPane.add(hideButton, BorderLayout.NORTH);
        contentPane.add(exitButton, BorderLayout.SOUTH);


    }

    private void translate() {
        URL url = null;

        System.setProperty("http.agent", "Chrome");
        try {
            url = new URL("https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=tr&dt=t&q=" + getClipboard());

        } catch (UnsupportedFlavorException | IOException e) {

            e.printStackTrace();

        }


        assert url != null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            for (String line; (line = reader.readLine()) != null; ) {
                //System.out.println(line);
                line = line.substring(0, line.indexOf("\",\""));
                line = line.substring(line.indexOf("\"") + 1, line.length());
                translatedTextPane.setText("<html><div style='color:black; padding:5px; font-size:18px; overflow-y: scroll; font-family: Calibri; '>" + line + "</div></html>");


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void trackTextCopiedToClipboard() throws IOException, UnsupportedFlavorException, InterruptedException {


        while (1 < 2) {

            Thread.sleep(800);

            if (!textToBeTranslate.equals(getClipboard()) && !isVisible()) {

                PointerInfo pointerPosition = MouseInfo.getPointerInfo();
                setLocation(pointerPosition.getLocation().x + 50, pointerPosition.getLocation().y + 50);
                translate();
                textToBeTranslate = getClipboard();
                setVisible(true);

            }
        }

    }

    private static String getClipboard() throws IOException, UnsupportedFlavorException {

        String data = (String) Toolkit.getDefaultToolkit()
                .getSystemClipboard().getData(DataFlavor.stringFlavor);
        data = data.replace(" ", "%20");
        data = data.replace("\n", "%20");
        data = data.replace("\t", "%20");

        return data;

    }

    private static int[] getScreenSize() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new int[]{(int) screenSize.getWidth(), (int) screenSize.getHeight()};

    }


}
