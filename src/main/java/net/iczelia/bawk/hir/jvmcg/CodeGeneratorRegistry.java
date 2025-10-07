package net.iczelia.bawk.hir.jvmcg;

import net.iczelia.bawk.hir.jvmcg.expr.ExprCodeGenerationRegistry;
import net.iczelia.bawk.hir.jvmcg.stmt.StmtCodeGeneratorRegistry;

import java.util.HashMap;

public class CodeGeneratorRegistry {
    private static HashMap<Class<?>, JVMCodeGenerator<?>> registryCache = null;

    private static HashMap<Class<?>, JVMCodeGenerator<?>> createRegistry() {
        HashMap<Class<?>, JVMCodeGenerator<?>> registry = new HashMap<>();
        registry.putAll(ExprCodeGenerationRegistry.createRegistry());
        registry.putAll(StmtCodeGeneratorRegistry.createRegistry());
        return registry;
    }

    public static HashMap<Class<?>, JVMCodeGenerator<?>> getRegistry() {
        if (registryCache == null) {
            registryCache = createRegistry();
        }
        return registryCache;
    }
}
