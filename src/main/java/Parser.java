import java.util.*;

class Cursor {
    int index;

    public Cursor(){
        this.index = 0;
    }
}

// TODO: write tests
// TODO: fix boundary conditions

public class Parser {

    public static int count(String command, Cursor cursor) {
        StringBuilder sb = new StringBuilder();
        
        while(command.charAt(cursor.index) != '\r') {
            if(!Character.isDigit(command.charAt(cursor.index))) throw new Error("length parsing error!");

            sb.append(command.charAt(cursor.index));
            cursor.index++;
        }

        cursor.index += 2;

        if(sb.length() == 0) throw new Error("length parsing error!");

        return (int) Integer.parseInt(sb.toString());
    }

    public static RespValue parse(String command, Cursor cursor) {
        List<RespValue> children = new ArrayList<>();
        
        if(command.charAt(cursor.index) == '*') {
                cursor.index++;
                int noOfElements = count(command, cursor);
                
                while(noOfElements > 0) {
                    RespValue child = parse(command, cursor);
                    children.add(child);
                    noOfElements--;
                }
                return RespValue.ofArray(children);
                
        }else if(command.charAt(cursor.index) == '$') {
                cursor.index++;
                int noOfChars = count(command, cursor);
                
                StringBuilder sb = new StringBuilder();
                int endOfWord = cursor.index + noOfChars;

                for(; cursor.index < endOfWord; cursor.index++) {
                    sb.append(command.charAt(cursor.index));
                }

                cursor.index+=2; // skip \r\n;

                return RespValue.ofString(sb.toString());
                
        }else {
            //error - invalid symbol
            throw new Error("invalid symbol: " + command.charAt(cursor.index));
        }
    }    

    

    public static void main(String[] args) {
        String command = "*2\r\n$4\r\nECHO\r\n$3\r\nhey\r\n";
        Cursor cursor = new Cursor();
        RespValue parsedCmd = parse(command, cursor);
        
        for(RespValue r: parsedCmd.arrayValue) {
            if(r.type == RespValue.Type.STRING) {
                System.out.print(r.stringValue + " -> ");
            }else if(r.type == RespValue.Type.INTEGER) {
                System.out.print(r.intValue + " -> ");

            } else {
                System.out.print(" [ ");
                
                for(RespValue t: r.arrayValue) {
                    System.out.print(t + " : ");
                    
                }
                System.out.print(" ] ");
            }
        }
    } 
}



