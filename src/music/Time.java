package music;

import java.util.ArrayList;

public class Time {

    public int x;

    private Time(int x, List list) {
        this.x = x;
        list.add(this);
    }

    // --- List
    public static class List extends ArrayList<Time> {
        public Sys sys;
        public List(Sys sys) {
            this.sys = sys;
        }

        public Time getTime(int x) {
            Time res = null;
            int dist = UC.SNAP_TIME;
            for (Time t: this) {
                int d = Math.abs(x - t.x);
                if (d < dist) {
                    dist = d;
                    res = t;
                }
            }
            return (res == null) ? new Time(x, this) : res ;
        }
    }
}
