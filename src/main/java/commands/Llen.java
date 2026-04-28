package commands;

import java.io.PrintWriter;

import resp.RespFormatter;
import resp.RespValue;
import storage.Data;
import storage.InMemoryStore;

public class Llen implements Command {

    @Override
    public String execute(RespValue input) {
        if(input.getArray().size() != 2) {
            return (RespFormatter.format(RespFormatter.Type.ERROR, "ERR wrong number of arguments for 'lpush'"));
            
            
        }

        String key = input.getArray().get(1).getString();

        Data existingList = InMemoryStore.store.get(key);

        if(existingList == null) {
            return (RespFormatter.format(RespFormatter.Type.INTEGER, 0));
            
            
        }

        if(existingList.getType() != Data.Type.LIST) {
            return (RespFormatter.format(RespFormatter.Type.ERROR, "WRONGTYPE Operation against a key holding the wrong kind of value"));
            
            
        }

        return (RespFormatter.format(RespFormatter.Type.INTEGER, existingList.getList().size()));
        

    }

}
