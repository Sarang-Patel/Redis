package commands;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import resp.RespFormatter;
import resp.RespValue;
import storage.Data;
import storage.InMemoryStore;

public class Lpop implements Command {

	@Override
	public void execute(RespValue input, PrintWriter out) {
        int inSize = input.getArray().size();

		if(inSize < 2 || inSize > 3 ) {
            out.print(RespFormatter.format(RespFormatter.Type.ERROR, "ERR wrong number of arguments for 'lpush'"));
            out.flush();
            return;
        }

        int toRemove = 1;

        if(inSize == 3) toRemove = Integer.parseInt(input.getArray().get(2).getString());

        if(toRemove > inSize) toRemove = inSize;

        String key = input.getArray().get(1).getString();

        Data existingList = InMemoryStore.store.get(key);

        if(existingList == null || existingList.getType() == Data.Type.LIST && existingList.getList().size() == 0) {
            out.print(RespFormatter.format(RespFormatter.Type.NULL_BULK_STRING, null));
            out.flush();
            return;
        }

         if(existingList.getType() != Data.Type.LIST) {
            out.print(RespFormatter.format(RespFormatter.Type.ERROR, "WRONGTYPE Operation against a key holding the wrong kind of value"));
            out.flush();
            return;
        }

        if(toRemove == 1) {
            out.print(RespFormatter.format(RespFormatter.Type.BULK_STRING, existingList.getList().remove(0)));
        }else {
            List<String> res = new ArrayList<>();
            for(int i = 0; i < toRemove; i++) {
                res.add(existingList.getList().remove(0));
            }
            out.print(RespFormatter.format(RespFormatter.Type.ARRAY, res));
        }

        out.flush();

	}

}
