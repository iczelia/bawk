package net.iczelia.bawk.ast;

public abstract class Statement extends AST {
    /**
     * The typing environment in which this statement resides.
     * Notably, This describes the outer environment. For statements like the for loop,
     * this does not describe the inner environment resulting from the
     * initial declaration.
     */
    protected TypeEnvironment typeEnv;

    public void checkConstraints(TypeEnvironment env) {
        if (this.typeEnv != null) return;
        this.typeEnv = env;
        check();
    }

    protected abstract void check();
}
