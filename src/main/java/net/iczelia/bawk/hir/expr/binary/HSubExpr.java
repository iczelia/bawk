package net.iczelia.bawk.hir.expr.binary;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.hir.expr.HExpr;
import net.iczelia.bawk.type.Type;

public class HSubExpr extends HBinaryExpression {
    public HSubExpr(HExpr left, HExpr right, Type type, HXlatUnit unit, HEnv env) {
        this.left = left;
        this.right = right;
        this.type = type;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }
}
