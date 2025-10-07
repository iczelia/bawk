package net.iczelia.bawk.hir.jvmcg.expr;

import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HStrLit;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HStrLitJVMCodeGenerator implements JVMCodeGenerator<HStrLit> {
    @Override
    public void visit(HStrLit node, CodeGenBase context) {
        context.mv.visitLdcInsn(node.value);
    }
}
