package reaction;

import graphicsLib.G;
import music.UC;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Shape implements Serializable { // once Serializable is implemented,
                                             // cannot change definition without errors

    public Prototype.List prototypes = new Prototype.List();
    public String name;
    public static String fileName = "ShapeDB.dat";
    public static HashMap<String, Shape> DB = loadShapeDB();
    public static Shape DOT = DB.get("DOT");
    public static Collection<Shape> LIST = DB.values();

    public static void saveShapeDB() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(DB);
            System.out.println("Saved as " + fileName);
            oos.close();
        } catch (Exception ex) {
            System.out.println("Database save failed");
            System.out.println(ex);
        }
    }

    public static HashMap<String, Shape> loadShapeDB() {
        HashMap<String, Shape> res = new HashMap<>();
        res.put("DOT", new Shape("DOT"));
        try {
            System.out.println("Attempting to load database...");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            res = (HashMap<String, Shape>) ois.readObject();
            System.out.println("Successfully loaded " + res.keySet());
            ois.close();
        } catch (Exception ex) {
            System.out.println("Database load failed");
            System.out.println(ex);
        }
        return res;
    }

    public static Shape recognize(Ink ink) {
        if (ink.vs.size.x < UC.DOT_THRESHOLD && ink.vs.size.y < UC.DOT_THRESHOLD) {return DOT;}
        Shape bestMatch = null;
        int bestSoFar = UC.NO_MATCH_DIST;
        for (Shape s: LIST) {
            int d = s.prototypes.bestDist(ink.norm);
            if (d < bestSoFar) {bestMatch = s; bestSoFar = d;}
        }
        return bestMatch;
    }

    public Shape(String name) { this.name = name; }

    // --- Prototype
    public static class Prototype extends Ink.Norm implements Serializable {
        int nBlend = 1;

        public void blend(Ink.Norm n) {
            blend(n, nBlend);
            nBlend++;
        }

        // --- List
        public static class List extends ArrayList<Prototype> implements Serializable {
            public static Prototype bestMatch;
            private static final int m = 10;
            private static final int w = 60;
            private static final G.VS showBox = new G.VS(m, m, w, w);
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
