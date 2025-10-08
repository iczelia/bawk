package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HExpr;
import net.iczelia.bawk.hir.expr.binary.HGeExpr;
import net.iczelia.bawk.hir.expr.binary.HGtExpr;
import net.iczelia.bawk.hir.expr.binary.HLeExpr;
import net.iczelia.bawk.hir.expr.binary.HLtExpr;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HComparisonExprJVMCodeGenerator implements JVMCodeGenerator<HExpr> {
    @Override
    public void visit(HExpr node, CodeGenBase context) {
        switch (node) {
            case HLtExpr e -> {
                emitComparison(e.left, e.right, context, Opcodes.IF_ICMPLT, Opcodes.IFLT);
            }
            case HLeExpr e -> {
                emitComparison(e.left, e.right, context, Opcodes.IF_ICMPLE, Opcodes.IFLE);
            }
            case HGtExpr e -> {
                emitComparison(e.left, e.right, context, Opcodes.IF_ICMPGT, Opcodes.IFGT);
            }
            case HGeExpr e -> {
                emitComparison(e.left, e.right, context, Opcodes.IF_ICMPGE, Opcodes.IFGE);
            }
            default -> throw new RuntimeException("Comparison expression code generator received an invalid HIR node.");
        }
    }

    private void emitComparison(HExpr left, HExpr right, CodeGenBase context, int intOpcode, int floatOpcode) {
        var leftType = left.type;
        var rightType = right.type;
        context.emitExpr(left);
        if (leftType == net.iczelia.bawk.type.PrimitiveType.I32 && rightType == net.iczelia.bawk.type.PrimitiveType.F32) {
            context.mv.visitInsn(Opcodes.I2F);
        }
        context.emitExpr(right);
        if (rightType == net.iczelia.bawk.type.PrimitiveType.I32 && leftType == net.iczelia.bawk.type.PrimitiveType.F32) {
            context.mv.visitInsn(Opcodes.I2F);
        }
        if (leftType == net.iczelia.bawk.type.PrimitiveType.F32 || rightType == net.iczelia.bawk.type.PrimitiveType.F32) {
            // For floats, use FCMPL and branch
            context.mv.visitInsn(Opcodes.FCMPL);
            context.emitIntBoolFromCompare(floatOpcode);
        } else {
            context.emitIntBoolFromCompare(intOpcode);
        }
    }
}
