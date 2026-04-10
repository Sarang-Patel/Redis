package resp;

public class RespFormatter {

    public enum Type {
        SIMPLE_STRING,
        BULK_STRING,
        ERROR,
        INTEGER,
        NULL_BULK_STRING
    }

    public static String format(Type type, Object value) {
        switch (type) {

            case SIMPLE_STRING:
                return "+" + value + "\r\n";

            case ERROR:
                return "-" + value + "\r\n";

            case INTEGER:
                return ":" + value + "\r\n";

            case BULK_STRING:
                String str = String.valueOf(value);
                return "$" + str.length() + "\r\n" + str + "\r\n";

            case NULL_BULK_STRING:
                return "$-1\r\n";

            default:
                throw new IllegalArgumentException("Unknown RESP type: " + type);
        }
    }
}