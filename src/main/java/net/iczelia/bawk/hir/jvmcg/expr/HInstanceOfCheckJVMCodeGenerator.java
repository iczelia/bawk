package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HAssignExpr;
import net.iczelia.bawk.hir.expr.HInstanceOfCheck;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.type.EitherType;
import net.iczelia.bawk.type.PrimitiveType;

public class HInstanceOfCheckJVMCodeGenerator implements JVMCodeGenerator<HInstanceOfCheck> {
    @Override
    public void visit(HInstanceOfCheck node, CodeGenBase context) {
        throw new UnsupportedOperationException("InstanceOfCheck is not supported in JVM backend yet.");
    }
}
