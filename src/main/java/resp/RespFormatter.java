package resp;

import java.util.List;

public class RespFormatter {

    public enum Type {
        SIMPLE_STRING,
        BULK_STRING,
        ERROR,
        INTEGER,
        NULL_BULK_STRING,
        ARRAY
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

            case ARRAY:
                if (value == null) {
                    return "*-1\r\n";
                }

                List<?> list = (List<?>) value;
                StringBuilder sb = new StringBuilder();

                sb.append("*").append(list.size()).append("\r\n");

                for (Object item : list) {
                    sb.append(formatObject(item));
                }

                return sb.toString();

            default:
                throw new IllegalArgumentException("Unknown RESP type: " + type);
        }
    }

    private static String formatObject(Object obj) {
        if (obj == null) {
            return format(Type.NULL_BULK_STRING, null);
        } else if (obj instanceof String) {
            return format(Type.BULK_STRING, obj);
        } else if (obj instanceof Integer || obj instanceof Long) {
            return format(Type.INTEGER, obj);
        } else if (obj instanceof List) {
            return format(Type.ARRAY, obj);
        } else {
            return format(Type.BULK_STRING, obj.toString());
        }
    }
}