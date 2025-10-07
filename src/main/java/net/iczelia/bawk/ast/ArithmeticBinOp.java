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

    private Type resolveScalarType(TypeEnvironment env, Type lt, Type rt) {
        switch (op) {
            case "+", "-", "*", "/" -> {
                if (lt == PrimitiveType.I32 && rt == PrimitiveType.I32) {
                    return PrimitiveType.I32;
                }
                env.errors.add(new ASTError("arithmetic '" + op + "' requires i32, got " + lt + " and " + rt));
                return null;
            }

            // comparisons
            case "<", ">", "<=", ">=" -> {
                if (lt == PrimitiveType.I32 && rt == PrimitiveType.I32) {
                    return PrimitiveType.I32;
                }
                env.errors.add(new ASTError("comparison '" + op + "' requires i32, got " + lt + " and " + rt));
                return null;
            }

            // equality
            case "==", "!=" -> {
                if (lt == rt) {
                    return PrimitiveType.I32;
                }
                env.errors.add(new ASTError("equality '" + op + "' requires operands of same type, got " + lt + " and " + rt));
                return null;
            }
        }
        env.errors.add(new ASTError("unknown operator '" + op + "'"));
        return null;
    }

    private Type stripArrayTypes(Type t) {
        while (t instanceof ArrayType at) {
            t = at.elementType;
        }
        return t;
    }

    private int rankOf(Type t) {
        int rank = 0;
        while (t instanceof ArrayType at) {
            rank++;
            t = at.elementType;
        }
        return rank;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        Type lt = left.getInferredType(env);
        Type rt = right.getInferredType(env);
        if (lt == null || rt == null) return null;
        Type ltA = stripArrayTypes(lt);
        Type rtA = stripArrayTypes(rt);
        Type elType = resolveScalarType(env, ltA, rtA);
        // Rank of result is maximum of operand ranks
        if (elType != null) {
            int rank = Math.max(rankOf(lt), rankOf(rt));
            while (rank-- > 0) {
                elType = new ArrayType(elType);
            }
        }
        return elType;
    }
}
