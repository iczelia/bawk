package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.binary.HNeqExpr;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

import java.lang.reflect.Method;
import java.util.Objects;

public class HNeqExprJVMCodeGenerator implements JVMCodeGenerator<HNeqExpr> {
    @Override
    public void visit(HNeqExpr eq, CodeGenBase context) {
        Type lt = eq.left.type;
        if (lt == PrimitiveType.I32) {
            context.emitExpr(eq.left);
            context.emitExpr(eq.right);
            context.emitIntBoolFromCompare(Opcodes.IF_ICMPNE);
        } else {
            context.emitExpr(eq.left);
            context.emitExpr(eq.right);
            Method m;
            try {
                m = Objects.class.getMethod("equals", Object.class, Object.class);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(m)).apply(context.mv, null);
            Label T = new Label(), D = new Label();
            context.mv.visitJumpInsn(Opcodes.IFNE, D);
            context.mv.visitLdcInsn(1);
            context.mv.visitJumpInsn(Opcodes.GOTO, T);
            context.mv.visitLabel(D);
            context.mv.visitLdcInsn(0);
            context.mv.visitLabel(T);
        }
    }
}
