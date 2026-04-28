package app;

import server.Server;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int port = 6379;
    
        try {
            Server server = new Server(port);
            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



