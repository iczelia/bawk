package net.iczelia.bawk.hir.jvmcg.expr;

import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HVar;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HVarJVMCodeGenerator implements JVMCodeGenerator<HVar> {
    @Override
    public void visit(HVar node, CodeGenBase context) {
        CodeGenBase.Slot s = context.require(node.var.name());
        context.load(s.index, s.type);
    }
}
