package sandbox;

import graphicsLib.Window;
import reaction.ShapeTrainer;

public class Main {
    public static void main(String[] args) {
        // Window.PANEL = new Paint();
        // Window.PANEL = new Squares();
        // Window.PANEL = new PaintInk();
        // Window.PANEL = new ShapeTrainer();
        // Window.PANEL = new ReactionTest();
        Window.PANEL = new MusicOne();
        Window.launch();
    }
}
