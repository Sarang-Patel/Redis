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
                
        }else {
            //error - invalid symbol
            throw new IllegalArgumentException("invalid symbol: " + command.charAt(cursor.index));
        }
    }    

    

    // public static void main(String[] args) {
    //     String command = "*2\r\n$4\r\nECHO\r\n$3\r\nhey\r\n$0\r\n\r\n";
    //     Cursor cursor = new Cursor();
    //     RespValue parsedCmd = parse(command, cursor);
        
        // for(RespValue r: parsedCmd.getArray()) {
    //         if(r.getType() == RespValue.Type.NULL) {
    //             System.out.print("NULL -> ");
    //         }
    //         else if(r.getType() == RespValue.Type.STRING) {
    //             System.out.print(r.getString() + " -> ");
    //         }else if(r.getType() == RespValue.Type.INTEGER) {
    //             System.out.print(r.getInteger() + " -> ");

    //         } else {
    //             System.out.print(" [ ");
                
    //             for(RespValue t: r.getArray()) {
    //                 System.out.print(t + " : ");
    //             }

    //             System.out.print(" ] ");
    //         }
    //     }
    // } 
}



