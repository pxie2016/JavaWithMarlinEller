package sandbox;

import graphicsLib.Window;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Paint extends Window {

    public static int clicks = 0;
    public static Path thePath = new Path();
    public static Path.Pic thePic = new Path.Pic();

    // constructor
    public Paint() {
        super("Paint", 1000, 600);
    }
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(graphicsLib.G.rndColor());
        g.fillOval(100, 100, 200, 300);

        g.setColor(Color.BLACK);
        g.drawLine(100, 500, 500, 100);

        int x = 400, y = 200;
        String str = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " + clicks;

        g.drawString(str, x, y);
        g.setColor(Color.RED);
        g.drawOval(x, y, 2, 2);

        FontMetrics fm = g.getFontMetrics();
        int a = fm.getAscent(), d = fm.getDescent(), w = fm.stringWidth(str);
        g.drawRect(x, y - a, w, a+d);


        g.setColor(Color.BLACK);
        thePic.draw(g);
        g.setColor(Color.RED);
        thePath.draw(g); // This is the method below, which is not primitive/built-in for the Graphics object g
    }

    @Override //Overrides method with same name in the super class (Window)
    public void mousePressed(MouseEvent me) {
        clicks++; // Increments upon each mouse click (reflects in Line 25)
        // thePath.clear();
        thePath = new Path();
        thePath.add(me.getPoint());
        thePic.add(thePath);
        repaint(); // Refreshes paintComponent() upon each mouse click
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        thePath.add(me.getPoint());
        repaint();
    }
    // This is a nested, helper class (called by Paint.Path)
    public static class Path extends ArrayList<Point> {
        public void draw(Graphics g) {
            for (int i = 1; i < size(); i++) {
                Point p = get(i - 1), n = get(i); // Previous and current points
                g.drawLine(p.x, p.y, n.x, n.y);
            }
        }
        // Another layer of nested class (called by Paint.Path.Pic)
        public static class Pic extends ArrayList<Path> {
            public void draw(Graphics g) {
                for (Path p: this) { // go thru all Paths
                    p.draw(g);
                }
            }
        }
    }
}

