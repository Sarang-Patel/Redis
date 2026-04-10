package resp;
import java.util.*;

public class Parser {

    public static int getLen(String command, Cursor cursor) {
        StringBuilder sb = new StringBuilder();
        
        while(cursor.index < command.length() && command.charAt(cursor.index) != '\r') {
            if(!Character.isDigit(command.charAt(cursor.index)) && command.charAt(cursor.index) != '-') throw new RuntimeException("length parsing error!");

            sb.append(command.charAt(cursor.index));
            cursor.index++;
        }

        cursor.index++;
        if(cursor.index >= command.length() || command.charAt(cursor.index) != '\n') throw new IllegalArgumentException("CRLF separator not found!");
        cursor.index++;
        
        if(sb.length() == 0) throw new RuntimeException("length parsing error!");
        return (int) Integer.parseInt(sb.toString());
    }

    public static RespValue parse(String command, Cursor cursor) {
        List<RespValue> children = new ArrayList<>();

        if(cursor.index >= command.length()) throw new RuntimeException("Incomplete Command!");

        if(command.charAt(cursor.index) == '*') {
                cursor.index++;
                int noOfElements = getLen(command, cursor);

                while(noOfElements > 0) {
                    RespValue child = parse(command, cursor);
                    children.add(child);
                    noOfElements--;
                }
                return RespValue.ofArray(children);
                
        }else if(command.charAt(cursor.index) == '$') {
                cursor.index++;
                int noOfChars = getLen(command, cursor);
                
                if(noOfChars == -1) {
                    return RespValue.ofNull();
                }

                StringBuilder sb = new StringBuilder();
                int endOfWord = cursor.index + noOfChars;

                for(; cursor.index < endOfWord; cursor.index++) {
                    if(cursor.index < command.length())
                    sb.append(command.charAt(cursor.index));
                    else throw new RuntimeException("Incomplete command!");
                }

                if(cursor.index >= command.length() || command.charAt(cursor.index) != '\r') throw new IllegalArgumentException("CRLF separator not found!");
                cursor.index++;
                if(cursor.index >= command.length() || command.charAt(cursor.index) != '\n') throw new IllegalArgumentException("CRLF separator not found!");
                cursor.index++;

                return RespValue.ofString(sb.toString());
                
        }else if (command.charAt(cursor.index) == '+') {
            cursor.index++;

            StringBuilder sb = new StringBuilder();

            while (cursor.index < command.length()
                    && command.charAt(cursor.index) != '\r') {
                sb.append(command.charAt(cursor.index));
                cursor.index++;
            }

            if (cursor.index + 1 >= command.length()
                    || command.charAt(cursor.index) != '\r'
                    || command.charAt(cursor.index + 1) != '\n') {
                throw new IllegalArgumentException("CRLF separator not found!");
            }

            cursor.index += 2;

            return RespValue.ofString(sb.toString());
        }else if (command.charAt(cursor.index) == '-') {
            cursor.index++;

            StringBuilder sb = new StringBuilder();

            while (cursor.index < command.length()
                    && command.charAt(cursor.index) != '\r') {
                sb.append(command.charAt(cursor.index));
                cursor.index++;
            }

            if (cursor.index + 1 >= command.length()
                    || command.charAt(cursor.index) != '\r'
                    || command.charAt(cursor.index + 1) != '\n') {
                throw new IllegalArgumentException("CRLF separator not found!");
            }

            cursor.index += 2;

            return RespValue.ofString(sb.toString());

        }
        else {
            throw new IllegalArgumentException("invalid symbol: " + command.charAt(cursor.index));
        }
    }    
}



