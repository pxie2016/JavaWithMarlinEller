package sandbox;

import graphicsLib.G;
import graphicsLib.Window;
import music.UC;
import reaction.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ReactionTest extends Window {

    static {
        new Layer ("Back");
        new Layer ("Fore");
    }

    public ReactionTest() {
        super("Simple reaction test", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
        Reaction.initialReactions.addReaction(new Reaction("SW-SW") {
            public int bid(Gesture g) { return 0; }
            public void act(Gesture g) { new Box(g.vs); }
        });
    }

    public void paintComponent(Graphics g) {
        G.fillBackground(g);
        g.setColor(Color.BLUE);
        Ink.BUFFER.show(g);
        Layer.ALL.show(g);
    }

    public void mousePressed(MouseEvent me) { Gesture.AREA.dn(me.getX(), me.getY()); repaint(); }
    public void mouseDragged(MouseEvent me) { Gesture.AREA.drag(me.getX(), me.getY()); repaint(); }
    public void mouseReleased(MouseEvent me) { Gesture.AREA.up(me.getX(), me.getY()); repaint(); }

    public static class Box extends Mass {

        public G.VS vs;
        public Color c = G.rndColor();

        public Box(G.VS vs) {
            super("Back");
            this.vs = vs;
            addReaction(new Reaction("S-S") { // Delete box
                public int bid(Gesture g) {
                    int x = g.vs.xM(), y = g.vs.yL();
                    if (Box.this.vs.hit(x, y)) {
                        return Math.abs(x - Box.this.vs.xM());
                    } else {
                        return UC.NO_BID;
                    }
                }

                public void act(Gesture g) {
                    Box.this.deleteMass(); // Compound "this"
                }
            });

            addReaction(new Reaction("DOT") { // Change color
                public int bid(Gesture g) {
                    int x = g.vs.xM(), y = g.vs.yM();
                    if (Box.this.vs.hit(x, y)) {
                        return Math.abs(x - Box.this.vs.xM()) + Math.abs(y - Box.this.vs.yM());
                    } else {
                        return UC.NO_BID;
                    }
                }

                public void act(Gesture g) {
                    Box.this.c = G.rndColor(); // Compound "this"
                }
            });
        }

        public void show(Graphics g) { vs.fill(g, c); }

    }

}
