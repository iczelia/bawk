import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestTailRecursion {
    @Test
    public void testSimpleTailSum() {
        String program = """
                fn sum(n: i32, acc: i32): i32 { if (n != 0) sum(n - 1, acc + n) else acc }
                print(sum(10, 0));
                """;
        String output = CompilationTestPipeline.testProgram(program).trim();
        assertEquals("55", output);
    }

    @Test
    public void testLargerTailSum() {
        int N = 50000; // recursion this deep would risk stack overflow without TCO
        long expected = (long)N * (N + 1) / 2;
        String program = "fn sum(n: i32, acc: i32): i32 { if (n != 0) sum(n - 1, acc + n) else acc }\n" +
                "print(sum(" + N + ", 0));";
        String output = CompilationTestPipeline.testProgram(program).trim();
        assertEquals(String.valueOf(expected), output);
    }

    @Test
    public void testNonTailRecursionNotTransformed() {
        // bad adds +1 after recursive call, so no tail transformation should occur.
        String program = """
                fn bad(n: i32): i32 { if (n != 0) bad(n - 1) + 1 else 0 }
                print(bad(5));
                """;
        // Value: number of calls = n, result becomes n because chain of +1s.
        String output = CompilationTestPipeline.testProgram(program).trim();
        assertEquals("5", output);
    }

    @Test
    public void testParallelMoves() {
        // Check if the compiler can handle parallel moves in TCO.
        String program = """
                fn swap(n: i32, a: i32, b: i32): i32 {
                    if (n == 0) a else swap(n - 1, b, a)
                }
                let x = swap(10000, 1, 2);
                print(x);
                """;
        String output = CompilationTestPipeline.testProgram(program).trim();
        // After an even number of swaps, values should be in original positions.
        assertEquals("1", output);
    }

    @Test
    public void testRecursiveProducts() {
        String program = """
                fn product1(x: i32, a: i32): i32 {
                    if (x > 1) product1(x - 1, a) - a else a
                }
                
                fn product_tail2(x: i32, a: i32, acc: i32): i32 {
                    if (x > 1) product_tail2(x - 1, a, acc - a) else acc
                }
                fn product2(x: i32, a: i32): i32 {
                    product_tail2(x, a, a)
                }
                
                print(product1(5, 3));
                print(product2(5, 3));
                
                fn product3(x: i32, a: i32): i32 {
                    if (x > 1) product3(x - 1, a) + a else a
                }
                
                fn product_tail4(x: i32, a: i32, acc: i32): i32 {
                    if (x > 1) product_tail4(x - 1, a, acc + a) else acc
                }
                fn product4(x: i32, a: i32): i32 {
                    product_tail4(x, a, a)
                }
                
                print(product3(5, 3));
                print(product4(5, 3));
                """;
        String output = CompilationTestPipeline.testProgram(program).trim();
        assertEquals("-9\n-9\n15\n15", output);
    }
}
