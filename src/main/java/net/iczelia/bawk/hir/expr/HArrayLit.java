package net.iczelia.bawk.hir.expr;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.type.Type;

public class HArrayLit extends HExpr {
    public HExpr[] initializers;

    public HArrayLit(HExpr[] initializers, Type type, HXlatUnit unit, HEnv env) {
        this.initializers = initializers;
        this.type = type;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        for (int i = 0; i < initializers.length; i++) {
            initializers[i] = (HExpr) visitor.transform(initializers[i], this);
        }
    }

}
