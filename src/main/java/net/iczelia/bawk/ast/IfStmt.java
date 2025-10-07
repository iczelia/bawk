package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class IfStmt extends Statement {
    public Expr cond;
    public Statement yes, no; // Can be Block or any Stmt

    @Override
    protected void check() {
        Type ct = cond.getInferredType(typeEnv);
        if (ct != PrimitiveType.I32) {
            typeEnv.errors.add(new ASTError("if condition must be i32 (0/≠0)"));
        }
        typeEnv.sym.enter(); // if-yes scope
        if (yes instanceof Block block) {
            for (Statement s : block.getStatements()) s.checkConstraints(typeEnv);
        } else {
            yes.checkConstraints(typeEnv);
        }
        typeEnv.sym.exit();
        if (no != null) {
            typeEnv.sym.enter(); // if-no scope
            if (no instanceof Block block) {
                for (Statement s : block.getStatements()) s.checkConstraints(typeEnv);
            } else {
                no.checkConstraints(typeEnv);
            }
            typeEnv.sym.exit();
        }
    }
}
