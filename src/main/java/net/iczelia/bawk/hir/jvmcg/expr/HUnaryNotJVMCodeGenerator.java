package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.unary.HUnaryNot;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HUnaryNotJVMCodeGenerator implements JVMCodeGenerator<HUnaryNot> {
    @Override
    public void visit(HUnaryNot node, CodeGenBase context) {
        context.emitExpr(node.expr);
        Label TRUE = new Label(), DONE = new Label();
        context.mv.visitJumpInsn(Opcodes.IFEQ, TRUE);
        context.mv.visitLdcInsn(0);
        context.mv.visitJumpInsn(Opcodes.GOTO, DONE);
        context.mv.visitLabel(TRUE);
        context.mv.visitLdcInsn(1);
        context.mv.visitLabel(DONE);
    }
}
