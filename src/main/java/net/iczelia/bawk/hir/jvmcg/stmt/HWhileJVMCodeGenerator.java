package net.iczelia.bawk.hir.jvmcg.stmt;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.hir.stmt.HBlock;
import net.iczelia.bawk.hir.stmt.HPrint;
import net.iczelia.bawk.hir.stmt.HStmt;
import net.iczelia.bawk.hir.stmt.HWhile;
import net.iczelia.bawk.type.PrimitiveType;

import java.lang.reflect.Method;

public class HWhileJVMCodeGenerator implements JVMCodeGenerator<HWhile> {
    @Override
    public void visit(HWhile node, CodeGenBase context) {
        Label Lstart = new Label();
        Label Lend = new Label();
        context.mv.visitLabel(Lstart);
        context.emitExpr(node.condition);
        context.mv.visitJumpInsn(Opcodes.IFEQ, Lend);
        context.enterScope();
        if (node.body instanceof HBlock block) {
            for (HStmt s : block.stmts) context.emitStmt(s);
        } else {
            context.emitStmt(node.body);
        }
        context.exitScope();
        context.mv.visitJumpInsn(Opcodes.GOTO, Lstart);
        context.mv.visitLabel(Lend);
    }
}
