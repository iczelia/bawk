package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HIfElseExpr;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HIfElseExprJVMCodeGenerator implements JVMCodeGenerator<HIfElseExpr> {
    @Override
    public void visit(HIfElseExpr node, CodeGenBase context) {
        context.emitExpr(node.cond);
        Label Lelse = new Label(), Lend = new Label();
        context.mv.visitJumpInsn(Opcodes.IFEQ, Lelse);
        context.emitExpr(node.yes);
        context.mv.visitJumpInsn(Opcodes.GOTO, Lend);
        context.mv.visitLabel(Lelse);
        context.emitExpr(node.no);
        context.mv.visitLabel(Lend);
    }
}
