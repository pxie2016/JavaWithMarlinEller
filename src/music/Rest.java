package music;

import reaction.Gesture;
import reaction.Reaction;

import java.awt.*;

public class Rest extends Duration {

    public Staff staff;
    public Time time;
    public int line = 4;

    public Rest(Staff staff, Time time) {
        this.staff = staff;
        this.time = time;
        addReaction(new Reaction("E-E") { // add a flag

            public int bid(Gesture g) {
                int y = g.vs.yM(), x1 = g.vs.xL(), x2= g.vs.xH(), x = Rest.this.time.x;
                if (x1 > x || x2 < x) { return UC.NO_BID; }
                return Math.abs(y - Rest.this.staff.yLine(4));
            }
            public void act(Gesture g) {
                Rest.this.incFlag();
            }
        });

        addReaction(new Reaction("W-W") { // remove a flag

            public int bid(Gesture g) {
                int y = g.vs.yM(), x1 = g.vs.xL(), x2 = g.vs.xH(), x = Rest.this.time.x;
                if (x1 > x || x2 < x) { return UC.NO_BID; }
                return Math.abs(y - Rest.this.staff.yLine(4));
            }

            public void act(Gesture g) {
                Rest.this.decFlag();
            }
        });

        addReaction(new Reaction("DOT") { // Aug. dot

            public int bid(Gesture g) {
                int xr = Rest.this.time.x, yr = Rest.this.y();
                int x = g.vs.xM(), y = g.vs.yM();
                if (x < xr || x > xr + 40 || y < yr - 40 || y > yr + 40) { return UC.NO_BID; }
                return Math.abs(x - xr) + Math.abs(y - yr);
            }

            public void act(Gesture g) {
                Rest.this.cycleDot();
            }
        });
    }

    public void show(Graphics g) {
        int h = staff.H(), y = y();
        if (nFlag == -2) { Glyph.REST_W.showAt(g, h, time.x, y); }
        if (nFlag == -1) { Glyph.REST_H.showAt(g, h, time.x, y); }
        if (nFlag == 0) { Glyph.REST_Q.showAt(g, h, time.x, y); }
        if (nFlag == 1) { Glyph.REST_1F.showAt(g, h, time.x, y); }
        if (nFlag == 2) { Glyph.REST_2F.showAt(g, h, time.x, y); }
        if (nFlag == 3) { Glyph.REST_3F.showAt(g, h, time.x, y); }
        if (nFlag == 4) { Glyph.REST_4F.showAt(g, h, time.x, y); }

        int off = UC.REST_AUG_DOT_OFFSET, sp = UC.AUG_DOT_SPACING;
        for (int i = 0; i < nDot; i++) {
            g.fillOval(time.x + off + i * sp, y - 3 * h / 2, h * 2 / 3, h * 2 / 3);
        }
    }

    public int y() { return staff.yLine(line); }

}
