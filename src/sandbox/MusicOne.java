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

    public void paintComponent(Graphics g) {
        G.fillBackground(g);
        Layer.ALL.show(g);
        g.setColor(Color.BLACK);
        Ink.BUFFER.show(g);

        if (Page.PAGE != null) {
            int h = 32;
            Glyph.HEAD_Q.showAt(g, h, 200, Page.PAGE.yMargin.lo + 4 * h);
            g.setColor(Color.RED);
            g.drawRect(200, Page.PAGE.yMargin.lo + 3 * h, 24 * h / 10, 2 * h);
        }
    }

    public void mousePressed(MouseEvent me) { Gesture.AREA.dn(me.getX(), me.getY()); repaint(); }
    public void mouseDragged(MouseEvent me) { Gesture.AREA.drag(me.getX(), me.getY()); repaint(); }
    public void mouseReleased(MouseEvent me) { Gesture.AREA.up(me.getX(), me.getY()); repaint(); }
}
