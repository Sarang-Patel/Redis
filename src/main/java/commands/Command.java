package commands;

import java.io.PrintWriter;

import resp.RespValue;

public interface Command {
    void execute(RespValue input, PrintWriter out);
}