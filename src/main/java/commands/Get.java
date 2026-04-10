package commands;

import java.io.PrintWriter;

import resp.RespFormatter;
import resp.RespValue;
import storage.Data;
import storage.InMemoryStore;

public class Get implements Command {

    @Override
    public void execute(RespValue input, PrintWriter out) {
        Data data = InMemoryStore.store.get(input.getArray().get(1).getString());
        
        if(data != null) {
            
            if (data.getExpiryTime() == -1 || System.currentTimeMillis() <= data.getExpiryTime()) {
                out.print(RespFormatter.format(RespFormatter.Type.BULK_STRING, data.getData()));
                out.flush();
                return;
            }
            InMemoryStore.store.remove(input.getArray().get(1).getString());
            
        }
        
        out.print(RespFormatter.format(RespFormatter.Type.NULL_BULK_STRING, null));
        out.flush();
        
    }
    
}
