package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.binary.HAddExpr;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HAddExprJVMCodeGenerator implements JVMCodeGenerator<HAddExpr> {
    @Override
    public void visit(HAddExpr node, CodeGenBase context) {
        context.emitExpr(node.left);
        context.emitExpr(node.right);
        context.mv.visitInsn(Opcodes.IADD);
    }
}
