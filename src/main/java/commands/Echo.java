package commands;

import java.io.PrintWriter;

import resp.RespFormatter;
import resp.RespValue;

public class Echo implements Command {

    @Override
    public void execute(RespValue input, PrintWriter out) {
        String msg = input.getArray().get(1).getString();
        out.print(RespFormatter.format(RespFormatter.Type.BULK_STRING, msg));
        out.flush();
    }
}
