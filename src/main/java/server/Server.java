package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

import commands.CommandHandler;
import resp.Cursor;
import resp.RespValue;
import resp.Parser;

public class Server {

    private final int port;
    private Selector selector;
    private ServerSocketChannel serverChannel;

    // buffer per client
    private static final Map<SocketChannel, StringBuilder> buffers = new HashMap<>();

    public Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        selector = Selector.open();

        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);

        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server listening on port " + port);

        while (true) {
            selector.select();

            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();

                if (key.isAcceptable()) {
                    handleAccept();
                } else if (key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }

    private void handleAccept() throws IOException {
        SocketChannel client = serverChannel.accept();
        client.configureBlocking(false);

        client.register(selector, SelectionKey.OP_READ);
        buffers.put(client, new StringBuilder());

        System.out.println("Client connected");
    }

    private void handleRead(SelectionKey key) {

        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            int bytesRead = client.read(buffer);

            if (bytesRead == -1) {
                closeClient(client);
                return;
            }

            buffer.flip();
            String incoming = new String(buffer.array(), 0, bytesRead);

            StringBuilder clientBuffer = buffers.get(client);
            clientBuffer.append(incoming);

            // 🔥 process as many commands as possible
            while (true) {
                Cursor cursor = new Cursor();

                try {
                    RespValue parsed = Parser.parse(clientBuffer.toString(), cursor);

                    int consumed = cursor.index;

                    // execute command
                    String response = CommandHandler.handle(parsed);

                    // write response
                    write(client, response);

                    // remove processed part
                    clientBuffer.delete(0, consumed);

                } catch (Exception e) {
                    // incomplete command → wait for more data
                    break;
                }
            }

        } catch (IOException e) {
            closeClient(client);
        }
    }

    private void write(SocketChannel client, String response) throws IOException {
        ByteBuffer outBuffer = ByteBuffer.wrap(response.getBytes());
        client.write(outBuffer);
    }

    private void closeClient(SocketChannel client) {
        try {
            System.out.println("Client disconnected");
            buffers.remove(client);
            client.close();
        } catch (IOException ignored) {}
    }
}