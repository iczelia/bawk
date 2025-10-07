package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HExpr;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.hir.transform.HTailSelfCall;

public class HTailSelfCallJVMCodeGenerator implements JVMCodeGenerator<HTailSelfCall> {
    @Override
    public void visit(HTailSelfCall node, CodeGenBase context) {
        var params = context.currentFnDecl.params;
        int paramCount = params.length;
        int[] tempIdx = new int[paramCount];
        for (int i = 0; i < paramCount; i++) {
            HExpr arg = node.args[i];
            context.emitExpr(arg);
            var pType = params[i].type();
            String tempName = "$tco_tmp_" + i;
            CodeGenBase.Slot tmpSlot = context.scope.lookup(tempName);
            if (tmpSlot == null) {
                int idx = context.nextLocal++;
                context.scope.map.put(tempName, new CodeGenBase.Slot(idx, pType));
                tmpSlot = context.scope.lookup(tempName);
            }
            context.store(tmpSlot.index, pType);
            tempIdx[i] = tmpSlot.index;
        }
        for (int i = 0; i < paramCount; i++) {
            var p = params[i];
            CodeGenBase.Slot s = context.require(p.name());
            context.load(tempIdx[i], p.type());
            context.store(s.index, p.type());
        }
        context.mv.visitJumpInsn(Opcodes.GOTO, context.tailLoopStart);
    }
}
