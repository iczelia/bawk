package net.iczelia.bawk.hir.stmt;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.hir.expr.HExpr;
import net.iczelia.bawk.type.Type;

public class HLetDecl extends HStmt {
    public HXlatUnit.VarToken name;
    public HExpr initializer;
    public Type type;

    public HLetDecl(HXlatUnit.VarToken name, HExpr initializer, Type type, HXlatUnit unit, HEnv env) {
        this.name = name;
        this.initializer = initializer;
        this.type = type;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        initializer = (HExpr) visitor.transform(initializer, this);
    }
}
