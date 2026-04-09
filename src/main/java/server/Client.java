package server;

import java.io.*;
import java.net.*;

import resp.Cursor;
import resp.Parser;
import resp.RespValue;

public class Client {

    private Socket socket = null;
    private OutputStream out = null;

    int port;
    String addr;

    public Client(String addr, int port) {
        this.addr = addr;
        this.port = port;
    }

    private void printResp(RespValue resp) {
        switch (resp.getType()) {
            case STRING:
                System.out.println(resp.getString());
                break;
            case INTEGER:
                System.out.println(resp.getInteger());
                break;
            case ARRAY:
                for (RespValue val : resp.getArray()) {
                    printResp(val);
                }
                break;
            case NULL:
                System.out.println("(nil)");
                break;
        }
    }

    private void start() {
        try {
            socket = new Socket(addr, port);
            System.out.println("Client started!");

            InputStream in = socket.getInputStream();
            out = socket.getOutputStream();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in)
            );

            StringBuilder buffer = new StringBuilder();
            byte[] temp = new byte[1024];

            while (true) {
                System.out.print("> ");
                String message = reader.readLine();

                // Build RESP request
                String[] parts = message.split(" ");
                StringBuilder resp = new StringBuilder();

                resp.append("*").append(parts.length).append("\r\n");

                for (String part : parts) {
                    resp.append("$").append(part.length()).append("\r\n");
                    resp.append(part).append("\r\n");
                }

                // Send request
                out.write(resp.toString().getBytes());
                out.flush();

                // Read response
                while (true) {
                    int len = in.read(temp);
                    if (len == -1) {
                        System.out.println("Server closed connection");
                        return;
                    }

                    buffer.append(new String(temp, 0, len));

                    // Try parsing as many responses as possible
                    while (true) {
                        try {
                            Cursor cursor = new Cursor();
                            RespValue res = Parser.parse(buffer.toString(), cursor);

                            // Remove consumed data
                            buffer.delete(0, cursor.index);

                            // Print response
                            printResp(res);

                            // break after one response (request-response style)
                            break;

                        } catch (Exception e) {
                            // Not enough data yet
                            break;
                        }
                    }

                    // if we printed something, move to next command
                    if (buffer.length() == 0) break;
                }
            }

        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 6379);
        client.start();
    }
}