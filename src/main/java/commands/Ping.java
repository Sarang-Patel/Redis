package commands;

import java.io.PrintWriter;

import resp.RespValue;

public class Ping implements Command {
    @Override
    public void execute(RespValue input, PrintWriter out) {
        out.print("+PONG" + "\r\n");
        out.flush();
    }
}
