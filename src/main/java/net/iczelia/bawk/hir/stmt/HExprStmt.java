package net.iczelia.bawk.hir.stmt;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.hir.expr.HExpr;

public class HExprStmt extends HStmt {
    public HExpr body;

    public HExprStmt(HExpr body, HXlatUnit unit, HEnv env) {
        this.body = body;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        body = (HExpr) visitor.transform(body, this);
    }
}
