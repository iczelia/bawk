package net.iczelia.bawk.hir.jvmcg.stmt;

import net.iczelia.bawk.hir.jvmcg.HNoOpJVMCodeGenerator;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.hir.stmt.*;

import java.util.HashMap;

public class StmtCodeGeneratorRegistry {
    public static HashMap<Class<?>, JVMCodeGenerator<?>> createRegistry() {
        HashMap<Class<?>, JVMCodeGenerator<?>> registry = new HashMap<>();
        registry.put(HBlock.class, new HBlockJVMCodeGenerator());
        registry.put(HLetDecl.class, new HLetDeclJVMCodeGenerator());
        registry.put(HPrint.class, new HPrintJVMCodeGenerator());
        registry.put(HWhile.class, new HWhileJVMCodeGenerator());
        registry.put(HIfElseStmt.class, new HIfElseStmtJVMCodeGenerator());
        registry.put(HFnDecl.class, new HNoOpJVMCodeGenerator());
        registry.put(HSimpleFor.class, new HSimpleForJVMCodeGenerator());
        registry.put(HExprStmt.class, new HExprStmtJVMCodeGenerator());
        return registry;
    }
}
