package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.Type;

public class AssignExpr extends Expr {
    private final LValue target;
    private final Expr value;

    public AssignExpr(LValue target, Expr value) {
        this.target = target;
        this.value = value;
    }

    public LValue getTarget() {
        return target;
    }

    public Expr getValue() {
        return value;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        Type lt = target.getInferredType(env);
        Type rt = value.getInferredType(env);
        if (lt != null && rt != null && !lt.compatible(rt)) {
            env.errors.add(new ASTError("type mismatch in assignment expression"));
            return null;
        }
        return lt;
    }
}
