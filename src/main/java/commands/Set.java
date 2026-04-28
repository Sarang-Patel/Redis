package commands;

import java.io.PrintWriter;

import resp.RespFormatter;
import resp.RespValue;
import storage.InMemoryStore;
import storage.Data;

public class Set implements Command {

    @Override
    public String execute(RespValue input) {

        if (input.getArray().size() < 3) {
            return (RespFormatter.format(RespFormatter.Type.ERROR, "ERR wrong number of arguments for 'set'"));
            
            
        }

        String key = input.getArray().get(1).getString();
        String val = input.getArray().get(2).getString();

        long expiryTime = -1;

        if (input.getArray().size() > 3) {

            if (input.getArray().size() < 5) {
                return (RespFormatter.format(RespFormatter.Type.ERROR, "ERR syntax error"));
                
                
            }

            String type = input.getArray().get(3).getString().toUpperCase();
            String timeStr = input.getArray().get(4).getString();

            long time;

            try {
                time = Long.parseLong(timeStr);
            } catch (NumberFormatException e) {
                return (RespFormatter.format(RespFormatter.Type.ERROR, "ERR invalid expire time"));
                
                
            }

            if (type.equals("EX")) {
                expiryTime = System.currentTimeMillis() + (time * 1000);
            } else if (type.equals("PX")) {
                expiryTime = System.currentTimeMillis() + time;
            } else {
                return (RespFormatter.format(RespFormatter.Type.ERROR, "ERR invalid option"));
                
                
            }
        }

        Data valueObj = Data.ofString(val, expiryTime);
        InMemoryStore.store.put(key, valueObj);

        return RespFormatter.format(RespFormatter.Type.SIMPLE_STRING, "OK");
    }
}