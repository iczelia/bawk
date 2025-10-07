package net.iczelia.bawk.ast;

public class ExprStmt extends Statement {
    private final Expr expr;

    public ExprStmt(Expr expr) {
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    protected void check() {
        expr.getInferredType(typeEnv); // Ignore if null, just make sure it type-checks.
    }
}

