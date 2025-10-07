package net.iczelia.bawk.hir;

import net.iczelia.bawk.type.Type;

public class HXlatUnit {
    public record VarToken(String name, Type type) {
    }

    public record FnToken(String name, Type type) {
    }
}
