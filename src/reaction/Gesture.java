package reaction;

import graphicsLib.G;
import music.I;

import java.util.ArrayList;

public class Gesture {
    public Shape shape;
    public G.VS vs;

    public static List UNDO = new List();
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
                if (gest.shape.name.equals("N-N")) {
                    undo();
                } else {
                    gest.doGesture();
                }
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

    public void doGesture() { // Doing a gesture first time adds to UNDO list
        Reaction r = Reaction.best(this); // this could return null
        if (r != null) { UNDO.add(this); r.act(this);
        }
    }

    public void redoGesture() { // Redoing a gesture does not add to UNDO list
        Reaction r = Reaction.best(this); // this could return null
        if (r != null) { r.act(this); }
    }

    public static void undo() {
        if (UNDO.size() > 0) {
            UNDO.remove(UNDO.size() - 1);
            Layer.nuke();
            Reaction.nuke();
            UNDO.redo();
        }
    }

    public static class List extends ArrayList<Gesture> {
        public void redo() {
            for (Gesture g: this) { g.redoGesture(); }
        }
    }
}
