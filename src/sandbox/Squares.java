package sandbox;

import graphicsLib.G;
import graphicsLib.Window;
import music.I;
import music.UC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class Squares extends Window implements ActionListener {

    public static Timer TIMER;
    public static Square.List theList = new Square.List();
    public static Square sizeSquare;
    public static Square dragSquare; // This can be null if no clicks
    public static G.V mouseOffset = new G.V(0, 0);
    public static G.V mouseDown = new G.V(0, 0);
    public static final int WIDTH = UC.WINDOW_WIDTH, HEIGHT = UC.WINDOW_HEIGHT;
    //Only need to change one place
    public static I.Area curArea;
    public static Square BACKGROUND = new Square(0, 0) {
        //Anonymous class (extension of Square, overriding a single object)
        public void dn(int x, int y) {
            sizeSquare = new Square(x, y);
            theList.add(sizeSquare);
        }
        public void drag(int x, int y) {
            sizeSquare.resize(x, y);
        }
        public void up(int x, int y) {}
    };
    //Static block
    static {
        BACKGROUND.size.set(3000, 3000);
        BACKGROUND.c = Color.WHITE;
        theList.add(BACKGROUND);
    }

    public Squares () {
        super("Squares", WIDTH, HEIGHT);
        // TIMER = new Timer(30, this);
        // TIMER.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        theList.draw(g);
    }

//    @Override
//    public void mousePressed(MouseEvent me) {
//        int x = me.getX(), y = me.getY();
//        dragSquare = theList.squareHit(x, y);
//        if (dragSquare == null) {
//            sizeSquare = new Square(x, y);
//            theList.add(sizeSquare);
//        } else {
//            mouseOffset.set(x - dragSquare.loc.x, y - dragSquare.loc.y);
//            dragSquare.dv.set(0, 0);
//        }
//        mouseDown.set(x, y);
//        repaint();
//    }

    @Override
    public void mousePressed(MouseEvent me) {
        int x = me.getX(), y = me.getY();
        curArea = theList.squareHit(x, y);
        curArea.dn(x, y);
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        int x = me.getX(), y = me.getY();
        curArea.drag(x, y);
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        int x = me.getX(), y = me.getY();
        curArea.up(x, y);
        repaint();
    }

//    @Override
//    public void mouseDragged(MouseEvent me) {
//        int x = me.getX(), y = me.getY();
//        if (dragSquare == null) {
//            sizeSquare.resize(x, y);
//        } else {
//            dragSquare.move(x - mouseOffset.x, y - mouseOffset.y);
//        }
//        repaint();
//    }



//    @Override
//    public void mouseReleased(MouseEvent me){
//        int x = me.getX(), y = me.getY();
//        if (dragSquare != null) {
//            dragSquare.dv.set(mouseDown.x - x, mouseDown.y - y);
//        }
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Square
    public static class Square extends G.VS implements I.Area {
        public Color c = G.rndColor();
        public G.V dv = new G.V(0,0);

        public Square(int x, int y) {
            super(x, y, 100, 100);
        }

        public void draw(Graphics g) {
            fill(g, c); loc.add(dv); bounce();
        }

        public void resize(int x, int y) {
            if (x > loc.x && y > loc.y) {
                size.set(x - loc.x, y - loc.y);
            }
        }

        public void move(int x, int y) {
            loc.set(x, y);
        }

        public void bounce() {
            if (xL() < 0 && dv.x < 0) { dv.x = -dv.x; }
            if (yL() < 0 && dv.y < 0) { dv.y = -dv.y; }
            if (xH() > WIDTH && dv.x > 0) { dv.x = -dv.x; }
            if (yH() > HEIGHT && dv.y > 0) { dv.y = -dv.y; }
        }

        public void dn(int x, int y) {
            mouseOffset.set(x - loc.x, y - loc.y);
        }

        public void drag(int x, int y) {
            move(x - mouseOffset.x, y - mouseOffset.y);
        }

        public void up(int x, int y) {}

        // List of Squares
        public static class List extends ArrayList<Square> {
            public void draw(Graphics g) {
                for (Square s: this) {
                    s.draw(g);
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
