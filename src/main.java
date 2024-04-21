import frontend.Window;

class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please give a port");
        }
        new Window(Integer.parseInt(args[0]));
    }
}