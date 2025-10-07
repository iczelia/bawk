package net.iczelia.bawk;

public class Main {
    public static void main(String[] args) throws Exception {
        String program =
                """
                        let msg: str = "hello";
                        let n: i32 = 3;
                        let i: i32 = 0;
                        while (i < n) {
                          print(msg);
                          i = i + 1;
                        }
                        print("done");
                        """;

        var pipeline = new CompilationPipeline();
        var cls = pipeline.compileAndLoad(program, "MainClass");
        cls.getMethod("main", String[].class).invoke(null, (Object) new String[]{});
    }
}
