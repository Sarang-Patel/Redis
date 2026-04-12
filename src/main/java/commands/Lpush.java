package commands;

import java.io.PrintWriter;
import java.util.ArrayList;

import resp.RespFormatter;
import resp.RespValue;
import storage.Data;
import storage.InMemoryStore;

public class Lpush implements Command{

    @Override
    public void execute(RespValue input, PrintWriter out) {
        if(input.getArray().size() < 3) {
            out.print(RespFormatter.format(RespFormatter.Type.ERROR, "ERR wrong number of arguments for 'set'"));
            out.flush();
            return;
        }

        String key = input.getArray().get(1).getString();

        Data existingList = InMemoryStore.store.get(key);

        if(existingList == null) {
            out.print(RespFormatter.format(RespFormatter.Type.ERROR, "ERR value doesn't exist"));
            out.flush();
            return;
        }

        if(existingList.getType() != Data.Type.LIST) {
            out.print(RespFormatter.format(RespFormatter.Type.ERROR, "WRONGTYPE Operation against a key holding the wrong kind of value"));
            out.flush();
            return;
        }


        for(int i = 2; i < input.getArray().size(); i++) {
            existingList.getList().addFirst(input.getArray().get(i).getString());
        }

        out.print(RespFormatter.format(RespFormatter.Type.INTEGER, existingList.getList().size()));
        out.flush();
    }

}
