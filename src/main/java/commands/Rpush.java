package commands;

import java.io.PrintWriter;
import java.util.*;

import resp.RespFormatter;
import resp.RespValue;
import storage.Data;
import storage.InMemoryStore;

public class Rpush implements Command {

    @Override
    public String execute(RespValue input) {

        if (input.getArray().size() < 3) {
            return (RespFormatter.format(RespFormatter.Type.ERROR,
                    "ERR wrong number of arguments for 'rpush'"));
            
            
        }

        String key = input.getArray().get(1).getString();

        Data existing = InMemoryStore.store.get(key);
        List<String> list;
        long expiryTime = -1;
        if (existing != null) {

            if (existing.getType() != Data.Type.LIST) {
                return (RespFormatter.format(RespFormatter.Type.ERROR,
                        "WRONGTYPE Operation against a key holding the wrong kind of value"));
                
                
            }

            list = existing.getList();
            expiryTime = existing.getExpiryTime();

        } else {
            list = new ArrayList<>();
        }

        for (int i = 2; i < input.getArray().size(); i++) {
            list.add(input.getArray().get(i).getString());
        }

        Data valueObj = Data.ofList(list, expiryTime);
        InMemoryStore.store.put(key, valueObj);

        return RespFormatter.format(RespFormatter.Type.INTEGER, list.size());
        
    }
}