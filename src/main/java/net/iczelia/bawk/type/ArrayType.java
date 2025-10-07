package net.iczelia.bawk.type;

import net.bytebuddy.description.type.TypeDescription;

public class ArrayType implements Type {
    public Type elementType;

    public ArrayType(Type elementType) {
        this.elementType = elementType;
    }

    @Override
    public String toString() {
        return elementType + "[]";
    }

    @Override
    public boolean compatible(Type another) {
        if (!(another instanceof ArrayType at)) return false;
        return this.elementType.compatible(at.elementType);
    }

    @Override
    public Class<?> getJavaType() {
        return java.lang.reflect.Array.newInstance(elementType.getJavaType(), 0).getClass();
    }

    @Override
    public String getJavaTypeString() {
        return "[" + elementType.getJavaTypeString();
    }

    @Override
    public TypeDescription getTypeDescription() {
        return TypeDescription.ForLoadedType.of(getJavaType());
    }

    @Override
    public HierarchyComparison compareHierarchy(Type another) {
        if (this.equals(another)) return HierarchyComparison.SAME;
        return HierarchyComparison.UNRELATED;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ArrayType at)) return false;
        return this.elementType.equals(at.elementType);
    }
}
