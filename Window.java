import javax.swing.JFrame;

class Window extends JFrame {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    public static void main(String[] args) {
        new Window();
    }

    public Window() {
        setWindowProperties();
    }

    private void setWindowProperties() {
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Test");
        setVisible(true);
    }

}
