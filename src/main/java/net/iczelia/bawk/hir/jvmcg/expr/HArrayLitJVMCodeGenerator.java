package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HArrayLit;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.type.ArrayType;
import net.iczelia.bawk.type.PrimitiveType;

public class HArrayLitJVMCodeGenerator implements JVMCodeGenerator<HArrayLit> {
    @Override
    public void visit(HArrayLit node, CodeGenBase context) {
        int size = node.initializers.length;
        IntegerConstant.forValue(size).apply(context.mv, null);
        if (node.type instanceof ArrayType at) {
            if (at.elementType == PrimitiveType.I32) {
                context.mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
            } else if (at.elementType == PrimitiveType.STR) {
                context.mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/String");
            } else {
                context.mv.visitTypeInsn(Opcodes.ANEWARRAY, at.elementType.getJavaTypeString());
            }
            for (int i = 0; i < size; i++) {
                context.mv.visitInsn(Opcodes.DUP);
                IntegerConstant.forValue(i).apply(context.mv, null);
                context.emitExpr(node.initializers[i]);
                if (at.elementType == PrimitiveType.I32) {
                    context.mv.visitInsn(Opcodes.IASTORE);
                } else if (at.elementType == PrimitiveType.STR) {
                    context.mv.visitInsn(Opcodes.AASTORE);
                } else {
                    context.mv.visitInsn(Opcodes.AASTORE);
                }
            }
        } else {
            throw new IllegalStateException("Array literal does not have array type");
        }
    }
}
