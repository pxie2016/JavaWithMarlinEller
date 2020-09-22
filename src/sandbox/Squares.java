package sandbox;

import graphicsLib.G;
import graphicsLib.Window;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Squares extends Window {

    public static Square.List theList = new Square.List();
    public static Square sizeSquare;
    public static Square dragSquare; // This can be null if no clicks
    public static G.V mouseDown = new G.V(0, 0);

    public Squares () {
        super("Squares", 1000, 600);
    }

    @Override
    public void paintComponent(Graphics g) {
        theList.draw(g);
    }

    @Override
    public void mousePressed(MouseEvent me) {
        int x = me.getX(), y = me.getY();
        dragSquare = theList.squareHit(x, y);
        if (dragSquare == null) {
            sizeSquare = new Square(x, y);
            theList.add(sizeSquare);
        } else {
            mouseDown.set(x - dragSquare.loc.x, y - dragSquare.loc.y);
        }
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        int x = me.getX(), y = me.getY();
        if (dragSquare == null) {
            sizeSquare.resize(x, y);
        } else {
            sizeSquare.move(x - mouseDown.x, y - mouseDown.y);
        }
        repaint();
    }

    // Square
    public static class Square extends G.VS {
        public Color c = G.rndColor();

        public Square(int x, int y) {
            super(x, y, 100, 100);
        }

        public void resize(int x, int y) {
            if (x > loc.x && y > loc.y) {
                size.set(x - loc.x, y - loc.y);
            }
        }

        public void move(int x, int y) {
            loc.set(x, y);
        }

        // List of Squares
        public static class List extends ArrayList<Square> {
            public void draw(Graphics g) {
                for (Square s: this) {
                    s.fill(g, s.c);
                }
            }
            // Check if any Square is hit
            public Square squareHit(int x, int y) {
                Square res = null;
                for (Square s: this) {
                    if (s.hit(x, y)) {
                        res = s;
                    }
                }
                return res;
            }
        }
    }
}
