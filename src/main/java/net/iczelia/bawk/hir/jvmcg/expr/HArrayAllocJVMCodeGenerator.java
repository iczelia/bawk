package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.unary.HArrayAlloc;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.type.ArrayType;
import net.iczelia.bawk.type.PrimitiveType;

public class HArrayAllocJVMCodeGenerator implements JVMCodeGenerator<HArrayAlloc> {
    @Override
    public void visit(HArrayAlloc node, CodeGenBase context) {
        context.emitExpr(node.expr);
        ArrayType arrayType = (ArrayType) node.type;
        if (arrayType.elementType == PrimitiveType.I32) {
            context.mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
        } else if (arrayType.elementType == PrimitiveType.STR) {
            context.mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/String");
        } else {
            context.mv.visitTypeInsn(Opcodes.ANEWARRAY, arrayType.elementType.getJavaTypeString());
        }
    }
}
