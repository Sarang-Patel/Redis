package commands;

import java.io.PrintWriter;

import resp.RespFormatter;
import resp.RespValue;
import storage.Data;
import storage.InMemoryStore;

public class Get implements Command {

    @Override
    public String execute(RespValue input) {
        Data data = InMemoryStore.store.get(input.getArray().get(1).getString());
        
        if(data != null) {
            
            if (data.getExpiryTime() == -1 || System.currentTimeMillis() <= data.getExpiryTime()) {
                return (RespFormatter.format(RespFormatter.Type.BULK_STRING, data.getString()));
                
                
            }
            InMemoryStore.store.remove(input.getArray().get(1).getString());
            
        }
        
        return (RespFormatter.format(RespFormatter.Type.NULL_BULK_STRING, null));
        
        
    }
    
}
