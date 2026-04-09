package commands;

import java.io.PrintWriter;

import resp.RespValue;

public class Echo implements Command {

    @Override
    public void execute(RespValue input, PrintWriter out) {
        String msg = input.getArray().get(1).getString();
        out.print("$" + msg.length() + "\r\n" + msg + "\r\n");
        out.flush();
    }
}
