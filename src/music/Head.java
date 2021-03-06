package music;

import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;

public class Head extends Mass implements Comparable<Head>{

    public Staff staff;
    public int line;
    public Time time;
    public Glyph forcedGlyph = null;
    public Stem stem = null;
    public boolean wrongSide = false;

    public Head(Staff staff, int x, int y) {
        super("NOTE");
        this.staff = staff;
        this.time = staff.sys.getTime(x);
        time.heads.add(this);
        this.line = staff.lineOfY(y);

        addReaction(new Reaction("S-S") { // stem or unstem heads
            public int bid(Gesture g) {
                int x = g.vs.xM(), y1 = g.vs.yL(), y2 = g.vs.yH();
                int w = Head.this.w(), yH = Head.this.y();
                if (y1 > yH || y2 < yH) { return UC.NO_BID; }
                int hL = Head.this.time.x, hR = hL + w;
                if (x < hL - w || x > hR + w) { return UC.NO_BID; }
                if (x < hL + w/2) { return hL - x; }
                if (x > hR - w/2) { return x - hR; }
                return UC.NO_BID;
            }

            public void act(Gesture g) {
                int x = g.vs.xM(), y1 = g.vs.yL(), y2 = g.vs.yH();
                Staff staff = Head.this.staff;
                Time t = Head.this.time;
                int w = Head.this.w();
                boolean up = x > t.x + w/2;
                if (Head.this.stem == null) {
                    t.stemHead(staff, up, y1, y2);
                } else {
                    t.unStemHead(y1, y2);
                }
            }
        });

        addReaction(new Reaction("DOT") { // adding dots (stub)

            public int bid(Gesture g) {
                int xH = Head.this.x(), yH = Head.this.y(), h = Head.this.staff.H(), w = Head.this.w();
                int x = g.vs.xM(), y = g.vs.yM();
                if (x < xH || x > xH + 2 * w || y < yH - h || y > yH + h) { return UC.NO_BID; }
                return Math.abs(xH + w - x) + Math.abs(yH - y);
            }

            public void act(Gesture g) {
                if (Head.this.stem != null) {
                    Head.this.stem.cycleDot();
                }
            }
        });
    }

    public void show(Graphics g) {
        int h = staff.H();
        g.setColor(wrongSide ? Color.RED : Color.BLUE);
        (forcedGlyph != null ? forcedGlyph: normalGlyph()).showAt(g, h, x(), staff.yTop() + line * h);
        if (stem != null) {
            int off = UC.REST_AUG_DOT_OFFSET, sp = UC.AUG_DOT_SPACING;
            for (int i = 0; i < stem.nDot; i++) {
                g.fillOval(time.x + off + i * sp, y() - 3 * h / 2, 2 * h / 3, 2 * h / 3);
            }
        }
    }

    public void unStem(){
        if (stem != null) {
            stem.heads.remove(this);
            if (stem.heads.size() == 0) {
                stem.deleteStem();
            }
            stem = null;
            wrongSide = false;
        }
    }

    public void joinStem(Stem s) {
        if (stem != null) { unStem(); }
        s.heads.add(this);
        stem = s;
    }

    public Glyph normalGlyph() {
        if (stem == null) { return Glyph.HEAD_Q; }
        if (stem.nFlag == -1) { return Glyph.HEAD_HALF; }
        if (stem.nFlag == -2) { return Glyph.HEAD_WHOLE; }
        return Glyph.HEAD_Q;
    }

    public int y() { return staff.yLine(line); }
    public int x() {
        int res = time.x;
        if (wrongSide) {
            res += (stem != null && stem.isUp) ? w() : -w();
        }
        return res;
    }

    public void deleteMass() { time.heads.remove(this); } // stub

    public int w() { return 24 * staff.H() / 10; }

    public int compareTo(Head head) {
        return (staff.iStaff != head.staff.iStaff) ? staff.iStaff - head.staff.iStaff : line - head.line;
    }

    public static class List extends ArrayList<Head> {}

}
