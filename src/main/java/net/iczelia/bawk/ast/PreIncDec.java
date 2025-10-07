package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class PreIncDec extends Expr {
    private final String op;   // "++" or "--"
    private final LValue target;

    public PreIncDec(String op, LValue target) {
        this.op = op;
        this.target = target;
    }

    public String getOp() {
        return op;
    }

    public LValue getTarget() {
        return target;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        Type vt = target.getInferredType(env);
        if (vt != PrimitiveType.I32) {
            env.errors.add(new ASTError("pre " + (op.equals("++") ? "increment" : "decrement") + " requires i32, got " + vt));
            return null;
        }
        return PrimitiveType.I32;
    }
}
