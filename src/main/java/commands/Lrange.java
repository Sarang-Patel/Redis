package commands;

import java.io.PrintWriter;
import java.util.*;

import resp.RespFormatter;
import resp.RespValue;
import storage.Data;
import storage.InMemoryStore;

public class Lrange implements Command {

    @Override
    public void execute(RespValue input, PrintWriter out) {
        if(input.getArray().size() != 4 ) {
            out.print(RespFormatter.format(RespFormatter.Type.ERROR, "ERR wrong number of arguments for 'LRANGE'"));
            out.flush();
            return;
        }

        String key = input.getArray().get(1).getString();
        int start = Integer.parseInt(input.getArray().get(2).getString());
        int stop = Integer.parseInt(input.getArray().get(3).getString());
        
        System.out.println(start);
        System.out.println(stop);

        Data existingList = InMemoryStore.store.get(key);

        if(existingList == null) {
            out.print(RespFormatter.format(RespFormatter.Type.ARRAY, new ArrayList<>()));
            out.flush();
            return;
        }

        if(existingList.getType() != Data.Type.LIST) {
            out.print(RespFormatter.format(RespFormatter.Type.ERROR, "WRONGTYPE Operation against a key holding the wrong kind of value"));
            out.flush();
            return;
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
            out.print(RespFormatter.format(RespFormatter.Type.ARRAY, new ArrayList<>()));
            out.flush();
            return;
        }

        if (stop >= size) {
            stop = size - 1;
        }

        List<String> res = new ArrayList<>();

        for(int i = start; i <= stop; i++) {
            res.add(existingList.getList().get(i));
        }

        out.print(RespFormatter.format(RespFormatter.Type.ARRAY, res));
        out.flush();

    }

}
