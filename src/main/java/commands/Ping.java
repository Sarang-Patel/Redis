package commands;

import java.io.PrintWriter;

import resp.RespFormatter;
import resp.RespValue;

public class Ping implements Command {
    @Override
    public String execute(RespValue input) {
        return RespFormatter.format(RespFormatter.Type.SIMPLE_STRING, "PONG");
    }
}
