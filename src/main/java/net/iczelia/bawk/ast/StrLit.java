package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class StrLit extends Expr {
    private final String value;

    public StrLit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        return PrimitiveType.STR;
    }
}
