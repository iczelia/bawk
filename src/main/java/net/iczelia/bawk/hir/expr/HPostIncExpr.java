package net.iczelia.bawk.hir.expr;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.type.Type;

public class HPostIncExpr extends HExpr {
    public HLValue target;

    public HPostIncExpr(HLValue target, Type type, HXlatUnit unit, HEnv env) {
        this.target = target;
        this.type = type;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        target = (HLValue) visitor.transform(target, this);
    }
}
