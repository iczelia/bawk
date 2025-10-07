package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.binary.HSubExpr;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HSubExprJVMCodeGenerator implements JVMCodeGenerator<HSubExpr> {
    @Override
    public void visit(HSubExpr node, CodeGenBase context) {
        context.emitExpr(node.left);
        context.emitExpr(node.right);
        context.mv.visitInsn(Opcodes.ISUB);
    }
}
