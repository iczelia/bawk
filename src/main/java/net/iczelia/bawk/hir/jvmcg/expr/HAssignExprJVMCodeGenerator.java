package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HAssignExpr;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HAssignExprJVMCodeGenerator implements JVMCodeGenerator<HAssignExpr> {
    @Override
    public void visit(HAssignExpr node, CodeGenBase context) {
        context.emitExpr(node.value);
        context.mv.visitInsn(Opcodes.DUP);
        context.storeLValue(node.assignmentTarget);
    }
}
