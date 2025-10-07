package net.iczelia.bawk.hir.expr.unary;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.hir.expr.HExpr;
import net.iczelia.bawk.type.Type;

public class HArrayAlloc extends HUnaryExpression {
    public HArrayAlloc(HExpr sizeExpr, Type type, HXlatUnit unit, HEnv env) {
        this.expr = sizeExpr;
        this.type = type;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }
}
