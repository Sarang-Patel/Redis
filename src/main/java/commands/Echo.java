package commands;

import java.io.PrintWriter;

import resp.RespFormatter;
import resp.RespValue;

public class Echo implements Command {

    @Override
    public String execute(RespValue input) {
        String msg = input.getArray().get(1).getString();
        return RespFormatter.format(RespFormatter.Type.BULK_STRING, msg);
    }
}
