package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class PostIncDec extends Expr {
    private final LValue target;
    private final String op;   // "++" or "--"

    public PostIncDec(String op, LValue target) {
        this.op = op;
        this.target = target;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        Type vt = target.getInferredType(env);
        if (vt != PrimitiveType.I32) {
            env.errors.add(new ASTError("post " + (op.equals("++") ? "increment" : "decrement") + " requires i32, got " + vt));
            return null;
        }
        return PrimitiveType.I32;
    }

    public String getOp() {
        return op;
    }

    public LValue getTarget() {
        return target;
    }
}
