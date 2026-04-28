package commands;

import java.io.PrintWriter;
import java.util.ArrayList;

import resp.RespFormatter;
import resp.RespValue;
import storage.Data;
import storage.InMemoryStore;

public class Lpush implements Command{

    @Override
    public String execute(RespValue input) {
        if(input.getArray().size() < 3) {
            return (RespFormatter.format(RespFormatter.Type.ERROR, "ERR wrong number of arguments for 'lpush'"));
            
            
        }

        String key = input.getArray().get(1).getString();

        Data existingList = InMemoryStore.store.get(key);

        if(existingList == null) {
            existingList = Data.ofList(new ArrayList<>(), -1);
            InMemoryStore.store.put(key, existingList);
        }

        if(existingList.getType() != Data.Type.LIST) {
            return (RespFormatter.format(RespFormatter.Type.ERROR, "WRONGTYPE Operation against a key holding the wrong kind of value"));
            
            
        }


        for(int i = 2; i < input.getArray().size(); i++) {
            existingList.getList().add(0, input.getArray().get(i).getString());
        }

        return RespFormatter.format(RespFormatter.Type.INTEGER, existingList.getList().size());
    }

}
