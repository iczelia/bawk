package net.iczelia.bawk.hir.stmt;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;

public class HBlock extends HStmt {
    public HStmt[] stmts;

    public HBlock(HStmt[] stmts, HXlatUnit unit, HEnv env) {
        this.stmts = stmts;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        for (int i = 0; i < stmts.length; i++) {
            stmts[i] = (HStmt) visitor.transform(stmts[i], this);
        }
    }
}
