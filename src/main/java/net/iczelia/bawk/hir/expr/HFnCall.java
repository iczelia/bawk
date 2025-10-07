package net.iczelia.bawk.hir.expr;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.type.Type;

public class HFnCall extends HExpr {
    public HXlatUnit.FnToken function;
    public HExpr[] args;

    public HFnCall(HXlatUnit.FnToken function, HExpr[] args, Type type, HXlatUnit unit, HEnv env) {
        this.function = function;
        this.args = args;
        this.type = type;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        for (int i = 0; i < args.length; i++) {
            args[i] = (HExpr) visitor.transform(args[i], this);
        }
    }
}
