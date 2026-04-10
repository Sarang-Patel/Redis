package commands;

import java.io.PrintWriter;

import resp.RespFormatter;
import resp.RespValue;
import storage.InMemoryStore;

public class Set implements Command {

    @Override
    public void execute(RespValue input, PrintWriter out) {
        InMemoryStore.store.put(input.getArray().get(1).getString(), input.getArray().get(2).getString());
        
        out.print(RespFormatter.format(RespFormatter.Type.SIMPLE_STRING, "OK"));
        out.flush();
        
    }
    
}
