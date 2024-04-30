package com.tg3000;

import com.tg3000.frontend.Window;

class Main {
    public static void main(String[] args) {
        System.out.println("Programm started");
        if (args.length < 1) {
            System.out.println("Please give a port");
            return;
        } else if(args.length < 2) {
            System.out.println("Please give a host (such as localhost or an ip address)");
            return;
        }
        new Window(Integer.parseInt(args[0]), args[1]);
    }
}