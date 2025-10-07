package net.iczelia.bawk.hir.stmt;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;

public class HProgram extends HStmt {
    public HStmt[] statements;

    public HProgram(HStmt[] statements, HXlatUnit unit, HEnv env) {
        this.statements = statements;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        for (int i = 0; i < statements.length; i++) {
            statements[i] = (HStmt) visitor.transform(statements[i], this);
        }
    }
}
