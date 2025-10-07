package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.ArrayType;
import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class ArrayAlloc extends Expr {
    private final Expr sizeExpr;
    private final Type elementType;

    public ArrayAlloc(Expr sizeExpr, Type elementType) {
        this.sizeExpr = sizeExpr;
        this.elementType = elementType;
    }

    public Expr getSizeExpr() {
        return sizeExpr;
    }

    public Type getElementType() {
        return elementType;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        Type st = sizeExpr.getInferredType(env);
        if (st != PrimitiveType.I32) {
            env.errors.add(new ASTError("array size must be i32, got " + st));
        }
        if (elementType == null) {
            env.errors.add(new ASTError("array element type is null"));
            return null;
        }
        return new ArrayType(elementType);
    }
}
