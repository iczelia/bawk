package net.iczelia.bawk.hir;

public abstract class HNode {
    public HXlatUnit translationUnitContext;
    public HEnv environmentContext;

    public abstract void accept(HIRChildTransformer visitor);
}
