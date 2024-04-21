package frontend;

import javax.swing.JFrame;

import backend.ConnectionHandler;

public class Window extends JFrame {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    private ConnectionHandler conHandler;

    public Window(int connection_port) {
        setWindowProperties();
        conHandler = new ConnectionHandler(connection_port);
    }

    private void setWindowProperties() {
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Test");
        setVisible(true);
    }

}
