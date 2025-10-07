package net.iczelia.bawk.hir.jvmcg.stmt;

import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HVar;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.hir.stmt.HBlock;
import net.iczelia.bawk.hir.stmt.HStmt;

public class HBlockJVMCodeGenerator implements JVMCodeGenerator<HBlock> {
    @Override
    public void visit(HBlock node, CodeGenBase context) {
        context.enterScope();
        for (HStmt s : node.stmts) context.emitStmt(s);
        context.exitScope();
    }
}
