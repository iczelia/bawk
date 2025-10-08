package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.ArrayType;
import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class ArithmeticBinOp extends Expr {
    private final String op;
    private final Expr left, right;

    public ArithmeticBinOp(String op, Expr left, Expr right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public String getOp() {
        return op;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        Type lt = left.getInferredType(env);
        Type rt = right.getInferredType(env);
        if (lt == null || rt == null) return null;
        switch (op) {
            case "+", "-", "*", "/" -> {
                if (lt == PrimitiveType.I32 && rt == PrimitiveType.I32) {
                    return PrimitiveType.I32;
                }
                if ((lt == PrimitiveType.F32 && rt == PrimitiveType.F32) ||
                    (lt == PrimitiveType.I32 && rt == PrimitiveType.F32) ||
                    (lt == PrimitiveType.F32 && rt == PrimitiveType.I32)) {
                    return PrimitiveType.F32;
                }
                env.errors.add(new ASTError("arithmetic '" + op + "' requires i32 or f32, got " + lt + " and " + rt));
                return null;
            }

            // comparisons
            case "<", ">", "<=", ">=" -> {
                if ((lt == PrimitiveType.I32 && rt == PrimitiveType.I32) ||
                    (lt == PrimitiveType.F32 && rt == PrimitiveType.F32) ||
                    (lt == PrimitiveType.I32 && rt == PrimitiveType.F32) ||
                    (lt == PrimitiveType.F32 && rt == PrimitiveType.I32)) {
                    return PrimitiveType.I32;
                }
                env.errors.add(new ASTError("comparison '" + op + "' requires i32 or f32, got " + lt + " and " + rt));
                return null;
            }

            // equality
            case "==", "!=" -> {
                if ((lt == rt) ||
                    (lt == PrimitiveType.I32 && rt == PrimitiveType.F32) ||
                    (lt == PrimitiveType.F32 && rt == PrimitiveType.I32)) {
                    return PrimitiveType.I32;
                }
                env.errors.add(new ASTError("equality '" + op + "' requires operands of compatible types, got " + lt + " and " + rt));
                return null;
            }
        }
        env.errors.add(new ASTError("unknown operator '" + op + "'"));
        return null;
    }
}
