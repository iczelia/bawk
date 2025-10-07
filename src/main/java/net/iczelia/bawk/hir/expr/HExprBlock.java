package net.iczelia.bawk.hir.expr;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.hir.stmt.HStmt;
import net.iczelia.bawk.type.Type;

public class HExprBlock extends HExpr {
    public HStmt[] precStmts;
    public HExpr finalExpr;

    public HExprBlock(HStmt[] precStmts, HExpr finalExpr, Type type, HXlatUnit unit, HEnv env) {
        this.precStmts = precStmts;
        this.finalExpr = finalExpr;
        this.type = type;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        for (int i = 0; i < precStmts.length; i++) {
            precStmts[i] = (HStmt) visitor.transform(precStmts[i], this);
        }
        finalExpr = (HExpr) visitor.transform(finalExpr, this);
    }
}
