package net.iczelia.bawk.hir.transform;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.hir.expr.HExpr;
import net.iczelia.bawk.type.Type;

/**
 * Special AST node produced by tail recursion transformer indicating a self-recursive
 * call occurring in tail position, eligible for loop-style TCO during code generation.
 */
public class HTailSelfCall extends HExpr {
    public HXlatUnit.FnToken function;
    public HExpr[] args;

    public HTailSelfCall(HXlatUnit.FnToken function, HExpr[] args, Type type, HXlatUnit unit, HEnv env) {
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

