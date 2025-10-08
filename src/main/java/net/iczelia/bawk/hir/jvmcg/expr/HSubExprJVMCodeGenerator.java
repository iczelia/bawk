package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.binary.HSubExpr;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HSubExprJVMCodeGenerator implements JVMCodeGenerator<HSubExpr> {
    @Override
    public void visit(HSubExpr node, CodeGenBase context) {
        var leftType = node.left.type;
        var rightType = node.right.type;
        context.emitExpr(node.left);
        if (leftType == net.iczelia.bawk.type.PrimitiveType.I32 && rightType == net.iczelia.bawk.type.PrimitiveType.F32) {
            context.mv.visitInsn(Opcodes.I2F);
        }
        context.emitExpr(node.right);
        if (rightType == net.iczelia.bawk.type.PrimitiveType.I32 && leftType == net.iczelia.bawk.type.PrimitiveType.F32) {
            context.mv.visitInsn(Opcodes.I2F);
        }
        if (leftType == net.iczelia.bawk.type.PrimitiveType.F32 || rightType == net.iczelia.bawk.type.PrimitiveType.F32) {
            context.mv.visitInsn(Opcodes.FSUB);
        } else {
            context.mv.visitInsn(Opcodes.ISUB);
        }
    }
}
