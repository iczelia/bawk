package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.ArrayType;
import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class ArrayIndexing extends LValue {
    private final Expr arrayReference;
    private final Expr arrayIndex;

    public ArrayIndexing(Expr arrayReference, Expr arrayIndex) {
        this.arrayReference = arrayReference;
        this.arrayIndex = arrayIndex;
    }

    public Expr getArrayReference() {
        return arrayReference;
    }

    public Expr getArrayIndex() {
        return arrayIndex;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        Type at = arrayReference.getInferredType(env);
        Type it = arrayIndex.getInferredType(env);
        if (!(at instanceof ArrayType at2)) {
            env.errors.add(new ASTError("array indexing requires array type, got " + at));
            return null;
        }
        if (it != PrimitiveType.I32) {
            env.errors.add(new ASTError("array index must be i32, got " + it));
            return null;
        }
        return at2.elementType;
    }
}
