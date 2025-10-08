package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.Type;

import java.util.*;

public class SymbolTable {
    private final Deque<Map<String, List<Type>>> scopes = new ArrayDeque<>();

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
        var overloads = top.getOrDefault(name, new ArrayList<>());
        for (Type existing : overloads) {
            if (existing.equals(t)) {
                errs.add(new ASTError("redeclaration of '" + name + "' with same signature"));
                return false;
            }
        }
        overloads.add(t);
        top.put(name, overloads);
        return true;
    }

    public List<Type> lookupAll(String name) {
        for (var m : scopes) {
            var overloads = m.get(name);
            if (overloads != null) return overloads;
        }
        return null;
    }

    public Type lookup(String name, List<Type> argTypes) {
        List<Type> overloads = lookupAll(name);
        if (overloads == null) return null;
        for (Type t : overloads) {
            if (t instanceof net.iczelia.bawk.type.FunctionType ft) {
                if (ft.paramTypes.equals(argTypes)) {
                    return t;
                }
            } else {
                return t;
            }
        }
        return null;
    }
}
