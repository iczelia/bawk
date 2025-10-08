package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class F32Lit extends Expr {
    private final float value;

    public F32Lit(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        return PrimitiveType.F32;
    }
}
