package net.iczelia.bawk.hir.jvmcg.stmt;

import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.hir.stmt.HBlock;
import net.iczelia.bawk.hir.stmt.HLetDecl;
import net.iczelia.bawk.hir.stmt.HStmt;
import net.iczelia.bawk.type.Type;

public class HLetDeclJVMCodeGenerator implements JVMCodeGenerator<HLetDecl> {
    @Override
    public void visit(HLetDecl node, CodeGenBase context) {
        Type t = node.type;
        int idx = context.allocLocal(node.name.name(), t);
        if (node.initializer != null) {
            context.emitExpr(node.initializer);
            context.store(idx, t);
        } else {
            context.defaultInit(t);
            context.store(idx, t);
        }
    }
}
