package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.*;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HArithmeticPostfixExprJVMCodeGenerator implements JVMCodeGenerator<HExpr> {
    @Override
    public void visit(HExpr node, CodeGenBase context) {
        switch (node) {
            case HPreIncExpr e -> {
                context.loadLValue(e.target);
                context.mv.visitInsn(Opcodes.ICONST_1);
                context.mv.visitInsn(Opcodes.IADD);
                context.mv.visitInsn(Opcodes.DUP);
                context.storeLValue(e.target);
            }
            case HPostIncExpr e -> {
                context.loadLValue(e.target);
                context.mv.visitInsn(Opcodes.DUP);
                context.mv.visitInsn(Opcodes.ICONST_1);
                context.mv.visitInsn(Opcodes.IADD);
                context.storeLValue(e.target);
            }
            case HPreDecExpr e -> {
                context.loadLValue(e.target);
                context.mv.visitInsn(Opcodes.ICONST_1);
                context.mv.visitInsn(Opcodes.ISUB);
                context.mv.visitInsn(Opcodes.DUP);
                context.storeLValue(e.target);
            }
            case HPostDecExpr e -> {
                context.loadLValue(e.target);
                context.mv.visitInsn(Opcodes.DUP);
                context.mv.visitInsn(Opcodes.ICONST_1);
                context.mv.visitInsn(Opcodes.ISUB);
                context.storeLValue(e.target);
            }
            default ->
                    throw new RuntimeException("Arithmetic postfix expression code generator received an invalid HIR node.");
        }
    }
}
