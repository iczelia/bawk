package net.iczelia.bawk;

import net.iczelia.bawk.ast.*;
import net.iczelia.bawk.codegen.CodeGen;
import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRLowering;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.hir.stmt.HProgram;
import net.iczelia.bawk.hir.transform.TailCallChildTransformer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class CompilationPipeline {
    public Class<?> compileAndLoad(String code, String className) {
        CharStream input = CharStreams.fromString(code);
        GrammarLexer lexer = new GrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GrammarParser parser = new GrammarParser(tokens);

        ParseTree tree = parser.program();
        ASTBuilder builder = new ASTBuilder();
        Program ast = (Program) builder.visit(tree);

        TypeChecker sem = new TypeChecker();
        var errs = sem.check(ast);
        if (!errs.isEmpty()) {
            throw new RuntimeException("Semantic errors: " + errs);
        }

        HXlatUnit unit = new HXlatUnit();
        HEnv topEnv = new HEnv();
        HProgram hir = new HIRLowering().lower(unit, topEnv, ast);
        hir.accept(new TailCallChildTransformer());

        CodeGen gen = new CodeGen(hir);
        Class<?> cls = gen.loadAndDefine(className);
        return cls;
    }
}
