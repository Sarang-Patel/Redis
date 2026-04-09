import java.util.List;

public class RespValue {
    enum Type {INTEGER, ARRAY, STRING, NULL};

    private Type type;
    private String stringValue;
    private List<RespValue> arrayValue;
    private Integer intValue;

    public Type getType() {
        return this.type;
    }

    private RespValue(Type type, String str, Integer i, List<RespValue> arr) {
        this.type = type;
        this.stringValue = str;
        this.intValue = i;
        this.arrayValue = arr;

        int count = 0;
        if (str != null) count++;
        if (i != null) count++;
        if (arr != null) count++;

        if (type == Type.NULL) {
            if (count != 0) {
                throw new IllegalStateException("NULL type must not have a value");
            }
        } else {
            if (count != 1) {
                throw new IllegalStateException("Exactly one value must be set");
            }
        }
    }

    public static RespValue ofString(String str) {
        return new RespValue(Type.STRING, str, null, null);
    }

    public static RespValue ofInteger(Integer i) {
        return new RespValue(Type.INTEGER, null, i, null);
    }

    public static RespValue ofArray(List<RespValue> arr) {
        return new RespValue(Type.ARRAY, null, null, arr);
    }

    public static RespValue ofNull() {
        return new RespValue(Type.NULL, null, null, null);
    }

    public String getString() {
        if (type != Type.STRING) {
            throw new IllegalStateException("Not a STRING");
        }
        return stringValue;
    }

    public Integer getInteger() {
        if (type != Type.INTEGER) {
            throw new IllegalStateException("Not an INTEGER");
        }
        return intValue;
    }

    public List<RespValue> getArray() {
        if (type != Type.ARRAY) {
            throw new IllegalStateException("Not an ARRAY");
        }

        return arrayValue;
    }
}

