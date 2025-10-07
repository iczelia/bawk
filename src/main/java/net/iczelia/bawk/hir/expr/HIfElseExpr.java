package net.iczelia.bawk.hir.expr;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.type.Type;

public class HIfElseExpr extends HExpr {
    public HExpr cond, yes, no;

    public HIfElseExpr(HExpr cond, HExpr yes, HExpr no, Type type, HXlatUnit unit, HEnv env) {
        this.cond = cond;
        this.yes = yes;
        this.no = no;
        this.type = type;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        cond = (HExpr) visitor.transform(cond, this);
        yes = (HExpr) visitor.transform(yes, this);
        no = (HExpr) visitor.transform(no, this);
    }
}
