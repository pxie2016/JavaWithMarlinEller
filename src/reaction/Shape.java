package reaction;

import graphicsLib.G;
import music.I;
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

    public static Shape getOrCreate(String name) {
        if (!DB.containsKey(name)) {
            DB.put(name, new Shape(name));
        }
        return DB.get(name);
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

        public Prototype(Ink.Norm norm) {
            super(norm);
        }

        public void blend(Ink.Norm n) {
            blend(n, nBlend);
            nBlend++;
        }

        // --- List
        public static class List extends ArrayList<Prototype> implements Serializable {
            public static Prototype bestMatch;
            private static final int m = UC.PROTOTYPE_LIST_MARGIN;
            private static final int w = UC.PROTOTYPE_LIST_SIZE;
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

            public int nHit(int x, int y) { return x / (m + w); } // truncating division

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

            public Prototype blendOrAdd(Ink.Norm norm) {
                int d = bestDist(norm);
                if (d < UC.NO_MATCH_DIST) {
                    bestMatch.blend(norm);
                    return bestMatch;
                }
                Prototype res = new Prototype(norm);
                add(res);
                return res;
            }

        }
    }

    // --- Trainer
    public static class Trainer implements I.Area{

        public NameState ns = new NameState();
        public static boolean isTraining = false;

        @Override
        public boolean hit(int x, int y) { return isTraining; }

        @Override
        public void dn(int x, int y) { Ink.BUFFER.dn(x, y); }

        @Override
        public void drag(int x, int y) { Ink.BUFFER.drag(x, y); }

        @Override
        public void up(int x, int y) {
            if (ns.illegal()) {ns.shapeTrained = null; return;}
            Ink ink = new Ink();
            Shape.Prototype prototype;
            ns.shapeTrained = Shape.getOrCreate(ns.Name);
            Prototype.List pList = ns.shapeTrained.prototypes;
            if (pList.size() > 0 && y < UC.PROTOTYPE_LIST_Y_LIM) {
                int iProto = pList.nHit(x, y);
                if (iProto < pList.size()) { pList.remove(iProto); }
            } else {
                pList.blendOrAdd(ink.norm);
            }
            ns.setState();
        }

        public void show(Graphics g) {
            g.setColor(UC.SHAPETRAINER_BACKGROUND_COLOR);
            g.fillRect(0, 0, 5000, 5000);
            g.setColor(Color.BLACK);
            Ink.BUFFER.show(g);
            ns.show(g);
        }

        public void keyTyped(char c) { ns.keyTyped(c); }

        // --- NameState
        public static class NameState {
            public static String UNKNOWN = "<- This shape name is currently unknown";
            public static String ILLEGAL = "<- This shape name is illegal";
            public static String KNOWN = "<- This shape name is currently known";
            public static String INSTRUCTIONS =
                        "<- Type name and draw examples - space clears name - Enter saves database";
            public String Name = "";
            public String State = ILLEGAL;
            public Shape shapeTrained;

            public void show(Graphics g) {
                    g.setColor(Color.BLACK);
                    g.drawString(INSTRUCTIONS, 20, 100);
                    if (State.equals(ILLEGAL)) { g.setColor(Color.RED); }
                    g.drawString(Name, 600, 30);
                    g.drawString(State, 700, 30);
                    if (shapeTrained != null) { shapeTrained.prototypes.show(g); }
                }

                public void setState() {
                    shapeTrained = null;
                    State = (Name.equals("") || Name.equals("DOT")) ? ILLEGAL : UNKNOWN;
                    if (State == UNKNOWN) {
                        if (Shape.DB.containsKey(Name)) {
                            State = KNOWN;
                            shapeTrained = Shape.DB.get(Name);
                        }
                    }
                }

                public boolean illegal() { return State == ILLEGAL; }

                public void keyTyped(char c) {
                    System.out.println("Typed: " + c);
                    Name = (c == ' ' || c == 0x0D || c == 0x0A) ? "" : Name + c;
                    setState();
                    if (c == 0x0D || c == 0x0A) { Shape.saveShapeDB();}
                }
        }

    }

}
