package net.iczelia.bawk.hir.stmt;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.hir.expr.HExpr;

public class HPrint extends HStmt {
    public HExpr expr;

    public HPrint(HExpr expr, HXlatUnit unit, HEnv env) {
        this.expr = expr;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        expr = (HExpr) visitor.transform(expr, this);
    }
}
