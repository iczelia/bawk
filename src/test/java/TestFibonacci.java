import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFibonacci {
    @Test
    public void testRecursiveFibonacci() {
        String program = """
                fn fib(x: i32): i32 {
                    if (x < 2) x else fib(x - 1) + fib(x - 2)
                }
                print(fib(10));
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(1, lines.length);
        assertEquals("55", lines[0]);
    }

    @Test
    public void testIterativeFibonacci() {
        String program = """
                fn fib(x: i32): i32 {
                    let a: i32 = 0;
                    let b: i32 = 1;
                    let i: i32 = 0;
                    while (i < x) {
                        let temp: i32 = a;
                        a = b;
                        b = temp + b;
                        i = i + 1;
                    }
                    a
                }
                print(fib(10));
                """;
        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(1, lines.length);
        assertEquals("55", lines[0]);
    }

    @Test
    public void testTailRecursiveFibonacci() {
        String program = """
                fn fib_helper(x: i32, a: i32, b: i32): i32 {
                    if (x == 0) a else fib_helper(x - 1, b, a + b)
                }
                fn fib(x: i32): i32 {
                    fib_helper(x, 0, 1)
                }
                print(fib(10));
                """;
        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(1, lines.length);
        assertEquals("55", lines[0]);
    }
}
