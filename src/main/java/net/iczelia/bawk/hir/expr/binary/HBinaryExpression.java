package net.iczelia.bawk.hir.expr.binary;

import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.expr.HExpr;

public abstract class HBinaryExpression extends HExpr {
    public HExpr left, right;

    @Override
    public void accept(HIRChildTransformer visitor) {
        left = (HExpr) visitor.transform(left, this);
        right = (HExpr) visitor.transform(right, this);
    }
}
