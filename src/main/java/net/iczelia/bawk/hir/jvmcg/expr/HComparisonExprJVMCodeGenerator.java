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
                context.emitExpr(e.left);
                context.emitExpr(e.right);
                context.emitIntBoolFromCompare(Opcodes.IF_ICMPLT);
            }
            case HLeExpr e -> {
                context.emitExpr(e.left);
                context.emitExpr(e.right);
                context.emitIntBoolFromCompare(Opcodes.IF_ICMPLE);
            }
            case HGtExpr e -> {
                context.emitExpr(e.left);
                context.emitExpr(e.right);
                context.emitIntBoolFromCompare(Opcodes.IF_ICMPGT);
            }
            case HGeExpr e -> {
                context.emitExpr(e.left);
                context.emitExpr(e.right);
                context.emitIntBoolFromCompare(Opcodes.IF_ICMPGE);
            }
            default -> throw new RuntimeException("Comparison expression code generator received an invalid HIR node.");
        }
    }
}
