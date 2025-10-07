package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.Type;

public class UnresolvedVar extends LValue {
    private final String name;

    public UnresolvedVar(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        Type t = env.sym.lookup(name);
        if (t == null) {
            env.errors.add(new ASTError("use of undeclared variable '" + name + "'"));
        }
        return t;
    }
}
