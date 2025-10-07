package net.iczelia.bawk.ast;

import net.bytebuddy.description.type.TypeDescription;
import net.iczelia.bawk.type.HierarchyComparison;
import net.iczelia.bawk.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TypeEnvironment {
    public final List<ASTError> errors = new ArrayList<>();
    public final SymbolTable sym = new SymbolTable();
}
