import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    Socket socket = null;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        )) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            while(true) {
                String message = in.readLine();
                if (message == null) break;

                System.out.println(message);
                if(message.equals("PING")) {
                    out.print("+PONG\r\n");
                    out.flush();
                }
            }

        }catch(IOException e) {
            System.out.println("socket disconnected");
        }
    }
}
