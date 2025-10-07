package net.iczelia.bawk.hir.jvmcg.expr;

import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.unary.HUnaryPlus;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HUnaryPlusJVMCodeGenerator implements JVMCodeGenerator<HUnaryPlus> {
    @Override
    public void visit(HUnaryPlus node, CodeGenBase context) {
        context.emitExpr(node.expr);
    }
}
