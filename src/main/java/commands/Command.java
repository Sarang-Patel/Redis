package commands;

import java.io.PrintWriter;

import resp.RespValue;

public interface Command {
    String execute(RespValue input);
}