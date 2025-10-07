package net.iczelia.bawk.type;

import net.bytebuddy.description.type.TypeDescription;

public enum PrimitiveType implements Type {
    I32, STR;

    @Override
    public String toString() {
        return switch (this) {
            case I32 -> "i32";
            case STR -> "str";
        };
    }

    @Override
    public boolean compatible(Type another) {
        if (!(another instanceof PrimitiveType pt)) return false;
        return this == pt;
    }

    @Override
    public Class<?> getJavaType() {
        return switch (this) {
            case I32 -> int.class;
            case STR -> String.class;
        };
    }

    @Override
    public String getJavaTypeString() {
        return switch (this) {
            case I32 -> "I";
            case STR -> "Ljava/lang/String;";
        };
    }

    @Override
    public TypeDescription getTypeDescription() {
        return switch (this) {
            case I32 -> TypeDescription.ForLoadedType.of(int.class);
            case STR -> TypeDescription.ForLoadedType.of(String.class);
        };
    }

    @Override
    public HierarchyComparison compareHierarchy(Type another) {
        if (this.equals(another)) return HierarchyComparison.SAME;
        return HierarchyComparison.UNRELATED;
    }
}
