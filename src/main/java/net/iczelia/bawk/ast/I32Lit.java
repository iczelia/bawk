package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class I32Lit extends Expr {
    private final int value;

    public I32Lit(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        return PrimitiveType.I32;
    }
}
