package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.Type;

import java.util.*;

public class SymbolTable {
    private final Deque<Map<String, Type>> scopes = new ArrayDeque<>();

    public SymbolTable() {
        enter();
    }

    public void enter() {
        scopes.push(new HashMap<>());
    }

    public void exit() {
        scopes.pop();
    }

    public boolean declare(String name, Type t, List<ASTError> errs) {
        var top = scopes.peek();
        if (top.containsKey(name)) {
            errs.add(new ASTError("redeclaration of '" + name + "'"));
            return false;
        }
        top.put(name, t);
        return true;
    }

    public Type lookup(String name) {
        for (var m : scopes) {
            var t = m.get(name);
            if (t != null) return t;
        }
        return null;
    }
}
