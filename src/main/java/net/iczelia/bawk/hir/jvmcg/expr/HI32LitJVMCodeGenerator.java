package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HI32Lit;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HI32LitJVMCodeGenerator implements JVMCodeGenerator<HI32Lit> {
    @Override
    public void visit(HI32Lit node, CodeGenBase context) {
        IntegerConstant.forValue(node.value).apply(context.mv, null);
    }
}
