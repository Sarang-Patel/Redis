package commands;

import java.io.PrintWriter;

import resp.RespFormatter;
import resp.RespValue;
import storage.InMemoryStore;
import storage.Data;

public class Set implements Command {

    @Override
    public void execute(RespValue input, PrintWriter out) {

        if (input.getArray().size() < 3) {
            out.print(RespFormatter.format(RespFormatter.Type.ERROR, "ERR wrong number of arguments for 'set'"));
            out.flush();
            return;
        }

        String key = input.getArray().get(1).getString();
        String val = input.getArray().get(2).getString();

        long expiryTime = -1;

        if (input.getArray().size() > 3) {

            if (input.getArray().size() < 5) {
                out.print(RespFormatter.format(RespFormatter.Type.ERROR, "ERR syntax error"));
                out.flush();
                return;
            }

            String type = input.getArray().get(3).getString().toUpperCase();
            String timeStr = input.getArray().get(4).getString();

            long time;

            try {
                time = Long.parseLong(timeStr);
            } catch (NumberFormatException e) {
                out.print(RespFormatter.format(RespFormatter.Type.ERROR, "ERR invalid expire time"));
                out.flush();
                return;
            }

            if (type.equals("EX")) {
                expiryTime = System.currentTimeMillis() + (time * 1000);
            } else if (type.equals("PX")) {
                expiryTime = System.currentTimeMillis() + time;
            } else {
                out.print(RespFormatter.format(RespFormatter.Type.ERROR, "ERR invalid option"));
                out.flush();
                return;
            }
        }

        Data valueObj = Data.ofString(val, expiryTime);
        InMemoryStore.store.put(key, valueObj);

        out.print(RespFormatter.format(RespFormatter.Type.SIMPLE_STRING, "OK"));
        out.flush();
    }
}