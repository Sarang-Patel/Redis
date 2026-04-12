package commands;

import java.io.PrintWriter;
import java.util.*;

import resp.RespFormatter;
import resp.RespValue;
import storage.Data;
import storage.InMemoryStore;

public class Rpush implements Command {

    @Override
    public void execute(RespValue input, PrintWriter out) {

        if (input.getArray().size() < 3) {
            out.print(RespFormatter.format(RespFormatter.Type.ERROR,
                    "ERR wrong number of arguments for 'rpush'"));
            out.flush();
            return;
        }

        String key = input.getArray().get(1).getString();

        Data existing = InMemoryStore.store.get(key);
        List<String> list;

        if (existing != null) {

            if (existing.getType() != Data.Type.LIST) {
                out.print(RespFormatter.format(RespFormatter.Type.ERROR,
                        "WRONGTYPE Operation against a key holding the wrong kind of value"));
                out.flush();
                return;
            }

            list = existing.getList();

        } else {
            list = new ArrayList<>();
        }

        for (int i = 2; i < input.getArray().size(); i++) {
            list.add(input.getArray().get(i).getString());
        }

        Data valueObj = Data.ofList(list, -1);
        InMemoryStore.store.put(key, valueObj);

        out.print(RespFormatter.format(RespFormatter.Type.INTEGER, list.size()));
        out.flush();
    }
}