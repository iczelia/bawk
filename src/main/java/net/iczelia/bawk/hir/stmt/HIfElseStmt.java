package net.iczelia.bawk.hir.stmt;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.hir.expr.HExpr;

public class HIfElseStmt extends HStmt {
    public HExpr cond;
    public HStmt yes, no;

    public HIfElseStmt(HExpr cond, HStmt yes, HStmt no, HXlatUnit unit, HEnv env) {
        this.cond = cond;
        this.yes = yes;
        this.no = no;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        cond = (HExpr) visitor.transform(cond, this);
        yes = (HStmt) visitor.transform(yes, this);
        if (no != null)
            no = (HStmt) visitor.transform(no, this);
    }
}
