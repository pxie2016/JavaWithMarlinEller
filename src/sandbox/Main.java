package sandbox;

import graphicsLib.Window;

public class Main {
    public static void main(String[] args) {
        // Window.PANEL = new Paint();
        // Window.PANEL = new Squares();
        Window.PANEL = new PaintInk();
        Window.launch();
    }
}
