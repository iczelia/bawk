package net.iczelia.bawk.hir.jvmcg.expr;

import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HArrayIndexing;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HArrayIndexingJVMCodeGenerator implements JVMCodeGenerator<HArrayIndexing> {
    @Override
    public void visit(HArrayIndexing node, CodeGenBase context) {
        context.loadLValue(node);
    }
}
