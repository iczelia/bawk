package net.iczelia.bawk.type;

import net.bytebuddy.description.type.TypeDescription;
import net.iczelia.bawk.ast.ParseResult;

public interface Type extends ParseResult {
    boolean compatible(Type another);

    Class<?> getJavaType();

    String getJavaTypeString();

    TypeDescription getTypeDescription();

    HierarchyComparison compareHierarchy(Type another);
}
