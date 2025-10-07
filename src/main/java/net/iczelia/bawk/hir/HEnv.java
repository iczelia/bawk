package net.iczelia.bawk.hir;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class HEnv {
    private final Stack<Map<String, HXlatUnit.VarToken>> bindingStack = new Stack<>();
    private final Stack<Map<String, HXlatUnit.FnToken>> fnBindingStack = new Stack<>();
    private HXlatUnit translationUnit;
    private Map<String, HXlatUnit.VarToken> bindings = new HashMap<>();
    private Map<String, HXlatUnit.FnToken> fnBindings = new HashMap<>();

    public void pushScope() {
        bindingStack.push(new HashMap<>(bindings));
        fnBindingStack.push(new HashMap<>(fnBindings));
    }

    public void popScope() {
        if (bindingStack.isEmpty() || fnBindingStack.isEmpty())
            throw new HLoweringException("No scope to pop");
        bindings = bindingStack.pop();
        fnBindings = fnBindingStack.pop();
    }

    public HXlatUnit.VarToken resolveVar(String name) {
        Map<String, HXlatUnit.VarToken> currentBindings = bindings;
        while (!currentBindings.containsKey(name)) {
            if (bindingStack.isEmpty())
                return null;
            currentBindings = bindingStack.pop();
        }
        return currentBindings.get(name);
    }

    public HXlatUnit.FnToken resolveFn(String name) {
        Map<String, HXlatUnit.FnToken> currentBindings = fnBindings;
        while (!currentBindings.containsKey(name)) {
            if (fnBindingStack.isEmpty())
                return null;
            currentBindings = fnBindingStack.pop();
        }
        return currentBindings.get(name);
    }

    public void bindVar(String name, HXlatUnit.VarToken token) {
        bindings.put(name, token);
    }

    public void bindFn(String name, HXlatUnit.FnToken token) {
        fnBindings.put(name, token);
    }
}
