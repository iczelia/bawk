package net.iczelia.bawk.ast;

import java.util.List;

public class Block extends Statement {
    private final List<Statement> statements;

    public Block(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    protected void check() {
        typeEnv.sym.enter(); // block scope
        for (Statement s : statements) s.checkConstraints(typeEnv);
        typeEnv.sym.exit();
    }
}

