import frontend.Window;

class Main {
    public static void main(String[] args) {
        System.out.println("Programm started");
        if (args.length != 1) {
            System.out.println("Please give a port");
            return;
        }
        new Window(Integer.parseInt(args[0]));
    }
}