package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.unary.HUnaryMinus;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HUnaryMinusJVMCodeGenerator implements JVMCodeGenerator<HUnaryMinus> {
    @Override
    public void visit(HUnaryMinus node, CodeGenBase context) {
        context.emitExpr(node.expr);
        context.mv.visitInsn(Opcodes.INEG);
    }
}
