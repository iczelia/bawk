package net.iczelia.bawk.hir;

public interface HIRChildTransformer {
    HNode transform(HNode child, HNode parent);
}
