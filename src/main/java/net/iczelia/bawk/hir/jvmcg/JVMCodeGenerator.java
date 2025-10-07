package net.iczelia.bawk.hir.jvmcg;

import net.iczelia.bawk.codegen.CodeGenBase;

public interface JVMCodeGenerator<T> {
    void visit(T node, CodeGenBase context);
}
