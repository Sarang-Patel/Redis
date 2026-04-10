package storage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStore {
    public static Map<String, String> store = new ConcurrentHashMap<>();
}
