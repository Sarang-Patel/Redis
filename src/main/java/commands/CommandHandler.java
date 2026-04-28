package commands;

import java.io.PrintWriter;

import resp.RespFormatter;
import resp.RespValue;

public class CommandHandler {

    public static String handle(RespValue parsedMessage) {
        String cmd = parsedMessage.getArray().get(0).getString().toUpperCase();
        System.out.println(cmd);

        Command command = CommandRegistry.get(cmd);

        if (command == null) {
            return (RespFormatter.format(RespFormatter.Type.ERROR, "Invalid Command"));            
        }

        return command.execute(parsedMessage);
    }
}