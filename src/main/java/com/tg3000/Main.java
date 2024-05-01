package com.tg3000;

import com.tg3000.frontend.Window;

class Main {
    public static void main(String[] args) {
        String host;
        System.out.println("Programm started");
        if (args.length < 1) {
            System.out.println("Please give a port");
            return;
        } else if(args.length < 2) {
            host = "localhost";
        } else {
            host = args[1];
        }
        new Window(Integer.parseInt(args[0]), host);
    }
}