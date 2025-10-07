package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.unary.HUnaryTally;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.type.ArrayType;
import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

import java.lang.reflect.Method;

public class HUnaryTallyJVMCodeGenerator implements JVMCodeGenerator<HUnaryTally> {
    @Override
    public void visit(HUnaryTally node, CodeGenBase context) {
        context.emitExpr(node.expr);
        Type et = node.expr.type;
        if (et instanceof ArrayType) {
            context.mv.visitInsn(Opcodes.ARRAYLENGTH);
        } else if (et == PrimitiveType.STR) {
            Method m;
            try {
                m = String.class.getMethod("length");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(m)).apply(context.mv, null);
        } else {
            throw new IllegalStateException("Tally operator requires array or string, got: " + et);
        }
    }
}
