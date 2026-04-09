import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    @Test
    void testSimpleEcho() {
        String command = "*2\r\n$4\r\nECHO\r\n$3\r\nhey\r\n";
        Cursor cursor = new Cursor();

        RespValue result = Parser.parse(command, cursor);

        assertEquals(RespValue.Type.ARRAY, result.getType());
        assertEquals(2, result.getArray().size());

        assertEquals("ECHO", result.getArray().get(0).getString());
        assertEquals("hey", result.getArray().get(1).getString());
    }

    @Test
    void testEmptyBulkString() {
        String command = "*1\r\n$0\r\n\r\n";
        Cursor cursor = new Cursor();

        RespValue result = Parser.parse(command, cursor);

        assertEquals(RespValue.Type.ARRAY, result.getType());
        assertEquals(1, result.getArray().size());
        assertEquals("", result.getArray().get(0).getString());
    }   

    @Test
    void testNullBulkString() {
        String command = "*1\r\n$-1\r\n";
        Cursor cursor = new Cursor();

        RespValue result = Parser.parse(command, cursor);

        assertEquals(RespValue.Type.ARRAY, result.getType());
        assertEquals(1, result.getArray().size());
        assertEquals(RespValue.Type.NULL, result.getArray().get(0).getType());
    }

    @Test
    void testNestedArray() {
        String command = "*1\r\n*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n";
        Cursor cursor = new Cursor();

        RespValue result = Parser.parse(command, cursor);

        assertEquals(RespValue.Type.ARRAY, result.getType());
        assertEquals(1, result.getArray().size());

        RespValue nested = result.getArray().get(0);
        assertEquals(RespValue.Type.ARRAY, nested.getType());
        assertEquals(2, nested.getArray().size());

        assertEquals("foo", nested.getArray().get(0).getString());
        assertEquals("bar", nested.getArray().get(1).getString());
    }

    @Test
    void testInvalidInput() {
        String command = "*1\r\n$3\r\nab"; // incomplete
        Cursor cursor = new Cursor();

        assertThrows(RuntimeException.class, () -> {
            Parser.parse(command, cursor);
        });
    }
}
