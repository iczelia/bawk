package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.implementation.bytecode.constant.FloatConstant;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HF32Lit;
import net.iczelia.bawk.hir.expr.HI32Lit;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;

public class HF32LitJVMCodeGenerator implements JVMCodeGenerator<HF32Lit> {
    @Override
    public void visit(HF32Lit node, CodeGenBase context) {
        FloatConstant.forValue(node.value).apply(context.mv, null);
    }
}
