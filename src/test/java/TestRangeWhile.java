import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRangeWhile {
    @Test
    public void testRangeWhileLoop() {
        String program = """
                let msg: str = "hello";
                let n: i32 = 3;
                let i: i32 = 0;
                while (i < n) {
                    print(msg);
                    i = i + 1;
                }
                print("done");
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(4, lines.length);
        assertEquals("hello", lines[0]);
        assertEquals("hello", lines[1]);
        assertEquals("hello", lines[2]);
        assertEquals("done", lines[3]);
    }

    @Test
    public void testRangeWhileLoopInference() {
        String program = """
                let msg = "hello";
                let n = 3;
                let i = 0;
                while (i < n) {
                    print(msg);
                    i = i + 1;
                }
                print("done");
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(4, lines.length);
        assertEquals("hello", lines[0]);
        assertEquals("hello", lines[1]);
        assertEquals("hello", lines[2]);
        assertEquals("done", lines[3]);
    }

    @Test
    public void testRangeWhileLoopMultiInit() {
        String program = """
                let msg = "hello";
                let n: i32;
                let i: i32;
                n = i = 1;
                i--;
                n++;
                n++;
                while (i < n) {
                    print(msg);
                    i = i + 1;
                }
                print("done");
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(4, lines.length);
        assertEquals("hello", lines[0]);
        assertEquals("hello", lines[1]);
        assertEquals("hello", lines[2]);
        assertEquals("done", lines[3]);
    }

    @Test
    public void testRangeWhileLoopIncDec() {
        String program = """
                let msg = "hello";
                let n: i32 = 3;
                while (n--) {
                    print(msg);
                }
                print("done");
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(4, lines.length);
        assertEquals("hello", lines[0]);
        assertEquals("hello", lines[1]);
        assertEquals("hello", lines[2]);
        assertEquals("done", lines[3]);
    }

    @Test
    public void testRangeWhileLoopStmtExpr() {
        String program = """
                let msg = "hello";
                let n: i32 = 3;
                while (n--)
                    print(msg);
                print("done");
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(4, lines.length);
        assertEquals("hello", lines[0]);
        assertEquals("hello", lines[1]);
        assertEquals("hello", lines[2]);
        assertEquals("done", lines[3]);
    }

    @Test
    public void testRangeWhileLoopCountdown() {
        String program = """
                let n: i32 = 3;
                while (n--)
                    print(n);
                print("done");
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(4, lines.length);
        assertEquals("2", lines[0]);
        assertEquals("1", lines[1]);
        assertEquals("0", lines[2]);
        assertEquals("done", lines[3]);
    }

    @Test
    public void testRangeForLoop() {
        String program = """
                for (let n: i32 = 3; n; n--)
                    print(n);
                print("done");
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(4, lines.length);
        assertEquals("3", lines[0]);
        assertEquals("2", lines[1]);
        assertEquals("1", lines[2]);
        assertEquals("done", lines[3]);
    }

    @Test
    public void testRangeForLoop2() {
        String program = """
                let n: i32 = 3; for (; n; n--)
                    print(n);
                print("done");
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(4, lines.length);
        assertEquals("3", lines[0]);
        assertEquals("2", lines[1]);
        assertEquals("1", lines[2]);
        assertEquals("done", lines[3]);
    }

    @Test
    public void testRangeForLoop3() {
        String program = """
                let n: i32 = 3; for (; n--; )
                    print(n);
                print("done");
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(4, lines.length);
        assertEquals("2", lines[0]);
        assertEquals("1", lines[1]);
        assertEquals("0", lines[2]);
        assertEquals("done", lines[3]);
    }

}
