package sandbox;

import graphicsLib.G;
import graphicsLib.Window;
import java.awt.*;
import java.awt.event.MouseEvent;

import reaction.*;
import music.UC;
import reaction.Shape;

public class PaintInk extends Window {

    public static Ink.List inkList = new Ink.List();
    public static Shape.Prototype.List plist = new Shape.Prototype.List();

    public PaintInk() {
        super("PaintInk", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        G.fillBackground(g);
        inkList.show(g);
        Ink.BUFFER.show(g);
        if (inkList.size() > 1) {
            int last = inkList.size() - 1;
            int dist = inkList.get(last).norm.dist(inkList.get(last - 1).norm);
            g.setColor(dist > UC.NO_MATCH_DIST ? Color.RED : Color.BLACK);
            g.drawString("Dist:" + dist, 600, 60);
        }
        plist.show(g);
    }

    @Override
    public void mousePressed(MouseEvent me) { Ink.BUFFER.dn(me.getX(), me.getY()); repaint(); }

    @Override
    public void mouseDragged(MouseEvent me) { Ink.BUFFER.drag(me.getX(), me.getY()); repaint(); }

    @Override
    public void mouseReleased(MouseEvent me) {
        Ink ink = new Ink();
        Shape.Prototype proto;
        inkList.add(new Ink());
        if (plist.bestDist(ink.norm) < UC.NO_MATCH_DIST) {
            proto = plist.bestMatch;
            proto.blend(ink.norm);
        } else {
            proto = new Shape.Prototype();
            plist.add(proto);
        }
        ink.norm = proto;
        repaint();
    }


}
