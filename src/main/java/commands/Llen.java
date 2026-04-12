package commands;

import java.io.PrintWriter;

import resp.RespFormatter;
import resp.RespValue;
import storage.Data;
import storage.InMemoryStore;

public class Llen implements Command {

    @Override
    public void execute(RespValue input, PrintWriter out) {
        if(input.getArray().size() != 2) {
            out.print(RespFormatter.format(RespFormatter.Type.ERROR, "ERR wrong number of arguments for 'lpush'"));
            out.flush();
            return;
        }

        String key = input.getArray().get(1).getString();

        Data existingList = InMemoryStore.store.get(key);

        if(existingList == null) {
            out.print(RespFormatter.format(RespFormatter.Type.INTEGER, 0));
            out.flush();
            return;
        }

        if(existingList.getType() != Data.Type.LIST) {
            out.print(RespFormatter.format(RespFormatter.Type.ERROR, "WRONGTYPE Operation against a key holding the wrong kind of value"));
            out.flush();
            return;
        }

        out.print(RespFormatter.format(RespFormatter.Type.INTEGER, existingList.getList().size()));
        out.flush();

    }

}
