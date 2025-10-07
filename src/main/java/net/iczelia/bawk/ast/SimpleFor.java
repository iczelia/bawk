package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class SimpleFor extends Statement {
    private final Statement init;    // Initialization (Decl or AssignStmt)
    private final Expr cond;    // Condition (Expr)
    private final Expr update;  // Update (AssignExpr, PreIncDec, PostIncDec, etc.)
    private final Statement body;    // Body (Block or Stmt)

    public SimpleFor(Statement init, Expr cond, Expr update, Statement body) {
        this.init = init;
        this.cond = cond;
        this.update = update;
        this.body = body;
    }

    public Statement getInit() {
        return init;
    }

    public Expr getCond() {
        return cond;
    }

    public Expr getUpdate() {
        return update;
    }

    public Statement getBody() {
        return body;
    }

    @Override
    protected void check() {
        // Check init, cond, update, body with proper scoping
        typeEnv.sym.enter(); // for-loop scope
        if (init != null) init.checkConstraints(typeEnv);
        Type ct = cond != null ? cond.getInferredType(typeEnv) : null;
        if (ct != null && ct != PrimitiveType.I32) {
            typeEnv.errors.add(new ASTError("for condition must be i32 (0/≠0)"));
        }
        if (update != null) update.getInferredType(typeEnv);
        if (body instanceof Block block) {
            for (Statement s : block.getStatements()) s.checkConstraints(typeEnv);
        } else if (body != null) {
            body.checkConstraints(typeEnv);
        }
        typeEnv.sym.exit();
    }
}

