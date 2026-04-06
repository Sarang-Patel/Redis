import java.io.*;
import java.net.*;

public class Client {

    private Socket s = null;
    private PrintWriter out = null;
    int port;
    String addr;


    public Client(String addr, int port) {
        this.addr = addr;
        this.port = port;
    }

    private void start() {
        
        try {
            s = new Socket(addr, port);
            if(s != null) {
                System.out.println("Client started!");
            }
            BufferedReader serverIn = new BufferedReader(
                new InputStreamReader(s.getInputStream())
            );
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(s.getOutputStream(), true);
            String message = "";

            while(true) {
                try{
                    System.out.print("> ");
                    message = reader.readLine();
                    
                    out.println(message);

                    String response = serverIn.readLine();
                    System.out.println(response);
                }catch (IOException i) {
                System.out.println(i);
            }
            }

        }catch (UnknownHostException u) {
            System.out.println(u);
            return;
        }
        catch (IOException i) {
            System.out.println(i);
            return;
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 6379);
        client.start(); 
    }
}
