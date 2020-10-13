package reaction;

import graphicsLib.Window;
import music.UC;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public class ShapeTrainer extends Window {
    public static Shape.Trainer trainer = new Shape.Trainer();

    public ShapeTrainer() {
        super("ShapeTrainer", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
    }

    public void paintComponent(Graphics g) { trainer.show(g); }

    public void keyTyped(KeyEvent ke) {
        trainer.keyTyped(ke.getKeyChar());
        repaint();
    }

    public void mousePressed(MouseEvent me) {
        trainer.dn(me.getX(), me.getY());
        repaint();
    }

    public void mouseDragged(MouseEvent me) {
        trainer.drag(me.getX(), me.getY());
        repaint();
    }

    public void mouseReleased(MouseEvent me){
        trainer.up(me.getX(), me.getY());
        repaint();
    }
}
