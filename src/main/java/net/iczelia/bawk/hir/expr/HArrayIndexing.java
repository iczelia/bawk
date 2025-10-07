package net.iczelia.bawk.hir.expr;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.type.Type;

public class HArrayIndexing extends HLValue {
    public HExpr arrayExpr;
    public HExpr indexExpr;

    public HArrayIndexing(HExpr arrayExpr, HExpr indexExpr, Type type, HXlatUnit unit, HEnv env) {
        this.arrayExpr = arrayExpr;
        this.indexExpr = indexExpr;
        this.type = type;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        arrayExpr = (HExpr) visitor.transform(arrayExpr, this);
        indexExpr = (HExpr) visitor.transform(indexExpr, this);
    }
}
