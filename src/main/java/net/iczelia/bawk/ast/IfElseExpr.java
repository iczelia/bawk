package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

public class IfElseExpr extends Expr {
    public Expr cond;
    public Expr yes, no;

    public IfElseExpr(Expr cond, Expr yes, Expr no) {
        this.cond = cond;
        this.yes = yes;
        this.no = no;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        Type condType = cond.getInferredType(env);
        if (!condType.compatible(PrimitiveType.I32)) {
            throw new RuntimeException("Condition must be coercible to a true or false value, got " + condType);
        }
        Type yesType = yes.getInferredType(env);
        Type noType = no.getInferredType(env);
        if (!yesType.compatible(noType)) {
            throw new RuntimeException("Branches of if-else must have compatible types, got " + yesType + " and " + noType);
        }
        return yesType;
    }

    public Expr getCond() {
        return cond;
    }

    public Expr getYes() {
        return yes;
    }

    public Expr getNo() {
        return no;
    }
}
