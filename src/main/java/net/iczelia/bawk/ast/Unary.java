package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.ArrayType;
import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class Unary extends Expr {
    public final String op;   // '+', '-', '!', etc.
    public final Expr expr;

    public Unary(String op, Expr expr) {
        this.op = op;
        this.expr = expr;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        Type rt = expr.getInferredType(env);
        if (rt == null) return null;
        switch (op) {
            case "-" -> {
                if (rt == PrimitiveType.I32) {
                    return PrimitiveType.I32;
                }
                env.errors.add(new ASTError("unary '-' requires i32, got " + rt));
                return null;
            }
            case "!" -> {
                if (rt == PrimitiveType.I32) {
                    return PrimitiveType.I32;
                }
                env.errors.add(new ASTError("unary '!' requires i32, got " + rt));
                return null;
            }
            case "+" -> {
                if (rt == PrimitiveType.I32) {
                    return PrimitiveType.I32;
                }
                env.errors.add(new ASTError("unary '+' requires i32, got " + rt));
                return null;
            }
            case "#" -> {
                if (rt instanceof ArrayType || rt == PrimitiveType.STR) {
                    return PrimitiveType.I32;
                }
                env.errors.add(new ASTError("unary '#' requires array or string, got " + rt));
                return null;
            }
        }
        env.errors.add(new ASTError("unknown unary operator '" + op + "'"));
        return null;
    }

    public String getOp() {
        return op;
    }

    public Expr getExpr() {
        return expr;
    }
}
