package net.iczelia.bawk.hir.expr;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.type.Type;

public class HInstanceOfCheck extends HExpr {
    public HExpr expr;
    public Type toCheck;

    public HInstanceOfCheck(HExpr expr, Type toCheck, Type type, HXlatUnit unit, HEnv env) {
        this.expr = expr;
        this.toCheck = toCheck;
        this.type = type;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        expr = (HExpr) visitor.transform(expr, this);
    }
}
