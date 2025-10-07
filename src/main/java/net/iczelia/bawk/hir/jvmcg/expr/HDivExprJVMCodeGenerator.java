package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.binary.HDivExpr;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HDivExprJVMCodeGenerator implements JVMCodeGenerator<HDivExpr> {
    @Override
    public void visit(HDivExpr node, CodeGenBase context) {
        context.emitExpr(node.left);
        context.emitExpr(node.right);
        context.mv.visitInsn(Opcodes.IDIV);
    }
}
