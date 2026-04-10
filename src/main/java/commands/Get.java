package commands;

import java.io.PrintWriter;

import resp.RespFormatter;
import resp.RespValue;
import storage.InMemoryStore;

public class Get implements Command {

    @Override
    public void execute(RespValue input, PrintWriter out) {
        String value = InMemoryStore.store.get(input.getArray().get(1).getString());
        if(value != null) {
            out.print(RespFormatter.format(RespFormatter.Type.BULK_STRING, value));
            out.flush();
        }else {
            out.print(RespFormatter.format(RespFormatter.Type.NULL_BULK_STRING, null));
            out.flush();
        }
    }
    
}
