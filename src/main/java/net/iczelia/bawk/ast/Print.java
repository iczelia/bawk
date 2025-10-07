package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class Print extends Statement {
    private final Expr value;

    public Print(Expr value) {
        this.value = value;
    }

    @Override
    protected void check() {
        Type t = value.getInferredType(typeEnv);
        if (t != PrimitiveType.I32 && t != PrimitiveType.STR) {
            typeEnv.errors.add(new ASTError("print expects i32 or str"));
        }
    }

    public Expr getValue() {
        return value;
    }
}
