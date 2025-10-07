package net.iczelia.bawk.hir.jvmcg.stmt;

import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.hir.stmt.HBlock;
import net.iczelia.bawk.hir.stmt.HIfElseStmt;
import net.iczelia.bawk.hir.stmt.HStmt;
import net.iczelia.bawk.hir.stmt.HWhile;

public class HIfElseStmtJVMCodeGenerator implements JVMCodeGenerator<HIfElseStmt> {
    @Override
    public void visit(HIfElseStmt ie, CodeGenBase context) {
        context.emitExpr(ie.cond);
        Label Lelse = new Label(), Lend = new Label();
        context.mv.visitJumpInsn(Opcodes.IFEQ, Lelse);
        if (ie.yes instanceof HBlock block) {
            context.enterScope();
            for (HStmt s : block.stmts) context.emitStmt(s);
            context.exitScope();
        } else {
            context.emitStmt(ie.yes);
        }
        context.mv.visitJumpInsn(Opcodes.GOTO, Lend);
        context.mv.visitLabel(Lelse);
        if (ie.no != null) {
            if (ie.no instanceof HBlock block) {
                context.enterScope();
                for (HStmt s : block.stmts) context.emitStmt(s);
                context.exitScope();
            } else {
                context.emitStmt(ie.no);
            }
        }
        context.mv.visitLabel(Lend);
    }
}
