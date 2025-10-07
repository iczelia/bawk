package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.binary.HMulExpr;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HMulExprJVMCodeGenerator implements JVMCodeGenerator<HMulExpr> {
    @Override
    public void visit(HMulExpr node, CodeGenBase context) {
        context.emitExpr(node.left);
        context.emitExpr(node.right);
        context.mv.visitInsn(Opcodes.IMUL);
    }
}
