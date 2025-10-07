package net.iczelia.bawk.hir.expr.unary;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.hir.expr.HExpr;
import net.iczelia.bawk.type.Type;

public class HUnaryNot extends HUnaryExpression {
    public HUnaryNot(HExpr expr, Type type, HXlatUnit unit, HEnv env) {
        this.expr = expr;
        this.type = type;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }
}
