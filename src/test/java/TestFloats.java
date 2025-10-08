import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFloats {
    @Test
    public void testFloatEQSimple() {
        String program = """
                let a: f32 = 1.0;
                let b: f32 = 2.0;
                print(a == b);
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(1, lines.length);
        assertEquals("0", lines[0]);
    }

    @Test
    public void testFloatNEQSimple() {
        String program = """
                let a: f32 = 1.0;
                let b: f32 = 2.0;
                print(a != b);
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(1, lines.length);
        assertEquals("1", lines[0]);
    }
}
