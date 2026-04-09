package commands;

import java.util.Map;
import java.util.HashMap;

public class CommandRegistry {

    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("ECHO", new Echo());
        commands.put("PING", new Ping());
        // commands.put("SET", new SetCommand());
    }

    public static Command get(String name) {
        return commands.get(name);
    }
}