package commands;

import java.io.PrintWriter;

import resp.RespFormatter;
import resp.RespValue;

public class Ping implements Command {
    @Override
    public void execute(RespValue input, PrintWriter out) {
        out.print(RespFormatter.format(RespFormatter.Type.SIMPLE_STRING, "PONG"));
        out.flush();
    }
}
