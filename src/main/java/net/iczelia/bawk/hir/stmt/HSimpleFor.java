package net.iczelia.bawk.hir.stmt;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.hir.expr.HExpr;

public class HSimpleFor extends HStmt {
    public HStmt init, body;
    public HExpr condition, step;

    public HSimpleFor(HStmt init, HExpr condition, HExpr step, HStmt body, HXlatUnit unit, HEnv env) {
        this.init = init;
        this.condition = condition;
        this.step = step;
        this.body = body;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        if (init != null)
            init = (HStmt) visitor.transform(init, this);
        if (condition != null)
            condition = (HExpr) visitor.transform(condition, this);
        if (step != null)
            step = (HExpr) visitor.transform(step, this);
        body = (HStmt) visitor.transform(body, this);
    }
}
