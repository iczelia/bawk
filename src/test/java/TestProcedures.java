import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestProcedures {
    @Test
    public void testSimpleProcedure() {
        String program = """
                fn greet() { print("hello"); }
                greet();
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(1, lines.length);
        assertEquals("hello", lines[0]);
    }

    @Test
    public void testSimpleFunction() {
        String program = """
                fn add(a: i32, b: i32): i32 { a + b }
                print(add(2, 3));
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(1, lines.length);
        assertEquals("5", lines[0]);
    }

    @Test
    public void testArraySuccessor() {
        String program = """
                fn successor(arr: i32[]): i32[] {
                  let result: i32[] = i32[#arr];
                  for (let i: i32 = 0; i < #arr; i++) {
                    result[i] = arr[i] + 1;
                  }
                  result
                }
                let res: i32[] = successor((i32[]) { 1, 2, 3 });
                for (let i: i32 = 0; i < #res; i++) {
                  print(res[i]);
                }
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(3, lines.length);
        assertEquals("2", lines[0]);
        assertEquals("3", lines[1]);
        assertEquals("4", lines[2]);
    }

    @Test
    public void testArraySuccessorIfStmt() {
        String program = """
                fn successor(arr: i32[]): i32[] {
                  let result: i32[] = i32[#arr];
                  for (let i: i32 = 0; i < #arr; i++) {
                    result[i] = arr[i] + 1;
                  }
                  result
                }
                let res: i32[] = successor((i32[]) { 1, 2, 3 });
                for (let i: i32 = 0; i < #res; i++) {
                  if (res[i] == 3) {
                    print("three");
                  } else {
                    print(res[i]);
                  }
                }
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(3, lines.length);
        assertEquals("2", lines[0]);
        assertEquals("three", lines[1]);
        assertEquals("4", lines[2]);
    }

    @Test
    public void testArraySuccessorIfStmtInference() {
        String program = """
                fn successor(arr: i32[]): i32[] {
                  let result = i32[#arr];
                  for (let i = 0; i < #arr; i++)
                    result[i] = arr[i] + 1;
                  result
                }
                let res = successor((i32[]) { 1, 2, 3 });
                for (let i = 0; i < #res; i++) {
                  if (res[i] == 3)
                    print("three");
                  else
                    print(res[i]);
                }
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(3, lines.length);
        assertEquals("2", lines[0]);
        assertEquals("three", lines[1]);
        assertEquals("4", lines[2]);
    }

    @Test
    public void testFactorialCompoundAssign() {
        String program = """
                fn factorial(n: i32): i32 {
                  let result = 1;
                  for (let i = 2; i <= n; i++)
                    result *= i;
                  result
                }
                print(factorial(5));
                """;

        String output = CompilationTestPipeline.testProgram(program);
        String[] lines = output.split(System.lineSeparator());
        assertEquals(1, lines.length);
        assertEquals("120", lines[0]);
    }
}
