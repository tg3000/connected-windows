package com.tg3000;

import com.tg3000.frontend.*;

class Main {
    public static void main(String[] args) {
        String host;
        int port;
        System.out.println("Programm started");
        if (args.length < 1) {
            port = 1234;
        } else {
            port = Integer.parseInt(args[0]);
        }

        if(args.length < 2) {
            host = "localhost";
        } else {
            host = args[1];
        }
        
        new Frame(port, host);
    }
}