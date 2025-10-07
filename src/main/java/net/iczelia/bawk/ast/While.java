package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class While extends Statement {
    private final Expr cond;
    private final Statement body; // Can be Block or any Stmt

    public While(Expr cond, Statement body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    protected void check() {
        Type ct = cond.getInferredType(typeEnv);
        if (ct != PrimitiveType.I32) {
            typeEnv.errors.add(new ASTError("while condition must be i32 (0/≠0)"));
        }
        typeEnv.sym.enter(); // while body scope
        if (body instanceof Block block) {
            for (Statement s : block.getStatements()) s.checkConstraints(typeEnv);
        } else {
            body.checkConstraints(typeEnv);
        }
        typeEnv.sym.exit();
    }

    public Expr getCond() {
        return cond;
    }

    public Statement getBody() {
        return body;
    }
}
