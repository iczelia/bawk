package net.iczelia.bawk.hir.jvmcg.stmt;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.hir.stmt.HExprStmt;
import net.iczelia.bawk.hir.stmt.HPrint;
import net.iczelia.bawk.type.PrimitiveType;

import java.lang.reflect.Method;

public class HExprStmtJVMCodeGenerator implements JVMCodeGenerator<HExprStmt> {
    @Override
    public void visit(HExprStmt node, CodeGenBase context) {
        context.emitExpr(node.body);
        // Pop result if not void (most expressions leave a value)
        context.mv.visitInsn(Opcodes.POP);
    }
}
