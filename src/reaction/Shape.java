package reaction;

import graphicsLib.G;
import music.UC;
import java.awt.*;
import java.util.ArrayList;

public class Shape {

    public Prototype.List prototypes = new Prototype.List();
    public String name;
    public Shape(String name) { this.name = name; }

    // --- Prototype
    public static class Prototype extends Ink.Norm {
        int nBlend = 1;

        public void blend(Ink.Norm n) {
            blend(n, nBlend);
            nBlend++;
        }

        // --- List
        public static class List extends ArrayList<Prototype> {
            public static Prototype bestMatch;
            private static int m = 10, w = 60;
            private static G.VS showBox = new G.VS(m, m, w, w);
            public int bestDist(Ink.Norm norm) {
                bestMatch = null;
                int bestSoFar = UC.NO_MATCH_DIST;
                for (Prototype p : this){
                    int d = p.dist(norm);
                    if (d < bestSoFar) {
                        bestMatch = p;
                        bestSoFar = d;
                    }
                }
                return bestSoFar;
            }

            public void show(Graphics g) {
                g.setColor(Color.ORANGE);
                for (int i = 0; i < size(); i++) {
                    Prototype p = get(i);
                    int x = m + i * (m + w);
                    showBox.loc.set(x, m);
                    p.drawAt(g, showBox);
                    g.drawString("" + p.nBlend, x, 20);
                }
            }

        }
    }
}
