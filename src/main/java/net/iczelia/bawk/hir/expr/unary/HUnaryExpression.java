package net.iczelia.bawk.hir.expr.unary;

import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.expr.HExpr;

public abstract class HUnaryExpression extends HExpr {
    public HExpr expr;

    @Override
    public void accept(HIRChildTransformer visitor) {
        expr = (HExpr) visitor.transform(expr, this);
    }
}
