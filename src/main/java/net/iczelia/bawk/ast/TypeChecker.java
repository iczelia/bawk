package net.iczelia.bawk.ast;

import java.util.List;

public class TypeChecker {
    private final TypeEnvironment env;

    public TypeChecker() {
        this.env = new TypeEnvironment();
    }

    public TypeChecker(TypeEnvironment env) {
        this.env = env;
    }

    public List<ASTError> check(Program p) {
        for (Statement n : p.statements) n.checkConstraints(env);
        return env.errors;
    }

    public void typeCheckStatement(Statement p) {
        p.checkConstraints(env);
    }
}
