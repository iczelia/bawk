package net.iczelia.bawk.hir.stmt;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.hir.expr.HExpr;

public class HWhile extends HStmt {
    public HExpr condition;
    public HStmt body;

    public HWhile(HExpr condition, HStmt body, HXlatUnit unit, HEnv env) {
        this.condition = condition;
        this.body = body;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        condition = (HExpr) visitor.transform(condition, this);
        body = (HStmt) visitor.transform(body, this);
    }
}
