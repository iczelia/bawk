package net.iczelia.bawk.type;

import net.bytebuddy.description.type.TypeDescription;

import java.util.ArrayList;
import java.util.List;

public class EitherType implements Type {
    public Type[] alternatives;

    public EitherType(Type[] alternatives) {
        this.alternatives = alternatives;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < alternatives.length; i++) {
            sb.append(alternatives[i].toString());
            if (i < alternatives.length - 1) sb.append(" | ");
        }
        return sb.toString();
    }

    @Override
    public boolean compatible(Type another) {
        if (!(another instanceof EitherType at)) return false;
        // all alternatives of both types must be the same
        if (this.alternatives.length != at.alternatives.length) return false;
        List<Type> t1 = new ArrayList<>(List.of(this.alternatives));
        List<Type> t2 = new ArrayList<>(List.of(at.alternatives));
        for (Type t : t1) {
            if (!t2.remove(t)) return false;
        }
        return t2.isEmpty();
    }

    @Override
    public Class<?> getJavaType() {
        return Object.class; // no direct mapping
    }

    @Override
    public String getJavaTypeString() {
        return "java.lang.Object"; // no direct mapping
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
        if (!(obj instanceof EitherType at)) return false;
        if (this.alternatives.length != at.alternatives.length) return false;
        List<Type> t1 = new ArrayList<>(List.of(this.alternatives));
        List<Type> t2 = new ArrayList<>(List.of(at.alternatives));
        for (Type t : t1) {
            if (!t2.remove(t)) return false;
        }
        return t2.isEmpty();
    }
}
