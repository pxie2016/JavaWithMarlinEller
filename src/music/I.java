package music;

import java.awt.*;

public interface I {
    // All methods in an interface are public and abstract but not final;
    // All fields in an interface are public, abstract and final;
    interface Hit {
        boolean hit(int x, int y);
    }
    interface Area extends Hit {
        void dn(int x, int y);
        void drag(int x, int y);
        void up(int x, int y);
    }
    interface Show {
        void show(Graphics g); }
}
