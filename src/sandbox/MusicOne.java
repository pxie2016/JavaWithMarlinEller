package sandbox;

import graphicsLib.G;
import graphicsLib.Window;
import music.Glyph;
import music.Page;
import music.UC;
import reaction.Gesture;
import reaction.Ink;
import reaction.Layer;
import reaction.Reaction;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MusicOne extends Window {

    static {
        new Layer("BACK");
        new Layer("FORE");
        new Layer("NOTE");
    }

    public MusicOne() {
        super("MusicOne", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
        Reaction.initialReactions.addReaction(new Reaction("E-W") {
            public int bid(Gesture g) { return 0; }
            public void act(Gesture g) { new Page(g.vs.yM()); }
        });
    }

    static int[] xPoly = {100, 200, 200, 100};
    static int[] yPoly = {50, 70, 80, 60};
    static Polygon poly = new Polygon(xPoly, yPoly, 4);

    public void paintComponent(Graphics g) {
        G.fillBackground(g);
        Layer.ALL.show(g);
        g.setColor(Color.BLACK);
        Ink.BUFFER.show(g);
        g.setColor(Color.CYAN);
        g.fillPolygon(poly);
    }

    public void mousePressed(MouseEvent me) { Gesture.AREA.dn(me.getX(), me.getY()); repaint(); }
    public void mouseDragged(MouseEvent me) { Gesture.AREA.drag(me.getX(), me.getY()); repaint(); }
    public void mouseReleased(MouseEvent me) { Gesture.AREA.up(me.getX(), me.getY()); repaint(); }
}
