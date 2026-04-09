package server;
import java.io.*;
import java.net.*;

public class Server {
    int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);

            System.out.println("Server listening on Port: " + port);

            while(true) {
                Socket clientSocket = serverSocket.accept();

                System.out.println("Client Connected.");

                new ClientHandler(clientSocket).start();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
