package commands;

import java.io.PrintWriter;
import resp.RespValue;

public class CommandHandler {

    public void handle(RespValue parsedMessage, PrintWriter out) {
        String cmd = parsedMessage.getArray().get(0).getString().toUpperCase();
        System.out.println(cmd);

        Command command = CommandRegistry.get(cmd);

        if (command == null) {
            out.print("-Invalid Command\r\n");
            out.flush();
            return;
        }

        command.execute(parsedMessage, out);
    }
}