import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestArrayBasic {
    @Test
    public void testArraySimple() {
        String program = """
                let array: i32[];
                array = i32[6];
                array[0] = 23;
                print(array[0]);
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(1, lines.length);
        assertEquals("23", lines[0]);
    }

    @Test
    public void testArrayWithLoop() {
        String program = """
                let array: i32[];
                array = i32[3];
                array[0] = 10;
                array[1] = 20;
                array[2] = 30;
                let i: i32 = 0;
                while (i < 3) {
                    print(array[i]);
                    i = i + 1;
                }
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(3, lines.length);
        assertEquals("10", lines[0]);
        assertEquals("20", lines[1]);
        assertEquals("30", lines[2]);
    }

    @Test
    public void testArrayNested() {
        String program = """
                let array: i32[][];
                array = i32[][2];
                array[0] = i32[2];
                array[1] = i32[2];
                array[0][0] = 10;
                array[0][1] = 20;
                array[1][0] = 30;
                array[1][1] = 40;
                let i: i32 = 0;
                while (i < 2) {
                    let j: i32 = 0;
                    while (j < 2) {
                        print(array[i][j]);
                        j = j + 1;
                    }
                    i = i + 1;
                }
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(4, lines.length);
        assertEquals("10", lines[0]);
        assertEquals("20", lines[1]);
        assertEquals("30", lines[2]);
        assertEquals("40", lines[3]);
    }

    @Test
    public void testArrayNestedInitializer() {
        String program = """
                let array: i32[][] = i32[][2];
                array[0] = { 10, 20 };
                array[1] = { 30, 40 };
                let i: i32 = 0;
                while (i < 2) {
                    let j: i32 = 0;
                    while (j < 2) {
                        print(array[i][j]);
                        j = j + 1;
                    }
                    i = i + 1;
                }
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(4, lines.length);
        assertEquals("10", lines[0]);
        assertEquals("20", lines[1]);
        assertEquals("30", lines[2]);
        assertEquals("40", lines[3]);
    }

    @Test
    public void testArrayNestedInitializer2() {
        String program = """
                let array: i32[][] = { { 10, 20 }, { 30, 40 } };
                for (let i: i32 = 0; i < 2; i++) {
                    for (let j: i32 = 0; j < 2; j++) {
                        print(array[i][j]);
                    }
                }
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(4, lines.length);
        assertEquals("10", lines[0]);
        assertEquals("20", lines[1]);
        assertEquals("30", lines[2]);
        assertEquals("40", lines[3]);
    }
}
