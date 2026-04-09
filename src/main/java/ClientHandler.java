import java.io.*;
import java.net.*;
import java.util.*;


public class ClientHandler extends Thread {
    Socket socket = null;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }


    Set<String> commands = Set.of("ECHO");

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        )) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            while(true) {
                String message = in.readLine();
                if (message == null) break;
                Cursor cursor = new Cursor();

                RespValue parsedMessage = Parser.parse(message, cursor);

                if(parsedMessage.getType() != RespValue.Type.ARRAY || parsedMessage.getArray().size() == 0) throw new RuntimeException("Message parsing error!");
                String cmd = parsedMessage.getArray().get(0).getString().toUpperCase();

                if(commands.contains(cmd))
                switch(cmd) {
                    case "ECHO":
                        System.out.println(parsedMessage.getArray().get(0).getString());
                        break;
                        default: break;
                    }
                    else {
                        // return invalid command error
                        System.out.println("asdas");

                }

                System.out.println("Received message: " + message);
                // if(message.equals("PING")) {
                //     out.print("+PONG\r\n");
                //     out.flush();
                // }else{
                //     out.print("-ERR\r\n");
                //     out.flush();
                // }
            }

        }catch(IOException e) {
            System.out.println("socket disconnected");
        }
    }
}
