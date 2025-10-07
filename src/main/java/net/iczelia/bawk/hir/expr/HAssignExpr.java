package net.iczelia.bawk.hir.expr;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.type.Type;

public class HAssignExpr extends HExpr {
    public HLValue assignmentTarget;
    public HExpr value;

    public HAssignExpr(HLValue assignmentTarget, HExpr value, Type type, HXlatUnit unit, HEnv env) {
        this.assignmentTarget = assignmentTarget;
        this.value = value;
        this.type = type;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        assignmentTarget = (HLValue) visitor.transform(assignmentTarget, this);
        value = (HExpr) visitor.transform(value, this);
    }
}
