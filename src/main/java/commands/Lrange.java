package commands;

import java.io.PrintWriter;
import java.util.*;

import resp.RespFormatter;
import resp.RespValue;
import storage.Data;
import storage.InMemoryStore;

public class Lrange implements Command {

    @Override
    public String execute(RespValue input) {
        if(input.getArray().size() != 4 ) {
            return (RespFormatter.format(RespFormatter.Type.ERROR, "ERR wrong number of arguments for 'LRANGE'"));
            
            
        }

        String key = input.getArray().get(1).getString();
        int start = Integer.parseInt(input.getArray().get(2).getString());
        int stop = Integer.parseInt(input.getArray().get(3).getString());

        Data existingList = InMemoryStore.store.get(key);

        if(existingList == null) {
            return (RespFormatter.format(RespFormatter.Type.ARRAY, new ArrayList<>()));
            
            
        }

        if(existingList.getType() != Data.Type.LIST) {
            return (RespFormatter.format(RespFormatter.Type.ERROR, "WRONGTYPE Operation against a key holding the wrong kind of value"));
            
            
        }

        int size = existingList.getList().size();

        if (start < 0) {
            start = size + start;
        }

        if (stop < 0) {
            stop = size + stop;
        }

        if (start < 0) start = 0;
        if (stop < 0) stop = 0;

        if (start >= size) {
            return (RespFormatter.format(RespFormatter.Type.ARRAY, new ArrayList<>()));
            
            
        }

        if (stop >= size) {
            stop = size - 1;
        }

        List<String> res = new ArrayList<>();

        for(int i = start; i <= stop; i++) {
            res.add(existingList.getList().get(i));
        }

        return RespFormatter.format(RespFormatter.Type.ARRAY, res);

    }

}
