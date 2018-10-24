package drawing;

import points.SeznamBodu;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Renderer {

    private BufferedImage img;
    private Canvas canvas;
    private static final int FPS = 1000 / 30;
    private SeznamBodu seznamBodu; //PointList = stejny balicek
    private int blue = 0x0000FF;
    private int red = 0xff0000;
    private int purple = 0x800080;
    private int green = 0x008000;

    public Renderer(BufferedImage img, Canvas canvas, SeznamBodu seznamBodu) {
        this.img = img;
        this.canvas = canvas;
        setLoop();
        this.seznamBodu = seznamBodu;
    }

    private void setLoop() {
        // časovač, který 30 krát za vteřinu obnoví obsah plátna aktuálním img
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                canvas.getGraphics().drawImage(img, 0, 0, null);
            }
        }, 0, FPS);
    }

    public void clear() {
        // https://stackoverflow.com/a/5843470
        Graphics g = img.getGraphics();
        g.setColor(Color.BLACK);
        g.clearRect(0, 0, 800, 600);
        //info pro uzivatele zustava celou dobu na platnu
        img.getGraphics().drawString("LMB for Line | CTRL+LMB for endpoints of Polyline | RMB for Polygon", 10, img.getHeight() - 50);
    }

    public void drawPixel(int x, int y, int color) { //kresleni pixelu
        if ((0 < x && x < img.getWidth()) && (0 < y && y < img.getHeight())) { //kontrola jestli bod ktery se pokousime kreslit patri do velikosti platna
            img.setRGB(x, y, color);
        }
    }


    public void DDALine(int x1, int y1, int x2, int y2, int color) { //kreslit DDA Line
        int dx = x2 - x1;
        int dy = y2 - y1;
        float x, y, k, g = 0, h = 0;

        k = dy / (float) dx;
        if (Math.abs(k) < 1) {
            // řídící osa X
            g = 1;
            h = k;

            if (x2 < x1) {
                int pom = x2;
                x2 = x1;
                x1 = pom;
                pom = y2;
                y2 = y1;
                y1 = pom;
            }
        } else {
            // řídící osa Y
            g = 1 / k;
            h = 1;

            if (y2 < y1) {
                int pom = y2;
                y2 = y1;
                y1 = pom;
                pom = x2;
                x2 = x1;
                x1 = pom;
            }
        }

        x = x1;
        y = y1;

        for (int i = 0; i <= Math.max(Math.abs(dx), Math.abs(dy)); i++) { //kresleni pixelu
            drawPixel(Math.round(x), Math.round(y), color);
            x += g;
            y += h;
        }
    }

    public void drawLine(MouseEvent e) { //Vykreslovat usecku
        clear();
        DDALine(seznamBodu.getPointList().get(0), seznamBodu.getPointList().get(1), e.getX(), e.getY(), purple);

    }

    public void N_uhelnik() { //Kresil n uhelnik zapomoci DDA linii
        clear();
        for (int i = 2; i < seznamBodu.getNPointList().size(); i += 2) {
            DDALine(seznamBodu.getNPointList().get(i - 2), seznamBodu.getNPointList().get(i - 1),
                    seznamBodu.getNPointList().get(i), seznamBodu.getNPointList().get(i + 1), blue);
        }
        if (seznamBodu.getNPointList().size() >= 6) {
            DDALine(seznamBodu.getNPointList().get(0), seznamBodu.getNPointList().get(1),
                    seznamBodu.getNPointList().get(seznamBodu.getNPointList().size() - 2),
                    seznamBodu.getNPointList().get(seznamBodu.getNPointList().size() - 1), green);
        }
    }

    public void drawPolygon(MouseEvent e) { //Vykreslovani polynomu
        clear();

        int x2 = e.getX();
        int y2 = e.getY();
        int x1 = seznamBodu.getPolynomPointList().get(0);
        int y1 = seznamBodu.getPolynomPointList().get(1);
        int p = 3;

        if (seznamBodu.getPolynomPointList().size() == 4) {
            x2 = seznamBodu.getPolynomPointList().get(2);
            y2 = seznamBodu.getPolynomPointList().get(3);
            p = Math.abs(Math.round((x2 - e.getX()) / 5));
            if (p < 3) p = 3;
        }

        double x0 = x2 - x1;
        double y0 = y2 - y1;
        double radius = 2 * Math.PI;

        double krok = radius / (double) p;
        for (double i = 0; i < radius; i += krok) {
            double x = x0 * Math.cos(krok) + y0 * Math.sin(krok);
            double y = y0 * Math.cos(krok) - x0 * Math.sin(krok);
            DDALine((int) x0 + x1, (int) y0 + y1, (int) x + x1, (int) y + y1, red);
            x0 = x;
            y0 = y;
        }
    }
}