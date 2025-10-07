package net.iczelia.bawk.hir.jvmcg;

import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.HNode;
import net.iczelia.bawk.hir.expr.HExpr;

public class HNoOpJVMCodeGenerator implements JVMCodeGenerator<HNode> {
    @Override
    public void visit(HNode ie, CodeGenBase context) {
    }
}
