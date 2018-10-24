package main;

import drawing.Renderer;
import points.SeznamBodu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MainClass {

    /**
     * Autor: Igor Milenković
     * Skupina: C05
     * Průběžná úloha z PGRF1: Úsečka a n-úhelník
     */

    private JFrame window;
    private BufferedImage img; // objekt pro zápis pixelů
    private Canvas canvas; // plátno pro vykreslení BufferedImage
    private drawing.Renderer renderer;
    private SeznamBodu seznamBodu = new SeznamBodu(); //naimportovani PointListu ze stejneho balicku
    private int blue = 0x0000FF;
    private int grey = 0xdbdbdb;

    public MainClass() {
        window = new JFrame();
        // bez tohoto nastavení se okno zavře, ale aplikace stále běží na pozadí
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(800, 600); // velikost okna
        window.setLocationRelativeTo(null);// vycentrovat okno
        window.setTitle("Igor Milenković | Ukol 1 | PGRF1 - Počítačová grafika 1"); // titulek okna

        // inicializace image, nastavení rozměrů (nastavení typu - pro nás nedůležité)
        img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);

        // inicializace plátna, do kterého budeme kreslit img
        canvas = new Canvas();
        //info pro uzivatele:
        img.getGraphics().drawString("LMB for Line | CTRL+LMB for endpoints of Polyline | RMB for Polygon", 10, img.getHeight() - 50);

        window.add(canvas); // vložit plátno do okna
        window.setVisible(true); // zobrazit okno

        renderer = new Renderer(img, canvas, seznamBodu);

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) { //leve tlacitko LMB
                    if (e.isControlDown()) { //pokud je stisknute CTRL -> N-uhelnik
                        seznamBodu.addToNPointList(e.getX());
                        seznamBodu.addToNPointList(e.getY());
                        renderer.drawPixel(e.getX(),e.getY(),blue); //pocatecni bod
                        if (seznamBodu.getNPointList().size() >= 4) {
                            renderer.N_uhelnik();
                        }
                    } else { //jinak zadat pocatecni bod usecky
                        seznamBodu.getPointList().clear();
                        seznamBodu.addToPointlist(e.getX());
                        seznamBodu.addToPointlist(e.getY());
                    }
                }

                if (SwingUtilities.isRightMouseButton(e)) { //prave tlacitko RMB
                    seznamBodu.addToPolynomPointList(e.getX());
                    seznamBodu.addToPolynomPointList(e.getY());
                }


            }
        });

        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) { //tazenim mysi
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if(e.isControlDown()){
                    } else { //jenom pokud neni stisknute CTRL
                        renderer.drawLine(e); //zadavat bodi za DDALine
                    }

                }
            }
        });

        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (seznamBodu.getPolynomPointList().size() == 2) {
                    renderer.drawPolygon(e);
                    renderer.DDALine(seznamBodu.getPolynomPointList().get(0),
                            seznamBodu.getPolynomPointList().get(1), e.getX(), e.getY(), grey);
                }
                if (seznamBodu.getPolynomPointList().size() == 4) {
                    renderer.drawPolygon(e);
                }
            }
        });

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) { //po zmacknuti klavesy C smazat cely obsah platna, zresetoval pointy a vypsat info pro uzivatele
                    renderer.clear();
                    seznamBodu.getPointList().clear();
                    seznamBodu.getNPointList().clear();
                    seznamBodu.getPolynomPointList().clear();
                    img.getGraphics().drawString("LMB for Line | CTRL+LMB for endpoints of Polyline | RMB for Polygon", 10, img.getHeight() - 50);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainClass::new);
        // https://www.google.com/search?q=SwingUtilities.invokeLater
        // https://www.javamex.com/tutorials/threads/invokelater.shtml
        // https://www.google.com/search?q=java+double+colon
    }
}
