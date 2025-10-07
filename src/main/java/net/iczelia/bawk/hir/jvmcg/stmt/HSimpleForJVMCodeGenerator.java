package net.iczelia.bawk.hir.jvmcg.stmt;

import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.hir.stmt.HBlock;
import net.iczelia.bawk.hir.stmt.HSimpleFor;
import net.iczelia.bawk.hir.stmt.HStmt;
import net.iczelia.bawk.hir.stmt.HWhile;

public class HSimpleForJVMCodeGenerator implements JVMCodeGenerator<HSimpleFor> {
    @Override
    public void visit(HSimpleFor f, CodeGenBase context) {
        Label Lstart = new Label();
        Label Lend = new Label();
        context.enterScope();
        if (f.init != null) context.emitStmt(f.init);
        context.mv.visitLabel(Lstart);
        context.emitExpr(f.condition);
        context.mv.visitJumpInsn(Opcodes.IFEQ, Lend);
        if (f.body instanceof HBlock block) {
            for (HStmt s : block.stmts) context.emitStmt(s);
        } else {
            context.emitStmt(f.body);
        }
        if (f.step != null) {
            context.emitExpr(f.step);
            context.mv.visitInsn(Opcodes.POP);
        }
        context.mv.visitJumpInsn(Opcodes.GOTO, Lstart);
        context.mv.visitLabel(Lend);
        context.exitScope();
    }
}
