package net.iczelia.bawk.hir.jvmcg.expr;

import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HExprBlock;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.hir.stmt.HStmt;

public class HExprBlockJVMCodeGenerator implements JVMCodeGenerator<HExprBlock> {
    @Override
    public void visit(HExprBlock node, CodeGenBase context) {
        context.enterScope();
        for (HStmt s : node.precStmts) context.emitStmt(s);
        if (node.finalExpr != null) {
            context.emitExpr(node.finalExpr);
        }
        context.exitScope();
    }
}
