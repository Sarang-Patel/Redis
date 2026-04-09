package commands;

import java.io.PrintWriter;
import resp.RespValue;

public class CommandHandler {

    public void handle(RespValue parsedMessage, PrintWriter out) {
        String cmd = parsedMessage.getArray().get(0).getString().toUpperCase();
        System.out.println(cmd);

        Command command;

        switch (cmd) {
            case "ECHO":
                command = new Echo();
                break;
            case "PING":
                command = new Ping();
                break;
            default:
                out.print("-Invalid Message\r\n");
                out.flush();
                return;
        }

        command.execute(parsedMessage, out);
    }
}