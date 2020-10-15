package reaction;

import graphicsLib.G;
import music.I;

public class Gesture {
    public Shape shape;
    public G.VS vs;
    public static I.Area AREA = new I.Area() {
        @Override
        public boolean hit(int x, int y) { return true; }
        @Override
        public void dn(int x, int y) { Ink.BUFFER.dn(x, y); }
        @Override
        public void drag(int x, int y) { Ink.BUFFER.drag(x, y); }
        @Override
        public void up(int x, int y) {
            Ink.BUFFER.add(x, y);
            Ink ink = new Ink();
            Gesture gest = Gesture.getNew(ink); // This could fail if unrecognized
            Ink.BUFFER.clear();
            if (gest != null) {
                Reaction r = Reaction.best(gest); // This could fail if no bids
                if (r != null) { r.act(gest); }
            }
        }
    };

    private Gesture(Shape shape, G.VS vs) {
        this.shape = shape;
        this.vs = vs;
    }

    public static Gesture getNew(Ink ink) { // This could return null
        Shape s = Shape.recognize(ink);
        return (s == null) ? null : new Gesture(s, ink.vs);
    }
}
