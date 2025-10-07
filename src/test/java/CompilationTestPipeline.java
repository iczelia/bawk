import net.iczelia.bawk.CompilationPipeline;
import net.iczelia.bawk.ast.GrammarLexer;
import net.iczelia.bawk.ast.GrammarParser;
import net.iczelia.bawk.codegen.CodeGen;
import net.iczelia.bawk.ast.ASTBuilder;
import net.iczelia.bawk.ast.Program;
import net.iczelia.bawk.ast.TypeChecker;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.UUID;

public class CompilationTestPipeline {
    public static String testProgram(String program) {
        String className = "TestClass" + UUID.randomUUID().toString().replace("-", "");
        var pipeline = new CompilationPipeline();
        var cls = pipeline.compileAndLoad(program, className); // Just to ensure it compiles

        java.io.PrintStream originalOut = System.out;
        try {
            StringBuilder output = new StringBuilder();
            java.io.PrintStream ps = new java.io.PrintStream(new java.io.OutputStream() {
                @Override
                public void write(int b) {
                    output.append((char) b);
                }
            });
            System.setOut(ps);
            cls.getMethod("main", String[].class).invoke(null, (Object) new String[]{});
            System.setOut(originalOut);
            return output.toString();
        } catch (VerifyError e) {
            System.err.println("VerifyError: " + e.getMessage());
            System.setOut(originalOut);
            throw new RuntimeException("Bytecode verification failed: " + e.getMessage(), e);
        } catch (Exception e) {
            System.setOut(originalOut);
            throw new RuntimeException("Error during execution: " + e.getMessage(), e);
        }
    }
}
