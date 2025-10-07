package net.iczelia.bawk.hir.stmt;

import net.iczelia.bawk.hir.HEnv;
import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HNode;
import net.iczelia.bawk.hir.HXlatUnit;
import net.iczelia.bawk.type.Type;

public class HFnDecl extends HStmt {
    public HXlatUnit.FnToken name;
    public Type returnType;
    public HXlatUnit.VarToken[] params;
    public HNode body;

    public HFnDecl(HXlatUnit.FnToken name, Type returnType, HXlatUnit.VarToken[] params, HNode body, HXlatUnit unit, HEnv env) {
        this.name = name;
        this.returnType = returnType;
        this.params = params;
        this.body = body;
        this.translationUnitContext = unit;
        this.environmentContext = env;
    }

    @Override
    public void accept(HIRChildTransformer visitor) {
        body = visitor.transform(body, this);
    }
}
