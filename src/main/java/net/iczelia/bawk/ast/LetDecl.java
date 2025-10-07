package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.Type;

public class LetDecl extends Statement {
    private final String name;
    private Type type; // may be null
    private final Expr init; // may be null

    public LetDecl(String name, Type type, Expr init) {
        this.name = name;
        this.type = type;
        this.init = init;
    }

    @Override
    protected void check() {
        if (type == null && init != null) {
            Type inferred = init.getInferredType(typeEnv);
            if (inferred == null) {
                typeEnv.errors.add(new ASTError("cannot infer type for variable '" + name + "' from initializer"));
                return;
            }
            type = inferred;
        } else if (type == null) {
            typeEnv.errors.add(new ASTError("cannot infer type for variable '" + name + "' (no type or initializer)"));
            return;
        }
        typeEnv.sym.declare(name, type, typeEnv.errors);
        if (init != null) {
            Type rt = init.getInferredType(typeEnv);
            if (rt != null && !rt.compatible(type)) {
                typeEnv.errors.add(new ASTError("type mismatch in init of '" + name + "': " + rt + " vs " + type));
            }
        }
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Expr getInit() {
        return init;
    }
}
