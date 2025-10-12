package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.ArrayType;
import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class InstanceOfCheck extends LValue {
    private final Expr reference;
    private final Type desiredType;

    public InstanceOfCheck(Expr reference, Type desiredType) {
        this.reference = reference;
        this.desiredType = desiredType;
    }

    public Expr getReference() {
        return reference;
    }

    public Type getDesiredType() {
        return desiredType;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        return PrimitiveType.I32;
    }
}
