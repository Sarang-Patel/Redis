package storage;

import java.util.List;

public class Data {

    public enum Type {
        STRING,
        LIST
    }

    private Type type;
    private String stringValue;
    private List<String> listValue;
    private long expiryTime;

    private Data(Type type, String str, List<String> list, long expiryTime) {
        this.type = type;
        this.stringValue = str;
        this.listValue = list;
        this.expiryTime = expiryTime;
    }

    public static Data ofString(String val, long expiryTime) {
        return new Data(Type.STRING, val, null, expiryTime);
    }

    public static Data ofList(List<String> list, long expiryTime) {
        return new Data(Type.LIST, null, list, expiryTime);
    }

    public Type getType() {
        return type;
    }

    public String getString() {
        if (type != Type.STRING) throw new IllegalStateException("Not a string");
        return stringValue;
    }

    public List<String> getList() {
        if (type != Type.LIST) throw new IllegalStateException("Not a list");
        return listValue;
    }

    public long getExpiryTime() {
        return expiryTime;
    }
}