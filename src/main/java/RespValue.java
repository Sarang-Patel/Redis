import java.util.List;

public class RespValue {
    enum Type {INTEGER, ARRAY, STRING};

    Type type;
    String stringValue;
    List<RespValue> arrayValue;
    Integer intValue;

    public static RespValue ofString(String str) {
        RespValue respValue = new RespValue();
        respValue.type = Type.STRING;
        respValue.stringValue = str;
        return respValue;
    }

    public static RespValue ofInteger(Integer i) {
        RespValue respValue = new RespValue();
        respValue.type = Type.INTEGER;
        respValue.intValue = i;
        return respValue;
    }

    public static RespValue ofArray(List<RespValue> arr) {
        RespValue respValue = new RespValue();
        respValue.type = Type.ARRAY;
        respValue.arrayValue = arr;
        return respValue;
    }
}

