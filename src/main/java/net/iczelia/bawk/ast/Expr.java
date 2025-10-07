package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.Type;

public abstract class Expr extends AST {
    private Type inferredType;

    protected abstract Type tryInfer(TypeEnvironment env);

    public Type getInferredType(TypeEnvironment env) {
        if (inferredType == null && env != null) {
            inferredType = tryInfer(env);
        }
        return inferredType;
    }
}
