package music;

import graphicsLib.G;
import java.awt.*;
import java.util.ArrayList;

public class Ink extends G.PL implements I.Show {

    public static Buffer BUFFER = new Buffer(); // Singleton

    public Ink() {
        super(BUFFER.n);
        for (int i = 0; i < BUFFER.n; i++) { points[i].set(BUFFER.points[i]); }
    }

    @Override
    public void show(Graphics g) {
        g.setColor(UC.INK_COLOR);
        draw(g);
    }

    public static class List extends ArrayList<Ink> implements I.Show {

        @Override
        public void show(Graphics g) {
            for (Ink ink: this) { ink.show(g); }
        }
    }

    public static class Buffer extends G.PL implements I.Show, I.Area {

        public int n;  // Number of points in buffer
        public G.BBox bbox = new G.BBox();
        public static final int MAX = UC.INK_BUFFER_MAX;

        private Buffer() { super(MAX); } // Private to prevent calling from another class; singleton pattern

        public void add(int x, int y) {
            if (n < MAX) {
                points[n++].set(x, y);
                bbox.add(x, y);
            }
        }

        public void clear() { n = 0; }

        @Override
        public boolean hit(int x, int y) { return true; }

        @Override
        public void dn(int x, int y) {
            clear();
            bbox.set(x, y);
            add(x, y);
        }

        @Override
        public void drag(int x, int y) { add(x, y); }

        @Override
        public void up(int x, int y) {}

        @Override
        public void show(Graphics g) { drawN(g, n); bbox.draw(g); }
    }
}
