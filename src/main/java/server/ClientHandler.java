package server;
import java.io.*;
import java.net.*;

import commands.CommandHandler;
import resp.Cursor;
import resp.Parser;
import resp.RespValue;


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
            StringBuilder buffer = new StringBuilder();

            char[] temp = new char[1024];
            CommandHandler handler = new CommandHandler();
            while(true) {
                int len = in.read(temp);

                if(len == -1) break;
                buffer.append(temp, 0, len);

                while(true) {
                    Cursor cursor = new Cursor();

                    try {
                        RespValue parsedMessage = Parser.parse(buffer.toString(), cursor);
                        int consumed = cursor.index;

                        if(parsedMessage.getType() != RespValue.Type.ARRAY || parsedMessage.getArray().size() == 0) throw new RuntimeException("Message parsing error!");
                        
                        System.out.println(handler.handle(parsedMessage));

                        buffer.delete(0, consumed);
                    } catch (Exception e) {
                        break;
                    }

                    
                }
            }

        }catch(IOException e) {
            System.out.println("socket disconnected");
        }
    }

    
}
