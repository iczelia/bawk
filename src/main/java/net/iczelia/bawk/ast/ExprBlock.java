package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.Type;

import java.util.ArrayList;
import java.util.List;

public class ExprBlock extends Expr {
    private final Expr finalExpr;
    private final List<Statement> statements = new ArrayList<>();

    public ExprBlock(List<Statement> statements, Expr finalExpr) {
        this.statements.addAll(statements);
        this.finalExpr = finalExpr;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public Expr getFinalExpr() {
        return finalExpr;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        env.sym.enter();
        TypeChecker checker = new TypeChecker(env);
        for (Statement stmt : statements) {
            checker.typeCheckStatement(stmt);
        }
        Type t = finalExpr.getInferredType(env);
        env.sym.exit();
        return t;
    }
}

