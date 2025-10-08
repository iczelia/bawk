package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.binary.HMulExpr;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HMulExprJVMCodeGenerator implements JVMCodeGenerator<HMulExpr> {
    @Override
    public void visit(HMulExpr node, CodeGenBase context) {
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
            context.mv.visitInsn(Opcodes.FMUL);
        } else {
            context.mv.visitInsn(Opcodes.IMUL);
        }
    }
}
