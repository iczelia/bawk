package net.iczelia.bawk.hir.expr;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.type.Type;

public class HStrLit extends HExpr {
    public String value;

    public HStrLit(String value, Type type, HXlatUnit unit, HEnv env) {
        this.value = value;
        this.type = type;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
    }
}
